package wtf.melonthedev.projectplugin.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.C;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.StatusCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class AfkSystem {

    public static List<Player> afkPlayers = new ArrayList<>();
    public static HashMap<Player, BukkitTask> afkTimeoutTasks = new HashMap<>();

    //Utility class
    private AfkSystem() {}

    public static void handleAfkModus(Player player) {
        if (isAfk(player)) {
            disableAfkMode(player);
        } else {
            startAfkModusTimeout(player);
        }
    }

    public static void startAfkModusTimeout(Player player) {
        afkPlayers.remove(player);
        BukkitTask oldtask = afkTimeoutTasks.get(player);
        if (oldtask != null) oldtask.cancel();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            if (afkTimeoutTasks.containsKey(player)) {
                enableAfkModus(player);
            }
        }, 20 * 60 * 10);
        afkTimeoutTasks.put(player, task);
    }

    public static void enableAfkModus(Player player) {
        afkPlayers.add(player);
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + ChatColor.RED + "Du bist nun im AFK Modus! Du kannst trotzdem sterben.");
        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " ist nun im AFK Modus!");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAfk(player)) cancel();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "AFK MODUS AKTIV!"));
            }
        }.runTaskTimer(Main.getPlugin(), 0, 40);
        player.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + player.getName());
        player.setPlayerListName(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + player.getName());
    }

    public static void disableAfkMode(Player player) {
        afkPlayers.remove(player);
        afkTimeoutTasks.remove(player);
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + ChatColor.RED + "Du bist nun nicht mehr im AFK Modus!");
        if (StatusCommand.statusList.containsKey(player.getName())) {
            player.setDisplayName(Main.getPlugin().translateHexAndCharColorCodes("[" + StatusCommand.statusList.get(player.getName()) + ChatColor.RESET + "] " + player.getName()));
            player.setPlayerListName(Main.getPlugin().translateHexAndCharColorCodes("[" + StatusCommand.statusList.get(player.getName()) + ChatColor.RESET + "] " + player.getName()));
        } else {
            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getName());
        }
    }

    public static boolean isAfk(Player player) {
        return afkPlayers.contains(player);
    }

}
