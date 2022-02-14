package com.starmediadev.plugins.starmcutils.skin;

import java.util.UUID;

/**
 * This represents a Minecraft Skin from the Mojang API
 */
public record Skin(UUID uniqueId, String name, String signature, String value) {
}