package me.heknon.supplypackagesv2.Listeners;

import me.heknon.supplypackagesv2.API.Events.PackageSummonEvent;
import me.heknon.supplypackagesv2.API.SummonReason;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackage.Summoning.SupplyPackage;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class PackageSent implements Listener {
    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private YamlConfiguration messages = plugin.getConfigManager().getConfig("messages.yml").get();

    @EventHandler
    public void packageSend(PackageSummonEvent e) {
        SupplyPackage sp = e.getSupplyPackage();
        Player receiver = e.getPackageReceiver();
        CommandSender sender = e.getPackageSender();
        SummonReason summonReason = e.getSummonReason();

        if (summonReason.equals(SummonReason.FIREWORK)) {
            sp.getSupplyPackage().getSignal().getSummonMessage().setRecipients(new HashSet<>(Collections.singletonList(receiver))).setToggleable(true).setPlaceholders(getSummonPlaceholders(receiver, receiver, sp.getSupplyPackage())).chat();
            sp.getSupplyPackage().getSignal().getSummonBroadcast().setToggleable(true).setPlaceholders(getSummonPlaceholders(receiver, receiver, sp.getSupplyPackage())).chat();
            return;
        }
        Message message = sp.getSupplyPackage().getSummonMessage().setToggleable(true);
        Message broadcast = sp.getSupplyPackage().getSummonBroadcast().setToggleable(true);


        if (sender instanceof Player && ((Player) sender).getUniqueId().equals(receiver.getUniqueId())) {
            message.setPlaceholders(getSummonPlaceholders(sender, receiver, sp.getSupplyPackage())).setRecipients(new HashSet<>(Collections.singleton(receiver))).chat();
            broadcast.setPlaceholders(getSummonPlaceholders(sender, receiver, sp.getSupplyPackage())).chat();
            return;
        }
        broadcast.setPlaceholders(getSummonPlaceholders(sender, receiver, sp.getSupplyPackage())).chat();
        new Message(messages.getString("package_sent"), false, true).setRecipients(new HashSet<>(Collections.singleton(sender))).setPlaceholders(getSummonPlaceholders(sender, receiver, sp.getSupplyPackage())).setToggleable(true).chat();
        new Message(messages.getString("package_received"), false, true).setRecipients(new HashSet<>(Collections.singleton(receiver))).setPlaceholders(getSummonPlaceholders(sender, receiver, sp.getSupplyPackage())).setToggleable(true).chat();
    }

    private HashMap<String, String> getSummonPlaceholders(CommandSender summoner, Player receiver, Package sp) {
        return new HashMap<String, String>() {
            {
                put("{summoner}", (summoner instanceof ConsoleCommandSender) ? messages.getString("console_name") : summoner.getName());
                put("{receiver}", receiver.getName());
                put("{package_name}", sp.getPackageName());
                put("{x}", String.valueOf((int)receiver.getLocation().getX()));
                put("{y}", String.valueOf((int)receiver.getLocation().getY()));
                put("{z}", String.valueOf((int)receiver.getLocation().getZ()));
            }
        };
    }
}
