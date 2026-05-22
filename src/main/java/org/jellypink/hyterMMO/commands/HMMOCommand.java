package org.example.hyterMMO.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.hyterMMO.hyterMMO;
import org.example.hyterMMO.utils.MessageUtils;

public class HMMOCommand implements CommandExecutor {

    private final hyterMMO plugin;
    private final ZonesCommand zonesCommand;

    public HMMOCommand(hyterMMO plugin) {
        this.plugin = plugin;
        this.zonesCommand = new ZonesCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendGeneralHelp(player);
            return true;
        }

        // Evaluamos el primer argumento: /hmmo [rama]
        String rama = args[0].toLowerCase();

        switch (rama) {
            case "zone","z","zones":
                // Le pasamos el control a la clase de zonas junto con los argumentos
                zonesCommand.execute(player, args);
                break;

            case "mob":
            case "mobs":
                // Aquí llamarías a tu comando de mobs en el futuro:
                // mobsCommand.execute(player, args);
                player.sendMessage(MessageUtils.getColoredMessage("&aPróximamente: Sistema de Mobs."));
                break;

            case "mine":
                player.sendMessage(MessageUtils.getColoredMessage("&aPróximamente: Sistema de Minería."));
                break;

            default:
                player.sendMessage(MessageUtils.getColoredMessage("&cSubcomando desconocido. Usa /hmmo para ver la ayuda."));
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