package com.starmediadev.plugins.starmcutils.timer;

import com.starmediadev.plugins.starmcutils.updater.*;
import org.bukkit.event.*;

public class TimerListener implements Listener {
    @EventHandler
    public void onUpdate(UpdateEvent e) {
        for (Timer timer : Timer.getTimers()) {
            timer.lastUpdates.put(e.getType(), e.getCurrentRun());
        }
    }
}
