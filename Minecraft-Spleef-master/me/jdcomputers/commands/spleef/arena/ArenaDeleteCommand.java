package me.jdcomputers.commands.spleef.arena;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import me.jdcomputers.worldedit.WorldEditCreations;
import me.jdcomputers.worlds.ArenaWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArenaDeleteCommand extends Command {
    public ArenaDeleteCommand(Command parent) {
        super("Delete", "Delete an arena permanently.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_DELETE, Permissions.ARENA_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Correct usage: /spleef arena delete <name>");

            return;
        }

        FileManager arena = Spleef.getInstance().getArenas().load();

        Set<String> keys = arena.getSection("arenas").getKeys(false);

        String name = args[2];

        if (!keys.contains(name)) {
            player.sendMessage(ChatColor.RED + "That name does not exist in the arenas.");

            return;
        }

        arena = Spleef.getInstance().getArenas().load();

        Location from = arena.getLocation("arenas." + name + ".from");
        Location to = arena.getLocation("arenas." + name + ".to");

        WorldEditCreations.setBlocks(Material.AIR, from, to);

        arena.set("arenas." + name, null);
        arena.save();

        player.sendMessage(ChatColor.GOLD + "The arena " + ChatColor.WHITE + name + ChatColor.GOLD + " was successfully deleted.");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_DELETE, Permissions.ARENA_ALL))
            return null;

        FileManager arena = Spleef.getInstance().getArenas().load();

        Set<String> keys = arena.getSection("arenas").getKeys(false);

        return new ArrayList<>(keys);
    }
}
