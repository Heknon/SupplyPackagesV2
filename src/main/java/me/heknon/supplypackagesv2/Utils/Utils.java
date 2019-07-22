package me.heknon.supplypackagesv2.Utils;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    private final FileManager.Config packages = new FileManager(plugin).getConfig("packages.yml");

    public String ChatColorFormat(String s) {
        char altColorChar = messages.get().getString("colorcode").charAt(0);
        return ChatColor.translateAlternateColorCodes(altColorChar, s);
    }

    public String replacer(String message, Player player, Player otherPlayer, String enteredArgs, String packageName, Location loc, String permission) {
        String playerName;
        String playerDisplayName;
        String otherPlayerName;
        String otherPlayerDisplayName;
        enteredArgs = (enteredArgs == null) ? "" : enteredArgs;
        packageName = (packageName == null) ? "" : packageName;
        otherPlayerName = (otherPlayer == null) ? "" : otherPlayer.getName();
        otherPlayerDisplayName = (otherPlayer == null) ? "" : otherPlayer.getDisplayName();
        playerName = (player == null) ? "" : player.getName();
        playerDisplayName = (player == null) ? "" : player.getDisplayName();
        if (permission == null) permission = "";
        String x = String.valueOf((int)loc.getX());
        String y = String.valueOf((int)loc.getY());
        String z = String.valueOf((int)loc.getZ());
        if (loc == null) x = "";
        if (loc == null) y = "";
        if (loc == null) z = "";
        try {
            message = message.replace("{permission}", permission);
            message = message.replace("{command_exec_name}", playerName);
            message = message.replace("{command_exec_display_name}", playerDisplayName);
            message = message.replace("{entered_player_display_name}", otherPlayerDisplayName);
            message = message.replace("{x}", x);
            message = message.replace("{y}", y);
            message = message.replace("{z}", z);
            message = message.replace("{entered_player_name}", otherPlayerName);
            message = message.replace("{entered_arg}", enteredArgs);
            message = message.replace("{package_name}", packageName);
        } catch (NullPointerException ignored) {}
        return message;
    }

    public boolean stringIsValidPlayer(String playerName) {
        return Bukkit.getPlayer(playerName) != null;
    }

    private ItemStack giveSignal(String SPName) {
        List<String> lore = packages.get().getStringList("packages." + SPName + ".firework_meta.lore").stream().map(this::ChatColorFormat).collect(Collectors.toList());

        String displayName = ChatColorFormat(packages.get().getString("firework_meta.display_name"));
        ItemStack firework = new ItemStack(Material.FIREWORK, 1);
        ItemMeta fwMeta = firework.getItemMeta();
        fwMeta.setDisplayName(displayName);
        fwMeta.setLore(lore);
        firework.setItemMeta(fwMeta);
        return firework;
    }
    public boolean isValidSP(String SPName) {
        for (String itemList : packages.get().getConfigurationSection("packages").getKeys(false)) {
            if (itemList.equalsIgnoreCase(SPName)) return true;
        }
        return false;
    }

    public List<String> getSPItemsFromName(String SPName) {
        return packages.get().getStringList("packages." + SPName + ".items");
    }

    public String getSoundOnClaimFromName(String SPName) {
        return packages.get().getString("packages." + SPName + ".sound_on_claim");
    }

    public String getSoundTilLClaimFromName(String SPName) {
        return packages.get().getString("packages." + SPName + ".sound_till_claim");
    }

    public String getEffectOnClaimFromName(String SPName) {
        return packages.get().getString("packages." + SPName + ".effect_on_claim");
    }

    public String getEffectTillClaimFromName(String SPName) {
        return packages.get().getString("packages." + SPName + ".effect_till_claim");
    }

    public List<String> getNamesOfSupplyPackages() {
        return new ArrayList<>(packages.get().getConfigurationSection("packages").getKeys(false));
    }
}
