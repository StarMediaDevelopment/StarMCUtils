package com.starmediadev.plugins.starmcutils.inventory;

import com.starmediadev.plugins.starmcutils.helper.ReflectionHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ItemUtils {
    
    private static Class<?> compoundTagClass, tagParserClass, craftItemStackClass, nmsItemStackClass;
    private static Method asNMSCopyMethod, hasTagMethod, getTagMethod, parseTagMethod, setTagMethod;
    
    static {
        try {
            compoundTagClass = ReflectionHelper.getNMSClass("nbt", "CompoundTag");
            tagParserClass = ReflectionHelper.getNMSClass("nbt", "TagParser");
            craftItemStackClass = ReflectionHelper.getCraftBukkitClass("inventory", "CraftItemStack");
            nmsItemStackClass = ReflectionHelper.getNMSClass("world.item", "ItemStack");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    
        try {
            asNMSCopyMethod = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            hasTagMethod = nmsItemStackClass.getDeclaredMethod("hasTag");
            getTagMethod = nmsItemStackClass.getDeclaredMethod("getTag");
            parseTagMethod = tagParserClass.getDeclaredMethod("parseTag", String.class);
            setTagMethod = nmsItemStackClass.getDeclaredMethod("setTag", compoundTagClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public static ItemStack saveItemsInNBT(ItemStack item, ItemStack[] items) {
        String dataString = itemsToString(items);
        NBTWrapper.addNBTString(item, "invdata", dataString);
        return item;
    }
    public static ItemStack[] getItemsFromNBT(ItemStack item) {
        String itemString = NBTWrapper.getNBTString(item, "invdata");

        if (!itemString.equals("")) {
            return stringToItems(itemString);
        }
        return null;
    }
    public static String itemsToString(ItemStack[] items) {
        try {
            Map<String, Object>[] serializedItemStacks = serializeItemStacks(items);
            if (serializedItemStacks == null) return "empty";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializeItemStacks(items));
            oos.flush();
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
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
    
                try {
                    Object nmsItem = asNMSCopyMethod.invoke(null, is);
                    
                    if ((boolean) hasTagMethod.invoke(nmsItem)) {
                        result[i].put("tag", getTagMethod.invoke(nmsItem).toString());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
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
    
            try {
                Object nmsItem = asNMSCopyMethod.invoke(null, is);
                if ((boolean) hasTagMethod.invoke(nmsItem)) {
                    result.put("tag", getTagMethod.invoke(nmsItem).toString());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
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

    private static ItemStack deserializeItemMap(Map<String, Object> s) {
        Map<String, Object> im = new HashMap<>((Map<String, Object>) s.remove("meta"));
        im.put("==", "ItemMeta");
        ItemStack is = ItemStack.deserialize(s);
        is.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(im));
        if (s.containsKey("tag")) {
            try {
                Object nmsItem = asNMSCopyMethod.invoke(null, is);
                Object compoundTag = parseTagMethod.invoke(null, s.get("tag"));
                setTagMethod.invoke(nmsItem, compoundTag);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
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
