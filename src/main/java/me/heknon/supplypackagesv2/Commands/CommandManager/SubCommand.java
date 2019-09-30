package me.heknon.supplypackagesv2.Commands.CommandManager;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    public SubCommand() {
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public abstract String name();

    public abstract String permission();

    public abstract String[] aliases();
}
