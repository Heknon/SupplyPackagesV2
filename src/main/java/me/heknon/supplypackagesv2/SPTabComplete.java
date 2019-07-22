package me.heknon.supplypackagesv2;

import me.heknon.supplypackagesv2.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SPTabComplete implements TabCompleter {
    private final Utils utils = new Utils();
    private List<String> packages = utils.getNamesOfSupplyPackages();
    private final List<String> sub = Arrays.asList("summon", "help", "reload", "additem", "resetitems");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("supplypackages")) {
            if (args.length == 1) {
                return sub.stream().map(String::toLowerCase).filter(s -> s.contains(args[0].toLowerCase())).collect(Collectors.toList());
            } else if (args.length == 2 && args[0].equalsIgnoreCase("summon")) {
                List<String> onlineNames = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).filter(s -> s.toLowerCase().contains(args[1].toLowerCase())).collect(Collectors.toList());
                return Stream.concat(packages.stream().filter(s -> s.toLowerCase().contains(args[1].toLowerCase())), onlineNames.stream()).collect(Collectors.toList());
            } else if (args.length == 3 && args[0].equalsIgnoreCase("summon") && packages.contains(args[1].toLowerCase())) {
                return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).filter(s -> s.toLowerCase().contains(args[2].toLowerCase())).collect(Collectors.toList());
            }
        }
        return null;
    }
}
