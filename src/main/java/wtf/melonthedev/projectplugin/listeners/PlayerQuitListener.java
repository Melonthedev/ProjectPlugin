package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wtf.melonthedev.projectplugin.utils.AfkSystem;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (AfkSystem.afkPlayers.contains(event.getPlayer()) || AfkSystem.afkTimeoutTasks.containsKey(event.getPlayer())) {
            AfkSystem.afkTimeoutTasks.remove(event.getPlayer());
            AfkSystem.afkPlayers.remove(event.getPlayer());
        }
        AfkSystem.handlePlayersSleepingPercentage();
        event.setQuitMessage(ChatColor.RED + "<<" + ChatColor.AQUA + " [Survivalprojekt] " + ChatColor.stripColor(event.getQuitMessage()) + " :(");
    }
}
