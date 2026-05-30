package org.jellypink.hyterMMO;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jellypink.hyterMMO.commands.HMMOCommand;
import org.jellypink.hyterMMO.commands.PvPCommand;
import org.jellypink.hyterMMO.listeners.player.ChatListener;
import org.jellypink.hyterMMO.listeners.player.PlayerDeathListener;
import org.jellypink.hyterMMO.listeners.player.PvPSystemListener;
import org.jellypink.hyterMMO.managers.PlaceholderAPIHook;
import org.jellypink.hyterMMO.utils.MessageUtils;
import org.jellypink.hyterMMO.managers.PvPSystem;

public final class Main extends JavaPlugin {

    private PvPSystem pvpSystem;
    private PvPSystemListener pvpSystemListener;
    private String version = getDescription().getVersion();
    public static String prefix = "&bHCore &7>>";
    public static String ingameprefix = "&e[MMO] &7>>";

    public void onLoad() {
        // Flags Zone Create
        setGreenZone();
        setYellowZone();
        setRedZone();
        setBlackZone();
        setCityZone();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            PlaceholderAPIHook.registerHook();
    }

    @Override
    public void onEnable() {
        this.pvpSystem = new PvPSystem();
        this.pvpSystemListener = new PvPSystemListener(this, this.pvpSystem);

        registerCommands();
        registerListeners();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&a"+MessageUtils.asciiart));

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cdisabled"));
    }

    public void registerCommands(){
        this.getCommand("hmmo").setExecutor(new HMMOCommand(this));
        this.getCommand("hmmo").setTabCompleter(new HMMOCommand(this));
        this.getCommand("pvp").setExecutor(new PvPCommand(this, this.pvpSystem, this.pvpSystemListener));
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(this.pvpSystemListener, this);
    }

    // Custom Flags

    public static StateFlag GREEN_ZONE;
    public static StateFlag YELLOW_ZONE;
    public static StateFlag RED_ZONE;
    public static StateFlag BLACK_ZONE;
    public static StateFlag CITY_ZONE;

    public void setGreenZone(){

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("green-zone", false);
            registry.register(flag);
            GREEN_ZONE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("green-zone");
            if (existing instanceof StateFlag) {
                GREEN_ZONE = (StateFlag) existing;
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cWorldGuard Has a Compatibility error."));
            }
        }
    }

    public void setYellowZone(){

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("yellow-zone", false);
            registry.register(flag);
            YELLOW_ZONE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("yellow-zone");
            if (existing instanceof StateFlag) {
                YELLOW_ZONE = (StateFlag) existing;
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cWorldGuard Has a Compatibility error."));
            }
        }
    }

    public void setRedZone(){

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("red-zone", false);
            registry.register(flag);
            RED_ZONE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("red-zone");
            if (existing instanceof StateFlag) {
                RED_ZONE = (StateFlag) existing;
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cWorldGuard Has a Compatibility error."));
            }
        }
    }

    public void setBlackZone(){

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("black-zone", false);
            registry.register(flag);
            BLACK_ZONE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("black-zone");
            if (existing instanceof StateFlag) {
                BLACK_ZONE = (StateFlag) existing;
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cWorldGuard Has a Compatibility error."));
            }
        }
    }

    public void setCityZone(){

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("city-zone", false);
            registry.register(flag);
            CITY_ZONE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("city-zone");
            if (existing instanceof StateFlag) {
                CITY_ZONE = (StateFlag) existing;
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cWorldGuard Has a Compatibility error."));
            }
        }
    }



}
