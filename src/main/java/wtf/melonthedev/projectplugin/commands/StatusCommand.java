package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.configs.StatusConfiguration;
import wtf.melonthedev.projectplugin.modules.AfkSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatusCommand implements TabExecutor {

    //public static final HashMap<String, String> statusList = new HashMap<>();
    public static final FileConfiguration statusConfig = StatusConfiguration.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, you cannot use this command in the console.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /status <String: status>");
            return true;
        }
        if (player.getGameMode() == GameMode.SPECTATOR) {
            sender.sendMessage(ChatColor.RED + "You cannot use this command in spectator mode.");
            return true;
        }
        if (AfkSystem.isAfk(player)) {
            AfkSystem.disableAfkMode(player);
        }

        if (args[0].equalsIgnoreCase("reset")) {
            player.sendMessage(ChatColor.GREEN + "[Status] Dein Status wurde zurück gesetzt!");
            player.displayName(Component.text(player.getName()));
            player.playerListName(Component.text(player.getName()));
            //statusList.remove(player.getName());
            statusConfig.set("status." + player.getUniqueId(), null);
            StatusConfiguration.saveConfig();
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : args)
            sb.append(s).append(" ");
        String status = sb.substring(0, sb.length() - 1);
        Component statusColor = Main.getMMComponent(status);
        int lengh = PlainTextComponentSerializer.plainText().serialize(statusColor).length();
        if (lengh > 30) {
            sender.sendMessage(ChatColor.RED + "Dein Status darf nicht länger als 30 Zeichen sein.");
            return true;
        }
        if (lengh < 1) {
            sender.sendMessage(ChatColor.RED + "Dein Status muss mindestens 1 Zeichen lang sein.");
            return true;
        }

        setStatus(player, statusColor);
        player.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(ChatColor.GREEN + "[Status] Dein Status ist nun '"), statusColor, Component.text(ChatColor.GREEN + "'.")));
        //statusList.put(player.getName(), status);
        statusConfig.set("status." + player.getUniqueId(), status);
        StatusConfiguration.saveConfig();
        return true;
    }

    public static void setStatus(Player player, Component status) {
        Component statusFinal = Component.join(JoinConfiguration.noSeparators(), Component.text("["), status, Component.text(ChatColor.RESET + "] " + player.getName()));
        player.displayName(statusFinal);
        player.playerListName(statusFinal);
    }

    public static void handlePlayerJoin(Player player) {
        /*if (statusList.containsKey(player.getName()) && (!Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) || player.getGameMode() != GameMode.SPECTATOR))
            setStatus(player, Main.getMMComponent(statusList.get(player.getName())));*/
        if (statusConfig.contains("status." + player.getUniqueId()) && (!Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) || player.getGameMode() != GameMode.SPECTATOR))
            setStatus(player, Main.getMMComponent(statusConfig.getString("status." + player.getUniqueId())));
    }

    public static void handleWithPvpCooldownColor(Player player) {
        /*if (statusList.containsKey(player.getName()) && (!Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) || player.getGameMode() != GameMode.SPECTATOR))
            setStatus(player, Main.getMMComponent(statusList.get(player.getName())));
        else player.playerListName(Component.text(ChatColor.GREEN + player.getName()));*/
        if (statusConfig.contains("status." + player.getUniqueId()) && (!Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) || player.getGameMode() != GameMode.SPECTATOR))
            setStatus(player, Main.getMMComponent(statusConfig.getString("status." + player.getUniqueId())));
        else player.playerListName(Component.text(ChatColor.GREEN + player.getName()));
    }

    public static void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playerListName(Component.text(player.getName()));
            player.displayName(Component.text(player.getName()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return null;
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("reset");
            if (statusConfig.contains("status." + player.getUniqueId()))
                tab.add(statusConfig.getString("status." + player.getUniqueId()));
        }
        return tab;
    }
}
