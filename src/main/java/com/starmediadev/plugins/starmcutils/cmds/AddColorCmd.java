package com.starmediadev.plugins.starmcutils.cmds;

import com.starmediadev.plugins.starmcutils.StarMCUtils;
import com.starmediadev.plugins.starmcutils.util.ColorUtils;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public record AddColorCmd(StarMCUtils plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(MCUtils.color("&cUsage: /addcolor <code> <hexcolor>"));
            return true;
        }
        
        String code = args[0], hexColor = args[1];
        
        if (code.length() != 2) {
            sender.sendMessage(MCUtils.color("&cThe color code must 2 characters, no more, no less"));
            return true;
        }
        
        if (!ColorUtils.validColorChar(code.charAt(0))) {
            sender.sendMessage(MCUtils.color("&cThe beginning code is not a valid code."));
            return true;
        }
        
        if (hexColor.charAt(0) != '#') {
            sender.sendMessage(MCUtils.color("&cYou must have a # at the beginning of the hex code."));
            return true;
        }
        
        if (hexColor.length() != 7) {
            sender.sendMessage(MCUtils.color("&cYou must have a total of 7 characters, the # at the beginning, and 6 characters for the value."));
            return true;
        }
        
        ColorUtils.addCustomColor(code, hexColor);
        sender.sendMessage(MCUtils.color("&eYou added a color with code &b") + code + MCUtils.color(" &eand hex value &b") + hexColor);
        return true;
    }
}
