package me.jdcomputers.spleef;

import me.jdcomputers.spleef.timers.GameTimer;
import me.jdcomputers.spleef.timers.NotInGameTimer;
import me.jdcomputers.spleef.timers.WaitingGameTimer;
import me.jdcomputers.src.MyListener;
import me.jdcomputers.src.Spleef;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefGame {
    private final Spleef spleef;
    private final List<SpleefPlayer> players;

    private String arena;
    private int gameTimer;
    private int levelTimer;
    private GameTimer timer;

    public SpleefGame(Spleef spleef) {
        this.spleef = spleef;

        players = new ArrayList<>();
    }

    public void setTimer(GameTimer timer) {
        if (this.timer != null)
            this.timer.end();

        this.timer = timer.reset();
    }

    public void end(SpleefPlayer winner) {
        String name = "Nobody";

        gameTimer = 0;
        levelTimer = 0;

        setTimer(MyListener.getGameOverTimer());

        if (winner != null) {
            name = winner.getPlayer().getName();

            winner.getPlayer().setGameMode(GameMode.SPECTATOR);
            winner.getPlayer().playSound(winner.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
            winner.getPlayer().sendTitle(ChatColor.GOLD + "You Won", "", 10, 60, 10);
            winner.sendMessage(ChatColor.GOLD + "You will be teleported to the lobby in " + ChatColor.WHITE + timer.getMaximum() + ChatColor.GOLD + " seconds.");
        }

        for (SpleefPlayer player : getPlayingPlayers()) {
            if (player != winner) {
                player.sendMessage(ChatColor.GOLD + "The game is over! " + ChatColor.GREEN + name + ChatColor.GOLD + " won. You will be teleported to the lobby in " + ChatColor.WHITE + timer.getMaximum() + ChatColor.GOLD + " seconds.");
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 0.8f);
            }
        }
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public SpleefPlayer addPlayer(Player player) {
        SpleefPlayer output = new SpleefPlayer(player);

        players.add(output);

        return output;
    }

    public boolean hasPlayer(Player player) {
        for (SpleefPlayer p : players)
            if (player.getUniqueId().equals(p.getPlayer().getUniqueId()))
                return true;

        return false;
    }

    public void removePlayer(Player player) {
        for (int i = players.size() - 1; i >= 0; i--) {
            if (player.getUniqueId().equals(players.get(i).getPlayer().getUniqueId())) {
                players.remove(i);

                return;
            }
        }
    }

    public void clearPlayers() {
        players.clear();
    }

    public SpleefPlayer getPlayer(Player player) {
        for (SpleefPlayer p : players)
            if (player.getUniqueId().equals(p.getPlayer().getUniqueId()))
                return p;

        return null;
    }

    public List<SpleefPlayer> getPlayers() {
        return players;
    }

    public List<SpleefPlayer> getPlayingPlayers() {
        return players.stream().filter(SpleefPlayer::isPlaying).toList();
    }

    public List<SpleefPlayer> getDeadPlayers() {
        return players.stream().filter(SpleefPlayer::isDead).toList();
    }

    public List<SpleefPlayer> getLivingPlayers() {
        return players.stream().filter(x -> !x.isDead()).toList();
    }

    public String getArena() {
        return arena;
    }

    public Spleef getPlugin() {
        return spleef;
    }

    public int getGameTimer() {
        return gameTimer;
    }

    public int getLevelTimer() {
        return levelTimer;
    }

    public boolean isInGame() {
        return !(timer instanceof NotInGameTimer);
    }

    public boolean isInWait() {
        return timer instanceof WaitingGameTimer;
    }

    public GameTimer getTimer() {
        return timer;
    }
}
