package com.starmediadev.plugins.starmcutils.util;

import net.md_5.bungee.api.ChatColor;

import java.util.*;

/**
 * A collection of color utilities to support adding custom color codes that can be used.
 * Developers must use MCUtils.color() method in order for custom color codes to be respected within this API
 */
public final class ColorUtils {
    private static List<Character> colorChars = new ArrayList<>(Collections.singleton('&'));
    private static Map<String, ChatColor> colors = new HashMap<>();
    
    /**
     * Adds a custom color
     * @param code The code to be used
     * @param hex The HEX value for this color code
     */
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
    
    /**
     * Gets the ChatColor representation of a code
     * @param code the code
     * @return The Bukkit ChatColor instance
     */
    public static ChatColor getCustomColor(String code) {
        return colors.get(code);
    }
    
    /**
     * Adds a custom prefix character. By default there is the & character, but this API supports using any character as a prefix
     * @param c The new character to add
     */
    public static void addColorChar(Character c) {
        colorChars.add(c);
    }
    
    /**
     * Checks to see if the character is a valid registered prefix character
     * @param c The character to check
     * @return If this character is valid
     */
    public static boolean validColorChar(char c) {
        return colorChars.contains(c);
    }
}