package org.jellypink.hyterMMO.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jellypink.hyterMMO.Main;
import org.jellypink.hyterMMO.models.ZoneType;
import org.jellypink.hyterMMO.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class HMMOCommand implements CommandExecutor, TabExecutor {

    private final Main plugin;
    private final ZoneCommand zoneCommand;

    public HMMOCommand(Main plugin) {
        this.plugin = plugin;
        this.zoneCommand = new ZoneCommand(plugin);
    }

    // AutoCompletar de los comandos

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        final List<String> validArguments = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("zone", "mob", "mine"), validArguments);
            return validArguments;

        }

        String rama = args[0].toLowerCase();
        switch (rama) {
            case "zone", "zones", "z":
                return ZoneCommand.onTabComplete(args);

            case "mob", "mobs", "m":
                // return mobsCommand.onTabComplete(args);
                break;
        }

        return List.of();
    }

    // ejecucion del comando

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendGeneralHelp(player);
            return true;
        }

        String rama = args[0].toLowerCase();

        switch (rama) {
            case "zone","z","zones":
                zoneCommand.execute(player, args);
                break;

            case "mob":
                // mobCommand.execute(player, args);
                break;

            case "mine":
                // mineCommand.execute(player, args);
                break;

            default:
                player.sendMessage(MessageUtils.getColoredMessage("&cSubcommand not found. Use /hmmo for help."));
                break;
        }

        return true;
    }

    private void sendGeneralHelp(Player player) {
        player.sendMessage(MessageUtils.getColoredMessage("&6====== &bHyterMMO Help &6======"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo zone &7- Gestión de regiones del MMO"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo mob &7- Gestión de monstruos personalizados"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo mine &7- Gestión de minería"));
    }

}