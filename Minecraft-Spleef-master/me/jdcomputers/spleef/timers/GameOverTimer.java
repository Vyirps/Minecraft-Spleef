package me.jdcomputers.spleef.timers;

import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;

public class GameOverTimer extends GameTimer {
    public GameOverTimer(SpleefGame game, long delay) {
        super(game, 10, 5, delay, 20L, true);
    }

    @Override
    protected void timerTick() {

    }

    @Override
    protected void timerInitialized() {
        for (SpleefPlayer p : game.getPlayingPlayers())
            for (PotionEffect effect : p.getPlayer().getActivePotionEffects())
                p.getPlayer().removePotionEffect(effect.getType());
    }

    @Override
    protected boolean timerPast() {
        return true;
    }

    @Override
    protected void timerIncrement(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers())
            p.sendMessage(ChatColor.GREEN + "Teleporting back to lobby in " + ChatColor.WHITE + second + " seconds.");
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers())
            p.sendMessage(ChatColor.RED + "Teleporting back to lobby in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
    }
}
