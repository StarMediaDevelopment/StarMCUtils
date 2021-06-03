package com.starmediadev.starmcutils.updater;

import com.starmediadev.starmcutils.StarMCUtils;

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
