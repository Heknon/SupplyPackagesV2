package me.heknon.supplypackagesv2.Commands;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import de.tr7zw.nbtapi.NBTItem;
import jdk.nashorn.internal.parser.JSONParser;
import me.heknon.supplypackagesv2.Commands.CommandManager.SubCommand;
import me.heknon.supplypackagesv2.SupplyPackage.Package;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.*;

public class AddItemCommand extends SubCommand {

    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        YamlConfiguration config = plugin.getConfigManager().getConfig("messages.yml").get();
        if (!(sender instanceof Player)) {
            new Message(config.getString("only_players"), sender, false).chat();
            return;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            Package sp = Package.getDefaultPackageInstance();
            Set<ItemStack> currentItems = sp.getDrops();
            ItemStack heldItem = p.getInventory().getItemInMainHand();
            currentItems.add(heldItem);
            sp.setDrops(currentItems, true);
            new Message(config.getString("added_item"), sender, true).setToggleable(true).setPlaceholders(getPlaceholderHashMap(sender, heldItem, sp.getPackageName())).chat();

        } else if (args.length == 1) {
            Package sp = new Package(args[0]);
            if (!sp.exists()) {
                new Message(config.getString("invalid_package_name"), sender, false).setToggleable(true).chat();
                return;
            }
            Set<ItemStack> currentItems = sp.getDrops();
            ItemStack heldItem = p.getInventory().getItemInMainHand();
            currentItems.add(heldItem);
            sp.setDrops(currentItems, true);
            new Message(config.getString("added_item"), sender, true).setToggleable(true).setPlaceholders(getPlaceholderHashMap(sender, heldItem, sp.getPackageName())).chat();
        }
    }

    @Override
    public String name() {
        return "additem";
    }

    @Override
    public String permission() {
        return "supplypackages.additem";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    private HashMap<String, String> getPlaceholderHashMap(CommandSender sender, ItemStack item, String packageName) {
        return new HashMap<String, String>() {
            {
                put("{sender}", sender.getName());
                put("{package_name}", packageName);
                put("{item_name}", item.getType().toString());
                put("{amount}", String.valueOf(item.getAmount()));
                put("{durability}", String.valueOf(item.getDurability()));
                put("{nbt}", getDesignedNbtMessage(new NBTItem(item)));
            }
        };
    }

    private String getDesignedNbtMessage(NBTItem nbt) {
        StringBuilder string = new StringBuilder();
        java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Gson gson = new Gson();
        Map<String, Object> json = gson.fromJson(nbt.asNBTString(), mapType);
        for (Map.Entry<String, Object> keyVal : json.entrySet()) {
            String value = keyVal.getValue().toString();
            if (value.charAt(value.length() - 1) == 'b') value = value.substring(0, value.length() - 1);
            string.append("      &5").append(keyVal.getKey()).append(": ").append(value).append("\n");
        }
        if (string.length() == 0) string.append("      No NBT Data Found");
        return string.toString();
    }

    private ConfigManager.Config getPackageConfigInstance() {
        return new ConfigManager(plugin).getConfig("packages.yml");
    }
}
