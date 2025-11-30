package me.jdcomputers.events;

import me.jdcomputers.spleef.SpleefGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpleefGameCancelledEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final SpleefGame game;

    public SpleefGameCancelledEvent(SpleefGame game) {
        this.game = game;
    }

    @NotNull
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
}
