package me.lukedluz.SpecterBanco.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.event.Listener;

public class Data implements Listener {
	public static Connection con;
	public static Statement statement;

	public static void CriarTabela() {
		try {
			PreparedStatement ps = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS Banco (player VARCHAR(100), money int, historico VARCHAR(100), transacoes int)");
			ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void setmoney(String player, int money) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("UPDATE `Banco` SET `money` = ? WHERE `player` = ?");
			stm.setInt(1, 0);
			stm.setString(2, player.toLowerCase());
			stm.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static int getmoney(String player) {
		if (checkaccount(player)) {
			PreparedStatement stm = null;
			try {
				stm = con.prepareStatement("SELECT * FROM `Banco` WHERE `player` = ?");
				stm.setString(1, player.toLowerCase());
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					return rs.getInt("money");
				}
				return 0;
			} catch (SQLException e) {
				return 0;
			}
		} else {
			setmoney(player, 0);
			return 0;
		}
	}
	
	public static void CreateAccount(String p) {
		try {
			PreparedStatement ps = Data.con
					.prepareStatement("INSERT INTO Banco (player, money, historico, transacoes) VALUES ( ?, ?, ?, ?)");
			ps.setString(1, p);
			ps.setInt(2, 0);
			ps.setString(3, ".");
			ps.setInt(4, 0);
			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			ex.toString();
		}
	}

	public static boolean checkaccount(String player) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Banco WHERE player= ?");
			ps.setString(1, player);
			ResultSet rs = ps.executeQuery();
			boolean user = rs.next();
			rs.close();
			rs.close();
			return user;
		} catch (Exception ex) {
		}
		return false;
	}

	public static int gettransacoes(String player) {
		if (checkaccount(player)) {
			PreparedStatement stm = null;
			try {
				stm = con.prepareStatement("SELECT * FROM `Banco` WHERE `player` = ?");
				stm.setString(1, player.toLowerCase());
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					return rs.getInt("transacoes");
				}
				return 0;
			} catch (SQLException e) {
				return 0;
			}
		} else {
			setmoney(player, 0);
			return 0;
		}
	}

	public static void addtransacao(String player) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("UPDATE `Banco` SET `transacoes` = ? WHERE `player` = ?");
			stm.setInt(1, gettransacoes(player) + 1);
			stm.setString(2, player.toLowerCase());
			stm.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static String gethistorico(String player) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `Banco` WHERE `player` = ?");
			stm.setString(1, player.toLowerCase());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				return rs.getString("historico");
			}
			return "null";
		} catch (SQLException e) {
			return "null";
		}
	}

	public static void addhistorico(String player, String historico) {
		PreparedStatement stm = null;
		if (gethistorico(player) != null) {
			String[] split = gethistorico(player).split(",");
			try {
				stm = con.prepareStatement("UPDATE `Banco` SET `historico` = ? WHERE `player` = ?");
				stm.setString(2, player.toLowerCase());
				if (split.length < 10) {
					stm.setString(1, gethistorico(player) + "," + historico);
				} else {
					stm.setString(1,
							split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + ","
									+ split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9] + ","
									+ historico);
				}
				stm.executeUpdate();
			} catch (SQLException e) {
			}
		} else {
			try {
				stm = con.prepareStatement("UPDATE `Banco` SET `historico` = ? WHERE `player` = ?");
				stm.setString(2, player.toLowerCase());
				stm.setString(1, gethistorico(player) + "," + historico);
				stm.executeUpdate();
			} catch (SQLException e) {
			}
		}
	}

}
