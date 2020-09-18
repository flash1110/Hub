package com.flash.hub.api.events.timer;

import com.flash.hub.game.cooldown.Timer;

public class TimerPauseEvent extends TimerEvent {

    public TimerPauseEvent(Timer timer) {
        super(timer);
    }
}