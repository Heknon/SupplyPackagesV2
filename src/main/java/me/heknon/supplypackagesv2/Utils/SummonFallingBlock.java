package me.heknon.supplypackagesv2.Utils;

import me.heknon.supplypackagesv2.FileManager;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SummonFallingBlock implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    private final FileManager.Config settings = new FileManager(plugin).getConfig("settings.yml");

    public void summonFallingBlock(Location loc) {
        String fallingBlockMaterial;
        try {
            fallingBlockMaterial = settings.get().getString("falling_from_sky_summoned_block");
            Material.getMaterial(fallingBlockMaterial);
        } catch (NullPointerException e) {
            settings.set("falling_from_sky_block", "BEACON");
            plugin.saveConfig();
            plugin.reloadConfig();
            fallingBlockMaterial = settings.get().getString("falling_from_sky_summoned_block");
        }
        final int abovePlayer = settings.get().getInt("blocks_above_player_summoned_block");
        loc = new Location(loc.getWorld(), Math.floor(loc.getX()), Math.floor(loc.getY()) + abovePlayer, Math.floor(loc.getZ()));
        final Location finalLoc = loc;
        final double X_Vector = settings.get().getInt("falling_block_speed_X_Vector");
        final double Y_Vector = settings.get().getInt("falling_block_speed_Y_Vector");
        final double Z_Vector = settings.get().getInt("falling_block_speed_Z_Vector");
        @SuppressWarnings("deprecation") final FallingBlock proj = finalLoc.getWorld().spawnFallingBlock(finalLoc, Material.getMaterial(fallingBlockMaterial), (byte) 6);
        proj.setVelocity(new Vector(X_Vector, Y_Vector, Z_Vector));
        proj.setHurtEntities(false);
        proj.setDropItem(false);
        settings.get().set("falling_block_uuid", proj.getUniqueId());
        final String materialOnHit = settings.get().getString("block_material_on_hit");
        List<Double> check = new ArrayList<>();
        spawnFireworks(finalLoc.subtract(0,abovePlayer - 3,0), settings.get().getInt("amount_of_fireworks_to_spawn_on_summon"));
        new BukkitRunnable() {
            @Override
            public void run() {
                Location proj_loc = proj.getLocation();
                double proj_loc_Y = proj_loc.getY();
                check.add(proj_loc_Y);
                Location hit_loc = proj_loc.getWorld().getHighestBlockAt(proj_loc).getLocation();
                double hit_loc_Y = hit_loc.getY();
                Block hit_block = hit_loc.getBlock();
                if (proj_loc_Y > proj_loc.getWorld().getMaxHeight()) return;
                boolean a = false;
                try {
                    a = check.get(0).equals(check.get(1));
                } catch (IndexOutOfBoundsException ignored) { }
                if (proj_loc.getBlock().getType().toString().contains("WATER")) {
                    if (settings.get().getBoolean("stop_at_water")) {
                        proj.getLocation().getWorld().getHighestBlockAt(proj_loc).setType(Material.getMaterial(settings.get().getString("block_material_on_hit")));
                        proj.getLocation().getWorld().getHighestBlockAt(proj_loc).setMetadata("SupplyPackages", new FixedMetadataValue(plugin, "asdbuibuiUBUIdbsa()@#$"));
                        proj.remove();
                        cancel();
                        return;
                    }
                    return;

                } else if (proj_loc_Y == hit_loc_Y || proj_loc_Y + 0.5 == hit_loc_Y || proj_loc_Y - 0.5 == hit_loc_Y ||hit_loc_Y + 1 == proj_loc_Y || proj_loc_Y - 0.5625 == hit_loc_Y || proj_loc_Y - 0.0625 == hit_loc_Y || proj_loc_Y - 0.8125 == hit_loc_Y || proj_loc_Y + 0.0625 == hit_loc_Y || proj_loc_Y - 2 == hit_loc_Y || (proj_loc_Y < hit_loc_Y && hit_loc.subtract(0,1,0).getBlock().getType().toString().contains("WEB")) || proj_loc_Y % 1 == 0 | a) {
                    if (hit_loc.getWorld().getBlockAt(hit_block.getX(), (int) (hit_block.getY() + 0.5), hit_block.getZ()).getType().toString().contains("SLAB")) {
                        hit_loc.getWorld().getBlockAt(hit_block.getX(), hit_block.getY(), hit_block.getZ()).setType(Material.getMaterial(settings.get().getString("block_material_on_hit")));
                        hit_loc.getWorld().getBlockAt(hit_block.getX(), hit_block.getY(), hit_block.getZ()).setMetadata("SupplyPackages", new FixedMetadataValue(plugin, "asdbuibuiUBUIdbsa()@#$"));
                    } else if (contains("OAK_DOOR", hit_block) || contains("IRON_DOOR", hit_block) || contains("SPRUCE_DOOR", hit_block) || contains("BIRCH_DOOR", hit_block) || contains("JUNGLE_DOOR", hit_block) || contains("ACACIA_DOOR", hit_block) || contains("DARK_OAK_DOOR", hit_block)) {
                        hit_loc.getWorld().getBlockAt(hit_block.getX(), hit_block.getY() + 2, hit_block.getZ()).setType(Material.getMaterial(settings.get().getString("block_material_on_hit")));
                        hit_loc.getWorld().getBlockAt(hit_block.getX(), hit_block.getY() + 2, hit_block.getZ()).setMetadata("SupplyPackages", new FixedMetadataValue(plugin, "asdbuibuiUBUIdbsa()@#$"));

                    } else if (contains("ANVIL", hit_block) || contains("GLASS_PANE", hit_block) || contains("TORCH", hit_block) || contains("CARPET", hit_block) || contains("CAULDRON", hit_block) || contains("ENCHANTMENT_TABLE", hit_block) || contains("ENDER_PORTAL", hit_block) || contains("SNOW", hit_block) || contains("LADDER", hit_block) || contains("CROPS", hit_block) || contains("BED", hit_block) || contains("POTATO", hit_block) || contains("CARROT", hit_block) || contains("SKULL", hit_block) || contains("MUSHROOM", hit_block) || contains("CHORUS", hit_block) || contains("ROSE", hit_block) || contains("DOUBLE", hit_block) || contains("SUGAR_CANE", hit_block) || contains("CHEST", hit_block) || contains("FLOWER", hit_block) || contains("SIGN", hit_block) || contains("END_ROD", hit_block) || contains("SHULKER", hit_block) || contains("BANNER", hit_block) || contains("BUTTON", hit_block) || contains("LEVER", hit_block) || contains("TRAP_DOOR", hit_block) || contains("RAIL", hit_block) || contains("BREWING_STAND", hit_block) || contains("_PLATE", hit_block) || contains("IRON_FENCE", hit_block) || contains("DIODE_BLOCK", hit_block) || contains("REDSTONE_COMPARATOR", hit_block) || contains("THIN_GLASS", hit_block) || contains("SLIME_BLOCK", hit_block) || contains("COBBLE_WALL", hit_block) || contains("FENCE", hit_block)) {
                        proj.remove();
                        hit_loc.getWorld().getBlockAt(hit_block.getX(), hit_block.getY() + 1, hit_block.getZ()).setType(Material.getMaterial(settings.get().getString("block_material_on_hit")));
                        hit_loc.getWorld().getBlockAt(hit_block.getX(), hit_block.getY() + 1, hit_block.getZ()).setMetadata("SupplyPackages", new FixedMetadataValue(plugin, "asdbuibuiUBUIdbsa()@#$"));
                    } else {
                        proj.remove();
                        proj_loc.getWorld().getBlockAt(proj_loc).setType(Material.getMaterial(settings.get().getString("block_material_on_hit")));
                        proj_loc.getWorld().getBlockAt(proj_loc).setMetadata("SupplyPackages", new FixedMetadataValue(plugin, "asdbuibuiUBUIdbsa()@#$"));
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (proj_loc.getWorld().getBlockAt(proj_loc).hasMetadata("SupplyPackages")) {
                                    if (proj_loc.getWorld().getBlockAt(proj_loc).getMetadata("SupplyPackages").get(0).asString().equals("asdbuibuiUBUIdbsa()@#$")) {
                                        proj_loc.getWorld().playEffect(proj_loc, Effect.valueOf(settings.get().getString("permanent_effect_until_right_click")), settings.get().getInt("data_of_permanent_effect_until_right_click"));
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0, 20);
                    }
                    cancel();

                }
                if (check.size() > 2) {
                    check.clear();
                }
            }
        }.runTaskTimer(plugin, 0, 1);

    }

    private boolean contains(String s, Block hit) {
        return hit.getType().toString().contains(s);
    }

    private void spawnFireworks(Location location, int amount){
        HashMap<String, Color> map = new HashMap<>();
        map.put("AQUA", Color.YELLOW);
        map.put("BLACK", Color.BLACK);
        map.put("BLUE", Color.BLUE);
        map.put("FUCHSIA", Color.FUCHSIA);
        map.put("GRAY", Color.GRAY);
        map.put("GREEN", Color.GREEN);
        map.put("LIME", Color.LIME);
        map.put("MAROON", Color.MAROON);
        map.put("NAVY", Color.NAVY);
        map.put("OLIVE", Color.OLIVE);
        map.put("ORANGE", Color.ORANGE);
        map.put("PURPLE", Color.PURPLE);
        map.put("RED", Color.RED);
        map.put("SILVER", Color.SILVER);
        map.put("TEAL", Color.TEAL);
        map.put("WHITE", Color.WHITE);
        map.put("YELLOW", Color.YELLOW);
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(map.get(settings.get().getString("color_of_fire_work"))).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }
}
