package me.heknon.supplypackagesv2.API.Events;

import me.heknon.supplypackagesv2.SupplyPackage.Package;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PackageClaimedEvent extends Event {

    Player claimer;
    Location claimLocation;
    Package supplyPackage;

    public PackageClaimedEvent(Player claimer, Location claimLocation, Package supplyPackage) {
        this.claimer = claimer;
        this.claimLocation = claimLocation;
        this.supplyPackage = supplyPackage;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getClaimer() {
        return claimer;
    }

    public Location getClaimLocation() {
        return claimLocation;
    }

    public Package getSupplyPackage() {
        return supplyPackage;
    }
}
