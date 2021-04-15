package com.MrEngMan.TeleportAllWithPermission;

import org.bukkit.ChatColor;

public class Utils {

    // Translate '&' as formatting codes
    public static String SendChatMessage(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}

