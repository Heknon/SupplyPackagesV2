package me.heknon.supplypackagesv2.Utils;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.entity.Player;

public class PlaceholderUtils {

    private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config placeholders = new FileManager(plugin).getConfig("placeholders.yml");
    // PLACEHOLDERS
    private final String permissionPlaceholder = placeholders.get().getString("command_permission");
    private final String namePlaceholder = placeholders.get().getString("command_exec_name");
    private final String displayNamePlaceholder = placeholders.get().getString("command_exec_displayname");
    private final String enteredNamePlaceholder = placeholders.get().getString("entered_name");

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String replacer(String message, Player player, String other_player, String permission) {
        String playerName;
        String displayNamePlayer;
        if (other_player == null) other_player = "";
        if (player == null) { playerName = ""; displayNamePlayer = "";} else { playerName = player.getName(); displayNamePlayer = player.getDisplayName(); }
        if (permission == null) permission = "";
        message = message.replace(permissionPlaceholder, permission);
        message = message.replace(namePlaceholder, playerName);
        message = message.replace(displayNamePlaceholder, displayNamePlayer);
        message = message.replace(enteredNamePlaceholder, other_player);
        return message;
    }
}
