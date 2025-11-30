package me.jdcomputers.spleef.timers;

import me.jdcomputers.events.SpleefGameCancelledEvent;
import me.jdcomputers.events.SpleefTeleportToArenaEvent;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.worldedit.WorldEditCreations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitingGameTimer extends GameTimer {
    public WaitingGameTimer(SpleefGame game, long delay) {
        super(game, 10, 5, delay, 20L, true);
    }

   
    @Override
    protected void timerTick() {
    	
 //remove for now	
//        if (game.getPlayingPlayers().size() < 1) {
//            SpleefGameCancelledEvent event = new SpleefGameCancelledEvent(game);
//
//            Bukkit.getPluginManager().callEvent(event);
//        }
    }

    @Override
    protected void timerInitialized() {
        SpleefTeleportToArenaEvent event = new SpleefTeleportToArenaEvent(game);

        Bukkit.getPluginManager().callEvent(event);

        Random random = new Random();

        FileManager arena = game.getPlugin().getArenas().load();

        List<String> names = new ArrayList<>(arena.getSection("arenas").getKeys(false));

        int index = random.nextInt(names.size());

        String name = "arenas." + names.get(index);

        game.setArena(name);

        Location from = arena.getLocation(name + ".from");
        Location to = arena.getLocation(name + ".to");
        Location spawnStored = arena.getLocation(name + ".spawn");
        Location arenaLoc = WorldEditCreations.ARENA_LOCATION;

        WorldEditCreations.clear(arenaLoc, 100, 50);
        WorldEditCreations.copy(from, to, arenaLoc);

        int xOffset = spawnStored.getBlockX() - Math.min(from.getBlockX(), to.getBlockX());
        int yOffset = spawnStored.getBlockY() - Math.min(from.getBlockY(), to.getBlockY());
        int zOffset = spawnStored.getBlockZ() - Math.min(from.getBlockZ(), to.getBlockZ());

        Location spawn = arenaLoc.clone().add(xOffset, yOffset, zOffset);

        spawn.setYaw(spawnStored.getYaw());
        spawn.setPitch(spawnStored.getPitch());

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GOLD + "Get ready! You have " + getMaximum() + " seconds before you can break blocks.");
            p.getPlayer().teleport(spawn);
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
        }
    }

    @Override
    protected boolean timerPast() {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GOLD + "The game has begun!");
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.8f);
        }

        return true;
    }

    @Override
    protected void timerIncrement(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GREEN + "Breaking blocks begins in " + ChatColor.WHITE + second + " seconds.");
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.RED + "Breaking blocks begins in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }
}
