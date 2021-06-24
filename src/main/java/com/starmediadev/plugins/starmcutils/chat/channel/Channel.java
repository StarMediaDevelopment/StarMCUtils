package com.starmediadev.plugins.starmcutils.chat.channel;

import org.bukkit.command.CommandSender;

public class Channel {
    protected static final String VAR_PREFIX = "{channelprefix}", VAR_SENDER = "{sender}", VAR_MESSAGE = "{chatmessage}"; 
    
    protected String name, permission, format, prefix, playerNameFormat, messageFormat, baseColor;
    
    public Channel(String name) {
        this.name = name;
    }
    
    public String format(CommandSender sender, String message) {
        String output = format;
        output = output.replace(VAR_MESSAGE, messageFormat.replace("{message}", message));
        return output;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getFormat() {
        return format;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPlayerNameFormat() {
        return playerNameFormat;
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public String getBaseColor() {
        return baseColor;
    }
}
