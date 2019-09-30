package me.heknon.supplypackagesv2.API.Events;

import me.heknon.supplypackagesv2.API.SummonReason;
import me.heknon.supplypackagesv2.SupplyPackage.Summoning.SupplyPackage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PackageSummonEvent extends Event {

    private Player packageReceiver;
    private CommandSender packageSender;
    private Location dropLocation;
    private SupplyPackage supplyPackage;
    private SummonReason summonReason;

    public PackageSummonEvent(Player packageReceiver, CommandSender packageSender, SupplyPackage supplyPackage, Location dropLocation, SummonReason summonReason) {
        this.packageReceiver = packageReceiver;
        this.packageSender = packageSender;
        this.dropLocation = dropLocation;
        this.supplyPackage = supplyPackage;
        this.summonReason = summonReason;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPackageReceiver() {
        return packageReceiver;
    }

    public CommandSender getPackageSender() {
        return packageSender;
    }

    public Location getDropLocation() {
        return dropLocation;
    }

    public SupplyPackage getSupplyPackage() {
        return supplyPackage;
    }

    public SummonReason getSummonReason() {
        return summonReason;
    }
}
