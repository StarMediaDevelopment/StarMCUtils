package com.starmediadev.starmcutils.chat;

import com.starmediadev.starmcutils.util.MCUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatFormatter {
    
    protected String format;
    
    protected List<ChatVariableHandler> variableHandlers = new ArrayList<>();

    public ChatFormatter(String format) {
        this.format = format;
    }
    
    public String format(Player player, String message) {
        String format = this.format;
        for (ChatVariableHandler variableHandler : this.variableHandlers) {
            format = variableHandler.handleVariable(format, player);
        }
        format = format.replace("{message}", message);
        return MCUtils.color(format);
    }
}
