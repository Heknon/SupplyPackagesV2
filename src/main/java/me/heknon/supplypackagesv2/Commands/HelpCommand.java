package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HelpCommand{

    private final SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    private final Utils utils = new Utils();

    private final ConfigurationSection helpSection = messages.get().getConfigurationSection("help");
    private final String helpMessagePermission = "supplypackages.help";

    public boolean playerHelp(Player player) {
        if (!player.hasPermission(helpMessagePermission)) {
            String helpMessagePermissionMissing = helpSection.getString("missing_permissions.missing_permission");
            helper(player, helpMessagePermissionMissing);
            return false;
        }

        String helpMessage = helpSection.getString("help");
        helper(player, helpMessage);
        return true;
    }

    public boolean consoleHelp() {
        String helpMessage = helpSection.getString("help_console");
        helper(null, helpMessage);
        return true;
    }

    private void helper(Player player, String message) {
        message = utils.replacer(message, player, null, null, null, player.getLocation(), helpMessagePermission);
        if (player != null) {
            player.sendMessage(utils.ChatColorFormat(message));
        } else {
            Bukkit.getConsoleSender().sendMessage(utils.ChatColorFormat(message));
        }
    }

    public boolean sendHelp(CommandSender sender) {
        HelpCommand helpCommand = new HelpCommand();
        if (!(sender instanceof Player)) {
            return helpCommand.consoleHelp();
        }
        Player player = (Player) sender;
        return helpCommand.playerHelp(player);
    }
}
