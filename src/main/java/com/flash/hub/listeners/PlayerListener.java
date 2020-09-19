package com.flash.hub.listeners;

import com.flash.hub.game.cooldown.PlayerTimer;
import com.flash.hub.game.cooldown.PlayerTimerType;
import com.flash.hub.game.lobby.LobbyManager;
import com.flash.hub.game.player.HubProfile;
import com.flash.hub.util.C;
import com.flash.hub.util.ConfigValues;
import com.flash.hub.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        player.getInventory().setItem(ConfigValues.SERVER_SELECTOR_SLOT, getServerSelector());
        player.getInventory().setItem(ConfigValues.PvP_SLOT, getPvPItem(player));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (HubProfile.getProfiles().containsKey(event.getPlayer().getUniqueId()))
            HubProfile.getProfiles().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player pdamager = (Player) event.getDamager();
        Player pdamaged = (Player) event.getEntity();

        HubProfile damager = HubProfile.getByPlayer(pdamager);
        if (!damager.isPvp()) {
            event.setCancelled(true);
            return;
        }
        HubProfile damaged = HubProfile.getByPlayer(pdamaged);
        if (!damaged.isPvp()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(true);

        if (event.getItem().equals(getServerSelector())) {
            LobbyManager.openGUI(event.getPlayer());
            return;
        }

        if (event.getItem().equals(getPvPItem(event.getPlayer()))) {
            HubProfile profile = HubProfile.getByPlayer(event.getPlayer());

            if (profile.hasTimer(PlayerTimerType.PVP_CHANGE)) {
                event.getPlayer().sendMessage(C.color("&cYou must wait &6" + profile.getTimerByType(PlayerTimerType.PVP_CHANGE).getFormattedTime() + " seconds before you can do that again"));
                return;
            }

            if (profile.isPvp()) {
                profile.setPvp(false);
                // Undisguise Player as Zombie Pigmen
                event.getPlayer().sendMessage(C.color("&aYou have been cured!"));
                PlayerTimer timer = new PlayerTimer(event.getPlayer(), PlayerTimerType.PVP_CHANGE);
                timer.add();
            } else {
                profile.setPvp(true);
                // Disguise Player as Zombie Pigmen
                event.getPlayer().sendMessage(C.color("&cYou have been infected!"));
                PlayerTimer timer = new PlayerTimer(event.getPlayer(), PlayerTimerType.PVP_CHANGE);
                timer.add();
            }

            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
            return;
        }

        if (event.getInventory().getName().equalsIgnoreCase(C.strip("Lobby Selector"))) {
            event.setCancelled(true);
            // Get Lobby ItemStack
            // Send Player to that Lobby
            return;
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    private ItemStack getServerSelector() {
        return new ItemBuilder(ConfigValues.SERVER_SELECTOR_MATERIAL).name(ConfigValues.SERVER_SELECTOR_NAME).get();
    }

    private ItemStack getPvPItem(Player player) {
        HubProfile profile = HubProfile.getByPlayer(player);

        if (profile.isPvp())
            return new ItemBuilder(ConfigValues.PvP_Enabled_MATERIAL).name(ConfigValues.PvP_Enabled_NAME).get();
        else
            return new ItemBuilder(ConfigValues.PvP_Disabled_MATERIAL).name(ConfigValues.PvP_Disabled_NAME).get();
    }
}
