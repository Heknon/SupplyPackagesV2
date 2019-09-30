package me.heknon.supplypackagesv2.Commands.CommandManager;

import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandManager implements CommandExecutor {

    //Sub Commands
    public String main = "supplypackages";
    public String summon = "summon";
    public ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);

    public CommandManager() {
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equalsIgnoreCase(main)) return true;
        if (args.length == 0) {
            if (!sender.hasPermission("supplypackages.help")) {
                new Message(plugin.getConfigManager().getConfig("messages.yml").get().getString("permission_missing"), sender, true).setToggleable(true).setPlaceholders(new HashMap<String, String>() {
                    {
                        put("{permission}", "supplypackages.help");
                        put("{sender}", (sender instanceof ConsoleCommandSender) ? plugin.getConfigManager().getConfig("messages.yml").get().getString("console_name") : sender.getName());
                    }
                }).chat();
            }
            new Message(plugin.getConfigManager().getConfig("messages.yml").get().getString("help"), sender, false).setToggleable(true).chat();
            return true;
        }

        SubCommand target = this.get(args[0]);

        if (target == null) {
            if (!sender.hasPermission("supplypackages.help")) {
                new Message(plugin.getConfigManager().getConfig("messages.yml").get().getString("permission_missing"), sender, true).setToggleable(true).setPlaceholders(new HashMap<String, String>() {
                    {
                        put("{permission}", "supplypackages.help");
                        put("{sender}", (sender instanceof ConsoleCommandSender) ? plugin.getConfigManager().getConfig("messages.yml").get().getString("console_name") : sender.getName());
                    }
                }).chat();
                return true;
            }
            new Message(plugin.getConfigManager().getConfig("messages.yml").get().getString("help"), sender, false).setToggleable(true).chat();
            return true;
        }

        if (!sender.hasPermission(target.permission()) && target.permission() != null) {
            new Message(plugin.getConfigManager().getConfig("messages.yml").get().getString("permission_missing"), sender, true).setToggleable(true).setPlaceholders(new HashMap<String, String>() {
                {
                    put("{permission}", target.permission());
                    put("{sender}", (sender instanceof ConsoleCommandSender) ? plugin.getConfigManager().getConfig("messages.yml").get().getString("console_name") : sender.getName());
                }
            }).chat();
            return true;
        }

        ArrayList<String> arguments1 = new ArrayList<String>(Arrays.asList(args));
        arguments1.remove(0);
        String[] arguments = arguments1.toArray(new String[0]);
        try {
            target.onCommand(sender, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private SubCommand get(String name) {

        for (SubCommand sc : this.commands) {
            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases = sc.aliases();

            for (String alias : aliases) {
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }
            }
        }
        return null;
    }
}