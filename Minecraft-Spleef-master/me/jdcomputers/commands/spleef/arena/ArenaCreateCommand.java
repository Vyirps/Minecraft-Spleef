package me.jdcomputers.commands.spleef.arena;

import me.jdcomputers.commands.Command;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.inventory.InventoryDefaults;
import me.jdcomputers.permissions.Permissions;
import me.jdcomputers.src.Spleef;
import me.jdcomputers.worldedit.WorldEditCreations;
import me.jdcomputers.worlds.ArenaWorld;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaCreateCommand extends Command {
    public ArenaCreateCommand(Command parent) {
        super("Create", "Create a new arena for spleef.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_CREATE, Permissions.ARENA_ALL)) {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");

            return;
        }

        if (args.length < 5) {
            player.sendMessage(ChatColor.RED + "Correct usage: /spleef arena create <water|lava> <diameter> <name>");

            return;
        }

        if (!args[2].equalsIgnoreCase("water") && !args[2].equalsIgnoreCase("lava")) {
            player.sendMessage(ChatColor.RED + "You must select rising water or lava.");

            return;
        }

        String type = args[2].toLowerCase();
        int diameter;

        try {
            diameter = Integer.parseInt(args[3]);

            if (diameter < 20 || diameter > 100) {
                player.sendMessage(ChatColor.RED + "The diameter of the arena must be a whole number between 20-100.");

                return;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "The diameter of the arena must be a whole number between 20-100.");

            return;
        }

        FileManager arena = Spleef.getInstance().getArenas().load();

        if (arena.has("arenas." + args[4])) {
            player.sendMessage(ChatColor.RED + "That arena name is already in use.");

            return;
        }

        String name = args[4];

        ArenaWorld world = Spleef.getInstance().getArenaWorld();

        Location last = arena.has("last_used_location") ? arena.getLocation("last_used_location") : new Location(world.getWorld(), 0, 0, 0);

        last = last.add(120, 0 , 0);

        arena.set("last_used_location", last);

        int minX = last.getBlockX();
        int minY = last.getBlockY();
        int minZ = last.getBlockZ();
        int maxX = minX + diameter;
        int maxY = minY + 109;
        int maxZ = minZ + diameter;

        player.sendMessage(ChatColor.GOLD + "Creating your " + ChatColor.WHITE + diameter + "x" + diameter + ChatColor.GOLD + " arena named " + ChatColor.GREEN + name + ChatColor.GOLD + "...");

        WorldEditCreations.createWall(Material.BARRIER, new Location(world.getWorld(), minX, minY, minZ), new Location(world.getWorld(), maxX, maxY, maxZ));
        WorldEditCreations.setBlocks(Material.BEDROCK, new Location(world.getWorld(), minX, minY, minZ), new Location(world.getWorld(), maxX, minY, maxZ));
        WorldEditCreations.setBlocks(Material.BARRIER, new Location(world.getWorld(), minX, maxY, minZ), new Location(world.getWorld(), maxX, maxY, maxZ));

        Location spawn = new Location(world.getWorld(), (minX + maxX) / 2, 2, (minZ + maxZ) / 2);

        arena.set("arenas." + name + ".type", type);
        arena.set("arenas." + name + ".size", diameter);
        arena.set("arenas." + name + ".from", new Location(world.getWorld(), minX, minY, minZ));
        arena.set("arenas." + name + ".to", new Location(world.getWorld(), maxX, maxY, maxZ));
        arena.set("arenas." + name + ".spawn", spawn);

        arena.save();

        player.sendMessage(ChatColor.GOLD + "Your arena was successfully created. Teleporting...");
        player.teleport(spawn);
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();

        InventoryDefaults.setupCreation(player);
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!Permissions.hasPermission(player, Permissions.SPLEEF_ALL, Permissions.ARENA_CREATE, Permissions.ARENA_ALL))
            return null;

        List<String> output = new ArrayList<>();

        if (args.length < 3) {
            output.add("water");
            output.add("lava");
        } else if (args.length < 4) {
            output.add("20");
            output.add("25");
            output.add("30");
            output.add("35");
            output.add("40");
            output.add("45");
            output.add("50");
            output.add("55");
            output.add("60");
            output.add("65");
            output.add("70");
            output.add("75");
            output.add("80");
            output.add("85");
            output.add("90");
            output.add("95");
            output.add("100");
        }

        return output;
    }
}
