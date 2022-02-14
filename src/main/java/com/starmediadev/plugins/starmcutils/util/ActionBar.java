package com.starmediadev.plugins.starmcutils.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * This class is meant to represent an ActionBar. You really only need to create one instance per actionbar and just use the send method
 */
public record ActionBar(String message) {
    public ActionBar(String message) {
        this.message = MCUtils.color(message);
    }
    
    /**
     * Sends this ActionBar to a player
     * @param player The player to send it to.
     */
    public void send(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
