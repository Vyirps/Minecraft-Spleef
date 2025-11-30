package me.jdcomputers.commands.spleef.arena;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.inventory.InventoryDefaults;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import me.jdcomputers.worlds.ArenaWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArenaEditCommand extends Command {
    public ArenaEditCommand(Command parent) {
        super("Edit", "Teleport to and begin editing an arena.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_EDIT, Permissions.ARENA_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Correct usage: /spleef arena edit <name>");

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

        if (!arena.has("arenas." + name + ".spawn")) {
            Location from = arena.getLocation("arenas." + name + ".from");
            Location to = arena.getLocation("arenas." + name + ".to");

            arena.set("arenas." + name + ".spawn", new Location(from.getWorld(), (from.getBlockX() + to.getBlockX()) / 2, 1, (from.getBlockZ() + to.getBlockZ()) / 2));
        }

        Location spawn = arena.getLocation("arenas." + name + ".spawn");

        player.sendMessage(ChatColor.GOLD + "Teleporting...");
        player.teleport(spawn);
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();

        InventoryDefaults.setupCreation(player);
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_EDIT, Permissions.ARENA_ALL))
            return null;

        FileManager arena = Spleef.getInstance().getArenas().load();

        Set<String> keys = arena.getSection("arenas").getKeys(false);

        return new ArrayList<>(keys);
    }
}
