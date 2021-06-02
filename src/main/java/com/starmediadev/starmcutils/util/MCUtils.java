package com.starmediadev.starmcutils.util;

import net.md_5.bungee.api.ChatColor;

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
                    i++; //This will skip the other color char
                }
            }
            colored.append(c);
        }
        
        return colored.toString();
    }
}
