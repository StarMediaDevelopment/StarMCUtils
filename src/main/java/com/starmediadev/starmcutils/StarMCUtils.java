package com.starmediadev.starmcutils;

import com.starmediadev.starmcutils.region.SelectionManager;
import com.starmediadev.starmcutils.updater.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public class StarMCUtils extends JavaPlugin {
    
    private SelectionManager selectionManager = new SelectionManager();
    private Updater updater;
}