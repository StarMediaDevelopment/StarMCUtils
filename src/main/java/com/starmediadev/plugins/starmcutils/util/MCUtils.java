package com.starmediadev.plugins.starmcutils.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public final class MCUtils {

    public static String color(String uncolored) {
        String text = ChatColor.translateAlternateColorCodes('&', uncolored);
        StringBuilder colored = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (ColorUtils.validColorChar(c)) {
                if (text.length() >= i + 2) {
                    String code = String.valueOf(c) + text.charAt(i + 1);
                    ChatColor color = ColorUtils.getCustomColor(code);
                    colored.append(color);
                    i++;
                }
            } else if (c == '#') {
                if (text.length() > i + 6) {
                    String colorCode = c + text.substring(i + 1, i + 7);
                    ChatColor color = ChatColor.of(colorCode);
                    colored.append(color);
                    i += 6;
                }
            } else {
                colored.append(c);
            }
        }

        return colored.toString();
    }
    
    public static void debugSender(CommandSender sender, String message) {
        sender.sendMessage(MCUtils.color("&7&o" + message));
    }

    public static Position locationToPosition(Location location) {
        return new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }

    public static Location positionToLocation(World world, Position position) {
        return new Location(world, position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch());
    }
}
