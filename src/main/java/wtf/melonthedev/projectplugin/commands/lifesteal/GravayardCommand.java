package wtf.melonthedev.projectplugin.commands.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.information.PositionCommand;

import java.util.ArrayList;
import java.util.List;

public class GravayardCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have the permissions to use this command!");
            return true;
        }
        //if (!(sender instanceof Player p)) {
        //    sender.sendMessage(ChatColor.RED + "Error: You are not a Player");
        //    return true;
        //}
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {


            return true;
        }
        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Error: do /graveyard <list/add/remove> " + ChatColor.ITALIC + "x y z");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "add" -> {
                try {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    String world = (sender instanceof Player p) ? p.getWorld().getName() : Bukkit.getWorlds().get(0).getName();
                    String id = world + x + y + z;
                    Main.getPlugin().getConfig().set("graveyardpositions." + id + ".x", x);
                    Main.getPlugin().getConfig().set("graveyardpositions." + id + ".y", y);
                    Main.getPlugin().getConfig().set("graveyardpositions." + id + ".z", z);
                    Main.getPlugin().getConfig().set("graveyardpositions." + id + ".world", world);
                    Main.getPlugin().getConfig().set("graveyardpositions." + id + ".uuid", null);
                    Main.getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Success! You added a graveyardposition at X: " + x + " Y: " + y + " Z: " + z);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(ChatColor.RED + "Error: Use /graveyard " + ChatColor.ITALIC + "x y z");
                }
                return true;
            }
            case "remove" -> {
                try {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    String world = sender instanceof Player p ? p.getWorld().getName() : Bukkit.getWorlds().get(0).getName();
                    String id = world + x + y + z;
                    Main.getPlugin().getConfig().set("graveyardpositions." + id, null);
                    Main.getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Success! You removed the graveyardposition at X: " + x + " Y: " + y + " Z: " + z);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(ChatColor.RED + "Error: Use /graveyard " + ChatColor.ITALIC + "x y z");
                }
            }
            default -> sender.sendMessage(ChatColor.RED + "Error: do /graveyard <list/add/remove> " + ChatColor.ITALIC + "x y z");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("add");
            tab.add("remove");
            tab.add("list");
        }
        if (sender instanceof Player) {
            Block targetBlock = ((Player) sender).getTargetBlockExact(5);
            if (targetBlock == null) return tab;
            if (args.length == 2) {
                tab.add(String.valueOf(targetBlock.getX()));
                tab.add(targetBlock.getX() + " " + targetBlock.getY());
                tab.add(targetBlock.getX() + " " + targetBlock.getY() + " " + targetBlock.getZ());
            } else if (args.length == 3) {
                tab.add(String.valueOf(targetBlock.getY()));
                tab.add(targetBlock.getY() + " " + targetBlock.getZ());
            } else if (args.length == 4) {
                tab.add(String.valueOf(targetBlock.getZ()));
            }
        }
        return tab;
    }
}
