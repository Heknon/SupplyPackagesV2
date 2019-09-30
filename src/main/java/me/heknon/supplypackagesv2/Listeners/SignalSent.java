package me.heknon.supplypackagesv2.Listeners;

import me.heknon.supplypackagesv2.API.Events.SignalSentEvent;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class SignalSent implements Listener {

    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private YamlConfiguration messages = plugin.getConfigManager().getConfig("messages.yml").get();
    private String signalReceived = messages.getString("signal_received");
    private String signalSent = messages.getString("signal_sent");
    private String consoleName = messages.getString("console_name");

    @EventHandler
    public void signalSend(SignalSentEvent e) {
        Package sp = e.getSignalPackage();
        CommandSender sender = e.getSender();
        Player receiver = e.getReceiver();
        new Message(signalReceived, receiver, true).setToggleable(true).setPlaceholders(getSignalHashMapPlaceholders(sender, receiver, sp)).chat();
        new Message(signalSent, sender, true).setToggleable(true).setPlaceholders(getSignalHashMapPlaceholders(sender, receiver, sp)).chat();
    }

    public HashMap<String, String> getSignalHashMapPlaceholders(CommandSender sender, Player receiver, Package sp) {
        return new HashMap<String, String>()
        {
            {
                put("{sender}", (sender instanceof ConsoleCommandSender) ? consoleName : sender.getName());
                put("{receiver}", receiver.getName());
                put("{package_name}", sp.getPackageName());
            }
        };
    }
}
