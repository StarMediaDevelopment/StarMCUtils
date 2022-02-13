package com.starmediadev.plugins.starmcutils.inventory;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ItemUtils {
    public static ItemStack saveItemsInNBT(ItemStack item, ItemStack[] items) throws Exception {
        String dataString = itemsToString(items);
        NBTWrapper.addNBTString(item, "invdata", dataString);
        return item;
    }
    public static ItemStack[] getItemsFromNBT(ItemStack item) throws Exception {
        String itemString = NBTWrapper.getNBTString(item, "invdata");

        if (!itemString.equals("")) {
            return stringToItems(itemString);
        }
        return null;
    }
    public static MysqlxDatatypes.Scalar.String itemsToString(ItemStack[] items) {
        try {
            Map<String, Object>[] serializedItemStacks = serializeItemStacks(items);
            if (serializedItemStacks == null) return "empty";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializeItemStacks(items));
            oos.flush();
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            //Logger.exception(e);
        }
        return "";
    }
    public static ItemStack[] stringToItems(String s) {
        if (!StringUtils.isEmpty(s) && !s.equalsIgnoreCase("empty")) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(s));
                ObjectInputStream ois = new ObjectInputStream(bis);
                return deserializeItemStack((Map<String, Object>[]) ois.readObject());
            } catch (Exception e) {
                //Logger.exception(e);
            }
        }
        return new ItemStack[]{new ItemStack(Material.AIR)};
    }
    private static Map<String, Object>[] serializeItemStacks(ItemStack[] items) {
        Map<String, Object>[] result = new Map[items.length];
        boolean empty = true;

        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is == null || is.getType().equals(Material.AIR)) {
                result[i] = null;
            } else {
                empty = false;
                result[i] = is.serialize();
                if (is.hasItemMeta()) {
                    result[i].put("meta", is.getItemMeta().serialize());
                }

                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
                if (nmsItem.hasTag()) {
                    result[i].put("tag", nmsItem.getTag().toString());
                }
            }
        }

        if (empty) return null;
        return result;
    }
    public static String serializeItemStack(ItemStack is) {
        if (is == null || is.getType().equals(Material.AIR)) {
            return null;
        } else {
            Map<String, Object> result;
            result = is.serialize();
            if (is.hasItemMeta()) {
                result.put("meta", is.getItemMeta().serialize());
            }

            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
            if (nmsItem.hasTag()) {
                result.put("tag", nmsItem.getTag().toString());
            }

            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(result);
                oos.flush();
                return Base64.getEncoder().encodeToString(bos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
    private static ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
        ItemStack[] items = new ItemStack[map.length];

        for (int i = 0; i < items.length; i++) {
            Map<String, Object> s = map[i];
            if (s == null || s.isEmpty()) {
                items[i] = null;
            } else {
                try {
                    if (s.containsKey("meta")) {
                        items[i] = deserializeItemMap(s);
                    } else {
                        items[i] = ItemStack.deserialize(s);
                    }
                } catch (Exception e) {
                    //Logger.exception(e);
                    items[i] = null;
                }
            }

        }

        return items;
    }

    private static ItemStack deserializeItemMap(Map<String, Object> s) throws CommandSyntaxException {
        Map<String, Object> im = new HashMap<>((Map<String, Object>) s.remove("meta"));
        im.put("==", "ItemMeta");
        ItemStack is = ItemStack.deserialize(s);
        is.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(im));
        if (s.containsKey("tag")) {
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
            CompoundTag compound = TagParser.parseTag((String) s.get("tag"));
            nmsItem.setTag(compound);
        }
        return is;
    }

    public static ItemStack deserializeItemStack(String s) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(s));
            ObjectInputStream ois = new ObjectInputStream(bis);
            Map<String, Object> map = (Map<String, Object>) ois.readObject();

            ItemStack itemStack;
            if (map == null || map.isEmpty()) {
                itemStack = null;
            } else {
                try {
                    if (map.containsKey("meta")) {
                        itemStack = deserializeItemMap(map);
                    } else {
                        itemStack = ItemStack.deserialize(map);
                    }
                } catch (Exception e) {
                    itemStack = null;
                }
            }
            return itemStack;
        } catch (Exception e) {
            return null;
        }
    }
}
