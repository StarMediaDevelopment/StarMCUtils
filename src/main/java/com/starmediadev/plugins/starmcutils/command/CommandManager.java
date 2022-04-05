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
    
        System.out.println("Main Command Permission Check");
        
        if (starCommand.getPermission() != null && !starCommand.getPermission().equals("")) {
            if (!sender.hasPermission(starCommand.getPermission())) {
                sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
                return true;
            } else {
                System.out.println("Has permission");
            }
        }
    
        System.out.println("Arguments Check");
        
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
        System.out.println("Handle Command");
        starCommand.handleSubCommands(actor, args);
        System.out.println("Handle Sub Commands");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        StarCommand starCommand = null;
        for (StarCommand command : this.commands) {
            if (command.matchesName(label)) {
                starCommand = command;
                break;
            }
        }
        
        if (starCommand == null) {
            return null;
        }
        
        if (starCommand.isOnlyPlayer() && !(sender instanceof Player)) {
            return null;
        }
        
        if (starCommand.isOnlyConsole() && !(sender instanceof ConsoleCommandSender)) {
            return null;
        }
        
        //return getCompletions(starCommand, args, args.length - 1, 0);
        return null;
    }
    
    private List<String> getCompletions(StarCommand starCommand, String[] args, int argIndex, int cmdIndex) {
        if (argIndex < 0) {
            return null;
        }
        List<String> completions = new ArrayList<>();
        
        if (cmdIndex == argIndex) {
            if (starCommand.getArguments().size() > 0) {
                for (Argument argument : starCommand.getArguments().values()) {
                    if (argument.getCompletions().size() > 0) {
                        for (String completion : argument.getCompletions()) {
                            if (completion.startsWith(args[argIndex])) {
                                completions.add(completion);
                            }
                        }
                    } else {
                        char leadSymbol, endSymbol;
                        if (argument.isRequired()) {
                            leadSymbol = '<';
                            endSymbol = '>';
                        } else {
                            leadSymbol = '[';
                            endSymbol = ']';
                        }
                        completions.add(leadSymbol + argument.getName() + endSymbol);
                    }
                }
            } else if (starCommand.getSubCommands().size() > 0) {
                for (SubCommand subCommand : starCommand.getSubCommands()) {
                    if (subCommand.getName().startsWith(args[argIndex])) {
                        completions.add(subCommand.getName());
                    }
                }
            }
        } else {
            if (starCommand.getSubCommands().size() >= argIndex) {
                cmdIndex++;
                for (SubCommand subCommand : starCommand.getSubCommands()) {
                    completions.addAll(getCompletions(subCommand, args, argIndex, cmdIndex));
                }
            }
        }
        
        return completions;
    }
    
    public JavaPlugin getPlugin() {
        return plugin;
    }
    
    public List<StarCommand> getCommands() {
        return commands;
    }
}