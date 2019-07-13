package me.heknon.supplypackagesv2.Utils;

import org.bukkit.Bukkit;

public class PlayerUtils {

    public boolean stringIsValidPlayer(String playerName) {
        return Bukkit.getPlayer(playerName) != null;
    }
}
