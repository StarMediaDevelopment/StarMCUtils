package com.starmediadev.plugins.starmcutils;

import com.starmediadev.plugins.starmcutils.integrations.StarDataIntegration;
import com.starmediadev.plugins.starmcutils.region.SelectionManager;
import com.starmediadev.plugins.starmcutils.updater.Updater;
import com.starmediadev.plugins.starmcutils.util.ColorUtils;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StarMCUtils extends JavaPlugin implements Listener {
    
    private SelectionManager selectionManager = new SelectionManager();
    private Updater updater;
    private StarDataIntegration starDataIntegration;

    public void onEnable() {
        updater = new Updater(this);
        getServer().getScheduler().runTaskTimer(this, updater, 1L, 1L);
        getServer().getPluginManager().registerEvents(this, this);

        if (Bukkit.getPluginManager().getPlugin("StarData") != null) {
            this.starDataIntegration = new StarDataIntegration(this);
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(MCUtils.color("&f" + e.getPlayer().getName() + "&8: &f" + e.getMessage()));
    }
}