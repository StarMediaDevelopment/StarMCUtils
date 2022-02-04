package com.starmediadev.plugins.starmcutils.module;

import com.starmediadev.plugins.starmcutils.util.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class StarModule {
    protected JavaPlugin plugin;
    protected String name;
    protected boolean enabled = true;
    protected Config config;
    
    protected Map<String, CommandExecutor> commands = new HashMap<>();
    protected Set<Listener> listeners = new HashSet<>();
    
    public StarModule(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        config = new Config(plugin, name.toLowerCase() + ".yml");
    }
    
    public final void init() {
        config.setup();
        loadValuesFromConfig();
        createCommandExecutors();
        createEventListeners();
    }
    
    public void save() {
        config.save();
    }
    
    protected void createCommandExecutors() {
        
    }
    protected void createEventListeners() {
        
    }
    protected void loadValuesFromConfig() {
        
    }
    
    public final String getName() {
        return name;
    }
    
    public final boolean isEnabled() {
        return enabled;
    }
    
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public final void registerCommands() {
        this.commands.forEach((name, executor) -> plugin.getCommand(name).setExecutor(executor));
    }
    
    public final void registerListeners() {
        this.listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }
    
    public final Config getConfig() {
        return config;
    }
}
