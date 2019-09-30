package me.heknon.supplypackagesv2.Listeners;

import me.heknon.supplypackagesv2.API.Events.PackageClaimedEvent;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class PlayerGetRewards implements Listener {

    @EventHandler
    public void playerGetReward(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock.getState().hasMetadata("SupplyPackageName")) {
                List<MetadataValue> spMeta = clickedBlock.getState().getMetadata("SupplyPackageName");
                if (spMeta.size() == 0) return;
                String spName = spMeta.get(0).asString();
                if (Package.isValidPackageName(spName)) {
                    Package sp = new Package(spName);
                    Event claimEvent = new PackageClaimedEvent(e.getPlayer(), e.getClickedBlock().getLocation(), sp);
                    Bukkit.getServer().getPluginManager().callEvent(claimEvent);
                }
            }
        }
    }
}