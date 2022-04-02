package com.starmediadev.plugins.starmcutils.command;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.utils.collection.IncrementalMap;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CommandManager implements TabExecutor {
    private JavaPlugin plugin;
    private List<StarCommand> commands = new ArrayList<>();
    
    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StarCommand starCommand = null;
        for (StarCommand command : this.commands) {
            if (command.matchesName(label)) {
                starCommand = command;
            }
        }
        
        if (starCommand == null) {
            Bukkit.getServer().getLogger().warning("A Plugin Command with the name " + cmd.getName() + " is registered with the StarMCUtils CommandAPI for plugin " + plugin.getName() + " but does not have an implementation registered. Please contact the plugin author.");
            return true;
        }
        
        if (starCommand.isOnlyConsole() && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(MCUtils.color("&cOnly the console can use that command."));
            return true;
        }
        
        if (starCommand.isOnlyPlayer() && !(sender instanceof Player)) {
            sender.sendMessage(MCUtils.color("&cOnly players can use that command."));
            return true;
        }
        
        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(MCUtils.color("&cOnly players and console can use that command."));
            return true;
        }
        
        if (starCommand.getPermission() != null && !starCommand.getPermission().equals("")) {
            if (!sender.hasPermission(starCommand.getPermission())) {
                sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
                return true;
            }
        }
        
        CommandActor actor = new CommandActor(sender);
        IncrementalMap<Argument> arguments = starCommand.getArguments();
        if (!arguments.isEmpty()) {
            for (int i = 0; i < arguments.size(); i++) {
                Argument argument = arguments.get(i);
                if (argument != null) {
                    if (argument.isRequired()) {
                        try {
                            String arg = args[i];
                            if (arg == null || arg.equals("")) {
                                throw new IllegalArgumentException();
                            }
                        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                            sender.sendMessage(MCUtils.color("&c" + argument.getErrorMessage()));
                            return true;
                        }
                    }
                }
            }
        }
        starCommand.handleCommand(actor, label, args);
        starCommand.handleSubCommands(actor, args);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
    
    public JavaPlugin getPlugin() {
        return plugin;
    }
    
    public List<StarCommand> getCommands() {
        return commands;
    }
}