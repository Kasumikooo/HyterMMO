package org.example.hyterMMO.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.hyterMMO.hyterMMO;
import org.example.hyterMMO.utils.MessageUtils;

public class HelpCommand implements CommandExecutor {

    private final hyterMMO plugin;

    public HelpCommand(hyterMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        final Player p = (Player) sender;



        return true;
    }
}
