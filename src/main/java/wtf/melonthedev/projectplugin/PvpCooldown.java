package wtf.melonthedev.projectplugin;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

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
        if (Main.getPlugin().getConfig().getInt("pvpCooldown." + uuid, totalMinutes) == 0 || remainingMinutes == 0) return;
        bar.progress(1);
        bar.color(BossBar.Color.GREEN);
        bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 3h"));
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.showBossBar(bar);
            if (remainingMinutes >= totalMinutes - 1)
                player.showTitle(Title.title(Component.text(ChatColor.GREEN + "The PvP Cooldown has started!"), Component.text(ChatColor.AQUA + "PvP will be enabled in 3 hours!")));
            initRunnable();
            runnable.runTaskTimer(Main.getPlugin(), 0, 1200);
        }
    }

    public void stop() {
        try {
            runnable.cancel();
        } catch (Exception ignored) {}
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.hideBossBar(bar);
            player.showTitle(Title.title(Component.text(ChatColor.RED + "PvP is now enabled!"), Component.empty()));
        }
    }

    public void disable() {
        runnable.cancel();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            player.hideBossBar(bar);
        Main.getPlugin().getConfig().set("pvpCooldown." + uuid, 0);
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
                    bar.color(BossBar.Color.GREEN);
                } else if (remainingMinutes <= 180 && remainingMinutes > 120) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 2h " + (remainingMinutes - 120) + "min"));
                    bar.color(BossBar.Color.GREEN);
                } else if (remainingMinutes <= 120 && remainingMinutes > 60) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: 1h " + (remainingMinutes - 60) + "min"));
                    bar.color(BossBar.Color.YELLOW);
                } else if (remainingMinutes <= 60 && remainingMinutes > 0) {
                    bar.name(Component.text(ChatColor.WHITE + "PvP Cooldown: " + (remainingMinutes) + "min"));
                    bar.color(BossBar.Color.RED);
                }
                remainingMinutes--;
                Main.getPlugin().getConfig().set("pvpCooldown." + uuid, remainingMinutes);
                Main.getPlugin().saveConfig();
                if (percentage <= 0) {
                    stop();
                    disable();
                }
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
