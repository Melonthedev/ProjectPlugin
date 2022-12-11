package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

import java.util.*;
import java.util.stream.Collectors;

public class TimerSystem {

    //private static ChatColor color = ChatColor.GOLD;
    private static String prefix = "<gold>";
    private static Timer timer = new Timer();
    private static boolean running = true;
    private static final List<UUID> shownPlayers = new ArrayList<>();
    private static boolean autoShow;
    private static int runningSeconds = 0;

    private static TimerTask task;
    private static void initTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                for (UUID uuid : shownPlayers) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    int sec = runningSeconds % 60;
                    int min = (runningSeconds / 60) % 60;
                    int hours = (runningSeconds / 60) / 60;
                    String strSec = (sec < 10) ? "0" + sec : Integer.toString(sec);
                    String strmin = (min < 10) ? "0" + min : Integer.toString(min);
                    String strHours = (hours < 10) ? "0" + hours : Integer.toString(hours);
                    player.sendActionBar(Main.getMMComponent(prefix + strHours + ":" + strmin + ":" + strSec));
                }
                if (!running) return;
                runningSeconds++;
            }
        };
    }

    public static void startTimer() {
        running = true;
        timer = new Timer();
        initTask();
        timer.schedule(task, 0, 1000);
    }

    public static void stopTimer() {
        try {
            timer.cancel();
        } catch (Exception ignored) {}
        runningSeconds = 0;
    }

    public static void pauseTimer() {
        running = false;
    }

    public static void resumeTimer() {
        running = true;
    }

    public static void showToAll() {
        shownPlayers.addAll(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()));
    }

    public static void showToPlayer(UUID uuid) {
        shownPlayers.add(uuid);
    }

    public static void setAutoShow(boolean flag) {
        autoShow = flag;
    }
    //public static void setColor(ChatColor color) {
    //    TimerSystem.color = color;
    //}

    public static Timer getTimer() {
        return timer;
    }

    //public static ChatColor getColor() {
    //    return color;
    //}

    public static boolean isAutoShow() {
        return autoShow;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String prefix) {
        TimerSystem.prefix = prefix;
    }
}
