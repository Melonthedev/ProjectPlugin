package wtf.melonthedev.projectplugin.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.PvpCooldown;

import java.util.HashMap;
import java.util.UUID;

public class PvpCooldownSystem {
    public static HashMap<UUID, PvpCooldown> pvpCooldowns = new HashMap<>();

    public static void handleForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(PvpCooldownSystem::handleForPlayer);
    }

    public static void handleForPlayer(Player player) {
        if (Main.getPlugin().getConfig().getInt("pvpCooldown." + player.getUniqueId(), 180) == 0) return;
        if (!pvpCooldowns.containsKey(player.getUniqueId()))
            pvpCooldowns.put(player.getUniqueId(), new PvpCooldown(player.getUniqueId(), 180));
        int ticksPlayedSinceStart = player.getStatistic(Statistic.TOTAL_WORLD_TIME);
        int minutesPlayedSinceStart = (ticksPlayedSinceStart / 20) / 60;

        if (!Main.getPlugin().getConfig().getBoolean("projectActive", false) || minutesPlayedSinceStart >= pvpCooldowns.get(player.getUniqueId()).getTotalMinutes()) return;
        pvpCooldowns.get(player.getUniqueId()).start();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "PvP Cooldown ist aktiv! Überspringe mit /skipPvpCooldown"));
    }

    public static void stopForPlayer(Player player) {
        if (pvpCooldowns.containsKey(player.getUniqueId()))
            pvpCooldowns.get(player.getUniqueId()).stop();
    }

    public static void resetAllPvpCooldowns() {
        pvpCooldowns.forEach((uuid, pvpCooldown) -> pvpCooldown.stop());
        pvpCooldowns.clear();
    }
}
