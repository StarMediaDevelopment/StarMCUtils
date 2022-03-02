package com.starmediadev.plugins.starmcutils.util;

import com.starmediadev.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A collection of utilities. Some of these will eventually be moved to their own classes at some point
 */
public final class MCUtils {
    
    public static final Map<String, Integer> nameToTicks = new LinkedHashMap<>();
    public static final int ticksPerDay = 24000;
    public static final int ticksPerHour = ticksPerDay / 24;
    public static final double ticksPerMinute = ticksPerHour / 60D;
    public static final double ticksPerSecond = ticksPerMinute / 60D;
    
    static {
        nameToTicks.put("sunrise", 23000);
        nameToTicks.put("dawn", 23000);
    
        nameToTicks.put("daystart", 0);
        nameToTicks.put("day", 0);
    
        nameToTicks.put("morning", 1000);
    
        nameToTicks.put("midday", 6000);
        nameToTicks.put("noon", 6000);
    
        nameToTicks.put("afternoon", 9000);
    
        nameToTicks.put("sunset", 12000);
        nameToTicks.put("dusk", 12000);
        nameToTicks.put("sundown", 12000);
        nameToTicks.put("nightfall", 12000);
    
        nameToTicks.put("nightstart", 14000);
        nameToTicks.put("night", 14000);
    
        nameToTicks.put("midnight", 18000);
    }
    
    /**
     * Colors text using Spigot's Chat color
     * @param uncolored The uncolored text
     * @return The colored text
     */
    public static String color(String uncolored) {
        return color(uncolored, true, true, true);
    }
    
    public static String color(String uncolored, boolean translateBukkit, boolean translateCustom, boolean translateHex) {
        String text = uncolored;
        if (translateBukkit) {
            text = ChatColor.translateAlternateColorCodes('&', uncolored);
        }
        StringBuilder colored = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (ColorUtils.validColorChar(c)) {
                if (translateCustom) {
                    if (text.length() >= i + 2) {
                        String code = String.valueOf(c) + text.charAt(i + 1);
                        ChatColor color = ColorUtils.getCustomColor(code);
                        colored.append(color);
                        i++;
                    }
                }
            } else if (c == '#') {
                if (translateHex) {
                    if (text.length() > i + 6) {
                        String colorCode = c + text.substring(i + 1, i + 7);
                        ChatColor color = ChatColor.of(colorCode);
                        colored.append(color);
                        i += 6;
                    }
                }
            } else {
                colored.append(c);
            }
        }
    
        return colored.toString();
    }
    
    /**
     * Sends a message to the sender in gray and italics. Useful for debugging things
     * @param sender The sender to send the message to
     * @param message The message to send
     */
    public static void debugSender(CommandSender sender, String message) {
        sender.sendMessage(MCUtils.color("&7&o" + message));
    }
    
    /**
     * Converts a Bukkit Location to a Position
     * Please use Postion.fromLocation() static method
     * @param location The location
     * @return The position
     */
    @Deprecated
    public static Position locationToPosition(Location location) {
        Bukkit.getConsoleSender().sendMessage(MCUtils.color("&cThere is use of the deprecated Position API from StarMCUtils. Please review the stack trace to find the plugin"));
        Utils.printCurrentStack();
        return new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }
    
    /**
     * Converts a Position to a Location
     * Please use Position.toLocation() instance method
     * @param world The world for this location
     * @param position The position
     * @return The Bukkit Location
     */
    @Deprecated
    public static Location positionToLocation(World world, Position position) {
        Bukkit.getConsoleSender().sendMessage(MCUtils.color("&cThere is use of the deprecated Position API from StarMCUtils. Please review the stack trace to find the plugin"));
        Utils.printCurrentStack();
        return new Location(world, position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch());
    }
    
    /**
     * Converts the world time into the human readable 24hr format
     * @param world The world
     * @return The formatted time
     */
    public static String getWorldTimeAs24Hr(World world) {
        long[] worldTimeBreakdown = getWorldTimeBreakdown(world);
        long totalHours = worldTimeBreakdown[1];
        long totalMinutes = worldTimeBreakdown[2];
        long totalSeconds = worldTimeBreakdown[3];
        
        String hours = formatTimeUnit(totalHours), minutes = formatTimeUnit(totalMinutes), seconds = formatTimeUnit(totalSeconds);
        return hours + ":" + minutes + ":" + seconds;
    }
    
    /**
     * Converts the world time into the human readable 12hr format
     * @param world The world
     * @return The formatted time
     */
    public static String getWorldTimeAs12Hr(World world) {
        long[] worldTimeBreakdown = getWorldTimeBreakdown(world);
        long totalHours = worldTimeBreakdown[1];
        long totalMinutes = worldTimeBreakdown[2];
        long totalSeconds = worldTimeBreakdown[3];
    
        String meridian;
        if (totalHours < 12) {
            meridian = "am";
        } else if (totalHours >= 12) {
            meridian = "pm";
        } else {
            throw new IllegalArgumentException("Invalid time format: Total hours is greater than 24.");
        }
        
        if (totalHours > 12) {
            totalHours = totalHours - 12;
        }
        
        String hours = formatTimeUnit(totalHours), minutes = formatTimeUnit(totalMinutes), seconds = formatTimeUnit(totalSeconds) + " " + meridian;
        return hours + ":" + minutes + ":" + seconds;
    }
    
    private static long[] getWorldTimeBreakdown(World world) {
        long worldTime = world.getTime();
        double totalTicks = worldTime + 6000;
        long totalDays = (long) (totalTicks / ticksPerDay);
        totalTicks = totalTicks - (ticksPerDay * totalDays);
        long totalHours = (long) (totalTicks / ticksPerHour);
        totalTicks = totalTicks - (ticksPerHour * totalHours);
        long totalMinutes = (long) (totalTicks / ticksPerMinute);
        totalTicks = totalTicks - (ticksPerMinute * totalMinutes);
        long totalSeconds = (long) (totalTicks / ticksPerSecond);
        return new long[]{totalDays, totalHours, totalMinutes, totalSeconds};
    }
    
    private static String formatTimeUnit(long time) {
        if (time == 0) {
            return "00";
        } else if (time < 10) {
            return "0" + time;
        } else {
            return time + "";
        }
    }
    
    /**
     * Gets a world based on the CommandSender
     * If it is the console, it gets the main world
     * If it is a player, then it gets the player's current world
     * Other senders return null
     * @param sender The command sender
     * @return The world
     */
    public static World getWorld(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return Bukkit.getWorld(ServerProperties.getLevelName());
        } else if (sender instanceof Player player) {
            return player.getWorld();
        }
        return null;
    }
}
