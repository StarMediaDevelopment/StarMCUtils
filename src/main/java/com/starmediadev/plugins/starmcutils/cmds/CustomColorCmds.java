package com.starmediadev.plugins.starmcutils.cmds;

import com.starmediadev.plugins.starmcutils.StarMCUtils;
import com.starmediadev.plugins.starmcutils.util.ColorUtils;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

@Command("customcolors")
public record CustomColorCmds(StarMCUtils plugin) {
    
    @Subcommand("add")
    @Description("Adds a custom color to the available colors")
    @CommandPermission("starmcutils.colors.add")
    public void addColor(CommandSender sender, @Named("colorCode") String colorCode, @Named("hexValue") String hexValue) {
        checkColorValues(colorCode, hexValue);
    
        if (ColorUtils.getCustomColor(colorCode) != null) {
            throw new CommandErrorException("That color code is already defined.");
        }
        
        ColorUtils.addCustomColor(colorCode, hexValue);
        sender.sendMessage(MCUtils.color("&eYou added a color with code &b") + colorCode + MCUtils.color(" &eand hex value &b") + hexValue);
    }
    
    private void checkColorValues(String colorCode, String hexValue) {
        if (colorCode.length() != 2) {
            throw new CommandErrorException("Invalid color code. Must be 2 characters, no more, no less.");
        }
        
        if (hexValue.length() != 7 || hexValue.charAt(0) != '#') {
            throw new CommandErrorException("Invalid Hex Value. Must have a # and 6 characters for the value.");
        }
        
        if (!ColorUtils.validColorChar(colorCode.charAt(0))) {
            throw new CommandErrorException("The beginning code is not a valid code.");
        }
    }
    
    @Subcommand("list")
    @Description("Get a list of all colors and their values")
    @CommandPermission("starmcutils.colors.list")
    public void listColors(CommandSender sender) {
        sender.sendMessage(MCUtils.color("&eList of available colors"));
        ColorUtils.getColors().forEach((code, value) -> {
            String hex = Integer.toHexString(value.getColor().getRGB()).toUpperCase();
            hex = hex.substring(2);
            sender.sendMessage(MCUtils.color("&7- ") + code + MCUtils.color("&7: #") + hex);
        });
    }
    
    @Subcommand("set")
    @Description("Sets a color code")
    @CommandPermission("starmcutils.colors.set")
    public void setColor(CommandSender sender, @Named("colorCode") String colorCode, @Named("hexValue") String hexValue) {
        checkColorValues(colorCode, hexValue);
    
        if (ColorUtils.getCustomColor(colorCode) == null) {
            throw new CommandErrorException("That color code is not defined.");
        }
        
        ColorUtils.addCustomColor(colorCode, hexValue);
        sender.sendMessage(MCUtils.color("&eYou set the color code &b") + colorCode + MCUtils.color(" &eto &b") + hexValue);
    }
}
