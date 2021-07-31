package com.starmediadev.plugins.starmcutils.integrations;

import com.starmediadev.plugins.data.events.HandlerRegisterEvent;
import com.starmediadev.plugins.starmcutils.StarMCUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StarDataIntegration implements Listener {
    public StarDataIntegration(StarMCUtils plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHandlerRegister(HandlerRegisterEvent e) {
        e.registerTypeHandler(new PositionTypeHandler());
    }
}
