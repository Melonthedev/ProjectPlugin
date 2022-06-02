package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import wtf.melonthedev.projectplugin.Main;

public class LogOutputCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                sender.sendMessage(ChatColor.GREEN + "Logging is now enabled!");
                Main.getPlugin().getConfig().set("logging", true);
                Main.getPlugin().saveConfig();
            } else if (args[0].equalsIgnoreCase("off")) {
                sender.sendMessage(ChatColor.GREEN + "Logging is now disabled!");
                Main.getPlugin().getConfig().set("logging", false);
                Main.getPlugin().saveConfig();
            } else {
                sender.sendMessage(ChatColor.RED + "Syntaxerror: /logoutput <on/off>");
            }
        } else if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "Log Output is currently " + (Main.getPlugin().getConfig().getBoolean("logging") ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        } else {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /logoutput <on/off>");
        }
        return false;
    }
}
