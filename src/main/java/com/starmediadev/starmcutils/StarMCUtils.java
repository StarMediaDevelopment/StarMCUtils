package com.starmediadev.starmcutils;

import com.starmediadev.starmcutils.region.Cuboid;
import com.starmediadev.starmcutils.region.SelectionManager;
import com.starmediadev.starmcutils.updater.Updater;
import com.starmediadev.starmcutils.util.ColorUtils;
import com.starmediadev.starmcutils.util.MCUtils;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StarMCUtils extends JavaPlugin implements Listener {
    
    static {
        ConfigurationSerialization.registerClass(Cuboid.class);
    }
    
    private SelectionManager selectionManager = new SelectionManager();
    private Updater updater;

    public void onEnable() {
        updater = new Updater(this);
        getServer().getScheduler().runTaskTimer(this, updater, 1L, 1L);
        getServer().getPluginManager().registerEvents(this, this);

        ColorUtils.addCustomColor("&g", "#aa2c66");
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(MCUtils.color("&f" + e.getPlayer().getName() + "&8: &f" + e.getMessage()));
    }
}