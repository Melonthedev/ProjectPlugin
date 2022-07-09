package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;
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
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /status <String: status>");
            return true;
        }
        Player player = (Player) sender;
        if (AfkSystem.isAfk(player)) {
            AfkSystem.disableAfkMode(player);
        }
        if (args[0].equalsIgnoreCase("reset")) {
            player.sendMessage(ChatColor.GREEN + "[Status] Dein Status wurde zurück gesetzt!");
            player.displayName(Component.text(player.getName()));
            player.playerListName(Component.text(player.getName()));
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : args)
            sb.append(s).append(" ");
        String status = sb.substring(0, sb.length() - 1);
        Component statusColor = Main.getPlugin().getMiniMessageComponent(status);
        int lengh = PlainTextComponentSerializer.plainText().serialize(statusColor).length();
        if (lengh > 30) {
            sender.sendMessage(ChatColor.RED + "Dein Status darf nicht länger als 30 Zeichen sein.");
            return true;
        }
        if (lengh < 1) {
            sender.sendMessage(ChatColor.RED + "Dein Status muss mindestens 1 Zeichen lang sein.");
            return true;
        }
        //String statusWithColor = Main.getPlugin().translateHexAndCharColorCodes(status);
        //if (statusWithColor.replaceAll("\\§[^;]", "").length() > 30) {
        //    sender.sendMessage(ChatColor.RED + "Dein Status darf nicht länger als 30 Zeichen sein.");
        //    return true;
        //}
        setStatus(player, statusColor);
        player.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(ChatColor.GREEN + "[Status] Dein Status ist nun '"), statusColor, Component.text(ChatColor.GREEN + "'.")));
        statusList.put(player.getName(), status);
        return true;
    }

    //public static void setStatus(Player player, String status) {
    //    player.setDisplayName("[" + status + ChatColor.RESET + "] " + player.getName());
    //    player.setPlayerListName("[" + status + ChatColor.RESET + "] " + player.getName());
    //}

    public static void setStatus(Player player, Component status) {
        Component statusFinal = Component.join(JoinConfiguration.noSeparators(), Component.text("["), status, Component.text(ChatColor.RESET + "] " + player.getName()));
        player.displayName(statusFinal);
        player.playerListName(statusFinal);
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
