package me.heknon.supplypackagesv2.SupplyPackage.Summoning;

import de.tr7zw.nbtapi.NBTItem;
import me.heknon.supplypackagesv2.API.Events.PackageSummonEvent;
import me.heknon.supplypackagesv2.API.SummonReason;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class FireworkSummon implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    final private ConfigManager.Config messages = plugin.getConfigManager().getConfig("messages.yml");

    @EventHandler
    public void FireworkLaunch(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            Location clickedLoc = e.getClickedBlock().getLocation();
            NBTItem nbtMain = new NBTItem(player.getInventory().getItemInMainHand());
            NBTItem nbtOffhand = new NBTItem(player.getInventory().getItemInOffHand());
            if (nbtMain.hasNBTData() || nbtOffhand.hasNBTData()) {
                // has nbt
                if (nbtMain.hasKey("SupplyPackageName") || nbtOffhand.hasKey("SupplyPackageName")) {
                    // has nbt key SupplyPackageName which stores the name of the package to be launched.
                    String SPName = (nbtMain.hasKey("SupplyPackageName")) ? nbtMain.getString("SupplyPackageName") : nbtOffhand.getString("SupplyPackageName");
                    SupplyPackage sp = new SupplyPackage(clickedLoc, new Package(SPName));
                    sp.summon();
                    Bukkit.getServer().getPluginManager().callEvent(new PackageSummonEvent(player, player, sp, clickedLoc, SummonReason.FIREWORK));
                }
            }
        }
    }
}
