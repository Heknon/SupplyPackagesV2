package me.heknon.supplypackagesv2.SupplyPackage;

import de.tr7zw.nbtapi.NBTItem;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import me.heknon.supplypackagesv2.Utils.Message;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Signal {
    private Package parentPackage;
    private ConfigurationSection signalConfigurationSection;
    private me.heknon.supplypackagesv2.SupplyPackage.Firework firework;
    private Material material;
    private List<String> lore;
    private String displayName;
    private Message summonMessage;
    private Message summonBroadcast;


    Signal(Package parentPackage) {
        this.parentPackage = parentPackage;
        ConfigManager.Config config = this.parentPackage.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.parentPackage.getPackageName());
        this.signalConfigurationSection = section.getConfigurationSection("signal");
        this.firework = new me.heknon.supplypackagesv2.SupplyPackage.Firework(section);
        try {
            this.material = Material.valueOf(this.signalConfigurationSection.getString("material"));
        } catch (IllegalArgumentException ignored) {
            this.material = Material.FIREWORK;
        }
        this.lore = this.signalConfigurationSection.getStringList("lore").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
        this.displayName = ChatColor.translateAlternateColorCodes('&', this.signalConfigurationSection.getString("display_name"));
        this.summonMessage = new Message(this.signalConfigurationSection.getString("summon_message"), false, true);
        this.summonBroadcast = new Message(this.signalConfigurationSection.getString("summon_broadcast"), true, true);
    }

    public ItemStack getSignalStack() {
        NBTItem signalNBT = new NBTItem(new ItemStack(this.material));
        signalNBT.setString("SupplyPackageName", parentPackage.getPackageName());
        ItemStack signal = signalNBT.getItem();
        ItemMeta meta = signal.getItemMeta();
        meta.setDisplayName(getEncodedDisplayName());
        meta.setLore(getEncodedLore());
        signal.setItemMeta(meta);
        return signal;
    }

    private String getEncodedDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', this.displayName);
    }

    private List<String> getEncodedLore() {
        return this.lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material signalMaterial) {
        this.material = signalMaterial;
    }

    public Package getParentPackage() {
        return parentPackage;
    }

    public me.heknon.supplypackagesv2.SupplyPackage.Firework getFirework() {
        return firework;
    }

    public ConfigurationSection getSignalConfigurationSection() {
        return signalConfigurationSection;
    }

    public Message getSummonMessage() {
        return summonMessage;
    }

    public Message getSummonBroadcast() {
        return summonBroadcast;
    }
}
