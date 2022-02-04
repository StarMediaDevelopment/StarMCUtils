package com.starmediadev.plugins.starmcutils.module;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class StarModule {
    //TODO configs specific to module, requires a change to the PlayerActionCmd send message methods
    protected JavaPlugin plugin;
    protected String name;
    protected boolean enabled = true;
    
    protected Map<String, CommandExecutor> commands = new HashMap<>();
    protected Set<Listener> listeners = new HashSet<>();
    
    public StarModule(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }
    
    public abstract void createCommandExecutors();
    public abstract void createEventListeners();
    
    public String getName() {
        return name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public final void registerCommands() {
        this.commands.forEach((name, executor) -> plugin.getCommand(name).setExecutor(executor));
    }
    
    public final void registerListeners() {
        this.listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}
