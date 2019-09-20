package me.heknon.supplypackagesv2.Commands;

import de.tr7zw.itemnbtapi.NBTItem;
import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class GiveSignalCommand {

    private Utils utils = new Utils();
    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private FileManager.Config packages = new FileManager(plugin).getConfig("packages.yml");
    private FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    private ConfigurationSection givesignal = messages.get().getConfigurationSection("givesignal");

    public boolean giveSignalSelfDefault(Player player) {
        String permission = "supplypackages.givesignal.default.self";
        if (!player.hasPermission(permission))  {
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
            return false;
        }
        String SPName = packages.get().getString("default_supply_package");
        giveSignal(player, SPName);
        player.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("default"), player, null, null, SPName, player.getLocation(), permission)));
        return true;
    }

    public boolean giveSignalSelf(Player player, String SPName) {
        String permission = "supplypackages.givesignal.self";
        if (!player.hasPermission(permission))  {
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
            return false;
        }
        if (!utils.isValidSP(SPName)) {
            player.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("invalidPackageName"), player, null, SPName, SPName, player.getLocation(), permission)));
            return false;
        }
        giveSignal(player, SPName);
        player.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("signal"), player, null, null, SPName, player.getLocation(), permission)));
        return true;
    }

    public boolean giveSignalOtherDefault(Player player, Player otherPlayer, String otherArg) {
        String permission = "supplypackages.givesignal.default.others";
        if (!player.hasPermission(permission))  {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }
        String SPName = packages.get().getString("default_supply_package");
        giveSignal(otherPlayer, SPName);
        otherPlayer.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("defaultOthers"), player, otherPlayer, otherArg, SPName, otherPlayer.getLocation(), permission)));
        player.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("defaultOthersConfirmation"), player, otherPlayer, otherArg, SPName, otherPlayer.getLocation(), permission)));
        return true;
    }

    public boolean giveSignalOther(Player player, Player otherPlayer, String otherArg, String SPName) {
        String permission = "supplypackages.givesignal.others";
        if (!player.hasPermission(permission))  {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }
        if (!utils.isValidSP(SPName)) {
            player.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("invalidPackageName"), player, otherPlayer, otherArg, SPName, player.getLocation(), permission)));
            return false;
        }
        giveSignal(otherPlayer, SPName);
        otherPlayer.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("signalOthers"), player, otherPlayer, otherArg, SPName, otherPlayer.getLocation(), permission)));
        player.sendMessage(utils.ChatColorFormat(utils.replacer(givesignal.getString("signalOthersConfirmation"), player, otherPlayer, otherArg, SPName, otherPlayer.getLocation(), permission)));
        return true;
    }

    private void giveSignal(Player otherPlayer, String SPName) {
        List<String> signalLore = utils.getSignalLore(SPName);
        String signalName = utils.getSignalName(SPName);
        Material signalMaterial = utils.getSignalMaterial(SPName);
        ItemStack signal = utils.createSignal(signalMaterial, signalName, signalLore);
        NBTItem item = new NBTItem(signal);
        item.setString("SupplyPackages", SPName);
        otherPlayer.getInventory().addItem(item.getItem());
    }
}
