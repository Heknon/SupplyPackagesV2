package me.heknon.supplypackagesv2.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Message {
    private String MESSAGE;
    private boolean IS_BROADCAST = false;
    private Set<CommandSender> RECIPIENTS = new HashSet<CommandSender>();
    private HashMap<String, String> PLACEHOLDERS = new HashMap<>();
    private boolean usePlaceholders = false;
    private boolean toggleable = false;

    public Message(String message, CommandSender recipient) {
        this.MESSAGE = message;
        this.RECIPIENTS = new HashSet<>(Collections.singletonList(recipient));
    }

    public Message(String message, CommandSender recipient, boolean usePlaceholders) {
        this.MESSAGE = message;
        this.RECIPIENTS = new HashSet<>(Collections.singletonList(recipient));
        this.usePlaceholders = usePlaceholders;
    }

    public Message(String message, Set<CommandSender> recipients) {
        this.MESSAGE = message;
        this.RECIPIENTS = recipients;
    }

    public Message(String message, Set<CommandSender> recipients, boolean usePlaceholders) {
        this.MESSAGE = message;
        this.RECIPIENTS = recipients;
        this.usePlaceholders = usePlaceholders;
    }

    public Message(String message, boolean isBroadcast) {
        this.MESSAGE = message;
        this.IS_BROADCAST = isBroadcast;
    }

    public Message(String message, boolean isBroadcast, boolean usePlaceholders) {
        this.MESSAGE = message;
        this.IS_BROADCAST = isBroadcast;
        this.usePlaceholders = usePlaceholders;
    }

    private String getPlaceholderMessage() {
        if (this.PLACEHOLDERS.isEmpty() || this.PLACEHOLDERS == null || !usePlaceholders) return this.MESSAGE;
        String modifiedMessage = this.MESSAGE;
        for (String replace : this.PLACEHOLDERS.keySet()) {
            String replaceWith = this.PLACEHOLDERS.get(replace);
            modifiedMessage = modifiedMessage.replace(replace, replaceWith);
        }
        return modifiedMessage;
    }

    public Message chat() {
        String message = getPlaceholderMessage();
        if (this.IS_BROADCAST) {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        if (this.RECIPIENTS != null) {
            for (CommandSender recipient : this.RECIPIENTS) {
                if (recipient instanceof Player && !((Player) recipient).isOnline()) continue;
                recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
        return this;
    }

    public Message addRecipient(CommandSender sender) {
        this.RECIPIENTS.add(sender);
        return this;
    }

    public Message setIsBroadcast(boolean isBroadcast) {
        this.IS_BROADCAST = isBroadcast;
        return this;
    }

    /**
     * is a broadcast message
     *
     * @return true if is a broadcast message otherwise false
     */
    public boolean isBroadcast() {
        return IS_BROADCAST;
    }

    /**
     * gets the message to be sent
     *
     * @return {String} The message set.
     */
    public String getMessage() {
        return this.MESSAGE;
    }

    // SETTERS
    public void setMessage(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }


    // GETTERS

    /**
     * gets the recipients of the message
     *
     * @return recipients of the message
     */
    public Set<CommandSender> getRecipients() {
        return this.RECIPIENTS;
    }

    public Message setRecipients(Set<CommandSender> recipients) {
        this.RECIPIENTS = recipients;
        return this;
    }

    public HashMap<String, String> getPlaceholders() {
        return this.PLACEHOLDERS;
    }

    public Message setPlaceholders(HashMap<String, String> placeholders) {
        this.PLACEHOLDERS = placeholders;
        return this;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public Message setToggleable(boolean toggleable) {
        this.toggleable = toggleable;
        return this;
    }
}
