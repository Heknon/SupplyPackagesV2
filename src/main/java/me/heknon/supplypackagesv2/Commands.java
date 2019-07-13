package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Utils.ChatUtils;
import me.heknon.supplypackagesv2.Utils.PlaceholderUtils;
import me.heknon.supplypackagesv2.Utils.PlayerUtils;
import me.heknon.supplypackagesv2.Utils.SummonFallingBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {

    private ChatUtils chat = new ChatUtils();
    private PlaceholderUtils placeholderUtils = new PlaceholderUtils();
    private SummonFallingBlock summonFallingBlock = new SummonFallingBlock();
    private PlayerUtils playerUtils = new PlayerUtils();

    private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    private final FileManager.Config permissions = new FileManager(plugin).getConfig("permissions.yml");
    private final FileManager.Config settings = new FileManager(plugin).getConfig("settings.yml");

    // PERMISSIONS
    private final String reloadPermission = this.permissions.get().getString("reload");
    private final String summonPermission = this.permissions.get().getString("summon");
    private final String summonOthersPermission = this.permissions.get().getString("summon_others");
    private final String helpPermission = this.permissions.get().getString("help");
    private final String give_signal_to_self = this.permissions.get().getString("give_signal_to_self");
    private final String give_signal_to_others = this.permissions.get().getString("give_signal_to_others");

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("supplypackages"))
            if (args.length < 1) { // What happens when only /supplypackages is entered
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    // HELP COMMAND
                    final String helpMessagePermission = permissions.get().getString("help");

                    if (player.hasPermission(helpMessagePermission)) {
                        String helpMessage = messages.get().getString("help_command");
                        helpMessage = placeholderUtils.replacer(helpMessage, player, null, helpPermission);
                        player.sendMessage(chat.ChatColorFormat(helpMessage));
                        return true;
                    }

                    // Sent if player doesn't have permission to view help page.
                    String helpMessagePermissionMissing = messages.get().getString("help_command_missing_permission");
                    helpMessagePermissionMissing = placeholderUtils.replacer(helpMessagePermissionMissing, player, null, helpPermission);
                    player.sendMessage(chat.ChatColorFormat(helpMessagePermissionMissing));
                    return false;

                } else if (sender instanceof ConsoleCommandSender) {
                    String helpMessage = messages.get().getString("help_command");
                    helpMessage = placeholderUtils.replacer(helpMessage, null, null, helpPermission);
                    Bukkit.getServer().getConsoleSender().sendMessage(chat.ChatColorFormat(helpMessage));
                    return true;
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("summon")) { // When player does /supplypackages summon
                    if (sender instanceof Player) {
                        Player player = (Player) sender;

                        final String summon_permission = permissions.get().getString("summon");

                        if (player.hasPermission(summon_permission)) {

                            String message = messages.get().getString("supply_package_summoned");

                            summonFallingBlock.summonFallingBlock(player.getLocation());
                            message = placeholderUtils.replacer(message, player, null, helpPermission);
                            player.sendMessage(chat.ChatColorFormat(message));
                            return true;
                        }

                        // If player doesn't have permission to use /supplypackages summon
                        String missing_permission_message = messages.get().getString("summon_missing_permission");
                        missing_permission_message = placeholderUtils.replacer(missing_permission_message, player, null, summonPermission);
                        player.sendMessage(chat.ChatColorFormat(missing_permission_message));
                        return false;
                    } else if (sender instanceof ConsoleCommandSender) {
                        String message = messages.get().getString("console_cant_do_summon");
                        message = placeholderUtils.replacer(message, null, null, summonPermission);
                        Bukkit.getConsoleSender().sendMessage(chat.ChatColorFormat(message));
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (!player.hasPermission(permissions.get().getString("reload"))) {
                            String message = messages.get().getString("reload_missing_permission");
                            message = placeholderUtils.replacer(message, player, null, reloadPermission);
                            player.sendMessage(chat.ChatColorFormat(message));
                        }
                        String message = messages.get().getString("config_reload");
                        message = placeholderUtils.replacer(message, player, null, reloadPermission);
                        player.sendMessage(chat.ChatColorFormat(message));
                        plugin.configReload();
                    } else if (sender instanceof ConsoleCommandSender) {
                        String message = messages.get().getString("config_reload");
                        message = placeholderUtils.replacer(message, null, null, reloadPermission);
                        Bukkit.getServer().getConsoleSender().sendMessage(message);
                        plugin.configReload();
                    }
                } else if (args[0].equalsIgnoreCase("givesignal")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (!player.hasPermission(give_signal_to_self)) {
                            String message = messages.get().getString("give_signal_to_self_missing_permission");
                            message = placeholderUtils.replacer(message, player, null, give_signal_to_self);
                            player.sendMessage(chat.ChatColorFormat(message));
                            return false;
                        }
                        player.getInventory().addItem(giveSignal());
                        String message = messages.get().getString("signal_given_to_self");
                        message = placeholderUtils.replacer(message, player, null, settings.get().getString("give_signal_to_self"));
                        player.sendMessage(chat.ChatColorFormat(message));
                        return true;
                    } else if (sender instanceof ConsoleCommandSender) {
                        String message = messages.get().getString("give_signal_console_no_player_given");
                        message = placeholderUtils.replacer(message, null, null, give_signal_to_self);
                        Bukkit.getServer().getConsoleSender().sendMessage(chat.ChatColorFormat(message));
                        return false;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("summon")) {
                    if (sender instanceof Player && !((Player) sender).getPlayer().hasPermission(summonOthersPermission)) {
                        final String message = placeholderUtils.replacer(messages.get().getString("summon_others_missing_permission"), ((Player) sender).getPlayer(), args[1], summonOthersPermission);
                        ((Player) sender).getPlayer().sendMessage(chat.ChatColorFormat(message));
                        return false;
                    }
                    if (!playerUtils.stringIsValidPlayer(args[1]) || !Bukkit.getPlayer(args[1]).isOnline()) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            String message = messages.get().getString("invalid_player");
                            message = placeholderUtils.replacer(message, player, args[1], summonOthersPermission);
                            player.sendMessage(chat.ChatColorFormat(message));
                            return true;
                        } else if (sender instanceof ConsoleCommandSender) {
                            String message = messages.get().getString("invalid_player_console");
                            message = placeholderUtils.replacer(message, null, args[1], summonOthersPermission);
                            Bukkit.getConsoleSender().sendMessage(chat.ChatColorFormat(message));
                            return true;
                        }
                    } else {
                        if (sender instanceof ConsoleCommandSender && !settings.get().getBoolean("allow_package_summoning_from_console")) {
                            final String message = placeholderUtils.replacer(messages.get().getString("summon_others_from_console_with_no_permission"), null, args[1], summonOthersPermission);
                            Bukkit.getConsoleSender().sendMessage(chat.ChatColorFormat(message));
                        }
                        Player other_player = Bukkit.getPlayer(args[1]);
                        summonFallingBlock.summonFallingBlock(other_player.getLocation());
                        String messageToEffected = messages.get().getString("message_sent_to_effected_summon_others");
                        String messageToExecutor = messages.get().getString("message_sent_to_executor_summon_others");
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            player.sendMessage(chat.ChatColorFormat(placeholderUtils.replacer(messageToExecutor, player, other_player.getName(), summonOthersPermission)));
                            other_player.sendMessage(chat.ChatColorFormat(placeholderUtils.replacer(messageToEffected, player, other_player.getName(), summonOthersPermission)));
                            return true;
                        } else {
                            String messageToEffectedConsole = messages.get().getString("message_sent_to_effected_from_console_summon_others");
                            Bukkit.getConsoleSender().sendMessage(chat.ChatColorFormat(placeholderUtils.replacer(messageToExecutor, null, other_player.getName(), summonOthersPermission)));
                            other_player.sendMessage(chat.ChatColorFormat(placeholderUtils.replacer(messageToEffectedConsole, null, null, summonOthersPermission)));
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("givesignal")) {
                    System.out.println("abc");
                    if (!playerUtils.stringIsValidPlayer(args[1]) || !Bukkit.getPlayer(args[1]).isOnline()) {
                        System.out.println("ab");
                        String message = (sender instanceof  Player) ? placeholderUtils.replacer(messages.get().getString("invalid_player"), ((Player) sender).getPlayer(), args[1], give_signal_to_others) : placeholderUtils.replacer(messages.get().getString("invalid_player_console"), null, args[1], give_signal_to_others);
                        if (sender instanceof Player) { ((Player) sender).getPlayer().sendMessage(chat.ChatColorFormat(message)); } else if (sender instanceof ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage(chat.ChatColorFormat(message));
                        return false;
                    }
                    if (sender instanceof Player) {
                        System.out.println("a");
                        Player player  = (Player) sender;
                        if (!player.hasPermission(give_signal_to_others)) {
                            System.out.println("b");
                            final String message = placeholderUtils.replacer(messages.get().getString("give_signal_to_others_missing_permission"), player, args[1], give_signal_to_others);
                            player.sendMessage(chat.ChatColorFormat(message));
                            return false;
                        }
                        System.out.println("c");
                        Bukkit.getPlayer(args[1]).getInventory().addItem(giveSignal());
                        String message_receiver = messages.get().getString("message_to_signal_receiver");
                        message_receiver = placeholderUtils.replacer(message_receiver, player, args[1], give_signal_to_others);
                        String message_sender = messages.get().getString("message_to_signal_sender");
                        message_sender = placeholderUtils.replacer(message_sender, player, args[1], give_signal_to_others);
                        Bukkit.getPlayer(args[1]).sendMessage(chat.ChatColorFormat(message_receiver));
                        player.sendMessage(chat.ChatColorFormat(message_sender));
                        return true;
                    }
                }
            }
        return true;
    }
    private ItemStack giveSignal() {
        List<String> lore = settings.get().getStringList("firework_meta.lore");
        List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            newLore.add(chat.ChatColorFormat(s));
        }
        String displayName = chat.ChatColorFormat(settings.get().getString("firework_meta.display_name"));
        ItemStack firework = new ItemStack(Material.FIREWORK, 1);
        ItemMeta fwMeta = firework.getItemMeta();
        fwMeta.setDisplayName(displayName);
        fwMeta.setLore(newLore);
        firework.setItemMeta(fwMeta);
        return firework;
    }
}
