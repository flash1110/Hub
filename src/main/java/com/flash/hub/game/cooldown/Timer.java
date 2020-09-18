package com.flash.hub.game.cooldown;

import com.flash.hub.Main;
import com.flash.hub.api.events.timer.TimerExpireEvent;
import com.flash.hub.api.events.timer.TimerPauseEvent;
import com.flash.hub.api.events.timer.TimerUnpauseEvent;
import com.flash.hub.game.player.HubProfile;
import com.flash.hub.util.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Timer {

    @Getter private static final HashMap<UUID, Timer> timers = new HashMap<>();

    @Getter private final UUID uuid;
    @Getter @Setter private long length;
    @Getter private long currentTime;
    @Getter private boolean paused;
    @Getter @Setter private boolean cancelled;

    public Timer(long length) {
        this.uuid = UUID.randomUUID();
        this.length = TimeUnit.SECONDS.toMillis(length);
        this.currentTime = System.currentTimeMillis() + this.length;
        this.paused = false;
        this.cancelled = false;
    }

    public long getTimeRemaining() {
        return (this.isPaused()) ? this.getLength() : this.getCurrentTime() - System.currentTimeMillis();
    }

    public void setNewLength(long length) {
        this.length = TimeUnit.SECONDS.toMillis(length);
        this.currentTime = System.currentTimeMillis() + this.length;
    }

    public String getFormattedTime() {
        return TimeUtils.getFormatted(this.getTimeRemaining(), true, true);
    }

    public void setCurrentTime(long newTime, boolean convert) {
        if(convert)
            this.currentTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(newTime);
        else
            this.currentTime = System.currentTimeMillis() + newTime;
    }

    public void setPaused(boolean paused) {
        if(paused) {
            Bukkit.getPluginManager().callEvent(new TimerPauseEvent(this));
            this.setLength(this.getTimeRemaining());
        }

        if(!paused) {
            Bukkit.getPluginManager().callEvent(new TimerUnpauseEvent(this));
            this.setCurrentTime(this.getLength(), false);
        }

        this.paused = paused;
    }

    public boolean cancel() {
        setCancelled(true);
        setCurrentTime(0L, false);

        return cancelled;
    }

    public static void instate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            Iterator<Timer> timerIterator = timers.values().iterator();
            while(timerIterator.hasNext()) {
                Timer timer = timerIterator.next();

                if(timer.getTimeRemaining() <= 2L) {
                    timerIterator.remove();

                    if(!timer.isCancelled())
                        Bukkit.getPluginManager().callEvent(new TimerExpireEvent(timer));
                }
            }

            for(HubProfile profile : HubProfile.getProfiles().values()) {
                Iterator<PlayerTimer> ite = profile.getTimers().iterator();
                while(ite.hasNext()) {
                    PlayerTimer timer = ite.next();

                    if(timer.getTimeRemaining() <= 2L) {
                        ite.remove();

                        if(!timer.isCancelled())
                            Bukkit.getPluginManager().callEvent(new TimerExpireEvent(timer));
                    }
                }
            }
        }, 2L, 2L);
    }
}
