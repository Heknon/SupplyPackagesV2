package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Utils;
import me.heknon.supplypackagesv2.Utils.SummonFallingBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SummonCommand {

    private final SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    private final FileManager.Config packages = new FileManager(plugin).getConfig("packages.yml");
    private final Utils utils = new Utils();
    private final SummonFallingBlock summonFallingBlock = new SummonFallingBlock();

    // SECTIONS
    private final ConfigurationSection summonSection = messages.get().getConfigurationSection("summon");
    private final ConfigurationSection othersSection = summonSection.getConfigurationSection("others");
    private final ConfigurationSection missingPermissions = summonSection.getConfigurationSection("missing_permissions");

    boolean playerSummonDefaultSelf(Player player) {
        String summonSelfDefault = "supplypackages.summon.default";
        if ((!(player.hasPermission(summonSelfDefault)))) {
            helper(player, null, missingPermissions.getString("default"), null, packages.get().getString("default_supply_package"), player.getLocation(), summonSelfDefault);
            return false;
        }
        summonFallingBlock.summonFallingBlock(player.getLocation(), packages.get().getString("default_supply_package"));
        String message = summonSection.getString("default");
        if (message.equalsIgnoreCase("disabled")) return true;
        helper(player, null, message,null, packages.get().getString("default_supply_package"), player.getLocation(), summonSelfDefault);
        if (!packages.get().getString("packages." + packages.get().getString("default_supply_package") + ".messages.broadcast_message").equalsIgnoreCase("disabled")) {
            String broadcast = packages.get().getString("packages." + packages.get().getString("default_supply_package") + ".messages.broadcast_message");
            Bukkit.getServer().broadcastMessage(utils.ChatColorFormat(utils.replacer(broadcast, player, null , null, packages.get().getString("default_supply_package"), player.getLocation(), summonSelfDefault)));
        }
        return true;
    }

    boolean playerSummonDefaultOthers(Player player, Player otherPlayer, String otherArg) {
        String summonOthersDefault = "supplypackages.summon.default.others";
        String summonDefaultSpecific = "supplypackages.summon.default";
        if (!(player.hasPermission(summonOthersDefault)) || !(player.hasPermission(summonDefaultSpecific + "." + otherPlayer.getName()))) {
            helper(player, otherPlayer, missingPermissions.getString("summon_others.default"), otherArg, packages.get().getString("default_supply_package"), player.getLocation(), summonOthersDefault);
            return false;
        }
        summonFallingBlock.summonFallingBlock(otherPlayer.getLocation(), packages.get().getString("default_supply_package"));
        helper(player, otherPlayer, othersSection.getString("default_confirmation"), otherArg, packages.get().getString("default_supply_package"), otherPlayer.getLocation(), summonOthersDefault);
        helper2(player, otherPlayer, othersSection.getString("default_receiver"), otherArg, packages.get().getString("default_supply_package"), otherPlayer.getLocation(), summonOthersDefault);
        if (!packages.get().getString("packages." + packages.get().getString("default_supply_package") + ".messages.broadcast_message").equalsIgnoreCase("disabled")) {
            String broadcast = packages.get().getString("packages." + packages.get().getString("default_supply_package") + ".messages.broadcast_message");
            Bukkit.getServer().broadcastMessage(utils.ChatColorFormat(utils.replacer(broadcast, player, otherPlayer, otherArg, packages.get().getString("default_supply_package"), player.getLocation(), summonOthersDefault)));
        }
        return true;
    }
    boolean playerSummonSelf(Player player, String supplypackageName) {
        String permission = "supplypackages.summon." + supplypackageName;
        if (!utils.isValidSP(supplypackageName)) {
            String message = summonSection.getString("invalid_package");
            helper(player, null, message, null, supplypackageName, player.getLocation(), permission);
            return false;
        }
        if (!player.hasPermission(permission)) {
            String message = missingPermissions.getString("summon_self.summon_by_name");
            helper(player, null, message, null, supplypackageName, player.getLocation(), permission);
            return false;
        }
        summonFallingBlock.summonFallingBlock(player.getLocation(), supplypackageName);
        String message = summonSection.getString("summon_by_name");
        helper(player, null, message, null, supplypackageName, player.getLocation(), permission);
        if (!packages.get().getString("packages." + supplypackageName + ".messages.broadcast_message").equalsIgnoreCase("disabled")) {
            String broadcast = packages.get().getString("packages." + supplypackageName + ".messages.broadcast_message");
            Bukkit.getServer().broadcastMessage(utils.ChatColorFormat(utils.replacer(broadcast, player, null, null, supplypackageName, player.getLocation(), permission)));
        }
        return true;
    }

    boolean playerSummonOthers(Player player, Player otherPlayer, String otherArg, String supplypackageName) {
        summonFallingBlock.summonFallingBlock(otherPlayer.getLocation(), supplypackageName);
        String permission = "supplypackages.summon.others" + supplypackageName;
        helper(player, otherPlayer, othersSection.getString("confirmation"), otherArg, supplypackageName, otherPlayer.getLocation(), permission);
        helper2(player, otherPlayer, othersSection.getString("receiver"), otherArg, supplypackageName, otherPlayer.getLocation(), permission);
        if (!packages.get().getString("packages." + supplypackageName + ".messages.broadcast_message").equalsIgnoreCase("disabled")) {
            String broadcast = packages.get().getString("packages." + supplypackageName + ".messages.broadcast_message");
            Bukkit.getServer().broadcastMessage(utils.ChatColorFormat(utils.replacer(broadcast, player, otherPlayer, otherArg, packages.get().getString("default_supply_package"), player.getLocation(), permission)));
        }
        return true;
    }

    private void helper(Player player, Player otherPlayer, String message, String otherArg, String packageName, Location loc, String permission) {
        message = utils.replacer(message, player, otherPlayer, otherArg, packageName, loc, permission);
        player.sendMessage(utils.ChatColorFormat(message));
    }

    private void helper2(Player player, Player otherPlayer, String message, String otherArg, String packageName, Location loc, String permission) {
        message = utils.replacer(message, player, otherPlayer, otherArg, packageName, loc, permission);
        otherPlayer.sendMessage(utils.ChatColorFormat(message));
    }
}
