package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.Commands.CommandManager.SubCommand;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.HashMap;

public class ReloadCommand extends SubCommand {
    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private YamlConfiguration messages = new ConfigManager(plugin).getConfig("messages.yml").get();

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        long start = new Date().getTime();
        plugin.configNames.forEach(config -> new ConfigManager(plugin).getConfig(config).save());
        plugin.configNames.forEach(config -> new ConfigManager(plugin).getConfig(config).reload());
        long end = new Date().getTime();
        new Message(messages.getString("reload"), sender, true).setToggleable(true).setPlaceholders(new HashMap<String, String>() {
            {
                put("{time}", String.valueOf(end - start));
                put("{sender}", (sender instanceof ConsoleCommandSender) ? messages.getString("console_name") : sender.getName());
            }
        }).chat();
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String permission() {
        return "supplypackages.reload";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
