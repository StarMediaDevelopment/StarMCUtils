package com.starmediadev.plugins.starmcutils.module;

import com.starmediadev.plugins.starmcutils.util.Config;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a module.
 * This is for shared code as some of the other projects will use this class. 
 * @param <P>
 */
public abstract class StarModule<P extends JavaPlugin> {
    protected P plugin;
    protected String name;
    protected boolean enabled = true;
    protected Config config;
    
    protected Map<String, CommandExecutor> commands = new HashMap<>();
    protected Set<Listener> listeners = new HashSet<>();
    protected Map<String, Object> defaultConfigValues = new HashMap<>();
    
    public StarModule(P plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        config = new Config(plugin, name.toLowerCase() + ".yml");
    }
    
    public final void init() {
        config.setup();
        registerDefaultConfigValues();
        saveDefaultValues();
        loadValuesFromConfig();
        createCommandExecutors();
        createEventListeners();
    }
    
    public final void save() {
        saveConfigSettings();
        config.save();
    }
    
    protected void registerDefaultConfigValues() {
    }
    
    protected void createCommandExecutors() {
        
    }
    protected void createEventListeners() {
        
    }
    protected void loadValuesFromConfig() {
        
    }
    
    protected void saveConfigSettings() {
        
    }
    
    private void saveDefaultValues() {
        this.defaultConfigValues.forEach((key, value) -> {
            config.getConfiguration().set(key, value);
        });
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
