package com.flash.hub.api.events.timer;

import com.flash.hub.game.cooldown.Timer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class TimerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter private Timer timer;

    TimerEvent(Timer timer) {
        this.timer = timer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}