package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.*;

public class MessageCommand implements TabExecutor {

    public static HashMap<CommandSender, CommandSender> conversations = new HashMap<>();
    public static HashMap<String, List<Map.Entry<String, String>>> offlinePlayerMessages = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please use '/msg <player> <message>'!");
            return true;
        }
        StringBuilder message = new StringBuilder();
        String[] words = Arrays.copyOfRange(args, 1, args.length);
        for (String word : words) message.append(word).append(" ");
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.AQUA + "Your message will be delivered as soon as " + args[0] + " joins the server!");
            if (!offlinePlayerMessages.containsKey(args[0]))
                offlinePlayerMessages.put(args[0], new ArrayList<>());
            offlinePlayerMessages.get(args[0]).add(new AbstractMap.SimpleEntry<>(sender.getName(), message.toString()));
            return true;
        }
        target.sendMessage(ChatColor.GRAY + sender.getName() + " whispers to you: " + message);
        sender.sendMessage(ChatColor.GRAY + "You whisper to " + target.getName() + ": " + message);
        conversations.put(sender, target);
        conversations.put(target, sender);
        return false;
    }

    public static void handleNewMessages(Player player) {
        if (offlinePlayerMessages.containsKey(player.getName())) {
            for (Map.Entry<String, String> entry : offlinePlayerMessages.get(player.getName()))
                player.sendMessage(ChatColor.GRAY + entry.getKey() + " whispers to you: " + entry.getValue());
            offlinePlayerMessages.remove(player.getName());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            CommandUtils.addOnlinePlayersTabComplete(tab, args[0]);
            CommandUtils.addOnlyOfflinePlayersTabComplete(tab, args[0]);
        }
        return tab;
    }
}
