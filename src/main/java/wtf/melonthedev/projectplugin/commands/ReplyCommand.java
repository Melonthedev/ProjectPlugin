package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class ReplyCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "Please use '/r <message>'!");
            return true;
        }
        if (!MessageCommand.conversations.containsKey(sender)) {
            sender.sendMessage(ChatColor.RED + "There is no conversation to reply!");
            return true;
        }
        CommandSender target = MessageCommand.conversations.get(sender);
        if (target == null) return true;
        StringBuilder message = new StringBuilder();
        for (String arg : args) message.append(arg).append(" ");
        target.sendMessage(ChatColor.GRAY + sender.getName() + " whispers to you: " + message);
        sender.sendMessage(ChatColor.GRAY + "You whisper to " + target.getName() + ": " + message);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(); //Empty list for no tabcomplete
    }
}
