package me.heknon.supplypackagesv2.Utils;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.ChatColor;

public class ChatUtils {

    private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");

    public String ChatColorFormat(String s) {
        char altColorChar = messages.get().getString("colorcode").charAt(0);
        return ChatColor.translateAlternateColorCodes(altColorChar, s);
    }
}
