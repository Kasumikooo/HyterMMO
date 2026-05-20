package org.example.hyterMMO;
import org.example.hyterMMO.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.hyterMMO.utils.MessageUtils;

public final class hyterMMO extends JavaPlugin {

    private String version = getDescription().getVersion();
    public static String prefix = "&bHCore &7>>";

    @Override
    public void onEnable() {
        registerCommands();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&a"+MessageUtils.asciiart));
    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cdisabled"));
    }

    public void registerCommands(){
        this.getCommand("Zones").setExecutor(new ZonesCommand(this));
    }
}
