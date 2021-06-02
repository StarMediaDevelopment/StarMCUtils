package com.starmediadev.starmcutils.util;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ColorUtils {
    private static List<Character> colorChars = new ArrayList<>();
    private static Map<String, ChatColor> colors = new HashMap<>();
    
    public static void addCustomColor(String code, String hex) {
        boolean validChar = false;
        for (Character colorChar : colorChars) {
            if (code.startsWith(String.valueOf(colorChar))) {
                validChar = true;
                break;
            }
        }
        
        if (code.length() != 2) {
            return;
        }
        
        if (validChar) {
            ChatColor color = ChatColor.of(hex);
            colors.put(code, color);
        }
    }
    
    public static ChatColor getCustomColor(String code) {
        return colors.get(code);
    }
    
    public static void addColorChar(Character c) {
        colorChars.add(c);
    }

    public static boolean validColorChar(char c) {
        return colorChars.contains(c);
    }
}