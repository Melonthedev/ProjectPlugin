package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /lifesteal <setheart | addheart | removeheart | revive>");
            return true;
        }
        if (args.length == 1){

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
