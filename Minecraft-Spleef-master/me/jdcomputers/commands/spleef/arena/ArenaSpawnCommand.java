package me.jdcomputers.commands.spleef.arena;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArenaSpawnCommand extends Command {
    public ArenaSpawnCommand(Command parent) {
        super("Spawn", "Sets the spawn location for a specifc arena.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_SPAWN, Permissions.ARENA_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Correct usage: /spleef arena spawn <name>");

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
        Location loc = player.getLocation();

        int minX = Math.min(from.getBlockX(), to.getBlockX());
        int minY = Math.min(from.getBlockY(), to.getBlockY());
        int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
        int maxX = Math.max(from.getBlockX(), to.getBlockX());
        int maxY = Math.max(from.getBlockY(), to.getBlockY());
        int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        if (x < minX || x > maxX || y < minY + 1 || y > maxY - 3 || z < minZ || z > maxZ) {
            player.sendMessage(ChatColor.RED + "The location you want to set the spawn for is outside of the arena and is invalid.");

            return;
        }

        arena.set("arenas." + name + ".spawn", player.getLocation());
        arena.save();

        player.sendMessage(ChatColor.WHITE + name + "'s" + ChatColor.GOLD + " spawn point has been set successfully.");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_SPAWN, Permissions.ARENA_ALL))
            return null;

        FileManager arena = Spleef.getInstance().getArenas().load();

        Set<String> keys = arena.getSection("arenas").getKeys(false);

        return new ArrayList<>(keys);
    }
}
