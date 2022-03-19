package me.lukedluz.SpecterBanco.API;

import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.event.Listener;

import me.lukedluz.SpecterBanco.Main;

public class APIGeral implements Listener {

	public static final NavigableMap<Long, String> suffixes = new TreeMap<>();

	public static String format(long value) {
		suffixes.put(1_000L, "K");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "B");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "Q");
		suffixes.put(1_000_000_000_000_000_000L, "QQ");
		if (value == Long.MIN_VALUE)
			return format(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + format(-value);
		if (value < 1000)
			return Long.toString(value);

		Entry<Long, String> e = suffixes.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10);
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
	
	public static boolean isInt(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

	public static void CreateAccount(String Player) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			if (Data.checkaccount(Player) == false){
				Data.CreateAccount(Player);
			}
		} else {
			if (!DataBase.fc.contains(Player)) {
				List<String> list = DataBase.fc.getStringList(Player + ".Historico");
				list.add(" ");
				DataBase.fc.set(Player + ".Money", 0);
				DataBase.fc.set(Player + ".Transacoes", 0);
				DataBase.fc.set(Player + ".Historico", list);
				DataBase.SaveConfig();
			}
		}
	}

	public static int GetMoney(String Player) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			return Data.getmoney(Player);
		} else {
			if (DataBase.fc.getInt(Player + ".Money") >= 0) {
				return DataBase.fc.getInt(Player + ".Money");
			} else {
				DataBase.fc.set(Player + ".Money", 0);
				DataBase.SaveConfig();
				return 0;
			}
		}
	}

	public static void SetMoney(String Player, int Money) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			Data.setmoney(Player, Money);
		} else {
			DataBase.fc.set(Player + ".Money", Money);
			DataBase.SaveConfig();
		}
	}

	public static void AddMoney(String Player, int Money) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			Data.setmoney(Player, Data.getmoney(Player) + Money);
		} else {
			if (DataBase.fc.getInt(Player + ".Money") >= 0) {
				int accountmoney = DataBase.fc.getInt(Player + ".Money");
				DataBase.fc.set(Player + ".Money", accountmoney + Money);
				DataBase.SaveConfig();
			} else {
				DataBase.fc.set(Player + ".Money", Money);
				DataBase.SaveConfig();
			}
		}
	}

	public static void RemoveMoney(String Player, int Money) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			if (Data.getmoney(Player) - Money > 0) {
				Data.setmoney(Player, Data.getmoney(Player) - Money);
			} else {
				Data.setmoney(Player, 0);
			}
		} else {
			if (DataBase.fc.getInt(Player + ".Money") >= 0) {
				int accountmoney = DataBase.fc.getInt(Player + ".Money");
				if (accountmoney - Money >= 0) {
					DataBase.fc.set(Player + ".Money", accountmoney - Money);
					DataBase.SaveConfig();
				} else {
					DataBase.fc.set(Player + ".Money", 0);
					DataBase.SaveConfig();
				}
			} else {
				DataBase.fc.set(Player + ".Money", 0);
				DataBase.SaveConfig();
			}
		}
	}

	public static int GetTransacoes(String Player) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			return Data.gettransacoes(Player);
		} else {
			if (DataBase.fc.getInt(Player + ".Transacoes") >= 0) {
				return DataBase.fc.getInt(Player + ".Transacoes");
			} else {
				DataBase.fc.set(Player + ".Transacoes", 0);
				DataBase.SaveConfig();
				return 0;
			}
		}
	}

	public static void addTransacao(String Player) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			Data.addtransacao(Player);
			;
		} else {
			DataBase.fc.set(Player + ".Transacoes", GetTransacoes(Player) + 1);
			DataBase.SaveConfig();
		}
	}

	public static String GetHistorico(String Player) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			return Data.gethistorico(Player);
		} else {
			List<String> Historico = DataBase.fc.getStringList(Player + ".Historico");
			String Historicos = "";
			for (String mHistoricos : Historico) {
				Historicos += mHistoricos;
				Historicos += ",";
			}
			return Historicos;
		}
	}

	public static void addHistorico(String Player, String Historico) {
		if (Main.m.getConfig().getBoolean("MySQL.Ativado") == true) {
			Data.addhistorico(Player, Historico);
		} else {
			List<String> list = DataBase.fc.getStringList(Player + ".Historico");
			if (list.size() <= 9) {
				list.add(Historico);
				DataBase.fc.set(Player + ".Historico", list);
				DataBase.SaveConfig();
			} else {
				list.remove(list.get(0));
				list.add(Historico);
				DataBase.fc.set(Player + ".Historico", list);
				DataBase.SaveConfig();
			}
		}
	}

}
