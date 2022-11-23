package wtf.melonthedev.projectplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

import java.util.HashMap;
import java.util.UUID;

public class PvpCooldownSystem {
    public static HashMap<UUID, PvpCooldown> pvpCooldowns = new HashMap<>();

    public static void handleForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(PvpCooldownSystem::handleForPlayer);
    }

    public static void handleForPlayer(Player player) {
        if (Main.getPlugin().getConfig().getInt("pvpCooldown." + player.getUniqueId(), (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) || Lifesteal.isLifestealActive()) ? 180 : 0) == 0) return;
        if (!pvpCooldowns.containsKey(player.getUniqueId()))
            pvpCooldowns.put(player.getUniqueId(), new PvpCooldown(player.getUniqueId(), 180));
        //int ticksPlayedSinceStart = player.getStatistic(Statistic.TOTAL_WORLD_TIME);
        //int minutesPlayedSinceStart = (ticksPlayedSinceStart / 20) / 60;
        //if (!Main.getPlugin().getConfig().getBoolean("projectActive", false) || minutesPlayedSinceStart >= pvpCooldowns.get(player.getUniqueId()).getTotalMinutes()) return;
        if (pvpCooldowns.get(player.getUniqueId()).getRemainingMinutes() > 0)
            pvpCooldowns.get(player.getUniqueId()).start();
    }

    public static void startForPlayer(UUID uuid, int minutes) {
        pvpCooldowns.put(uuid, new PvpCooldown(uuid, minutes));
        pvpCooldowns.get(uuid).start();
    }

    public static void disableForPlayer(Player player) {
        if (pvpCooldowns.containsKey(player.getUniqueId()))
            pvpCooldowns.get(player.getUniqueId()).disable();
    }

    public static void disableForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(PvpCooldownSystem::disableForPlayer);
    }

    public static void pauseForPlayer(Player player) {
        if (pvpCooldowns.containsKey(player.getUniqueId()))
            pvpCooldowns.get(player.getUniqueId()).pause();
    }

    public static void resetAllPvpCooldowns() {
        pvpCooldowns.forEach((uuid, pvpCooldown) -> pvpCooldown.pause());
        pvpCooldowns.clear();
    }
}
