package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import me.jdcomputers.commands.spleef.arena.*;
import me.jdcomputers.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefArenaCommand extends Command {
    public SpleefArenaCommand(Command parent) {
        super("Arena", "Create, list, update, or remove arenas.", parent);

        addSubCommand(new ArenaCreateCommand(this));
        addSubCommand(new ArenaListCommand(this));
        addSubCommand(new ArenaEditCommand(this));
        addSubCommand(new ArenaDeleteCommand(this));
        addSubCommand(new ArenaSpawnCommand(this));
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA, Permissions.ARENA_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        player.sendMessage(ChatColor.RED + "Correct usage: /spleef arena <list|create|edit|delete|spawn>");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA, Permissions.ARENA_ALL))
            return null;

        List<String> output = new ArrayList<>();

        output.add("list");
        output.add("create");
        output.add("edit");
        output.add("delete");
        output.add("spawn");

        return output;
    }
}
