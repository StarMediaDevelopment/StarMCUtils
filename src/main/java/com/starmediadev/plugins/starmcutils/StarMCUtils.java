package com.starmediadev.plugins.starmcutils;

import com.starmediadev.nmswrapper.NMS;
import com.starmediadev.nmswrapper.NMS.Version;
import com.starmediadev.plugins.starmcutils.command.*;
import com.starmediadev.plugins.starmcutils.region.SelectionManager;
import com.starmediadev.plugins.starmcutils.skin.SkinManager;
import com.starmediadev.plugins.starmcutils.updater.Updater;
import com.starmediadev.plugins.starmcutils.util.*;
import com.starmediadev.plugins.starmcutils.timer.Timer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * This utility for Spigot runs as a plugin and provides Bukkit Services for the relevent utilities
 */
public class StarMCUtils extends JavaPlugin implements Listener {
    
    private SelectionManager selectionManager = new SelectionManager();
    private SkinManager skinManager = new SkinManager();
    
    private Config colorsConfig;
    
    private NMS nms;
    
    public void onEnable() {
        nms = NMS.getNMS(Version.MC_1_18_R2);
        
        Updater updater = new Updater(this);
        getServer().getScheduler().runTaskTimer(this, updater, 1L, 1L);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServicesManager().register(SelectionManager.class, selectionManager, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(SkinManager.class, skinManager, this, ServicePriority.Highest);
        
        Timer.startTimerUpdater(this);
        
        colorsConfig = new Config(this, "colors.yml");
        colorsConfig.setup();
        
        List<Character> chars = colorsConfig.getCharacterList("chars");
        chars.forEach(ColorUtils::addColorChar);
        
        ConfigurationSection colorsSection = colorsConfig.getConfigurationSection("colors");
        if (colorsSection != null) {
            colorsSection.getKeys(false).forEach(code -> {
                int red = colorsSection.getInt(code + ".r");
                int green = colorsSection.getInt(code + ".g");
                int blue = colorsSection.getInt(code + ".b");
                Color color = new Color(red, green, blue);
                ChatColor chatcolor = ChatColor.of(color);
                ColorUtils.addCustomColor(code, chatcolor);
            });
        }
    
        CommandManager commandManager = new CommandManager(this);
    
        StarCommand customColorsCommand = new StarCommand("customcolors", "Manage custom colors", "starmcutils.colors.admin");
        SubCommand addColorSubCommand = new SubCommand(customColorsCommand, "add", "Add a color", "starmcutils.colors.admin.add") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                String colorCode = args[0], hexValue = args[1];
                try {
                    checkColorValues(colorCode, hexValue);
                } catch (Exception e) {
                    actor.sendMessage(e.getMessage());
                    return;
                }
            
                if (ColorUtils.getCustomColor(colorCode) != null) {
                    actor.sendMessage("&cThat color code is already defined.");
                    return;
                }
            
                ColorUtils.addCustomColor(colorCode, hexValue);
                actor.sendUncoloredMessage(MCUtils.color("&eYou added a color with code &b") + colorCode + MCUtils.color(" &eand hex value &b") + hexValue);
            }
        };
        addColorSubCommand.addArgument(new Argument("colorCode", true, "You must provide the code of the color"));
        addColorSubCommand.addArgument(new Argument("hexValue", true, "You must provide the hex value of the color"));
        customColorsCommand.addSubCommand(addColorSubCommand);
        
        customColorsCommand.addSubCommand(new SubCommand(customColorsCommand, "list", "Lists all colors", "starmcutils.colors.list") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                actor.sendMessage("&eList of available colors");
                ColorUtils.getColors().forEach((code, value) -> {
                    String hex = Integer.toHexString(value.getColor().getRGB()).toUpperCase();
                    hex = hex.substring(2);
                    actor.sendUncoloredMessage(MCUtils.color("&7- ") + code + MCUtils.color("&7: #") + hex);
                });
            }
        });
    
        SubCommand setColorSubCommand = new SubCommand(customColorsCommand, "set", "Sets a predefined color", "starmcutils.colors.admin.set") {
            @Override
            public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
                String colorCode = args[0], hexValue = args[1];
                try {
                    checkColorValues(colorCode, hexValue);
                } catch (Exception e) {
                    actor.sendMessage(e.getMessage());
                    return;
                }
            
                ColorUtils.addCustomColor(colorCode, hexValue);
                actor.sendUncoloredMessage(MCUtils.color("&eYou set the color code &b") + colorCode + MCUtils.color(" &eto &b") + hexValue);
            }
        };
        setColorSubCommand.addArgument(new Argument("colorCode", true, "You must provide the code of the color"));
        setColorSubCommand.addArgument(new Argument("hexValue", true, "You must provide the new hex value of the color"));
        customColorsCommand.addSubCommand(setColorSubCommand);
        
        customColorsCommand.register(commandManager);
    }
    
    private void checkColorValues(String colorCode, String hexValue) throws Exception {
        if (colorCode.length() != 2) {
            throw new Exception("Invalid color code. Must be 2 characters, no more, no less.");
        }
        
        if (hexValue.length() != 7 || hexValue.charAt(0) != '#') {
            throw new Exception("Invalid Hex Value. Must have a # and 6 characters for the value.");
        }
        
        if (!ColorUtils.validColorChar(colorCode.charAt(0))) {
            throw new Exception("The beginning code is not a valid code.");
        }
    }
    
    @Override
    public void onDisable() {
        List<Character> colorChars = ColorUtils.getColorChars();
        Map<String, ChatColor> colors = ColorUtils.getColors();
        colorsConfig.set("chars", colorChars);
        colors.forEach((code, chatColor) -> {
            colorsConfig.set("colors." + code + ".r", chatColor.getColor().getRed());
            colorsConfig.set("colors." + code + ".g", chatColor.getColor().getGreen());
            colorsConfig.set("colors." + code + ".b", chatColor.getColor().getBlue());
        });
        
        colorsConfig.save();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(MCUtils.color("&f" + e.getPlayer().getName() + "&8: &f" + e.getMessage()));
    }
    
    public NMS getNMS() {
        return this.nms;
    }
}