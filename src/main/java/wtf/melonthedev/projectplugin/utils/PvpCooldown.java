package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.melonthedev.projectplugin.Main;

import java.util.UUID;

public class PvpCooldown {
    
    private final UUID uuid;
    private final int totalMinutes;
    private int remainingMinutes;
    private final BossBar bar = BossBar.bossBar(Component.text(ChatColor.WHITE + "PvP Cooldown: 3h"), 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_12);
    public BukkitRunnable runnable;

    public PvpCooldown(UUID uuid, int totalMinutes) {
        this.uuid = uuid;
        this.totalMinutes = Math.max(Main.getPlugin().getConfig().getInt("pvpCooldown." + uuid, totalMinutes), totalMinutes);
        this.remainingMinutes = Main.getPlugin().getConfig().getInt("pvpCooldown." + uuid, totalMinutes);
        initRunnable();
    }

    public void start() {
        //if (Main.getPlugin().getConfig().getInt("pvpCooldown." + uuid, totalMinutes) == 0 || remainingMinutes == 0) return;
        Main.getPlugin().getConfig().set("pvpCooldown." + uuid, remainingMinutes);
        bar.progress(1);
        bar.color(BossBar.Color.GREEN);
        bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 3h"));
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.showBossBar(bar);
            if (remainingMinutes >= totalMinutes - 1)
                player.showTitle(Title.title(Component.text(ChatColor.GREEN + "The PvP Cooldown has started!"), Component.text(ChatColor.AQUA + "PvP will be enabled when the timer runs out!")));
            initRunnable();
            runnable.runTaskTimer(Main.getPlugin(), 0, 1200);
        }
    }

    public void pause() {
        try {
            runnable.cancel();
        } catch (Exception ignored) {}
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            player.hideBossBar(bar);
    }

    public void disable() {
        try {
            runnable.cancel();
        } catch (Exception ignored) {}
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            player.hideBossBar(bar);
        Main.getPlugin().getConfig().set("pvpCooldown." + uuid, null);
        Main.getPlugin().saveConfig();
        PvpCooldownSystem.pvpCooldowns.remove(uuid);
    }

    public void initRunnable() {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                float percentage = (float) remainingMinutes / totalMinutes;
                bar.progress(percentage);
                if (remainingMinutes == 180) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 3h"));
                } else if (remainingMinutes <= 180 && remainingMinutes > 120) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 2h " + (remainingMinutes - 120) + "min"));
                } else if (remainingMinutes <= 120 && remainingMinutes > 60) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 1h " + (remainingMinutes - 60) + "min"));
                } else if (remainingMinutes <= 60 && remainingMinutes > 0) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: " + (remainingMinutes) + "min"));
                }
                if (percentage >= 0.66) {
                    bar.color(BossBar.Color.GREEN);
                } else if (percentage >= 0.33) {
                    bar.color(BossBar.Color.YELLOW);
                } else {
                    bar.color(BossBar.Color.RED);
                }
                Main.getPlugin().getConfig().set("pvpCooldown." + uuid, remainingMinutes);
                Main.getPlugin().saveConfig();
                if (percentage <= 0)
                    disable();
                remainingMinutes--;
            }
        };
    }

    public boolean isRunning() {
        return remainingMinutes > 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    public BossBar getBar() {
        return bar;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }
}
