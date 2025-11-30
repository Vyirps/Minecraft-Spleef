package me.jdcomputers.events;

import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.timers.GameTimer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomGameTimerFinishedEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final SpleefGame game;
    private final GameTimer timer;

    public CustomGameTimerFinishedEvent(GameTimer timer) {
        this.game = timer.getGame();
        this.timer = timer;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public SpleefGame getGame() {
        return game;
    }

    public GameTimer getTimer() {
        return timer;
    }
}
