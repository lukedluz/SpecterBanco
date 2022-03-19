package me.lukedluz.SpecterBanco.Gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lukedluz.SpecterBanco.Main;
import me.lukedluz.SpecterBanco.API.APIGeral;
import me.lukedluz.SpecterBanco.API.SignAPI.SignGUI;
import me.lukedluz.SpecterBanco.API.SignAPI.SignGUIUpdateEvent;

public class BancoGUI implements CommandExecutor, Listener {

    private ItemStack vidro, deposit, saque, information, historico, depositofull, deposito50, depositocustom, Saquefull, Saque50, Saque20, Saquecustom, back;
    private ItemMeta vidrometa, depositmeta, saquemeta, informationmeta, historicometa, depositofullmeta, deposito50meta, depositocustommeta, Saquefullmeta, Saque50meta, Saque20meta, Saquecustommeta, backmeta;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (cmd.getLabel().equalsIgnoreCase("banco")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cVocê precisa ser um player");
                return true;
            }
        }

            Inventory Banco = Bukkit.createInventory(p, 36, Main.m.getConfig().getString("Menu.nome").replace("&", "§"));

            vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            vidrometa = vidro.getItemMeta();
            vidrometa.setDisplayName("");
            vidro.setItemMeta(vidrometa);

            deposit = new ItemStack(Material.CHEST);
            depositmeta = deposit.getItemMeta();
            depositmeta.setDisplayName(Main.m.getConfig().getString("Deposito.Titulo").replace("&", "§"));
            List<String> listdeposit = Main.m.getConfig().getStringList("Deposito.Lore");
            List<String> loredeposit = new ArrayList<String>();
            for (String string : listdeposit) {
                loredeposit.add(string.replace("&", "§").replace("{juros}", Main.m.getConfig().getString("Juros"))
                        .replace("{tempo}", Main.m.getConfig().getString("TempoJuros"))
                        .replace("{saldo}", String.valueOf((long) Main.getEconomy().getBalance(p))));
                depositmeta.setLore(loredeposit);
            }
            deposit.setItemMeta(depositmeta);

            saque = new ItemStack(Material.DROPPER);
            saquemeta = saque.getItemMeta();
            saquemeta.setDisplayName(Main.m.getConfig().getString("Saque.Titulo").replace("&", "§"));
            List<String> listSaque = Main.m.getConfig().getStringList("Saque.Lore");
            List<String> loreSaque = new ArrayList<String>();
            for (String string : listSaque) {
                loreSaque.add(string.replace("&", "§").replace("{saldo}", String.valueOf(APIGeral.GetMoney(p.getName()))));
                saquemeta.setLore(loreSaque);
            }
            saque.setItemMeta(saquemeta);

            historico = new ItemStack(Material.MAP);
            historicometa = historico.getItemMeta();
            historicometa.setDisplayName(Main.m.getConfig().getString("Historico.Titulo").replace("&", "§"));
            List<String> listhistorico = Main.m.getConfig().getStringList("Historico.Lore");
            List<String> lorehistorico = new ArrayList<String>();
            for (String string : listhistorico) {
                lorehistorico.add(
                        string.replace("&", "§").replace("{transacoes}", String.valueOf(APIGeral.GetTransacoes(p.getName())))
                                .replace("{juros}", Main.m.getConfig().getString("Juros"))
                                .replace("{tempo}", Main.m.getConfig().getString("TempoJuros")));
            }
            if (APIGeral.GetHistorico(p.getName()) != null) {
                String[] Historico = APIGeral.GetHistorico(p.getName()).split(",");
                for (String mHistoricos : Historico) {
                    lorehistorico.add("§7" + mHistoricos);
                }
            }
            historicometa.setLore(lorehistorico);
            historico.setItemMeta(historicometa);

            information = new ItemStack(Material.REDSTONE_TORCH_ON);
            informationmeta = information.getItemMeta();
            informationmeta.setDisplayName(Main.m.getConfig().getString("Informacao.Titulo").replace("&", "§"));
            List<String> listinformation = Main.m.getConfig().getStringList("Informacao.Lore");
            List<String> loreinformation = new ArrayList<String>();
            for (String string : listinformation) {
                loreinformation.add(string.replace("&", "§").replace("{juros}", Main.m.getConfig().getString("Juros"))
                        .replace("{tempo}", Main.m.getConfig().getString("TempoJuros")));
                informationmeta.setLore(loreinformation);
            }
            information.setItemMeta(informationmeta);

            back = new ItemStack(Material.ARROW);
            backmeta = back.getItemMeta();
            backmeta.setDisplayName(Main.m.getConfig().getString("Voltar.Titulo").replace("&", "§"));
            List<String> listback = Main.m.getConfig().getStringList("Voltar.Lore");
            List<String> loreback = new ArrayList<String>();
            for (String string : listback) {
                loreback.add(string.replace("&", "§"));
                backmeta.setLore(loreback);
            }
            back.setItemMeta(backmeta);

            String depositoful = String.valueOf((long) Main.getEconomy().getBalance(p));
            String deposito50s = String.valueOf((long) Main.getEconomy().getBalance(p) / 2);

            depositofull = new ItemStack(Material.CHEST, 64);
            depositofullmeta = depositofull.getItemMeta();
            depositofullmeta.setDisplayName(Main.m.getConfig().getString("Deposito_Total.Titulo").replace("&", "§"));
            List<String> listdepositofull = Main.m.getConfig().getStringList("Deposito_Total.Lore");
            List<String> loredepositofull = new ArrayList<String>();
            for (String string : listdepositofull) {
                loredepositofull.add(
                        string.replace("&", "§").replace("{saldo}", String.valueOf((long) Main.getEconomy().getBalance(p)))
                                .replace("{quantia}", depositoful));
                depositofullmeta.setLore(loredepositofull);
            }
            depositofull.setItemMeta(depositofullmeta);

            deposito50 = new ItemStack(Material.CHEST, 32);
            deposito50meta = deposito50.getItemMeta();
            deposito50meta.setDisplayName(Main.m.getConfig().getString("Deposito_Metade.Titulo").replace("&", "§"));
            List<String> listdeposito50 = Main.m.getConfig().getStringList("Deposito_Metade.Lore");
            List<String> loredeposito50 = new ArrayList<String>();
            for (String string : listdeposito50) {
                loredeposito50.add(
                        string.replace("&", "§").replace("{saldo}", String.valueOf((long) Main.getEconomy().getBalance(p)))
                                .replace("{quantia}", deposito50s));
                deposito50meta.setLore(loredeposito50);
            }
            deposito50.setItemMeta(deposito50meta);

            depositocustom = new ItemStack(Material.CHEST);
            depositocustommeta = depositocustom.getItemMeta();
            depositocustommeta.setDisplayName(Main.m.getConfig().getString("Deposito_Custom.Titulo").replace("&", "§"));
            List<String> listdepositocustom = Main.m.getConfig().getStringList("Deposito_Custom.Lore");
            List<String> loredepositocustom = new ArrayList<String>();
            for (String string : listdepositocustom) {
                loredepositocustom.add(
                        string.replace("&", "§").replace("{saldo}", String.valueOf((long) Main.getEconomy().getBalance(p))));
                depositocustommeta.setLore(loredepositocustom);
            }
            depositocustom.setItemMeta(depositocustommeta);

            String saquefulls = String.valueOf(APIGeral.GetMoney(p.getName()));
            String saque50s = String.valueOf(APIGeral.GetMoney(p.getName()) / 2);
            String saque20s = String.valueOf((APIGeral.GetMoney(p.getName()) * 20) / 100);

            Saquefull = new ItemStack(Material.DROPPER, 64);
            Saquefullmeta = Saquefull.getItemMeta();
            Saquefullmeta.setDisplayName(Main.m.getConfig().getString("Saque_Total.Titulo").replace("&", "§"));
            List<String> listSaquefull = Main.m.getConfig().getStringList("Saque_Total.Lore");
            List<String> loreSaquefull = new ArrayList<String>();
            for (String string : listSaquefull) {
                loreSaquefull.add(string.replace("&", "§").replace("{saldo}", String.valueOf(APIGeral.GetMoney(p.getName())))
                        .replace("{quantia}", saquefulls));
                Saquefullmeta.setLore(loreSaquefull);
            }
            Saquefull.setItemMeta(Saquefullmeta);

            Saque50 = new ItemStack(Material.DROPPER, 32);
            Saque50meta = Saque50.getItemMeta();
            Saque50meta.setDisplayName(Main.m.getConfig().getString("Saque_Metade.Titulo").replace("&", "§"));
            List<String> listSaque50 = Main.m.getConfig().getStringList("Saque_Metade.Lore");
            List<String> loreSaque50 = new ArrayList<String>();
            for (String string : listSaque50) {
                loreSaque50.add(string.replace("&", "§").replace("{saldo}", String.valueOf(APIGeral.GetMoney(p.getName())))
                        .replace("{quantia}", saque50s));
                Saque50meta.setLore(loreSaque50);
            }
            Saque50.setItemMeta(Saque50meta);

            Saque20 = new ItemStack(Material.DROPPER);
            Saque20meta = Saque20.getItemMeta();
            Saque20meta.setDisplayName(Main.m.getConfig().getString("Saque_Porcentagem.Titulo").replace("&", "§"));
            List<String> listSaque20 = Main.m.getConfig().getStringList("Saque_Porcentagem.Lore");
            List<String> loreSaque20 = new ArrayList<String>();
            for (String string : listSaque20) {
                loreSaque20.add(string.replace("&", "§").replace("{saldo}", String.valueOf(APIGeral.GetMoney(p.getName())))
                        .replace("{quantia}", saque20s));
                Saque20meta.setLore(loreSaque20);
            }
            Saque20.setItemMeta(Saque20meta);

            Saquecustom = new ItemStack(Material.DROPPER);
            Saquecustommeta = Saquecustom.getItemMeta();
            Saquecustommeta.setDisplayName(Main.m.getConfig().getString("Saque_Custom.Titulo").replace("&", "§"));
            List<String> listSaquecustom = Main.m.getConfig().getStringList("Saque_Custom.Lore");
            List<String> loreSaquecustom = new ArrayList<String>();
            for (String string : listSaquecustom) {
                loreSaquecustom
                        .add(string.replace("&", "§").replace("{saldo}", String.valueOf(APIGeral.GetMoney(p.getName()))));
                Saquecustommeta.setLore(loreSaquecustom);
            }
            Saquecustom.setItemMeta(Saquecustommeta);

            for (int i = 0; i != 36; i++) {
                Banco.setItem(i, vidro);
                Banco.setItem(11, deposit);
                Banco.setItem(13, saque);
                Banco.setItem(15, historico);
                Banco.setItem(31, information);
            }

            p.openInventory(Banco);
            return false;
        }

        @SuppressWarnings("static-access")
        @EventHandler
        public void Execute (InventoryClickEvent e){
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getItemMeta() != null)) {

                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                Player p = (Player) e.getWhoClicked();

                Inventory Deposito = Bukkit.createInventory(p, 36, "Deposito");
                Inventory Saque = Bukkit.createInventory(p, 36, "Saque");
                Inventory Banco = Bukkit.createInventory(p, 36, "Banco");

                for (int i = 0; i != 36; i++) {
                    Banco.setItem(i, vidro);
                    Banco.setItem(11, deposit);
                    Banco.setItem(13, saque);
                    Banco.setItem(15, historico);
                    Banco.setItem(31, information);
                    Deposito.setItem(i, vidro);
                    Deposito.setItem(11, depositofull);
                    Deposito.setItem(13, deposito50);
                    Deposito.setItem(15, depositocustom);
                    Deposito.setItem(31, back);
                    Saque.setItem(i, vidro);
                    Saque.setItem(10, Saquefull);
                    Saque.setItem(12, Saque50);
                    Saque.setItem(14, Saque20);
                    Saque.setItem(16, Saquecustom);
                    Saque.setItem(31, back);
                }
                Inventory Inv = e.getInventory();

                if (Inv.getTitle().equalsIgnoreCase("Banco")) {
                    e.setCancelled(true);
                    if (e.getCurrentItem().isSimilar(deposit)) {
                        p.closeInventory();
                        p.openInventory(Deposito);
                        return;
                    }
                    if (e.getCurrentItem().isSimilar(saque)) {
                        p.closeInventory();
                        p.openInventory(Saque);
                        return;
                    }
                }
                if (Inv.getTitle().equalsIgnoreCase("Saque")) {
                    e.setCancelled(true);
                    if (e.getCurrentItem().isSimilar(back)) {
                        p.closeInventory();
                        p.openInventory(Banco);
                        return;
                    }
                    if (e.getCurrentItem().isSimilar(Saquefull)) {
                        Main.m.getEconomy().depositPlayer(p, APIGeral.GetMoney(p.getName()));
                        p.sendMessage("§aVocê sacou " + APIGeral.format(APIGeral.GetMoney(p.getName())) + " coins");
                        APIGeral.addHistorico(p.getName(), "§c- §6" + APIGeral.format(APIGeral.GetMoney(p.getName()))
                                + "§a no dia " + format.format(now) + "§7 Por §b" + p.getName());
                        APIGeral.addTransacao(p.getName());
                        APIGeral.RemoveMoney(p.getName(), APIGeral.GetMoney(p.getName()));
                        p.closeInventory();
                        p.chat("/banco");
                        return;
                    }
                    if (e.getCurrentItem().isSimilar(Saque50)) {
                        Main.m.getEconomy().depositPlayer(p, APIGeral.GetMoney(p.getName()) / 2);
                        p.sendMessage("§aVocê sacou " + APIGeral.format(APIGeral.GetMoney(p.getName()) / 2) + " coins");
                        APIGeral.RemoveMoney(p.getName(), APIGeral.GetMoney(p.getName()) / 2);
                        APIGeral.addHistorico(p.getName(), "§c- §6" + APIGeral.format(APIGeral.GetMoney(p.getName()) / 2)
                                + "§a no dia " + format.format(now) + "§7 Por §b" + p.getName());
                        APIGeral.addTransacao(p.getName());
                        p.closeInventory();
                        p.chat("/banco");
                        return;
                    }
                    if (e.getCurrentItem().isSimilar(Saque20)) {
                        Main.m.getEconomy().depositPlayer(p, (APIGeral.GetMoney(p.getName()) * 20) / 100);
                        p.sendMessage(
                                "§aVocê sacou " + APIGeral.format((APIGeral.GetMoney(p.getName()) * 20) / 100) + " coins");
                        APIGeral.RemoveMoney(p.getName(), (APIGeral.GetMoney(p.getName()) * 20) / 100);
                        APIGeral.addHistorico(p.getName(),
                                "§c- §6" + APIGeral.format((APIGeral.GetMoney(p.getName()) * 20) / 100) + "§a no dia "
                                        + format.format(now) + "§7 Por §b" + p.getName());
                        APIGeral.addTransacao(p.getName());
                        p.closeInventory();
                        p.chat("/banco");
                        return;
                    }
                /*if (e.getCurrentItem().isSimilar(Saquecustom)) {
                    SignGUI.openSignEditor(p, new String[] { "", "^^^^^^^^^^^^^^^", "Insira quanto", "deseja sacar" },
							new SignGUI.SignGUIListener() {
								@Override
								public void onSignDone(Player player, String[] lines) {
									if (APIGeral.isInt(lines[0].replace(" ", ""))) {
										int Quantidade = Integer.valueOf(lines[0].replace(" ", ""));
										if (APIGeral.GetMoney(p.getName()) >= Quantidade) {
											Main.getEconomy().depositPlayer(p, Quantidade);
											APIGeral.addHistorico(p.getName(), "§c- §6" + APIGeral.format(Quantidade)
													+ "§a no dia " + format.format(now) + "§7 Por §b" + p.getName());
											APIGeral.RemoveMoney(p.getName(), Quantidade);
											p.sendMessage("§aVoc§ sacou " + APIGeral.format(Quantidade) + " coins");
											p.closeInventory();
											p.chat("/banco");
										}
									} else {
										p.sendMessage("§cVoc§ precisa colocar um numero v§lido");
										p.closeInventory();
										p.openInventory(Deposito);
									}
								}
							});*\
                    return;
                }
            }
            if (Inv.getTitle().equalsIgnoreCase("Deposito")) {
                e.setCancelled(true);
                if (e.getCurrentItem().isSimilar(back)) {
                    p.closeInventory();
                    p.openInventory(Banco);
                    return;
                }
                if (e.getCurrentItem().isSimilar(depositofull)) {
                    if (Main.m.getEconomy().getBalance(p) > 0) {
                        APIGeral.AddMoney(p.getName(), (int) Main.m.getEconomy().getBalance(p));
                        APIGeral.addHistorico(p.getName(),
                                "§a+ §6" + APIGeral.format((long) Main.m.getEconomy().getBalance(p)) + "§a no dia "
                                        + format.format(now) + "§7 Por §b" + p.getName());
                        APIGeral.addTransacao(p.getName());
                        p.sendMessage("§aVoc§ depositou " + APIGeral.format((long) Main.m.getEconomy().getBalance(p))
                                + " coins");
                        Main.m.getEconomy().withdrawPlayer(p, Main.m.getEconomy().getBalance(p));
                        p.closeInventory();
                        p.chat("/banco");
                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(deposito50)) {
                    if (Main.m.getEconomy().getBalance(p) > 0) {
                        APIGeral.AddMoney(p.getName(), (int) (Main.m.getEconomy().getBalance(p) / 2));
                        APIGeral.addHistorico(p.getName(),
                                "§a+ §6" + APIGeral.format((long) (Main.m.getEconomy().getBalance(p) / 2))
                                        + "§a no dia " + format.format(now) + "§7 Por §b" + p.getName());
                        APIGeral.addTransacao(p.getName());
                        p.sendMessage("§aVoc§ depositou "
                                + APIGeral.format((long) (Main.m.getEconomy().getBalance(p) / 2)) + " coins");
                        Main.m.getEconomy().withdrawPlayer(p, Main.m.getEconomy().getBalance(p) / 2);
                        p.closeInventory();
                        p.chat("/banco");
                    }
                    return;
                }
                if (e.getCurrentItem().isSimilar(depositocustom)) {
                    String[] text = new String[] { "", "^^^^^^^^^^^^^^^", "Insira quanto", "deseja depositar" };
                    SignGUI.openSignEditor(p, text);
					/*if (APIGeral.isInt(lines[0].replace(" ", ""))) {
						int Quantidade = Integer.valueOf(lines[0].replace(" ", ""));
						if (Main.m.getEconomy().getBalance(p) >= Quantidade) {
							APIGeral.AddMoney(p.getName(), Quantidade);
							APIGeral.addHistorico(p.getName(), "§a+ §6" + APIGeral.format(Quantidade) + "§a no dia "
									+ format.format(now) + "§7 Por §b" + p.getName());
							p.sendMessage("§aVoc§ depositou " + APIGeral.format(Quantidade) + " coins");
							Main.m.getEconomy().withdrawPlayer(p, Quantidade);
							p.closeInventory();
							p.chat("/banco");
						}
					} else {
						p.sendMessage("§cVoc§ precisa colocar um numero v§lido");
						p.closeInventory();
						p.openInventory(Deposito);
					}
                }*/
                    return;
                }
            }
        }

        @EventHandler
        public void signUpdate (SignGUIUpdateEvent event){
            Player player = event.getPlayer();

            for (String str : event.getSignText()) {
                player.sendMessage(str);
            }
        }
}