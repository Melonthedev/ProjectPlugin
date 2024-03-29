package wtf.melonthedev.projectplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandUtils {

    public static void addOnlinePlayersTabComplete(List<String> tab, String arg) {
        tab.addAll(Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).toList());
    }

    public static void addOfflinePlayersTabComplete(List<String> tab, String arg) {
        tab.addAll(Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).toList());
    }

    public static void addOnlyOfflinePlayersTabComplete(List<String> tab, String arg) {
        tab.addAll(Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(offlinePlayer -> !offlinePlayer.isOnline())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).toList());
    }

    public static String getLocationString(Location location) {
        return ChatColor.GREEN + "X: " + location.getBlockX() + ChatColor.RED + " Y: " + location.getBlockY() + ChatColor.BLUE + " Z: " + location.getBlockZ();
    }
}
