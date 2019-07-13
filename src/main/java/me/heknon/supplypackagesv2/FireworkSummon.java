package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Utils.ChatUtils;
import me.heknon.supplypackagesv2.Utils.PlaceholderUtils;
import me.heknon.supplypackagesv2.Utils.SummonFallingBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class FireworkSummon implements Listener {

    final private SupplyPackagesV2 plugin = SupplyPackagesV2.getPlugin(SupplyPackagesV2.class);
    final private FileManager.Config settings = new FileManager(plugin).getConfig("settings.yml");
    final private FileManager.Config messages = new FileManager(plugin).getConfig("messages.yml");
    final private FileManager.Config permissions = new FileManager(plugin).getConfig("permissions.yml");
    final private SummonFallingBlock summonFallingBlock = new SummonFallingBlock();
    final private ChatUtils chatUtils = new ChatUtils();
    final private PlaceholderUtils placeholderUtils = new PlaceholderUtils();

    @EventHandler
    public void FireworkLaunch(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            String permission = permissions.get().getString("use_signal");
            if (!player.hasPermission(permission)) {
                String message = messages.get().getString("use_signal_no_permission");
                message = placeholderUtils.replacer(message, player, null, permission);
                player.sendMessage(chatUtils.ChatColorFormat(message));
            }
            if (player.getInventory().getItemInMainHand().getType().equals(Material.FIREWORK) || player.getInventory().getItemInOffHand().getType().equals(Material.FIREWORK)) {
                if (player.getInventory().getItemInMainHand().hasItemMeta() ||  player.getInventory().getItemInMainHand().hasItemMeta()) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(chatUtils.ChatColorFormat(settings.get().getString("firework_meta.display_name")))) {
                        List<String> lore = new ArrayList<>();
                        List<String> oldLore = settings.get().getStringList("firework_meta.lore");
                        for (String s : oldLore) {
                            lore.add(chatUtils.ChatColorFormat(s));
                        }
                        if (player.getInventory().getItemInMainHand().getItemMeta().getLore().equals(lore)) {
                            summonFallingBlock.summonFallingBlock(e.getClickedBlock().getLocation());
                            String message = messages.get().getString("summon_with_firework");
                            message = placeholderUtils.replacer(message, player, null, permission);
                            player.sendMessage(chatUtils.ChatColorFormat(message));
                        }
                    }
                }
            }
        }
    }
}
