package me.lukedluz.SpecterBanco.Eventos;

import me.lukedluz.SpecterBanco.API.APIGeral;
import me.lukedluz.SpecterBanco.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Eventos implements Listener {

    @EventHandler
    public void evento(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                int money = (int) (APIGeral.GetMoney(p.getName())
                        * Double.valueOf(Main.m.getConfig().getString("Juros")) / 100);

                if (money > 0) {
                    APIGeral.AddMoney(p.getName(), money);
                    APIGeral.addHistorico(p.getName(),
                            "§a+ §6" + APIGeral.format(money) + "§a no dia " + format.format(now) + "§7 Por §bBanco");
                }
            }
        }.runTaskTimer((Plugin) Main.m, 0L, Integer.valueOf(Main.m.getConfig().getString("TempoJuros")) * 1200L);
    }

    @EventHandler
    public void evento2(PlayerDeathEvent e) {
        Player p = e.getEntity();
        long money = (long) (Main.getEconomy().getBalance(p) / 2);

        if (money > 0) {
            Main.getEconomy().withdrawPlayer(p, money);

            p.sendMessage("§cVocê perdeu " + APIGeral.format(money) + " Coins");
        }
    }

    @EventHandler
    public void evento3(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        APIGeral.CreateAccount(p.getName());
    }

}
