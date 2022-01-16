package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MessageCommand implements TabExecutor {

    public static HashMap<CommandSender, CommandSender> conversations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please use '/msg <player> <message>'!");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Please use an online player!");
            return true;
        }
        StringBuilder message = new StringBuilder();
        String[] words = Arrays.copyOfRange(args, 1, args.length);
        for (String word : words) message.append(word).append(" ");
        target.sendMessage(ChatColor.GRAY + sender.getName() + " whispers to you: " + message);
        sender.sendMessage(ChatColor.GRAY + "You are whispering to " + target.getName() + ": " + message);
        conversations.put(sender, target);
        conversations.put(target, sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
        }
        return tab;
    }
}
