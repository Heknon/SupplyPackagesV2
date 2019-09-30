package me.heknon.supplypackagesv2.SupplyPackage;

import de.tr7zw.nbtapi.NBTItem;
import me.heknon.supplypackagesv2.Errors.ConfigurationSectionNotFoundException;
import me.heknon.supplypackagesv2.Errors.PackageItemException;
import me.heknon.supplypackagesv2.Errors.PackageNotExistException;
import me.heknon.supplypackagesv2.SupplyPackagesV2;
import me.heknon.supplypackagesv2.Utils.ConfigManager;
import me.heknon.supplypackagesv2.Utils.Message;
import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Package {
    private SupplyPackagesV2 plugin = JavaPlugin.getPlugin(SupplyPackagesV2.class);
    private String packageName;
    private Signal signal;
    private Set<ItemStack> drops;
    private Message summonBroadcast;
    private Message summonMessage;
    private Message claimMessage;
    private Message claimBroadcast;
    private Material fallingMaterial;
    private Material landingMaterial;
    private boolean lightningLand;
    private Sound soundTillClaim;
    private Sound soundOnClaim;
    private Effect effectTillClaim;
    private Effect effectOnClaim;

    /**
     * Constructor for Package class.
     * Data class for packages from packages.yml
     *
     * @param packageName The name of the package that appears in packages.yml
     */
    public Package(String packageName) {
        this.packageName = packageName;
        if (!this.exists()) return;
        this.drops = this.getDropsFromConfig();
        this.initializeMessages();
        this.initializeMaterials();
        this.signal = new Signal(this);
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        try {
            this.effectOnClaim = Effect.valueOf(section.getString("effect_on_claim"));
        } catch (IllegalArgumentException ignored) {
            this.effectOnClaim = null;
        }
        try {
            this.effectTillClaim = Effect.valueOf(section.getString("effect_till_claim"));
        } catch (IllegalArgumentException ignored) {
            this.effectTillClaim = null;
        }
        try {
            this.soundTillClaim = Sound.valueOf(section.getString("sound_till_claim"));
        } catch (IllegalArgumentException ignored) {
            this.soundTillClaim = null;
        }
        try {
            this.soundOnClaim = Sound.valueOf(section.getString("sound_on_claim"));
        } catch (IllegalArgumentException ignored) {
            this.soundOnClaim = null;
        }
    }

    public boolean exists() {
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        return section != null;
    }

    public static Package getDefaultPackageInstance() {
        return new Package(JavaPlugin.getPlugin(SupplyPackagesV2.class).getConfigManager().getConfig("packages.yml").get().getString("default_supply_package"));
    }

    public static boolean isValidPackageName(String supplyPackage) {
        for (String itemList : JavaPlugin.getPlugin(SupplyPackagesV2.class).getConfigManager().getConfig("packages.yml").get().getConfigurationSection("packages").getKeys(false)) {
            if (itemList.equalsIgnoreCase(supplyPackage)) return true;
        }
        return false;
    }

    public static List<String> getAllPackageNames() {
        return new ArrayList<>(JavaPlugin.getPlugin(SupplyPackagesV2.class).getConfigManager().getConfig("packages.yml").get().getConfigurationSection("packages").getKeys(false));
    }

    private Set<ItemStack> getDropsFromConfig() {
        Set<ItemStack> returnValue = new HashSet<>();
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        ConfigurationSection items = section.getConfigurationSection("items");
        for (String key : items.getKeys(false)) {
            ConfigurationSection curr = items.getConfigurationSection(key);
            String materialStr = curr.getString("material");
            String amountStr = curr.getString("amount");
            ConfigurationSection nbt = curr.getConfigurationSection("NBT");
            int amount;
            Material material;
            if (!Utils.stringIsMaterial(materialStr)) {
                Bukkit.getLogger().severe("SupplyPackagesV2 - packages.yml | items:" + key + ":material " + "^ Entered material \"" + materialStr + "\" is not a valid material! The material will default to AIR!!!");
                material = Material.AIR;
            } else {
                material = Material.valueOf(materialStr);
            }
            if (!Utils.isNumeric(amountStr)) {
                Bukkit.getLogger().severe("SupplyPackagesV2 - packages.yml | items:" + key + ":amount " + "^ Entered amount \"" + amountStr + "\" is not a valid number! The number will default to 1!!!");
                amount = 1;
            } else {
                amount = Integer.parseInt(amountStr);
            }
            HashMap<String, String> nbtData = nbt == null ? null : Utils.getConfigCorrespondingKeyValueMap(nbt);
            ItemStack item = new ItemStack(material, amount);
            NBTItem nbtItem = new NBTItem(item);
            if (nbtData != null) {
                for (String nbtKey : nbtData.keySet()) {
                    String nbtValue = nbtData.get(nbtKey);
                    if (Utils.isNumeric(nbtValue) || Utils.stringIsBoolean(nbtValue)) {
                        if (Utils.isNumeric(nbtValue)) {
                            nbtItem.setInteger(nbtKey, Integer.parseInt(nbtValue));
                        } else if (Utils.stringIsBoolean(nbtValue)){
                            nbtItem.setBoolean(nbtKey, Boolean.parseBoolean(nbtValue));
                        }
                    } else {
                        nbtItem.setString(nbtKey, nbtValue);
                    }
                }
            }
            returnValue.add(nbtItem.getItem());
        }
        return returnValue;
    }

    public Set<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Set the drops of the package.
     *
     * @param drops the drops
     */
    public void setDrops(Set<ItemStack> drops) {
        this.drops = drops;
    }

    /**
     * Set the drops of the package.
     *
     * @param drops        the drops
     * @param changeConfig should change in packages.yml
     */
    public void setDrops(Set<ItemStack> drops, boolean changeConfig) {
        if (!changeConfig) setDrops(drops);
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        section.set("items", null);
        for (ItemStack item : drops) {
            section.set("items." + item.getType().toString(), item.getAmount());
        }
        this.drops = drops;
        config.save();
        config.reload();
    }

    public void resetItems() {
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        ConfigurationSection items = section.getConfigurationSection("items");
        for (String item : items.getKeys(false)) {
            items.set(item, null);
        }
        config.save();
        config.reload();
    }

    private void initializeMessages() {
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        this.summonBroadcast = new Message(section.getConfigurationSection("messages").getString("summon_broadcast"), true, true);
        this.claimBroadcast = new Message(section.getConfigurationSection("messages").getString("claim_broadcast"), true, true);
        this.summonMessage = new Message(section.getConfigurationSection("messages").getString("summon_message"), false, true);
        this.claimMessage = new Message(section.getConfigurationSection("messages").getString("claim_message"), false, true);
    }

    private void initializeMaterials() {
        ConfigManager.Config config = this.getPackagesConfig();
        ConfigurationSection section = config.get().getConfigurationSection("packages").getConfigurationSection(this.packageName);
        this.fallingMaterial = Material.valueOf(section.getString("falling_material"));
        this.landingMaterial = Material.valueOf(section.getString("landing_material"));
    }

    ConfigManager.Config getPackagesConfig() {
        return plugin.getConfigManager().getConfig("packages.yml");
    }

    public String getPackageName() {
        return packageName;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public Message getSummonBroadcast() {
        return summonBroadcast;
    }

    public void setSummonBroadcast(Message summonBroadcast) {
        this.summonBroadcast = summonBroadcast;
    }

    public Message getSummonMessage() {
        return summonMessage;
    }

    public void setSummonMessage(Message summonMessage) {
        this.summonMessage = summonMessage;
    }

    public Message getClaimMessage() {
        return claimMessage;
    }

    public void setClaimMessage(Message claimMessage) {
        this.claimMessage = claimMessage;
    }

    public Message getClaimBroadcast() {
        return claimBroadcast;
    }

    public void setClaimBroadcast(Message claimBroadcast) {
        this.claimBroadcast = claimBroadcast;
    }

    public Material getFallingMaterial() {
        return fallingMaterial;
    }

    public void setFallingMaterial(Material fallingMaterial) {
        this.fallingMaterial = fallingMaterial;
    }

    public Material getLandingMaterial() {
        return landingMaterial;
    }

    public void setLandingMaterial(Material landingMaterial) {
        this.landingMaterial = landingMaterial;
    }

    public boolean isLightningLand() {
        return lightningLand;
    }

    public void setLightningLand(boolean lightningLand) {
        this.lightningLand = lightningLand;
    }

    public Sound getSoundTillClaim() {
        return soundTillClaim;
    }

    public void setSoundTillClaim(Sound soundTillClaim) {
        this.soundTillClaim = soundTillClaim;
    }

    public Sound getSoundOnClaim() {
        return soundOnClaim;
    }

    public void setSoundOnClaim(Sound soundOnClaim) {
        this.soundOnClaim = soundOnClaim;
    }

    public Effect getEffectTillClaim() {
        return effectTillClaim;
    }

    public void setEffectTillClaim(Effect effectTillClaim) {
        this.effectTillClaim = effectTillClaim;
    }

    public Effect getEffectOnClaim() {
        return effectOnClaim;
    }

    public void setEffectOnClaim(Effect effectOnClaim) {
        this.effectOnClaim = effectOnClaim;
    }
}
