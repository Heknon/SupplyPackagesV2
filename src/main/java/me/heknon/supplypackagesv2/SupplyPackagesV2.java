package me.heknon.supplypackagesv2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupplyPackagesV2 extends JavaPlugin {

    @Override
    public void onEnable() {

        final FileManager.Config messages = new FileManager(this).getConfig("messages.yml");
        final FileManager.Config permissions = new FileManager(this).getConfig("permissions.yml");
        final FileManager.Config placeholders = new FileManager(this).getConfig("placeholders.yml");
        final FileManager.Config settings = new FileManager(this).getConfig("settings.yml");
        final FileManager.Config items = new FileManager(this).getConfig("items.yml");
        messages.copyDefaults(true).save();
        permissions.copyDefaults(true).save();
        placeholders.copyDefaults(true).save();
        settings.copyDefaults(true).save();
        items.copyDefaults(true).save();
        getCommand("supplypackages").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new PlayerGetRewards(), this);
        getServer().getPluginManager().registerEvents(new FireworkSummon(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "SupplyPackagesV2 has been disabled.");
    }

    public void configReload() {
        new FileManager(this).getConfig("messages.yml").reload();
        new FileManager(this).getConfig("items.yml").reload();
        new FileManager(this).getConfig("permissions.yml").reload();
        new FileManager(this).getConfig("placeholders.yml").reload();
        new FileManager(this).getConfig("settings.yml").reload();
    }
}
