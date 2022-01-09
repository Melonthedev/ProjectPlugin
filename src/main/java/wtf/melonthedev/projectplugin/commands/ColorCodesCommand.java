package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ColorCodesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChatColor colorinfo = ChatColor.AQUA;
        ChatColor colorsecondinfo = ChatColor.DARK_AQUA;
        sender.sendMessage(colorinfo + "----------Color Codes----------");
        if (args.length == 1) {
            for (ChatColor color : ChatColor.values()) {
                if (!args[0].equalsIgnoreCase(color.name())) continue;
                sender.sendMessage(colorinfo + "The colorcode for " + args[0] + " is &" + color.toString().substring(1));
                sender.sendMessage(color + "The color will look like this when used :)");
                sender.sendMessage(colorinfo + "-------------------------------");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "There is no valid color named '" + args[0] + "'. Try to find your color in the list: /colorcodes");
        } else {
            sender.sendMessage(colorinfo + "-" + ChatColor.DARK_RED + "DARK_RED: &4");
            sender.sendMessage(colorinfo + "-" + ChatColor.RED + "RED (aka. coral): &c");
            sender.sendMessage(colorinfo + "-" + ChatColor.GOLD + "GOLD (aka. orange): &6");
            sender.sendMessage(colorinfo + "-" + ChatColor.YELLOW + "YELLOW: &e");
            sender.sendMessage(colorinfo + "-" + ChatColor.DARK_GREEN + "DARK GREEN: &2");
            sender.sendMessage(colorinfo + "-" + ChatColor.GREEN + "GREEN (aka. lime): &a");
            sender.sendMessage(colorinfo + "-" + ChatColor.AQUA + "AQUA: &b");
            sender.sendMessage(colorinfo + "-" + ChatColor.DARK_AQUA + "DARK AQUA: &3");
            sender.sendMessage(colorinfo + "-" + ChatColor.DARK_BLUE + "DARK BLUE: &1");
            sender.sendMessage(colorinfo + "-" + ChatColor.BLUE + "BLUE: &9");
            sender.sendMessage(colorinfo + "-" + ChatColor.LIGHT_PURPLE + "LIGHT PURPLE (aka. pink): &d");
            sender.sendMessage(colorinfo + "-" + ChatColor.DARK_PURPLE + "DARK PURPLE: &5");
            sender.sendMessage(colorinfo + "-" + ChatColor.WHITE + "WHITE: &f");
            sender.sendMessage(colorinfo + "-" + ChatColor.GRAY + "GRAY: &7");
            sender.sendMessage(colorinfo + "-" + ChatColor.DARK_GRAY + "DARK GRAY: &8");
            sender.sendMessage(colorinfo + "-" + ChatColor.BLACK + "BLACK: &0");
        }
        sender.sendMessage(colorinfo + "-------------------------------");
        return false;
    }
}
