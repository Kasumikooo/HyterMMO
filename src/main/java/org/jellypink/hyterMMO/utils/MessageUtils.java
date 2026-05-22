package org.example.hyterMMO.utils;

import org.bukkit.ChatColor;

import static org.example.hyterMMO.hyterMMO.prefix;

public class MessageUtils {

    public static String asciiart = """
            .-----------------------------------.
            |  _   _   __  __   __  __    ___   |
            | | | | | |  \\/  | |  \\/  |  / _ \\  |
            | | |_| | | |\\/| | | |\\/| | | | | | |
            | |  _  | | |  | | | |  | | | |_| | |
            | |_| |_| |_|  |_| |_|  |_|  \\___/  |
            '-----------------------------------'
            
            
            """;

    public static String getColoredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String ConsoleMessage() {
        return MessageUtils.getColoredMessage(prefix+ " &cOnly players can use this command.");
    }

}
