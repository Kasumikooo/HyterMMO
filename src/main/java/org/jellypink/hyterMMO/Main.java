package org.jellypink.hyterMMO;
import com.sk89q.worldedit.util.Identifiable;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.jellypink.hyterMMO.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jellypink.hyterMMO.commands.HMMOCommand;
import org.jellypink.hyterMMO.listeners.player.PlayerDeathListener;
import org.jellypink.hyterMMO.managers.FlagManager;
import org.jellypink.hyterMMO.utils.MessageUtils;

public final class Main extends JavaPlugin {

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
        this.getCommand("hmmo").setExecutor(new HMMOCommand(this));
        this.getCommand("hmmo").setTabCompleter(new HMMOCommand(this));
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

}
