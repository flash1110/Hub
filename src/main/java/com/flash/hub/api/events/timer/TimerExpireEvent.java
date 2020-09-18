package com.flash.hub.api.events.timer;

import com.flash.hub.game.cooldown.Timer;

public class TimerExpireEvent extends TimerEvent {

    public TimerExpireEvent(Timer timer) {
        super(timer);
    }
}