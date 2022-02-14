package com.starmediadev.plugins.starmcutils.updater;

import com.starmediadev.plugins.starmcutils.StarMCUtils;

/**
 * Part of the Updater feature. This is the main class that handles it
 * The Updater sends a Bukkit event based on a certain amount of time based on the UpdateType enum
 * An example is tracking playtime based on this system
 */
public class Updater implements Runnable {
    
    private StarMCUtils plugin;

    public Updater(StarMCUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (UpdateType type : UpdateType.values()) {
            final long lastRun = type.getLastRun();
            if (type.run()) {
                try {
                    plugin.getServer().getPluginManager().callEvent(new UpdateEvent(type, lastRun));
                } catch (Exception ex) {
                    try {
                        throw new UpdateException(ex);
                    } catch (UpdateException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
    }
}
