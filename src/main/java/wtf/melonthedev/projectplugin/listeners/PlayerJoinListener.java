package wtf.melonthedev.projectplugin.listeners;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.melonthedev.projectplugin.Main;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setPlayerListHeaderFooter(
                ChatColor.GOLD.toString() + ChatColor.BOLD + "Survivalprojekt 3.0\n" + ChatColor.RESET + ChatColor.GRAY + "McSurvivalprojekt.de",
                ChatColor.GREEN + "Online: " + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + " | " + ChatColor.GREEN + "TPS: " + ((int) MinecraftServer.getServer().recentTps[0] + 1)
        );
        Main.getPlugin().handleTabScoreboard();
    }
}
