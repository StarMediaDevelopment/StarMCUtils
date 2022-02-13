package com.starmediadev.plugins.starmcutils.helper;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class ReflectionHelper {
    private ReflectionHelper() {}
    
    private static final String minecraftVersion, nmsBasePackage = "net.minecraft", craftBukkitBasePackage;
    private static final Map<String, Class<?>> cachedClasses = new HashMap<>();
    
    static {
        minecraftVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        craftBukkitBasePackage = "org.bukkit.craftbukkit." + minecraftVersion;
    }
    
    public static Class<?> getNMSClass(String packageName, String className) throws ClassNotFoundException {
        return getClass(packageName, className, nmsBasePackage);
    }
    
    public static Class<?> getCraftBukkitClass(String packageName, String className) throws ClassNotFoundException {
        return getClass(packageName, className, craftBukkitBasePackage);
    }
    
    private static Class<?> getClass(String packageName, String className, String basePackage) throws ClassNotFoundException {
        String fullyQualifiedName = basePackage + "." + packageName + "." + className;
        if (cachedClasses.containsKey(fullyQualifiedName)) {
            return cachedClasses.get(fullyQualifiedName);
        } else {
            Class<?> clazz = Class.forName(fullyQualifiedName);
            cachedClasses.put(fullyQualifiedName, clazz);
            return clazz;
        }
    }
}
