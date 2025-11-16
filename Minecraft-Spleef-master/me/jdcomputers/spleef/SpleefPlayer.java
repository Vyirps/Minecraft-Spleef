package me.jdcomputers.spleef;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class SpleefPlayer {
    private final Player player;

    public SpleefPlayer(Player player) {
        this.player = player;
    }

    public void setup() {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
    }

    public void sendMessage(Object message) {
        if (message == null)
            return;

        player.sendMessage(message.toString());
    }

    public void kill() {
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
        player.sendTitle(ChatColor.RED + "You Died", "Please wait for the next game.", 10, 60, 10);
    }

    public boolean isPlaying() {
        return player.getGameMode() != GameMode.CREATIVE;
    }

    public boolean isDead() {
        return player.getGameMode() == GameMode.SPECTATOR;
    }

    public Player getPlayer() {
        return player;
    }
}
