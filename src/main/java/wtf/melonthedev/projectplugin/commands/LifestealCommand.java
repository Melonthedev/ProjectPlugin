package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.Lifesteal;

import java.util.List;

public class LifestealCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final String prefix = ChatColor.GREEN + ChatColor.BOLD.toString() + "[Life" + ChatColor.DARK_RED + ChatColor.BOLD + "Steal] " + ChatColor.RESET;
        if (!sender.isOp()){
            sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to use this command.");
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /lifesteal <setheartcount | addheart | removeheart | revive | eliminate>");
            return true;
        }
        if (args.length == 1){

        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null){
                sender.sendMessage(prefix + ChatColor.RED + "Error: This player was not found!");
                return true;
            }
            switch (args[0]){
                case "setheartcount":
                    Integer setheartcount;
                    try {
                        setheartcount = Integer.valueOf(args[0]);
                    }catch(NumberFormatException exception){
                        sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: do /lifesteal setheartcount " + ChatColor.ITALIC + "Anzahl");
                        return true;
                    }
                    Lifesteal.setHeartCount(target, setheartcount);

                case "addheart":
                    Integer addcount;
                    try {
                        addcount = Integer.valueOf(args[0]);
                    }catch(NumberFormatException exception){
                        sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: do /lifesteal addheart " + ChatColor.ITALIC + "Anzahl");
                        return true;
                    }
                    if (addcount < 0){
                        sender.sendMessage(prefix + ChatColor.RED + "Error: number must be positive");
                        return true;
                    }else{
                        Lifesteal.giveHeart(target, addcount);
                    }

                case "removeheart":
                    Integer removecount;
                    try {
                        removecount = Integer.valueOf(args[0]);
                    }catch(NumberFormatException exception){
                        sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: do /lifesteal removeheart " + ChatColor.ITALIC + "Anzahl");
                        return true;
                    }
                    if (removecount < 0){
                        sender.sendMessage(prefix + ChatColor.RED + "Error: number must be positive");
                        return true;
                    }else{
                        Lifesteal.removeHeart(target,removecount);
                    }

                case "revive":
                    Lifesteal.revivePlayer(target);

                case "eliminate":
                    Lifesteal.eliminatePlayer(target);

                }
            }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
