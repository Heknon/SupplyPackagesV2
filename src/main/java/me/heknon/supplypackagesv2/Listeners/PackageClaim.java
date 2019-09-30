package me.heknon.supplypackagesv2.Listeners;

import me.heknon.supplypackagesv2.API.Events.PackageClaimedEvent;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PackageClaim implements Listener {

    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);

    @EventHandler
    public void packageClaim(PackageClaimedEvent e) {
        World claimWorld = e.getClaimLocation().getWorld();
        Block claimedBlock = e.getClaimLocation().getBlock();
        Player claimer = e.getClaimer();
        Package supplyPackage = e.getSupplyPackage();
        claimedBlock.setType(Material.AIR);
        supplyPackage.getDrops().forEach(drop -> claimWorld.dropItem(claimedBlock.getLocation(), drop));

        try {
            claimWorld.playEffect(claimedBlock.getLocation(), supplyPackage.getEffectOnClaim(), 100, 7);
        } catch (NullPointerException ignored) {
        }
        try {
            claimWorld.playSound(claimedBlock.getLocation(), supplyPackage.getSoundOnClaim(), 3, 7);
        } catch (NullPointerException ignored) {
        }
        supplyPackage.getClaimMessage().setPlaceholders(getClaimPlaceholderMap(claimer, supplyPackage, claimedBlock)).setToggleable(true).chat();
        supplyPackage.getClaimBroadcast().setPlaceholders(getClaimPlaceholderMap(claimer, supplyPackage, claimedBlock)).setToggleable(true).chat();
        claimedBlock.removeMetadata("SupplyPackageName", this.plugin);
    }


    private HashMap<String, String> getClaimPlaceholderMap(Player claimer, Package supplyPackage, Block claimedBlock) {
        return new HashMap<String, String>() {
            {
                put("{claimer}", claimer.getName());
                put("{package_name}", supplyPackage.getPackageName());
                put("{x}", String.valueOf(claimedBlock.getX()));
                put("{y}", String.valueOf(claimedBlock.getY()));
                put("{z}", String.valueOf(claimedBlock.getZ()));
            }
        };
    }
}
