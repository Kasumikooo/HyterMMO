package org.jellypink.hyterMMO.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jellypink.hyterMMO.Main;
import org.jellypink.hyterMMO.listeners.player.PvPSystemListener;
import org.jellypink.hyterMMO.listeners.player.ZoneFlagType;
import org.jellypink.hyterMMO.managers.PvPSystem;
import org.jellypink.hyterMMO.utils.MessageUtils;

public class PvPCommand implements CommandExecutor {

    private final Main plugin;
    private final PvPSystem pvpSystem;
    private final PvPSystemListener listener;

    public PvPCommand(Main plugin, PvPSystem pvpSystem, PvPSystemListener listener) {
        this.plugin = plugin;
        this.pvpSystem = pvpSystem;
        this.listener = listener;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        Player player = (Player) sender;
        ZoneFlagType currentZone = listener.getPlayerZone(player);

        if (currentZone != ZoneFlagType.YELLOW) {
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &cYou can only toggle your PvP status manually while inside a Yellow Zone!"));
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &aCurrent Zone: " + currentZone.getDisplayName()));
            return true;
        }

        // Toggle PvP
        boolean currentStatus = pvpSystem.hasPvPEnabled(player);
        boolean newStatus = !currentStatus;
        pvpSystem.setPvP(player, newStatus);

        if (newStatus) {
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &aYour PvP is now enabled!."));
        } else {
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &eYour PvP status is now disabled!"));
        }

        return true;
    }
}