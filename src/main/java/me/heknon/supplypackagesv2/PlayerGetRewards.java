package me.heknon.supplypackagesv2;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerGetRewards implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    final private FileManager.Config settings = new FileManager(plugin).getConfig("settings.yml");
    final private FileManager.Config items = new FileManager(plugin).getConfig("items.yml");

    @EventHandler
    public void playerGetReward(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock.getType().equals(Material.getMaterial(settings.get().getString("block_material_on_hit")))) {
                if (clickedBlock.hasMetadata("SupplyPackages")) {
                    Player player = e.getPlayer();
                    if (clickedBlock.getMetadata("SupplyPackages").get(0).asString().equals("asdbuibuiUBUIdbsa()@#$")) {
                        World world = clickedBlock.getLocation().getWorld();
                        clickedBlock.setType(Material.AIR);
                        List<String> itemsAndAmounts = items.get().getStringList("items");
                        System.out.println(itemsAndAmounts);
                        List<Material> materials = new ArrayList<>();
                        List<Integer> amounts = new ArrayList<>();
                        for (String itemAndAmount : itemsAndAmounts) {
                            materials.add(Material.getMaterial(itemAndAmount.substring(0, itemAndAmount.indexOf(","))));
                            amounts.add(Integer.valueOf(itemAndAmount.substring(itemAndAmount.indexOf(",") + 2)));
                        }
                        for (int i = 0; i < itemsAndAmounts.size(); i++) {
                            world.dropItem(clickedBlock.getLocation(), new ItemStack(materials.get(i), amounts.get(i)));
                        }

                        String effect_at_right_click = settings.get().getString("effect_at_right_click_on_ground");
                        if (effect_at_right_click.equalsIgnoreCase("disable")) return;
                        world.playEffect(clickedBlock.getLocation(), Effect.valueOf(effect_at_right_click), settings.get().getInt("data_of_effect_at_right_click_on_ground"));
                        clickedBlock.removeMetadata("SupplyPackages", plugin);
                    }
                }
            }
        }
    }
}