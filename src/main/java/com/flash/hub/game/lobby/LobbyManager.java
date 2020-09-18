package com.flash.hub.game.lobby;

import com.flash.hub.util.C;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LobbyManager {

    public static void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 18, C.color("&6Lobby Selector"));

        // Create itemstacks that correspond to the open lobbies, with the lore being the online/maximum and phase

        player.openInventory(inv);
    }
}
