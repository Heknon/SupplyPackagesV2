package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Commands.*;
import me.heknon.supplypackagesv2.Commands.CommandManager.CommandManager;
import me.heknon.supplypackagesv2.Commands.CommandManager.SPTabComplete;
import me.heknon.supplypackagesv2.Listeners.PackageClaim;
import me.heknon.supplypackagesv2.Listeners.PackageSent;
import me.heknon.supplypackagesv2.Listeners.PlayerGetRewards;
import me.heknon.supplypackagesv2.Listeners.SignalSent;
import me.heknon.supplypackagesv2.SupplyPackage.Summoning.FireworkSummon;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class SupplyPackagesV2 extends JavaPlugin {

    public List<String> configNames = Arrays.asList("messages.yml", "packages.yml");
    public List<Double> velocityExceptions = Arrays.asList(-0.7513484036819682, -0.4958899661349951);
    public List<Material> breakers = Arrays.asList(Material.STONE_BUTTON, Material.STONE_BUTTON, Material.LEVER, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.GOLD_PLATE, Material.IRON_PLATE, Material.STONE_PLATE, Material.WOOD_PLATE, Material.REDSTONE, Material.REDSTONE_COMPARATOR, Material.DIODE, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF, Material.RAILS, Material.BREWING_STAND, Material.BREWING_STAND_ITEM, Material.BANNER, Material.STANDING_BANNER, Material.WALL_BANNER, Material.THIN_GLASS, Material.IRON_FENCE, Material.CHEST, Material.ENDER_CHEST, Material.TRAPPED_CHEST, Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK, Material.DOUBLE_PLANT, Material.WOOD_STEP, Material.WOOD_DOUBLE_STEP, Material.DOUBLE_STEP, Material.STEP, Material.SKULL, Material.ENDER_PORTAL_FRAME, Material.LEVER, Material.FENCE);
    public List<Material> exceptions = Arrays.asList(Material.AIR, Material.CHORUS_FLOWER, Material.CHORUS_PLANT);
    public List<Material> doubleException = Arrays.asList(Material.BED, Material.BED_BLOCK, Material.SOIL, Material.CROPS, Material.SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS, Material.BEETROOT_SEEDS);
    public CommandManager commandManager;


    @Override
    public void onEnable() {
        registerCommand();
        registerEvents();
        this.configNames.forEach(config -> getConfigManager().getConfig(config).copyDefaults(true).save());
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SupplyPackagesV2 has been disabled.");
    }

    private void registerCommand() {
        commandManager = new CommandManager();
        this.getCommand("supplypackages").setExecutor(commandManager);
        this.getCommand("supplypackages").setTabCompleter(new SPTabComplete());
        commandManager.commands.addAll(Arrays.asList(new SummonCommand(), new GiveSignalCommand(), new ReloadCommand(), new AddItemCommand(), new ResetItemsCommand()));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PackageClaim(), this);
        getServer().getPluginManager().registerEvents(new PackageSent(), this);
        getServer().getPluginManager().registerEvents(new SignalSent(), this);
        getServer().getPluginManager().registerEvents(new PlayerGetRewards(), this);
        getServer().getPluginManager().registerEvents(new FireworkSummon(), this);
    }

    public ConfigManager getConfigManager() {
        return new ConfigManager(this);
    }

}
