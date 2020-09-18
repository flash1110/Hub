package com.flash.hub.game.player;

import com.flash.hub.game.cooldown.PlayerTimer;
import com.flash.hub.game.cooldown.PlayerTimerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HubProfile {

    @Getter private static final Map<String, HubProfile> profiles = new HashMap<>();

    @Getter private final UUID uuid;

    @Getter @Setter private boolean pvp;

    @Getter private List<PlayerTimer> timers;

    public HubProfile(UUID uuid, boolean cache) {
        this.uuid = uuid;

        pvp = false;

        this.timers = new ArrayList<>();

        if(cache)
            profiles.put(uuid.toString(), this);
    }

    public HubProfile(UUID uuid) {
        this(uuid, true);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void addTimer(PlayerTimer timer) {
        timers.add(timer);
    }

    public void removeTimersByType(PlayerTimerType type) {
        List<PlayerTimer> timers = this.getTimers().stream()
                .filter(timer -> timer.getType() == type)
                .collect(Collectors.toList());

        timers.forEach(timer -> {
            timer.cancel();
            this.getTimers().remove(timer);
        });
    }

    public PlayerTimer getTimerByType(PlayerTimerType type) {
        return timers.stream()
                .filter(timer -> timer.getType() == type)
                .findFirst()
                .orElse(null);
    }

    public void updateTimer(PlayerTimerType type, long time, boolean convert) {
        timers.stream()
                .filter(timer -> timer.getType() == type)
                .forEach(timer -> timer.setCurrentTime(time, convert));
    }

    public boolean hasTimer(PlayerTimerType type) {
        return timers.stream()
                .anyMatch(timer -> timer.getType() == type);
    }

    public static HubProfile getByUuid(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return new HubProfile(uuid);
        }

        return getByPlayer(player);
    }

    public static HubProfile getByPlayer(Player player) {
        if(profiles.containsKey(player.getUniqueId().toString()))
            return profiles.get(player.getUniqueId().toString());

        return new HubProfile(player.getUniqueId());
    }

}
