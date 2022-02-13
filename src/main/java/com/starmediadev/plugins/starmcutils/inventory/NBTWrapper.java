package com.starmediadev.plugins.starmcutils.inventory;

import net.minecraft.nbt.StringTag;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class NBTWrapper {

    private NBTWrapper() {}

    public static ItemStack addNBTString(ItemStack itemStack, String key, String value) {
        var nmsStack = CraftItemStack.asNMSCopy(itemStack);
        var tagCompound = nmsStack.getOrCreateTag();
        tagCompound.put(key, StringTag.valueOf(value));
        nmsStack.setTag(tagCompound);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static String getNBTString(ItemStack itemStack, String key) {
        var nmsStack = CraftItemStack.asNMSCopy(itemStack);
        var tagCompound = nmsStack.getTag();
        if (tagCompound == null) return "";
        return tagCompound.getString(key);
    }

    public static ItemStack cloneItemStack(ItemStack itemStack) {
        var nmsStack = CraftItemStack.asNMSCopy(itemStack);
        var copy = nmsStack.copy();
        return CraftItemStack.asBukkitCopy(copy);
    }

    public static ItemStack resetTags(ItemStack itemStack, String key) {
        var nmsStack = CraftItemStack.asNMSCopy(itemStack);
        var tag = nmsStack.getTag();
        if (tag != null) {
            tag.remove(key);
        }
        nmsStack.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }
}
