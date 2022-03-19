package me.lukedluz.SpecterBanco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import me.lukedluz.SpecterBanco.Gui.BancoGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.lukedluz.SpecterBanco.API.DataBase;
import me.lukedluz.SpecterBanco.Eventos.Eventos;
import net.milkbowl.vault.economy.Economy;
import me.lukedluz.SpecterBanco.API.APIGeral;
import me.lukedluz.SpecterBanco.API.Data;

public class Main extends JavaPlugin {

    private static Economy econ = null;

    public static Main m;

    public static Economy getEconomy() {
        return econ;
    }

    Connection conn = null;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§b[SpecterPlugins] §fO plugin '§aSpecterBanco§f' foi ativado com sucesso.!");
        Bukkit.getConsoleSender().sendMessage("§b##############################################");
        Bukkit.getConsoleSender().sendMessage("§b#  §fPlugin idealizado por: §bLucas L.           #");
        Bukkit.getConsoleSender().sendMessage("§b#  §fPlugin desenvolvido por: §bLucas L.         #");
        Bukkit.getConsoleSender().sendMessage("§b#  §fPlugin publicado por: §bSpecterPlugins      #");
        Bukkit.getConsoleSender().sendMessage("§b#  §fAjudantes: §bNinguém                        #");
        Bukkit.getConsoleSender().sendMessage("§b##############################################");
        Bukkit.getPluginManager().registerEvents(new APIGeral(), this);
        Bukkit.getPluginManager().registerEvents(new Eventos(), this);
        Bukkit.getPluginManager().registerEvents(new BancoGUI(), this);
        getCommand("banco").setExecutor(new BancoGUI());
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        m = this;

        if (getConfig().getBoolean("MySQL.Ativado") == true) {
            try {
                try {

                    Class.forName("com.mysql.jdbc.Driver");

                    Data.con = (Connection) DriverManager.getConnection(
                            "jdbc:mysql://" + getConfig().getString("MySQL.Host") + ":"
                                    + getConfig().getString("MySQL.Port") + "/"
                                    + getConfig().getString("MySQL.Database"),
                            getConfig().getString("MySQL.User"), getConfig().getString("MySQL.Pass"));

                } catch (ClassNotFoundException | SQLException e) {

                    e.printStackTrace();

                }
                Data.statement = conn.createStatement();
                Data.CriarTabela();
            } catch (Exception e2) {
            }
        }

        setupEconomy();

        DataBase.create();
        DataBase.SaveConfig();

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§b[SpecterPlugins] §fO plugin '§cSpecterBanco§f' foi desativado com sucesso.!");
    }
}
