package me.heknon.supplypackagesv2.Utils;

import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    private static SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private static YamlConfiguration messages = plugin.getConfigManager().getConfig("messages.yml").get();
    private static String playerUsageOnly = messages.getString("only_players");

    /**
     * Send message to all server operators
     *
     * @param message the message to send
     */
    public static void sendOPMessage(Message message) {
        Set<CommandSender> onlineOperators = getOnlineOperators();
        message.setRecipients(onlineOperators);
        message.chat();
    }

    public static boolean stringIsMaterial(String material) {
        try {
            Material.valueOf(material);
        } catch(IllegalArgumentException ignored) {
            return false;
        }
        return true;
    }

    public static boolean stringIsBoolean(String bool) {
        return bool.equals("true") || bool.equals("false");
    }

    public static boolean notPlayerSenderCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            new Message(playerUsageOnly, sender, false).chat();
            return true;
        }
        return false;
    }

    private static Set<CommandSender> getOnlineOperators() {
        Set<OfflinePlayer> operators = Bukkit.getServer().getOperators();
        return operators.stream().filter(OfflinePlayer::isOnline).map(offlinePlayer -> (CommandSender) offlinePlayer).collect(Collectors.toSet());
    }

    public static HashMap<String, String> getConfigCorrespondingKeyValueMap(ConfigurationSection section) {
        HashMap<String, String> returnValue = new HashMap<String, String>();
        for (String key : section.getKeys(false)) {
            String value = section.getString(key);
            returnValue.putIfAbsent(key, value);
        }
        return returnValue;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
