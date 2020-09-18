package com.flash.hub.util;

import com.flash.hub.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValues {

    /*=============================*/
    // Server Selector
    public static String SERVER_SELECTOR_NAME;
    public static Integer SERVER_SELECTOR_SLOT;
    public static Material SERVER_SELECTOR_MATERIAL;
    /*=============================*/

    /*=============================*/
    // PvP
    public static String PvP_Enabled_NAME, PvP_Disabled_NAME;
    public static Integer PvP_SLOT, COOLDOWN_TIME;
    public static Material PvP_Enabled_MATERIAL, PvP_Disabled_MATERIAL;
    /*=============================*/

    static {
        FileConfiguration config = Main.getInstance().getConfig();

        SERVER_SELECTOR_NAME = config.getString("server-selector.name");
        SERVER_SELECTOR_SLOT = config.getInt("server-selector.slot");
        SERVER_SELECTOR_MATERIAL = Material.matchMaterial(config.getString("server-selector.material"));

        PvP_Enabled_NAME = config.getString("pvp-enabler.name-when-enabled");
        PvP_Disabled_NAME = config.getString("pvp-enabler.name-when-disabled");
        PvP_SLOT = config.getInt("pvp-enabler.slot");
        COOLDOWN_TIME = config.getInt("pvp-enabler.cooldown");
        PvP_Enabled_MATERIAL = Material.matchMaterial(config.getString("pvp-enabler.on-material"));
        PvP_Disabled_MATERIAL = Material.matchMaterial(config.getString("pvp-enabler.off-material"));
    }
}
