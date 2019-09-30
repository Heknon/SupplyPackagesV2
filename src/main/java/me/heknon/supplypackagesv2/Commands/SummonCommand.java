package me.heknon.supplypackagesv2.Commands;

import me.heknon.supplypackagesv2.API.Events.PackageSummonEvent;
import me.heknon.supplypackagesv2.API.SummonReason;
import me.heknon.supplypackagesv2.Commands.CommandManager.SubCommand;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackage.Summoning.SupplyPackage;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.Message;
import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SummonCommand extends SubCommand {

    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private YamlConfiguration messages = plugin.getConfigManager().getConfig("messages.yml").get();
    private String notOnline = messages.getString("not_online");
    private String invalidPackageName = messages.getString("invalid_package_name");

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // sender hasn't entered any arguments. Summon at sender location
            if (Utils.notPlayerSenderCheck(sender))
                return; // The command was sent from console and console does not have world location.
            Player summoner = (Player) sender;
            // Summoner hasn't entered a package either. Summon default package.
            SupplyPackage sp = new SupplyPackage(summoner.getLocation(), Package.getDefaultPackageInstance());
            sp.summon();
            PackageSummonEvent packageSummonEvent = new PackageSummonEvent(summoner, summoner, sp, ((Player)sender).getLocation(), SummonReason.COMMAND);
            Bukkit.getServer().getPluginManager().callEvent(packageSummonEvent);
        } else if (args.length == 1) {
            // sender entered a user to send package to
            // /supplypackage summon <player>
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null || !p.isOnline()) {
                new Message(this.notOnline, sender, false).chat();
                return;
            }
            SupplyPackage sp = new SupplyPackage(p.getLocation(), Package.getDefaultPackageInstance());
            sp.summon();
            PackageSummonEvent packageSummonEvent = new PackageSummonEvent(p, sender, sp, p.getLocation(), SummonReason.COMMAND);
            Bukkit.getServer().getPluginManager().callEvent(packageSummonEvent);
        } else if (args.length == 2) {
            // Player and package name entered
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null || !p.isOnline()) {
                new Message(this.notOnline, sender, false).chat();
                return;
            } else if (!Package.isValidPackageName(args[1])) {
                new Message(this.invalidPackageName, sender, false).chat();
                return;
            }
            SupplyPackage sp = new SupplyPackage(p.getLocation(), new Package(args[1]));
            sp.summon();
            PackageSummonEvent packageSummonEvent = new PackageSummonEvent(p, sender, sp, p.getLocation(), SummonReason.COMMAND);
            Bukkit.getServer().getPluginManager().callEvent(packageSummonEvent);
        }

    }

    @Override
    public String name() {
        return "summon";
    }

    @Override
    public String permission() {
        return "supplypackages.summon";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
