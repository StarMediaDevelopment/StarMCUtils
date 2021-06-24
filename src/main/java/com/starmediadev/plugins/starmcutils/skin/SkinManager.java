package com.starmediadev.plugins.starmcutils.skin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinManager {
    private static final String skinUrlString = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";
    
    private Map<UUID, Skin> skins = new HashMap<>();
    
    public SkinManager() {}
    
    public Skin getSkin(UUID uuid) {
        if (skins.containsKey(uuid)) {
            return skins.get(uuid);
        }
        
        String profileUrl = skinUrlString.replace("{uuid}", uuid.toString().replace("-", ""));

        URL url;
        try {
            url = new URL(profileUrl);
        } catch (MalformedURLException e) {
            return null;
        }

        StringBuilder jsonBuilder = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            int read;
            char[] chars = new char[256];
            while ((read = reader.read()) != -1) {
                jsonBuilder.append(chars, 0, read);
            }
        } catch (Exception e) {
            return null;
        }
        
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonBuilder.toString());
        JsonArray properties = (JsonArray) jsonObject.get("properties");
        
        JsonObject property = (JsonObject) properties.get(0);
        var name = property.get("name").getAsString();
        var value = property.get("value").getAsString();
        var signature = property.get("signature").getAsString();
        
        Skin skin = new Skin(uuid, name, signature, value);
        this.skins.put(uuid, skin);
        return skin;
    }
}
