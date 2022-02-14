package com.starmediadev.plugins.starmcutils.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class to represent a YAML config file and adds passthrough methods and easy methods to setup and save
 */
public class Config {
    private File file;
    private YamlConfiguration yamlConfiguration;
    
    private JavaPlugin plugin;
    private String name;
    
    public Config(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }
    
    //TODO Add pass through methods for YamlConfiguration so that this getter is not needed anymore
    
    /**
     * Gets the YamlConfiguration instance. This is temporary until passthrough methods are implemented
     * @return The YamlConfiguration instance
     */
    public YamlConfiguration getConfiguration() {
        return yamlConfiguration;
    }
    
    /**
     * Sets up this config
     */
    public void setup() {
        this.file = new File(plugin.getDataFolder(), name);
        Path filePath = file.toPath();
        if (Files.notExists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + name + ": " + e.getMessage());
                return;
            }
        }
        
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }
    
    /**
     * Saves this config
     */
    public void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + name);
        }
    }
}
