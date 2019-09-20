package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Commands.ConnectCommands;
import me.heknon.supplypackagesv2.Utils.SummonFallingBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SupplyPackagesV2 extends JavaPlugin {

    public final List<Double> velocityExceptions = Arrays.asList(-0.7513484036819682, -0.4958899661349951);
    public final List<Material> breakers = Arrays.asList(Material.STONE_BUTTON, Material.STONE_BUTTON, Material.LEVER, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.GOLD_PLATE, Material.IRON_PLATE, Material.STONE_PLATE, Material.WOOD_PLATE, Material.REDSTONE, Material.REDSTONE_COMPARATOR, Material.DIODE, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF, Material.RAILS, Material.BREWING_STAND, Material.BREWING_STAND_ITEM, Material.BANNER, Material.STANDING_BANNER, Material.WALL_BANNER, Material.THIN_GLASS, Material.IRON_FENCE, Material.CHEST, Material.ENDER_CHEST, Material.TRAPPED_CHEST, Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK, Material.DOUBLE_PLANT, Material.WOOD_STEP, Material.WOOD_DOUBLE_STEP, Material.DOUBLE_STEP, Material.STEP, Material.SKULL, Material.ENDER_PORTAL_FRAME, Material.LEVER, Material.FENCE);
    public final List<Material> exceptions = Arrays.asList(Material.AIR, Material.CHORUS_FLOWER, Material.CHORUS_PLANT);
    public final List<Material> doubleException = Arrays.asList(Material.BED, Material.BED_BLOCK, Material.SOIL, Material.CROPS, Material.SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS, Material.BEETROOT_SEEDS);
    public Map<String, Color> stringToColor = new HashMap<>();

    @Override
    public void onEnable() {

        final FileManager.Config messages = new FileManager(this).getConfig("messages.yml");
        final FileManager.Config settings = new FileManager(this).getConfig("settings.yml");
        final FileManager.Config items = new FileManager(this).getConfig("packages.yml");
        messages.copyDefaults(true).save();
        settings.copyDefaults(true).save();
        items.copyDefaults(true).save();
        this.getCommand("supplypackages").setExecutor(new ConnectCommands());
        this.getCommand("supplypackages").setTabCompleter(new SPTabComplete());
        getServer().getPluginManager().registerEvents(new PlayerGetRewards(), this);
        getServer().getPluginManager().registerEvents(new FireworkSummon(), this);
        getServer().getPluginManager().registerEvents(new SummonFallingBlock(), this);
        initStringToColor();


    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SupplyPackagesV2 has been disabled.");
    }

    public void pluginReload() {
        new FileManager(this).getConfig("messages.yml").reload();
        new FileManager(this).getConfig("packages.yml").reload();
        this.getServer().getPluginManager().disablePlugin(this);
        this.getServer().getPluginManager().enablePlugin(this);
    }
    
    private void initStringToColor() {
        stringToColor.put("AQUA", Color.YELLOW);
        stringToColor.put("BLACK", Color.BLACK);
        stringToColor.put("BLUE", Color.BLUE);
        stringToColor.put("FUCHSIA", Color.FUCHSIA);
        stringToColor.put("GRAY", Color.GRAY);
        stringToColor.put("GREEN", Color.GREEN);
        stringToColor.put("LIME", Color.LIME);
        stringToColor.put("MAROON", Color.MAROON);
        stringToColor.put("NAVY", Color.NAVY);
        stringToColor.put("OLIVE", Color.OLIVE);
        stringToColor.put("ORANGE", Color.ORANGE);
        stringToColor.put("PURPLE", Color.PURPLE);
        stringToColor.put("RED", Color.RED);
        stringToColor.put("SILVER", Color.SILVER);
        stringToColor.put("TEAL", Color.TEAL);
        stringToColor.put("WHITE", Color.WHITE);
        stringToColor.put("YELLOW", Color.YELLOW);
    }
}
