package wtf.melonthedev.projectplugin.modules;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
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

    private AfkSystem() {} // Utility Class, cannot be instantiated

    public static void handleAfkModus(Player player) {
        if (Main.isFeatureDisabled("afkSystem") || (player.getGameMode() == GameMode.SPECTATOR && Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)))
            return;
        if (isAfk(player)) disableAfkMode(player);
        else startAfkModusTimeout(player);
    }

    public static void startAfkModusTimeout(Player player) {
        afkPlayers.remove(player);
        BukkitTask oldtask = afkTimeoutTasks.get(player);
        if (oldtask != null) oldtask.cancel();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            if (afkTimeoutTasks.containsKey(player) && Bukkit.getOnlinePlayers().contains(player))
                enableAfkModus(player);
        }, 20L * 60 * Main.getPlugin().getConfig().getInt("config.afkSystem.timeoutInMinutes", 10));
        afkTimeoutTasks.put(player, task);
    }

    public static void enableAfkModus(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR && Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) return;
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
        player.setSleepingIgnored(true);
    }

    public static void disableAfkMode(Player player) {
        afkPlayers.remove(player);
        afkTimeoutTasks.remove(player);
        player.displayName(Component.text(player.getName()));
        player.playerListName(Component.text(player.getName()));
        player.setSleepingIgnored(false);
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[AFK] " + ChatColor.RESET + ChatColor.RED + "Du bist nun nicht mehr im AFK Modus!");
        StatusCommand.handlePlayerJoin(player);
    }

    /**
     * @deprecated in favor of Player.java setSleepingIgnored() method
     */
    @Deprecated
    public static void handlePlayersSleepingPercentage() {
        if (!Main.getPlugin().getConfig().getBoolean("config.afkSystem.handleSleepingPercentage", true)) return;
        int targetPlayers = (Bukkit.getOnlinePlayers().size() - afkPlayers.size()) / 2;
        if (targetPlayers % 2 == 0) targetPlayers++;
        int percentage = (targetPlayers * 100) / Bukkit.getOnlinePlayers().size();
        //System.out.println(targetPlayers + " müssen Schlafen. Das sind " + percentage + "%");
        World world = Bukkit.getWorld("world");
        if (world == null)
            return;
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, percentage);
    }

    public static boolean isAfk(Player player) {
        return afkPlayers.contains(player);
    }

}
