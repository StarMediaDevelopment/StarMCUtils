package com.starmediadev.starmcutils.chat;

import org.bukkit.command.CommandSender;

public abstract class ChatVariableHandler {
    protected String variable;

    public ChatVariableHandler(String variable) {
        this.variable = variable;
    }
    
    public abstract String handleVariable(String input, CommandSender sender);
}
