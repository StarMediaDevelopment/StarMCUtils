package com.starmediadev.plugins.starmcutils.util;

import com.starmediadev.utils.Utils;
import com.starmediadev.utils.helper.StringHelper;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityNames {
    public static Map<EntityType, String> entityNames = new HashMap<>();
    //These are used for instantiation, which is needed for the setting of the names.
    private static EntityNames instance = new EntityNames();

    private EntityNames() {
        for (EntityType entityType : EntityType.values()) {
            entityNames.put(entityType, StringHelper.capitalizeEveryWord(entityType.name()));
        }

        //This is where custom names can be set.
    }
    public static String getName(EntityType entityType) {
        return entityNames.get(entityType);
    }

    private EntityNames getInstance() {
        return instance;
    }
}
