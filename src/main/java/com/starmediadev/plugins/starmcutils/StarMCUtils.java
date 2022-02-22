package com.starmediadev.plugins.starmcutils;

import com.starmediadev.plugins.starmcutils.cmds.AddColorCmd;
import com.starmediadev.plugins.starmcutils.region.SelectionManager;
import com.starmediadev.plugins.starmcutils.skin.SkinManager;
import com.starmediadev.plugins.starmcutils.updater.Updater;
import com.starmediadev.plugins.starmcutils.util.ColorUtils;
import com.starmediadev.plugins.starmcutils.util.Config;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * This utility for Spigot runs as a plugin and provides Bukkit Services for the relevent utilities
 */
public class StarMCUtils extends JavaPlugin implements Listener {
    
    private SelectionManager selectionManager = new SelectionManager();
    private SkinManager skinManager = new SkinManager();
    
    private Config colorsConfig;

    public void onEnable() {
        Updater updater = new Updater(this);
        getServer().getScheduler().runTaskTimer(this, updater, 1L, 1L);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServicesManager().register(SelectionManager.class, selectionManager, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(SkinManager.class, skinManager, this, ServicePriority.Highest);
        getCommand("addcolor").setExecutor(new AddColorCmd(this));
        
        colorsConfig = new Config(this, "colors.yml");
        colorsConfig.setup();
    
        YamlConfiguration config = colorsConfig.getConfiguration();
        List<Character> chars = config.getCharacterList("chars");
        chars.forEach(ColorUtils::addColorChar);
    
        ConfigurationSection colorsSection = config.getConfigurationSection("colors");
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
    }
    
    @Override
    public void onDisable() {
        List<Character> colorChars = ColorUtils.getColorChars();
        Map<String, ChatColor> colors = ColorUtils.getColors();
        YamlConfiguration config = colorsConfig.getConfiguration();
        config.set("chars", colorChars);
        colors.forEach((code, chatColor) -> {
            config.set("colors." + code + ".r", chatColor.getColor().getRed());
            config.set("colors." + code + ".g", chatColor.getColor().getGreen());
            config.set("colors." + code + ".b", chatColor.getColor().getBlue());
        });
        
        colorsConfig.save();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(MCUtils.color("&f" + e.getPlayer().getName() + "&8: &f" + e.getMessage()));
    }
}