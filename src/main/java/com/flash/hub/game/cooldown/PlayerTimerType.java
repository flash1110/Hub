package com.flash.hub.game.cooldown;

import com.flash.hub.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public enum PlayerTimerType {

    PVP_CHANGE(10);

    @Getter private final long defaultTime;

    PlayerTimerType(long defaultTime) {
        this.defaultTime = getTime() != -1 ? getTime() : defaultTime;
    }

    public int getTime() {
        FileConfiguration config = Main.getInstance().getConfig();

        return config.contains("timers." + name().toLowerCase().replaceAll("_", "-")) ? config.getInt("timers." + name().toLowerCase().replaceAll("_", "-")) : -1;
    }
}
