package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.utils.AfkSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatusCommand implements TabExecutor {

    public static final HashMap<String, String> statusList = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, you cannot use this command in the console.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /status <String: status>");
            return true;
        }
        Player player = (Player) sender;
        if (AfkSystem.isAfk(player)) {
            AfkSystem.disableAfkMode(player);
        }
        if (args[0].equalsIgnoreCase("reset")) {
            player.sendMessage(ChatColor.GREEN + "[Status] Dein Status wurde zurück gesetzt!");
            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getName());
            return true;
        }
        String status = args[0];
        String statusWithColor = ChatColor.translateAlternateColorCodes('&', status);
        if (status.length() > 30) {
            sender.sendMessage(ChatColor.RED + "Dein Status darf nicht länger als 30 Zeichen sein.");
            return true;
        }
        player.setDisplayName("[" + statusWithColor + ChatColor.RESET + "] " + player.getName());
        player.setPlayerListName("[" + statusWithColor + ChatColor.RESET + "] " + player.getName());
        player.sendMessage(ChatColor.GREEN + "[Status] Dein Status ist nun '" + statusWithColor + ChatColor.GREEN + "'.");
        statusList.put(player.getName(), status);
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("reset");
            if (statusList.containsKey(sender.getName())) {
                tab.add(statusList.get(sender.getName()));
            }
        }
        return tab;
    }
}
