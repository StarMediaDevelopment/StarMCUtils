package com.starmediadev.plugins.starmcutils.command;

import com.starmediadev.utils.collection.IncrementalMap;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class SubCommand extends StarCommand {
    protected final StarCommand parent;
    
    public SubCommand(StarCommand parent, String name, String description, String permission) {
        this(parent, name, description, permission, new ArrayList<>());
    }
    
    public SubCommand(StarCommand parent, String name, String description, String permission, List<String> aliases) {
        this(parent, name, description, permission, false, false, aliases);
    }
    
    public SubCommand(StarCommand parent, String name, String description, String permission, boolean onlyPlayer, boolean onlyConsole, List<String> aliases) {
        super(name, description, permission, onlyPlayer, onlyConsole, aliases);
        this.parent = parent;
    }
    
    @Override
    public final void handleCommand(CommandActor actor, String label, String[] args) {
        throw new UnsupportedOperationException("Wrong method called for a sub command");
    }
    
    public void handleCommand(StarCommand starCommand, CommandActor actor, String[] previousArgs, String label, String[] args) {
        
    }
    
    @Override
    public final void handleSubCommands(CommandActor actor, String[] args) {
        throw new UnsupportedOperationException("Invalid sub command method called.");
    }
    
    public final void handleSubCommands(CommandActor actor, String[] oldPreviousArgs, String oldLabel, String[] args) {
        if (this.subCommands.isEmpty()) {
            return;
        }
        
        String[] previousArgs = new String[oldPreviousArgs.length + 1];
        previousArgs[previousArgs.length - 1] = oldLabel;
        String label = args[0];
        String[] afterArgs = new String[args.length - 1];
        if (args.length > 1) {
            System.arraycopy(args, 1, afterArgs, 0, args.length - 1);
        }
    
        boolean foundSubCommand = false;
        for (SubCommand subCommand : this.subCommands) {
            if (subCommand.matchesName(label)) {
                foundSubCommand = true;
                IncrementalMap<Argument> arguments = subCommand.getArguments();
                if (!arguments.isEmpty()) {
                    for (int i = 0; i < arguments.size(); i++) {
                        Argument argument = arguments.get(i);
                        if (argument != null) {
                            if (argument.isRequired()) {
                                try {
                                    String arg = afterArgs[i];
                                    if (arg == null || arg.equals("")) {
                                        throw new IllegalArgumentException();
                                    }
                                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                                    actor.sendMessage("&c" + argument.getErrorMessage());
                                    return;
                                }
                            }
                        }
                    }
                }
                subCommand.handleCommand(this, actor, previousArgs, label, afterArgs);
                subCommand.handleSubCommands(actor, previousArgs, label, args);
            }
        }
    }
    
    public StarCommand getParent() {
        return parent;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public boolean isOnlyPlayer() {
        return onlyPlayer;
    }
    
    public boolean isOnlyConsole() {
        return onlyConsole;
    }
    
    public List<String> getAliases() {
        return aliases;
    }
    
    public boolean matchesName(String label) {
        if (label.equalsIgnoreCase(this.name)) {
            return true;
        } else {
            for (String alias : this.aliases) {
                if (label.equalsIgnoreCase(alias)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String getUsage() {
       return "";
    }
}
