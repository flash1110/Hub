package com.flash.hub.api.events.timer;

import com.flash.hub.game.cooldown.Timer;

public class TimerUnpauseEvent extends TimerEvent {

    public TimerUnpauseEvent(Timer timer) {
        super(timer);
    }
}
