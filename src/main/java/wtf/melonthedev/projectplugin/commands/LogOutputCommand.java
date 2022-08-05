package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.List;

public class LogOutputCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("normal")) {
                sender.sendMessage(ChatColor.GREEN + "Logging is now set to normal!");
                Main.getPlugin().getConfig().set("logging", "normal");
                Main.getPlugin().saveConfig();
            } else if (args[0].equalsIgnoreCase("detailed")) {
                sender.sendMessage(ChatColor.GREEN + "Logging is now set to detailed!");
                Main.getPlugin().getConfig().set("logging", "detailed");
                Main.getPlugin().saveConfig();
            } else if (args[0].equalsIgnoreCase("off")) {
                sender.sendMessage(ChatColor.GREEN + "Logging is now disabled!");
                Main.getPlugin().getConfig().set("logging", "off");
                Main.getPlugin().saveConfig();
            } else {
                sender.sendMessage(ChatColor.RED + "Syntaxerror: /logoutput <normal/detailed/off>");
            }
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "Log Output is currently set to " + Main.getPlugin().getConfig().getString("logging"));
        } else {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /logoutput <normal/detailed/off>");
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("normal");
            tab.add("detailed");
            tab.add("off");
        }
        return tab;
    }
}
