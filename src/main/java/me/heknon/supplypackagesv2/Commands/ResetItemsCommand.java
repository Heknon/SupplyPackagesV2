package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.Commands.CommandManager.SubCommand;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ResetItemsCommand extends SubCommand {

    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private YamlConfiguration messages = plugin.getConfigManager().getConfig("messages.yml").get();
    private String invalidPackageName = messages.getString("invalid_package_name");

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        ConfigManager.Config config = plugin.getConfigManager().getConfig("packages.yml");
        ConfigurationSection packages = config.get().getConfigurationSection("packages");
        if (args.length == 0) {
            Package pkg = Package.getDefaultPackageInstance();
            packages.getConfigurationSection(pkg.getPackageName()).set("items", "");
            config.save().reload();
            new Message(messages.getString("reset_items"), sender, true).setToggleable(true).setPlaceholders(getPlaceholderMap(sender, pkg)).chat();
        } else if (args.length == 1) {
            if (!Package.isValidPackageName(args[0])) {
                new Message(this.invalidPackageName, sender, false).chat();
                return;
            }
            Package pkg = new Package(args[0]);
            packages.getConfigurationSection(pkg.getPackageName()).set("items", "");
            config.save().reload();
            new Message(messages.getString("reset_items"), sender, true).setToggleable(true).setPlaceholders(getPlaceholderMap(sender, pkg)).chat();
        }
    }

    @Override
    public String name() {
        return "resetitems";
    }

    @Override
    public String permission() {
        return "supplypackages.resetitems";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }


    private HashMap<String, String> getPlaceholderMap(CommandSender sender, Package pkg) {
        return new HashMap<String, String>() {
            {
                put("{package_name}", pkg.getPackageName());
                put("{sender}", (sender instanceof ConsoleCommandSender) ? messages.getString("console_name") : sender.getName());
            }
        };
    }
}
