package com.flash.hub;

import com.flash.hub.game.cooldown.Timer;
import com.flash.hub.listeners.ListenerManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter private static Main instance;

    @Getter private ListenerManager listenerManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        listenerManager = new ListenerManager();
        listenerManager.registerListeners();

        Timer.instate();
    }
}
