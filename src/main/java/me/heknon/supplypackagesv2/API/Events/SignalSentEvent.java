package me.heknon.supplypackagesv2.API.Events;

import me.heknon.supplypackagesv2.SupplyPackage.Package;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignalSentEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private CommandSender sender;
    private Player receiver;
    private Package signalPackage;


    public SignalSentEvent(CommandSender sender, Player receiver, Package signalPackage) {
        this.sender = sender;
        this.receiver = receiver;
        this.signalPackage = signalPackage;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public Package getSignalPackage() {
        return signalPackage;
    }
}
