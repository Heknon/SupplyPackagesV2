package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.API.Events.SignalSentEvent;
import me.heknon.supplypackagesv2.Commands.CommandManager.SubCommand;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Message;
import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GiveSignalCommand extends SubCommand {

    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private YamlConfiguration messages = plugin.getConfigManager().getConfig("messages.yml").get();
    private String notOnline = messages.getString("not_online");
    private String invalidPackage = messages.getString("invalid_package_name");

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (Utils.notPlayerSenderCheck(sender)) return;
            Package sp = Package.getDefaultPackageInstance();
            Player receiver = (Player) sender;
            addSignalAndCallEvent(receiver, sp, sender);
        } else if (args.length == 1) {
            Player receiver = Bukkit.getPlayer(args[0]);
            if (receiver == null || !receiver.isOnline()) {
                new Message(notOnline, sender, false).setToggleable(true).chat();
                return;
            }
            Package sp = Package.getDefaultPackageInstance();
            addSignalAndCallEvent(receiver, sp, sender);
        } else if (args.length == 2) {
            Player receiver = Bukkit.getPlayer(args[0]);
            if (receiver == null || !receiver.isOnline()) {
                new Message(notOnline, sender, false).setToggleable(true).chat();
                return;
            } else if (!Package.isValidPackageName(args[1])) {
                new Message(invalidPackage, sender, false).setToggleable(true).chat();
                return;
            }
            Package sp = new Package(args[1]);
            addSignalAndCallEvent(receiver, sp, sender);
        }
    }

    @Override
    public String name() {
        return "givesignal";
    }

    @Override
    public String permission() {
        return "supplypackages.givesignal";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }


    private void addSignalAndCallEvent(Player receiver, Package sp, CommandSender sender) {
        if (receiver.getInventory().firstEmpty() != -1)
            receiver.getInventory().addItem(sp.getSignal().getSignalStack()); // inventory not full so add to inventory
        else
            receiver.getLocation().getWorld().dropItem(receiver.getLocation(), sp.getSignal().getSignalStack()); // Inventory full so drop on floor
        SignalSentEvent signalSentEvent = new SignalSentEvent(sender, receiver, sp);
        Bukkit.getServer().getPluginManager().callEvent(signalSentEvent);
    }
}
