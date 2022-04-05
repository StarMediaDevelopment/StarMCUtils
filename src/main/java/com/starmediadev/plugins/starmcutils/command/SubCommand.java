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
        System.out.println("Handling sub commands of the sub command " + this.getName());
        if (this.subCommands.isEmpty()) {
            System.out.println("Sub commands is empty for " + this.name);
            return;
        }
        
        String[] previousArgs = new String[oldPreviousArgs.length + 1];
        previousArgs[previousArgs.length - 1] = oldLabel;
        String label = args[0];
        String[] afterArgs = new String[args.length - 1];
        if (args.length > 1) {
            System.arraycopy(args, 1, afterArgs, 0, args.length - 1);
        }
    
        System.out.println("Determined the previous arguments to be " + Arrays.toString(previousArgs));
        System.out.println("Determined the label for the additional sub command to be " + label);
        System.out.println("Determined the remaining arguments to be " + Arrays.toString(afterArgs));
    
        boolean foundSubCommand = false;
        for (SubCommand subCommand : this.subCommands) {
            System.out.println("Checking registered subcommand " + subCommand.getName() + " in parent " + this.getName());
            if (subCommand.matchesName(label)) {
                System.out.println("The registered sub command " + subCommand.getName() + "in parent" + this.getName() + " matches the label provided");
                foundSubCommand = true;
                System.out.println("Checking sub command arguments");
                IncrementalMap<Argument> arguments = subCommand.getArguments();
                if (!arguments.isEmpty()) {
                    for (int i = 0; i < arguments.size(); i++) {
                        Argument argument = arguments.get(i);
                        System.out.println("Found argument: " + argument.getName());
                        if (argument != null) {
                            if (argument.isRequired()) {
                                System.out.println("Argument " + argument.getName() + " is required");
                                try {
                                    String arg = afterArgs[i];
                                    if (arg == null || arg.equals("")) {
                                        throw new IllegalArgumentException();
                                    }
                                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                                    actor.sendMessage("&c" + argument.getErrorMessage());
                                    return;
                                }
                            } else {
                                System.out.println("Argument " + argument.getName() + " is not required");
                            }
                        }
                    }
                } else {
                    System.out.println("No arguments for sub command: " + subCommand.getName() + " are registered");
                }
                System.out.println("Handling direct sub command implementation for " + subCommand.getName() + " in parent " + this.getName());
                subCommand.handleCommand(this, actor, previousArgs, label, afterArgs);
                System.out.println("Handling additional sub commands in the sub command " + subCommand.getName() + " in parent " + this.getName());
                subCommand.handleSubCommands(actor, previousArgs, label, args);
            }
        }
    
        if (!foundSubCommand) {
            actor.sendMessage("&cCould not find a sub command.");
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
