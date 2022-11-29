package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.Lifesteal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LifestealCommand implements TabExecutor {

    private static final String[] subCommands = new String[] {
            "setHeartCount",
            "getHeartCount",
            "addHeart",
            "removeHeart",
            "revive",
            "eliminate",
            "enable",
            "disable",
            "giveHeartItem"
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String prefix = Lifesteal.prefix;
        if (!sender.isOp()) {
            sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to use this command.");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("enable")) {
                Lifesteal.setLifestealActive(true);
                sender.sendMessage(prefix + "You enabled Lifesteal!");
                return true;
            } else if (args[0].equalsIgnoreCase("disable")) {
                Lifesteal.setLifestealActive(false);
                sender.sendMessage(prefix + "You disabled Lifesteal!");
                return true;
            } else if (args[0].equalsIgnoreCase("giveheartitem")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(prefix + "You must be a player!");
                    return true;
                }
                ((Player) sender).getInventory().addItem(Lifesteal.getHeartItem());
                sender.sendMessage(prefix + "Gave 1 Heart Item");
                return true;
            }
        }

        if (args.length < 2) {
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /lifesteal <" + Arrays.toString(subCommands).replace(", ", " | ").replace("[", "").replace("]", "") + ">");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
        if (target == null) {
            sender.sendMessage(prefix + ChatColor.RED + "Player was not found!");
            return true;
        }
        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "addheart" -> {
                    Lifesteal.giveHeart(target.getUniqueId(), 1);
                    sender.sendMessage(prefix + "Added 1 heart to " + target.getName());
                }
                case "removeheart" -> {
                    Lifesteal.removeHeart(target.getUniqueId(), 1);
                    sender.sendMessage(prefix + "Removed 1 heart from " + target.getName());
                }
                case "revive" -> {
                    Lifesteal.revivePlayer(target.getUniqueId());
                    sender.sendMessage(prefix + "Revived " + target.getName());
                }
                case "eliminate" -> {
                    Lifesteal.eliminatePlayer(target.getUniqueId());
                    sender.sendMessage(prefix + "Eliminated " + target.getName());
                }
                case "giveheartitem" -> {
                    if (target.getPlayer() == null) return true;
                    target.getPlayer().getInventory().addItem(Lifesteal.getHeartItem());
                    sender.sendMessage(prefix + "Gave Heart Item to " + target.getName());
                }
                case "getheartcount" -> sender.sendMessage(prefix + target.getName() + " has " + Lifesteal.getHeartCount(target.getUniqueId()) + " Heart" + (Lifesteal.getHeartCount(target.getUniqueId()) != 1 ? "s!" : "!"));
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "setheartcount" -> {
                    int count = getHeartCountFromString(args[2], sender);
                    Lifesteal.setHeartCount(target.getUniqueId(), count);
                    sender.sendMessage(prefix + "Set heartcount of " + target.getName() + " to " + count);
                }
                case "addheart" -> {
                    int count = getHeartCountFromString(args[2], sender);
                    Lifesteal.giveHeart(target.getUniqueId(), count);
                    sender.sendMessage(prefix + "Added " + count + " heart" + (count != 1 ? "s" : "") + " to " + target.getName());
                }
                case "removeheart" -> {
                    int count = getHeartCountFromString(args[2], sender);
                    Lifesteal.removeHeart(target.getUniqueId(), count);
                    sender.sendMessage(prefix + "Removed " + count + " heart" + (count != 1 ? "s" : "") + " from " + target.getName());
                }
                case "giveheartitem" -> {
                    if (target.getPlayer() == null) return true;
                    int count = getHeartCountFromString(args[2], sender);
                    ItemStack stack = Lifesteal.getHeartItem();
                    stack.setAmount(count);
                    target.getPlayer().getInventory().addItem(stack);
                    sender.sendMessage(prefix + "Gave " + count + " Heart Item to " + target.getName());
                }
            }
        }
        return false;
    }

    public int getHeartCountFromString(String s, CommandSender sender) {
        int count;
        try {
            count = Integer.parseInt(s);
        } catch (NumberFormatException exception) {
            sender.sendMessage(Lifesteal.prefix + ChatColor.RED + "Syntaxerror: /lifesteal setHeartCount " + ChatColor.ITALIC + "<Player> <Anzahl>");
            return 0;
        }
        if (count < 0) {
            sender.sendMessage(Lifesteal.prefix + ChatColor.RED + "Error: Number must be positive");
            return 0;
        }
        return count;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.addAll(List.of(subCommands));
        } else if (args.length == 2) {
            tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        }
        return tab;
    }
}
