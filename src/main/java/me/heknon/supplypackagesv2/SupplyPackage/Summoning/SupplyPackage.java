package me.heknon.supplypackagesv2.SupplyPackage.Summoning;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTTileEntity;
import de.tr7zw.nbtinjector.NBTInjector;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackage.Signal;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class SupplyPackage implements Listener {

    private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private String supplyPackageName;
    private Location summonLocation;
    private Package supplyPackage;
    private int abovePlayer = 200;
    private FallingBlock projectile;
    private Location projectileLocation;

    public SupplyPackage(Location summonLocation, Package supplyPackage) {
        this.summonLocation = summonLocation.add(0, this.abovePlayer, 0);
        this.supplyPackage = supplyPackage;
    }

    public void summon() {
        this.projectile = this.summonLocation.getWorld().spawnFallingBlock(this.summonLocation, this.supplyPackage.getFallingMaterial(), (byte) 6);
        this.projectile.setVelocity(new Vector(0, -1, 0));
        this.projectile.setHurtEntities(false);
        this.projectile.setDropItem(false);
        this.supplyPackage.getSignal().getFirework().summonFireworks(this.supplyPackage.getSignal().getFirework().getAmount(), this.summonLocation.subtract(0, this.abovePlayer - 3, 0));
        BukkitTask task = Tracker();
        if (this.supplyPackage.getSignal().getFirework().isFollowPackage()) {
            Signal signal = this.supplyPackage.getSignal();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (task.isCancelled()) {
                        cancel();
                        return;
                    }
                    signal.getFirework().summonFireworks(1, projectile.getLocation());
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }

    private BukkitTask Tracker() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.velocityExceptions.contains(projectile.getVelocity().getY())) {
                    projectileLocation = projectile.getLocation();
                    projectile.remove();
                    if (plugin.exceptions.contains(projectileLocation.getBlock().getType())) {
                        projectileLocation.add(0, 1, 0).getBlock().setType(supplyPackage.getLandingMaterial());
                    } else {
                        projectileLocation.getBlock().setType(supplyPackage.getLandingMaterial());
                        NBTTileEntity nbtTile = new NBTTileEntity(projectileLocation.getBlock().getState());
                        nbtTile.setString("SupplyPackageName", supplyPackage.getPackageName());
                    }
                    cancel();
                    if (supplyPackage.isLightningLand())
                        projectileLocation.getWorld().strikeLightningEffect(projectileLocation);
                } else if (projectile.getVelocity().getY() == 0) {
                    Location projectileLocation = projectile.getLocation();
                    Material hitBlock = projectileLocation.getBlock().getType();
                    projectile.remove();
                    if (plugin.doubleException.contains(hitBlock)) {
                        projectileLocation.add(0, 2, 0).getBlock().setType(supplyPackage.getLandingMaterial());
                        cancel();
                    } else if ((plugin.breakers.contains(hitBlock)) || (!(hitBlock.isSolid()) && !plugin.exceptions.contains(hitBlock))) {
                        projectileLocation.add(0, 1, 0).getBlock().setType(supplyPackage.getLandingMaterial());
                        cancel();
                    } else {
                        projectileLocation.getBlock().setType(supplyPackage.getLandingMaterial());
                        cancel();
                    }

                    projectileLocation.getBlock().getState().setMetadata("SupplyPackageName", new FixedMetadataValue(plugin, supplyPackage.getPackageName()));
                    if (supplyPackage.isLightningLand())
                        projectileLocation.getWorld().strikeLightningEffect(projectileLocation);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public Location getSummonLocation() {
        return summonLocation;
    }

    public void setSummonLocation(Location summonLocation) {
        this.summonLocation = summonLocation;
    }

    public Package getSupplyPackage() {
        return supplyPackage;
    }

    public void setSupplyPackage(Package supplyPackage) {
        this.supplyPackage = supplyPackage;
    }

    public FallingBlock getProjectile() {
        return projectile;
    }

    public void setProjectile(FallingBlock projectile) {
        this.projectile = projectile;
    }

}
