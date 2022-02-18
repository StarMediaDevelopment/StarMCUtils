package com.starmediadev.plugins.starmcutils.util;

import com.starmediadev.utils.helper.FileHelper;
import com.starmediadev.utils.helper.StringHelper;
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
    private String name, modulesFolder = "";
    
    public Config(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }
    
    public Config(JavaPlugin plugin, String modulesFolder, String name) {
        this.plugin = plugin;
        this.modulesFolder = modulesFolder;
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
        if (StringHelper.isEmpty(modulesFolder)) {
            this.file = new File(plugin.getDataFolder(), name);
        } else {
            this.file = Path.of(plugin.getDataFolder().toPath().toString(), modulesFolder, name).toFile();
        }
        FileHelper.createFileIfNotExists(file.toPath());
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
