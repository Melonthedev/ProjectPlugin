package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.Lifesteal;

import java.util.ArrayList;
import java.util.List;

public class LifestealCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String prefix = Lifesteal.prefix;
        if (!sender.isOp()) {
            sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to use this command.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /lifesteal <setheartcount | getheartcount | addheart | removeheart | revive | eliminate>");
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
                    sender.sendMessage(prefix + "Added " + count + " heart" + (count != 1 ? "s" : "") + " from " + target.getName());
                }
                case "removeheart" -> {
                    int count = getHeartCountFromString(args[2], sender);
                    Lifesteal.removeHeart(target.getUniqueId(), count);
                    sender.sendMessage(prefix + "Removed " + count + " heart" + (count != 1 ? "s" : "") + " from " + target.getName());
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
            tab.add("setHeartCount");
            tab.add("getHeartCount");
            tab.add("addHeart");
            tab.add("removeHeart");
            tab.add("revive");
            tab.add("eliminate");
        } else if (args.length == 2) {
            tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        }
        return tab;
    }
}
