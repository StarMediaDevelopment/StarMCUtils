package com.starmediadev.plugins.starmcutils;

import com.starmediadev.plugins.starmcutils.region.SelectionManager;
import com.starmediadev.plugins.starmcutils.skin.SkinManager;
import com.starmediadev.plugins.starmcutils.updater.Updater;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This utility for Spigot runs as a plugin and provides Bukkit Services for the relevent utilities
 */
public class StarMCUtils extends JavaPlugin implements Listener {
    
    private SelectionManager selectionManager = new SelectionManager();
    private SkinManager skinManager = new SkinManager();

    public void onEnable() {
        Updater updater = new Updater(this);
        getServer().getScheduler().runTaskTimer(this, updater, 1L, 1L);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServicesManager().register(SelectionManager.class, selectionManager, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(SkinManager.class, skinManager, this, ServicePriority.Highest);
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(MCUtils.color("&f" + e.getPlayer().getName() + "&8: &f" + e.getMessage()));
    }
}