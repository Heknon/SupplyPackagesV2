package me.heknon.supplypackagesv2;

import de.tr7zw.itemnbtapi.NBTItem;
import me.heknon.supplypackagesv2.Utils.Utils;
import me.heknon.supplypackagesv2.Utils.SummonFallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FireworkSummon implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    final private FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    final private FileManager.Config packages = new FileManager(plugin).getConfig("packages.yml");
    final private SummonFallingBlock summonFallingBlock = new SummonFallingBlock();
    final private Utils utils = new Utils();

    @EventHandler
    public void FireworkLaunch(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            ItemStack mainhandItem = player.getInventory().getItemInMainHand();
            ItemStack offhandItem = player.getInventory().getItemInOffHand();
            NBTItem mainhandItemNBT = new NBTItem(mainhandItem);
            NBTItem offhandItemNBT = new NBTItem(offhandItem);
            if (mainhandItemNBT.hasNBTData() || offhandItemNBT.hasNBTData()) {
                if (mainhandItemNBT.hasKey("SupplyPackages") || mainhandItemNBT.hasKey("SupplyPackages")) {
                    String SPName = mainhandItemNBT.getString("SupplyPackages");
                    String permission = "supplypackages.usesignal";
                    if (!player.hasPermission(permission)) {
                        String message = messages.get().getString("use_signal_no_permission");
                        message = utils.replacer(message, player, null, null, null, e.getClickedBlock().getLocation(), permission);
                        player.sendMessage(utils.ChatColorFormat(message));
                    }
                    summonFallingBlock.summonFallingBlock(e.getClickedBlock().getLocation(), SPName);
                    String message = messages.get().getString("summon_with_firework");
                    message = utils.replacer(message, player, null, null, null, e.getClickedBlock().getLocation(), permission);
                    player.sendMessage(utils.ChatColorFormat(message));
                }
            }
        }
    }
}
