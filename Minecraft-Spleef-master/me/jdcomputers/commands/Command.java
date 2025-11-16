package me.jdcomputers.commands;

import me.jdcomputers.commands.spleef.SpleefHelpCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final String name;
    private final String description;
    protected final Command parent;
    protected final List<Command> subCommands;

    public Command(String name, String description) {
        this(name, description, null);
    }

    public Command(String name, String description, Command parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.subCommands = new ArrayList<>();
    }

    public void addSubCommand(Command command) {
        subCommands.add(command);
    }

    public abstract void run(Player player, String[] args);
    public abstract List<String> tabComplete(Player player, String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Command find(String subCommand) {
        for (Command sub : subCommands)
            if (sub.getName().equalsIgnoreCase(subCommand))
                return sub;

        return null;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Command getParent() {
        return parent;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }
}