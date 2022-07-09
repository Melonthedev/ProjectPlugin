package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.StatusCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
            if (afkTimeoutTasks.containsKey(player) && Bukkit.getOnlinePlayers().contains(player)) {
                enableAfkModus(player);
            }
        }, 20 * 60 * 10);
        afkTimeoutTasks.put(player, task);
    }

    public static void enableAfkModus(Player player) {
        afkPlayers.add(player);
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + ChatColor.RED + "Du bist nun im AFK Modus! Du kannst trotzdem sterben.");
        Bukkit.getServer().broadcast(Component.text(ChatColor.RED + player.getName() + ChatColor.GRAY + " ist nun im AFK Modus!"));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isAfk(player)) cancel();
                player.sendActionBar(Component.text(ChatColor.RED + "AFK MODUS AKTIV!"));
            }
        }.runTaskTimer(Main.getPlugin(), 0, 40);
        player.displayName(Component.text(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + player.getName()));
        player.playerListName(Component.text(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + player.getName()));
        handlePlayersSleepingPercentage();
    }

    public static void disableAfkMode(Player player) {
        afkPlayers.remove(player);
        afkTimeoutTasks.remove(player);
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + ChatColor.RED + "Du bist nun nicht mehr im AFK Modus!");
        if (StatusCommand.statusList.containsKey(player.getName())) {
            StatusCommand.setStatus(player, Main.getPlugin().getMiniMessageComponent(StatusCommand.statusList.get(player.getName())));
        } else {
            player.displayName(Component.text(player.getName()));
            player.playerListName(Component.text(player.getName()));
        }
        handlePlayersSleepingPercentage();
    }

    public static boolean isAfk(Player player) {
        return afkPlayers.contains(player);
    }


    public static void handlePlayersSleepingPercentage() {
        int targetPlayers = Bukkit.getOnlinePlayers().size() / 2 - afkPlayers.size();
        if (targetPlayers % 2 == 0) targetPlayers++;
        int percentage = targetPlayers * 100 / Bukkit.getOnlinePlayers().size();
        System.out.println(targetPlayers + " müssen Schlafen. Das sind " + percentage + "%");
        if (Bukkit.getWorld("world") == null)
            return;
        Objects.requireNonNull(Bukkit.getWorld("world")).setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, percentage);
    }

}
