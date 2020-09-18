package com.flash.hub.game.cooldown;

import com.flash.hub.game.player.HubProfile;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerTimer extends Timer {

    @Getter private final Player player;
    @Getter private final PlayerTimerType type;

    public PlayerTimer(Player player, PlayerTimerType type, long length) {
        super(length);

        this.player = player;
        this.type = type;
    }

    public PlayerTimer(Player player, PlayerTimerType type) {
        this(player, type, type.getDefaultTime());
    }

    public void add() {
        HubProfile profile = HubProfile.getByPlayer(this.getPlayer());
        profile.addTimer(this);
    }

    @Override
    public boolean cancel() {
        HubProfile profile = HubProfile.getByPlayer(player);
        profile.getTimers().remove(this);

        return super.cancel();
    }
}
