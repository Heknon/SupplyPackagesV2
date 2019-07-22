package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerGetRewards implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    final private FileManager.Config packages = new FileManager(plugin).getConfig("packages.yml");
    final private Utils utils = new Utils();


    @EventHandler
    public void playerGetReward(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock.hasMetadata("SupplyPackages")) {
                String data = clickedBlock.getMetadata("SupplyPackages").get(0).asString();
                if (utils.isValidSP(data)) {
                    World world = clickedBlock.getLocation().getWorld();
                    clickedBlock.setType(Material.AIR);
                    List<String> itemsAndAmounts = utils.getSPItemsFromName(data);
                    List<Material> materials = new ArrayList<>();
                    List<Integer> amounts = new ArrayList<>();;
                    for (String itemAndAmount : itemsAndAmounts) {
                        materials.add(Material.getMaterial(itemAndAmount.substring(0, itemAndAmount.indexOf(","))));
                        amounts.add(Integer.valueOf(itemAndAmount.substring(itemAndAmount.indexOf(",") + 2)));
                    }
                    for (int i = 0; i < itemsAndAmounts.size(); i++) {
                        world.dropItem(clickedBlock.getLocation(), new ItemStack(materials.get(i), amounts.get(i)));
                    }
                    String effectOnClaim = utils.getEffectOnClaimFromName(data);
                    String soundOnClaim = utils.getSoundOnClaimFromName(data);
                    try {
                        world.playEffect(clickedBlock.getLocation(), Effect.valueOf(effectOnClaim), 100, 7);
                        world.playEffect(clickedBlock.getLocation(), Effect.valueOf(soundOnClaim), 100, 7);
                    } catch (NullPointerException ignored) {}
                    if (!packages.get().getString("packages." + data + ".messages.claim_message").equalsIgnoreCase("disable")) {
                        String message = packages.get().getString("packages." + data + ".messages.claim_message");
                        message = utils.replacer(message, e.getPlayer(), null, null, data, e.getClickedBlock().getLocation(), null);
                        e.getPlayer().sendMessage(utils.ChatColorFormat(message));
                    }
                    if (!packages.get().getString("packages." + data + ".messages.broadcast_claim").equalsIgnoreCase("disable")) {
                        String message = packages.get().getString("packages." + data + ".messages.broadcast_claim");
                        message = utils.replacer(message, e.getPlayer(), null, null, data, e.getClickedBlock().getLocation(), null);
                        Bukkit.getServer().broadcastMessage(utils.ChatColorFormat(message));
                    }
                    clickedBlock.removeMetadata("SupplyPackages", plugin);
                }
            }
        }
    }
}