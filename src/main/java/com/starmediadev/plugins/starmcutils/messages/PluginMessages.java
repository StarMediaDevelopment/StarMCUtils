package com.starmediadev.plugins.starmcutils.messages;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PluginMessages {
    
    private final File file;
    private final FileConfiguration config;
    
    private final Map<String, Message> messages = new HashMap<>();
    
    public PluginMessages(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    
    public String get(String name, String... args) {
        Message message = messages.get(name.toLowerCase());
        if (message != null) {
            return MCUtils.color(message.get(args));
        }
        return "";
    }
    
    public void add(String name, String message) {
        this.messages.put(name.toLowerCase(), new Message(message));
    }
    
    public void save() {
        this.messages.forEach((key, message) -> {
            config.set("messages." + key, message.get());
        });
        try {
            config.save(file);
        } catch (IOException e) {}
    }
    
    public void load() {
        if (!this.messages.isEmpty()) {
            this.messages.clear();
        }
        ConfigurationSection section = this.config.getConfigurationSection("messages");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            this.messages.put(key, new Message(section.getString(key)));
        }
    }
}
