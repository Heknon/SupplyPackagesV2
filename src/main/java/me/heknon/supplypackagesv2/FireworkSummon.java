package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Utils.Utils;
import me.heknon.supplypackagesv2.Utils.SummonFallingBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.stream.Collectors;

public class FireworkSummon implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    final private FileManager.Config settings = new FileManager(plugin).getConfig("settings.yml");
    final private FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    final private FileManager.Config items = new FileManager(plugin).getConfig("packages.yml");
    final private SummonFallingBlock summonFallingBlock = new SummonFallingBlock();
    final private Utils utils = new Utils();

    @EventHandler
    public void FireworkLaunch(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            if (player.getInventory().getItemInMainHand().getType().equals(Material.FIREWORK) || player.getInventory().getItemInOffHand().getType().equals(Material.FIREWORK)) {
                if (player.getInventory().getItemInMainHand().hasItemMeta() ||  player.getInventory().getItemInMainHand().hasItemMeta()) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(utils.ChatColorFormat(settings.get().getString("firework_meta.display_name")))) {
                        String permission = "supplypackages.usesignal";
                        if (!player.hasPermission(permission)) {
                            String message = messages.get().getString("use_signal_no_permission");
                            message = utils.replacer(message, player, null, null, null, e.getClickedBlock().getLocation(), permission);
                            player.sendMessage(utils.ChatColorFormat(message));
                        }
                        List<String> lore = settings.get().getStringList("firework_meta.lore").stream().map(utils::ChatColorFormat).collect(Collectors.toList());
                        if (player.getInventory().getItemInMainHand().getItemMeta().getLore().equals(lore)) {
                            summonFallingBlock.summonFallingBlock(e.getClickedBlock().getLocation(), items.get().getString("default_supply_package"));
                            String message = messages.get().getString("summon_with_firework");
                            message = utils.replacer(message, player, null, null, null, e.getClickedBlock().getLocation(), permission);
                            player.sendMessage(utils.ChatColorFormat(message));
                        }
                    }
                }
            }
        }
    }
}
