package me.jdcomputers.spleef.timers;

import me.jdcomputers.events.CustomGameTimerFinishedEvent;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.src.Spleef;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class GameTimer {
    protected final SpleefGame game;
    private final int increment;
    private final long delay;
    private final long tick;
    private final boolean skip;
    
    
    protected int current;
    protected int maximum;

    
    
    
    private BukkitTask task;

    public GameTimer(SpleefGame game, int maximum, int increment, long delay, long tick, boolean skip) {
        this.game = game;
        this.maximum = maximum;
        this.increment = increment;
        this.delay = delay;
        this.tick = tick;
        this.skip = skip;
    }

    public GameTimer reset() {
        end();

        current = 0;

        timerInitialized();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                current++;

                tick();
            }
        }.runTaskTimer(game.getPlugin(), delay, tick);

        return this;
    }

    public void end() {
        if (task != null && !task.isCancelled())
            task.cancel();
    }

    protected void tick() {
        timerTick();

        if (current >= maximum) {
            if (timerPast()) {
                CustomGameTimerFinishedEvent event = new CustomGameTimerFinishedEvent(this);

                Bukkit.getPluginManager().callEvent(event);
            }

            return;
        }

        if (current % increment == 0) {
            timerIncrement(maximum - current);

            return;
        }

        if (current < maximum - (skip ? 3:4))
            return;

        timerUpcoming(maximum - current);
    }

    protected abstract void timerTick();
    protected abstract void timerInitialized();
    protected abstract boolean timerPast();
    protected abstract void timerIncrement(int second);
    protected abstract void timerUpcoming(int second);

    public SpleefGame getGame() {
        return game;
    }

    public Spleef getSpleef() {
        return game.getPlugin();
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getIncrement() {
        return increment;
    }

    public long getTick() {
        return tick;
    }

    public boolean isComplete() {
        return current >= maximum;
    }

    public boolean isEnded() {
        return task.isCancelled() || current >= maximum;
    }
}
