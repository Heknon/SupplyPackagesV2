package me.heknon.supplypackagesv2.SupplyPackage;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;

public class Firework {
    private ConfigurationSection packageSection;
    private ConfigurationSection fireworkSection;
    private Color color;
    private int amount;
    private boolean followPackage;
    private Location launchLocation;
    private static HashMap<String, Color> stringColorHashMap = new HashMap<String, Color>() {
        {
            put("aqua", Color.AQUA);
            put("black", Color.BLACK);
            put("blue", Color.BLUE);
            put("fuchsia", Color.FUCHSIA);
            put("gray", Color.GRAY);
            put("green", Color.GREEN);
            put("lime", Color.LIME);
            put("maroon", Color.MAROON);
            put("navy", Color.NAVY);
            put("olive", Color.OLIVE);
            put("orange", Color.ORANGE);
            put("purple", Color.PURPLE);
            put("red", Color.RED);
            put("silver", Color.SILVER);
            put("teal", Color.TEAL);
            put("white", Color.WHITE);
            put("yellow", Color.YELLOW);
        }
    };

    private Firework(ConfigurationSection packageSection, Location launchLocation) {
        this.fireworkSection = packageSection.getConfigurationSection("firework");
        this.color = stringColorHashMap.getOrDefault(this.fireworkSection.getString("color"), Color.AQUA);
        this.amount = this.fireworkSection.getInt("amount");
        this.followPackage = this.fireworkSection.getBoolean("follow_package");
        this.launchLocation = launchLocation;
    }

    Firework(ConfigurationSection packageSection) {
        this.fireworkSection = packageSection.getConfigurationSection("firework");
        this.color = stringColorHashMap.getOrDefault(this.fireworkSection.getString("color"), Color.AQUA);
        this.amount = this.fireworkSection.getInt("amount");
        this.followPackage = this.fireworkSection.getBoolean("follow_package");
    }


    public void summonFireworks(int amount, Location loc) {
        org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(this.color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.setInvulnerable(true);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            org.bukkit.entity.Firework fw2 = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public void summonFireworks() {
        org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) this.launchLocation.getWorld().spawnEntity(this.launchLocation, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(this.color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.setInvulnerable(true);
        fw.detonate();

        for (int i = 0; i < this.amount; i++) {
            org.bukkit.entity.Firework fw2 = (org.bukkit.entity.Firework) this.launchLocation.getWorld().spawnEntity(this.launchLocation, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public ConfigurationSection getPackageSection() {
        return packageSection;
    }

    public ConfigurationSection getFireworkSection() {
        return fireworkSection;
    }

    public Color getColor() {
        return color;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isFollowPackage() {
        return followPackage;
    }

    public Location getLaunchLocation() {
        return launchLocation;
    }

    public static HashMap<String, Color> getStringColorHashMap() {
        return stringColorHashMap;
    }
}