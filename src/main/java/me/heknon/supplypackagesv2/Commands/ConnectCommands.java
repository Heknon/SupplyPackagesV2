package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConnectCommands implements CommandExecutor {

    private final SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    private final Utils utils= new Utils();
    private final HelpCommand helpCommand = new HelpCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return helpCommand.sendHelp(sender);
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                return helpCommand.sendHelp(sender);

            } else if (args[0].equalsIgnoreCase("summon")) {
                SummonCommand summonCommand = new SummonCommand();
                if (!(sender instanceof Player)) {
                    Bukkit.getServer().getConsoleSender().sendMessage(messages.get().getString("only_players"));
                }
                Player player = (Player) sender;
                return summonCommand.playerSummonDefaultSelf(player);

            }
            else return unknownCommand(sender);

        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("summon")) {
                if (utils.stringIsValidPlayer(args[1])) {
                    Player player = (Player) sender;
                    return new SummonCommand().playerSummonDefaultOthers(player, Bukkit.getPlayer(args[1]), args[1]);
                }
                if (utils.isValidSP(args[1])) {
                    Player player = (Player) sender;
                    return new SummonCommand().playerSummonSelf(player, args[1]);
                }
                if (!utils.stringIsValidPlayer(args[1]) || !utils.isValidSP(args[1])) {
                    String message = messages.get().getString("summon.others.invalid_player_package_name");
                    message = utils.replacer(message, (Player) sender, null, args[1], args[1], ((Player) sender).getLocation(), null);
                    sender.sendMessage(utils.ChatColorFormat(message));
                    return false;
                }
            }
        }
        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("summon")) {
                String permission = "supplypackages.summon.others." + args[1];
                if (!utils.isValidSP(args[1]) || !utils.stringIsValidPlayer(args[2])) {
                    Player player = (Player) sender;
                    String message = messages.get().getString("summon.others.invalid_player_package_name");
                    message = utils.replacer(message, player, null, args[2], args[1], player.getLocation(), null);
                    sender.sendMessage(utils.ChatColorFormat(message));
                    return false;
                }
                if (!sender.hasPermission(permission)) {
                    Player player = (Player) sender;
                    String message = messages.get().getString("summon.missing_permissions.summon_others.summon_by_name");
                    message = utils.replacer(message, player, Bukkit.getPlayer(args[2]), args[2], args[1], player.getLocation(), permission);
                    sender.sendMessage(utils.ChatColorFormat(message));
                    return false;
                }
                return new SummonCommand().playerSummonOthers((Player) sender, Bukkit.getPlayer(args[2]), args[2], args[1]);

            }
        }

        else unknownCommand(sender);

        return false;
    }

    private boolean unknownCommand(CommandSender sender) {
        sender.sendMessage(utils.ChatColorFormat(messages.get().getString("unknown_command")));
        return true;
    }
}
