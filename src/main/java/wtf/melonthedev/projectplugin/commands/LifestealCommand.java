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
            sender.sendMessage(prefix + ChatColor.RED + "Error: This player was not found!");
            return true;
        }
        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "addheart" -> {
                    Lifesteal.giveHeart(target.getUniqueId(), 1);
                    sender.sendMessage(prefix + "Success! Added 1 heart to " + target.getName());
                }
                case "removeheart" -> {
                    Lifesteal.removeHeart(target.getUniqueId(), 1);
                    sender.sendMessage(prefix + "Success! Removed 1 heart from " + target.getName());
                }
                case "getheartcount" -> sender.sendMessage(prefix + target.getName() + " has " + Lifesteal.getHeartCount(target.getUniqueId()) + " Hearts!");
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "setheartcount":
                    int count;
                    try {
                        count = Integer.parseInt(args[0]);
                        Lifesteal.setHeartCount(target.getUniqueId(), count);
                        sender.sendMessage(prefix + "Success! Set heartcount of " + target.getName() + " to " + count);
                    } catch(NumberFormatException exception) {
                        sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: do /lifesteal setHeartCount " + ChatColor.ITALIC + "<Player> <Anzahl>");
                    }
                    break;
                case "addheart":
                    int addcount;
                    try {
                        addcount = Integer.parseInt(args[0]);
                    } catch(NumberFormatException exception) {
                        sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: do /lifesteal addHeart " + ChatColor.ITALIC + "<Player> <Anzahl>");
                        return true;
                    }
                    if (addcount < 0) {
                        sender.sendMessage(prefix + ChatColor.RED + "Error: number must be positive");
                        return true;
                    }
                    Lifesteal.giveHeart(target.getUniqueId(), addcount);
                    sender.sendMessage(prefix + "Success! Added " + addcount + " heart(s) to " + target.getName());
                case "removeheart":
                    int removecount;
                    try {
                        removecount = Integer.parseInt(args[0]);
                    } catch(NumberFormatException exception) {
                        sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: do /lifesteal addHeart " + ChatColor.ITALIC + "<Player> <Anzahl>");
                        return true;
                    }
                    if (removecount < 0) {
                        sender.sendMessage(prefix + ChatColor.RED + "Error: number must be positive");
                        return true;
                    }
                    Lifesteal.removeHeart(target.getUniqueId(), removecount);
                    sender.sendMessage(prefix + "Success! Removed " + removecount + " heart(s) from " + target.getName());
                    break;
                case "revive":
                    Lifesteal.revivePlayer(target.getUniqueId());
                    sender.sendMessage(prefix + "Success! Revived " + target.getName());

                    break;
                case "eliminate":
                    Lifesteal.eliminatePlayer(target.getUniqueId());
                    sender.sendMessage(prefix + "Success! Eliminated " + target.getName());
                    break;
                }
            }
        return false;
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
