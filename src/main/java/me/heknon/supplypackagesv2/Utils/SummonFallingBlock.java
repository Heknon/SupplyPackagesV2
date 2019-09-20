package me.heknon.supplypackagesv2.Utils;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;


public class SummonFallingBlock implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config packages = new FileManager(plugin).getConfig("packages.yml");
    private final Utils utils = new Utils();

    public void summonFallingBlock(Location loc, String supplypackageName) {
        String fallingBlockMaterial;
        try {
            fallingBlockMaterial = packages.get().getString("packages." + supplypackageName + ".material_falling");
            Material.getMaterial(fallingBlockMaterial);
        } catch (NullPointerException e) {
            packages.set("packages." + supplypackageName + ".material_falling", "BEACON");
            plugin.saveConfig();
            plugin.reloadConfig();
            fallingBlockMaterial = packages.get().getString("packages." + supplypackageName + ".material_falling");
        }
        final int abovePlayer = 200;
        loc = new Location(loc.getWorld(), Math.floor(loc.getX()), Math.floor(loc.getY()) + abovePlayer, Math.floor(loc.getZ()));
        final Location finalLoc = loc;
        final double X_Vector = packages.get().getInt(supplypackageName + ".falling_block_speed_X_Vector");
        final double Y_Vector = packages.get().getInt(supplypackageName + ".falling_block_speed_Y_Vector");
        final double Z_Vector = packages.get().getInt(supplypackageName + ".falling_block_speed_Z_Vector");
        @SuppressWarnings("deprecation") final FallingBlock proj = finalLoc.getWorld().spawnFallingBlock(finalLoc, Material.getMaterial(fallingBlockMaterial), (byte) 6);
        proj.setVelocity(new Vector(X_Vector, Y_Vector, Z_Vector));
        proj.setHurtEntities(false);
        proj.setDropItem(false);
        spawnFireworks(finalLoc.subtract(0, abovePlayer - 3, 0), packages.get().getInt("packages." + supplypackageName + ".fireworks_to_summon_on_summon"), supplypackageName);
        BukkitTask a = Runnable(proj, supplypackageName);
        if (packages.get().getBoolean("packages." + supplypackageName + ".firwork_follow_package")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (a.isCancelled()) {cancel();return;}
                    spawnFireworks(proj.getLocation(), 1, supplypackageName);
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }

    private void spawnFireworks(Location loc, int amount, String SPName) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(plugin.stringToColor.get(packages.get().getString("packages." + SPName + ".fireworks_color"))).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.setInvulnerable(true);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    @EventHandler
    public void FireworkDamage(EntityDamageByEntityEvent e) {
        if ((e.getDamager().getType().equals(EntityType.FIREWORK) && e.getEntity().getType().equals(EntityType.ARMOR_STAND)) || (e.getDamager().getType().equals(EntityType.FIREWORK) && e.getEntity().getType().equals(EntityType.PLAYER))) {
            e.setCancelled(true);
        }
    }

    private BukkitTask Runnable(FallingBlock proj, String supplypackageName) {
        return new BukkitRunnable() {
            Material onLand = Material.getMaterial(packages.get().getString("packages." + supplypackageName + ".material_on_land"));
            @Override
            public void run() {
                if (plugin.velocityExceptions.contains(proj.getVelocity().getY())) {
                    Location projLoc = proj.getLocation();
                    proj.remove();
                    if (plugin.exceptions.contains(projLoc.getBlock().getType())) {
                        projLoc.add(0, 1, 0).getBlock().setType(onLand);
                    } else {
                        projLoc.getBlock().setType(onLand);
                        projLoc.getBlock().setMetadata("SupplyPackages", new FixedMetadataValue(plugin, supplypackageName));
                    }
                    cancel();
                    if (packages.get().getBoolean("packages." + supplypackageName + ".lightning_on_land")) projLoc.getWorld().strikeLightningEffect(projLoc);
                    particlesEffect(projLoc, supplypackageName);
                    soundEffect(projLoc, supplypackageName);
                }
                else if (proj.getVelocity().getY() == 0) {
                    Location projLoc = proj.getLocation();
                    Material hitBlock = projLoc.getBlock().getType();
                    proj.remove();
                    if (plugin.doubleException.contains(hitBlock)) {
                        projLoc.add(0, 2, 0).getBlock().setType(onLand);
                        cancel();
                    }
                    else if ((plugin.breakers.contains(hitBlock)) || (!(hitBlock.isSolid()) && !plugin.exceptions.contains(hitBlock))) {
                        projLoc.add(0, 1, 0).getBlock().setType(onLand);
                        cancel();
                    } else {
                        projLoc.getBlock().setType(onLand);
                        cancel();
                    }

                    projLoc.getBlock().setMetadata("SupplyPackages", new FixedMetadataValue(plugin, supplypackageName));
                    particlesEffect(projLoc, supplypackageName);
                    soundEffect(projLoc, supplypackageName);
                    if (packages.get().getBoolean("packages." + supplypackageName + ".lightning_on_land")) projLoc.getWorld().strikeLightningEffect(projLoc);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private BukkitTask particlesEffect(Location projLoc, String supplypackageName) {
        return new BukkitRunnable() {
            World world = projLoc.getWorld();
            @Override
            public void run() {
                if (world.getBlockAt(projLoc).hasMetadata("SupplyPackages")) {
                    if (utils.isValidSP(world.getBlockAt(projLoc).getMetadata("SupplyPackages").get(0).value().toString())) {
                        try {
                            world.playEffect(projLoc, Effect.valueOf(utils.getEffectTillClaimFromName(supplypackageName)), 1000, 7);
                        } catch (IllegalArgumentException ignored) {cancel();}
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
    private BukkitTask soundEffect(Location projLoc, String supplypackageName) {
        return new BukkitRunnable() {
            World world = projLoc.getWorld();
            @Override
            public void run() {
                if (world.getBlockAt(projLoc).hasMetadata("SupplyPackages")) {
                    if (utils.isValidSP(world.getBlockAt(projLoc).getMetadata("SupplyPackages").get(0).value().toString())) {
                        try {
                            world.playEffect(projLoc, Effect.valueOf(utils.getSoundTilLClaimFromName(supplypackageName)), 1000, 7);
                        } catch (IllegalArgumentException ignored) {cancel();}
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
