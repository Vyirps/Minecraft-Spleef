package me.jdcomputers.src;

import me.jdcomputers.events.CustomGameTimerFinishedEvent;
import me.jdcomputers.events.SpleefGameCancelledEvent;
import me.jdcomputers.events.SpleefTeleportToArenaEvent;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.spleef.timers.*;
import me.jdcomputers.worldedit.WorldEditCreations;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class MyListener implements Listener {
    private final Spleef spleef;

    private static NotInGameTimer notInGameTimer;
    private static WaitingGameTimer waitingGameTimer;
    private static InGameTimer inGameTimer;
    private static GameOverTimer gameOverTimer;

    public MyListener(Spleef spleef) {
        this.spleef = spleef;

        notInGameTimer = new NotInGameTimer(spleef.getGame(), 40L);
        waitingGameTimer = new WaitingGameTimer(spleef.getGame(), 40L);
        inGameTimer = new InGameTimer(spleef.getGame(), 0L);
        gameOverTimer = new GameOverTimer(spleef.getGame(), 0L);
    }

    @EventHandler
    public void onBukkitLoad(ServerLoadEvent event) {
        FileManager config = spleef.getSpleefConfig().load();

        for (Player p : Bukkit.getOnlinePlayers()) {
            SpleefPlayer spleefPlayer = spleef.getGame().addPlayer(p);

            if (config.has("lobby"))
                p.teleport(config.getLocation("lobby"));

            spleefPlayer.setup();
        }

        spleef.getGame().setTimer(notInGameTimer);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileManager config = spleef.getSpleefConfig().load();
        FileManager arena = spleef.getArenas().load();
        SpleefGame game = spleef.getGame();

        if (!game.hasPlayer(player))
            game.addPlayer(player).setup();

        if (!game.isInGame()) {
            if (config.has("lobby"))
                player.teleport(config.getLocation("lobby"));

            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.GREEN + "A new game is about to begin. Prepare yourself!");

            return;
        }

        if (game.getPlayingPlayers().isEmpty()) {
            SpleefGameCancelledEvent newEvent = new SpleefGameCancelledEvent(game);
            player.sendMessage("hi");
            Bukkit.getPluginManager().callEvent(newEvent);

            return;
        }

        Location from = arena.getLocation(game.getArena() + ".from");
        Location to = arena.getLocation(game.getArena() + ".to");
        Location spawnStored = arena.getLocation(game.getArena() + ".spawn");
        Location arenaLoc = WorldEditCreations.ARENA_LOCATION;

        int xOffset = spawnStored.getBlockX() - Math.min(from.getBlockX(), to.getBlockX());
        int yOffset = spawnStored.getBlockY() - Math.min(from.getBlockY(), to.getBlockY());
        int zOffset = spawnStored.getBlockZ() - Math.min(from.getBlockZ(), to.getBlockZ());

        Location spawn = arenaLoc.clone().add(xOffset, yOffset, zOffset);

        spawn.setYaw(spawnStored.getYaw());
        spawn.setPitch(spawnStored.getPitch());

        player.teleport(spawn);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(ChatColor.RED + "A game is already in progress. Please spectate until the next game.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SpleefGame game = spleef.getGame();

        game.removePlayer(player);

        List<SpleefPlayer> players = game.getPlayingPlayers();

        if (game.isInGame() && players.size() == 1) {
            game.end(players.getFirst());

            return;
        }

        for (SpleefPlayer p : players)
            p.getPlayer().playSound(p.getPlayer().getLocation().add(0, 50, 0), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);

        if (players.size() > 1)
            return;

        if (!game.isInGame() || game.isInWait()) {
            SpleefGameCancelledEvent newEvent = new SpleefGameCancelledEvent(game);

            Bukkit.getPluginManager().callEvent(newEvent);

            return;
        }

        game.end(players.isEmpty() ? null : players.getFirst());
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageByEntityEvent event) {
        Entity start = event.getEntity();

        if (!(start instanceof Player))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPrematureDig(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        SpleefGame game = spleef.getGame();

        if (game.isInGame() && !game.isInWait())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onNormalDig(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        SpleefGame game = spleef.getGame();

        if (!game.isInGame() || game.isInWait())
            return;

        FileManager config = spleef.getSpleefConfig();

        int percent = config.getInt("drop_concrete_likelihood");

        Random random = new Random();

        if (random.nextInt(100 / percent) == 0)
            return;

        event.setDropItems(false);
    }

    @EventHandler
    public void onBarrierBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (event.getBlock().getType() != Material.BARRIER && event.getBlock().getType() != Material.BEDROCK)
            return;

        FileManager arenas = spleef.getArenas().load();

        Set<String> names = arenas.getSection("arenas").getKeys(false);

        Location breakLocation = event.getBlock().getLocation();

        for (String name : names) {
            Location from = arenas.getLocation("arenas." + name + ".from");
            Location to = arenas.getLocation("arenas." + name + ".to");

            int minX = Math.min(from.getBlockX(), to.getBlockX());
            int minY = Math.min(from.getBlockY(), to.getBlockY());
            int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
            int maxX = Math.max(from.getBlockX(), to.getBlockX());
            int maxY = Math.max(from.getBlockY(), to.getBlockY());
            int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());
            int x = breakLocation.getBlockX();
            int y = breakLocation.getBlockY();
            int z = breakLocation.getBlockZ();

            if (x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ) {
                event.setCancelled(true);

                player.sendMessage(ChatColor.RED + "You cannot break bedrock or barrier blocks of an arena!");

                return;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        SpleefGame game = spleef.getGame();

        if (game.isInGame() && !game.isInWait())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBarrierPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (event.getBlock().getType() != Material.BARRIER && event.getBlock().getType() != Material.BEDROCK)
            return;

        FileManager arenas = spleef.getArenas().load();

        Set<String> names = arenas.getSection("arenas").getKeys(false);

        Location breakLocation = event.getBlock().getLocation();

        for (String name : names) {
            Location from = arenas.getLocation("arenas." + name + ".from");
            Location to = arenas.getLocation("arenas." + name + ".to");

            int minX = Math.min(from.getBlockX(), to.getBlockX());
            int minY = Math.min(from.getBlockY(), to.getBlockY());
            int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
            int maxX = Math.max(from.getBlockX(), to.getBlockX());
            int maxY = Math.max(from.getBlockY(), to.getBlockY());
            int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());
            int x = breakLocation.getBlockX();
            int y = breakLocation.getBlockY();
            int z = breakLocation.getBlockZ();

            if (x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ) {
                event.setCancelled(true);

                player.sendMessage(ChatColor.RED + "You cannot place bedrock or barrier blocks in an arena!");

                return;
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.SURVIVAL)
            return;

        Location loc = player.getLocation();

        Block in = loc.getBlock();
        Block under = loc.clone().subtract(0, 0.5, 0).getBlock();
        Block above = loc.clone().add(0, 1, 0).getBlock();

        if (in.getType() != Material.WATER &&
                in.getType() != Material.LAVA &&
                above.getType() != Material.WATER &&
                above.getType() != Material.LAVA &&
                under.getType() != Material.BEDROCK)
            return;

        SpleefGame game = spleef.getGame();

        if (!game.isInGame() || game.isInWait())
            return;

        SpleefPlayer sp = game.getPlayer(player);

        sp.kill();

        List<SpleefPlayer> remainingPlayers = game.getLivingPlayers();

        int remaining = remainingPlayers.size();

        if (remaining > 1) {
            for (SpleefPlayer p : game.getPlayingPlayers()) {
                p.sendMessage(ChatColor.GREEN + sp.getPlayer().getName() + ChatColor.GOLD + " has died. There are now " + ChatColor.WHITE + remaining + ChatColor.GOLD + " " + (remaining == 1 ? "player" : "players") + " remaining...");
                p.getPlayer().playSound(p.getPlayer().getLocation().add(0, 50, 0), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
            }

            return;
        }

        game.end(remainingPlayers.getFirst());
    }

    @EventHandler
    public void onCustomGameTimerCompleted(CustomGameTimerFinishedEvent event) {
        GameTimer timer = event.getTimer();
        SpleefGame game = event.getGame();

        if (game.getPlayingPlayers().size() < 1) {
            game.setTimer(notInGameTimer);

            return;
        }

        if (timer instanceof NotInGameTimer)
            game.setTimer(waitingGameTimer);
        else if (timer instanceof WaitingGameTimer || timer instanceof InGameTimer)
            game.setTimer(inGameTimer);
        else if (timer instanceof GameOverTimer)
            game.setTimer(notInGameTimer);
    }

    @EventHandler
    public void onGameCancelled(SpleefGameCancelledEvent event) {
        SpleefGame game = event.getGame();

        game.getTimer().end();
        game.setTimer(notInGameTimer);
    }

    public static NotInGameTimer getNotInGameTimer() {
        return notInGameTimer;
    }

    public static WaitingGameTimer getWaitingGameTimer() {
        return waitingGameTimer;
    }

    public static InGameTimer getInGameTimer() {
        return inGameTimer;
    }

    public static GameOverTimer getGameOverTimer() {
        return gameOverTimer;
    }
}
