package com.starmediadev.plugins.starmcutils.inventory;

import com.starmediadev.plugins.starmcutils.helper.ReflectionHelper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class NBTWrapper {
    
    private static Class<?> stringTagClass, craftItemStackClass, nmsItemStackClass, compoundTagClass;
    private static Method getOrCreateTagMethod, putMethod, asNMSCopyMethod, asBukkitCopyMethod, stringTagValueOfMethod,
            getTagMethod, setTagMethod, getStringMethod, copyMethod, removeMethod;
    
    static {
        try {
            stringTagClass = ReflectionHelper.getNMSClass("nbt", "StringTag");
            craftItemStackClass = ReflectionHelper.getCraftBukkitClass("inventory", "CraftItemStack");
            nmsItemStackClass = ReflectionHelper.getNMSClass("world.item", "ItemStack");
            compoundTagClass = ReflectionHelper.getNMSClass("nbt", "CompoundTag");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
            getOrCreateTagMethod = nmsItemStackClass.getDeclaredMethod("getOrCreateTag");
            putMethod = compoundTagClass.getDeclaredMethod("put", String.class, compoundTagClass);
            asNMSCopyMethod = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asBukkitCopyMethod = craftItemStackClass.getDeclaredMethod("asBukkitCopy", nmsItemStackClass);
            stringTagValueOfMethod = stringTagClass.getDeclaredMethod("valueOf", String.class);
            getTagMethod = nmsItemStackClass.getDeclaredMethod("getTag");
            setTagMethod = nmsItemStackClass.getDeclaredMethod("setTag");
            getStringMethod = compoundTagClass.getDeclaredMethod("getString", String.class);
            copyMethod = nmsItemStackClass.getDeclaredMethod("copy");
            removeMethod = compoundTagClass.getDeclaredMethod("remove", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    private NBTWrapper() {
    }
    
    public static ItemStack addNBTString(ItemStack itemStack, String key, String value) {
        try {
            Object nmsItem = asNMSCopyMethod.invoke(null, itemStack);
            Object compound = getOrCreateTagMethod.invoke(nmsItem);
            putMethod.invoke(compound, key, stringTagValueOfMethod.invoke(null, value));
            setTagMethod.invoke(nmsItem, compound);
            return (ItemStack) asBukkitCopyMethod.invoke(null, nmsItem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return itemStack;
    }
    
    public static String getNBTString(ItemStack itemStack, String key) {
        try {
            Object nmsItem = asNMSCopyMethod.invoke(null, itemStack);
            Object tagCompound = getTagMethod.invoke(nmsItem);
            if (tagCompound != null) {
                return (String) getStringMethod.invoke(tagCompound, key);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static ItemStack cloneItemStack(ItemStack itemStack) {
        try {
            Object nmsItem = asNMSCopyMethod.invoke(null, itemStack);
            Object copy = copyMethod.invoke(nmsItem);
            return (ItemStack) asBukkitCopyMethod.invoke(null, copy);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return itemStack.clone();
    }
    
    public static ItemStack resetTags(ItemStack itemStack, String key) {
        try {
            Object nmsItem = asNMSCopyMethod.invoke(null, itemStack);
            Object tag = getTagMethod.invoke(nmsItem);
            if (tag != null) {
                removeMethod.invoke(tag, key);
            }
            setTagMethod.invoke(nmsItem, tag);
            return (ItemStack) asBukkitCopyMethod.invoke(null, nmsItem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return itemStack;
    }
}
