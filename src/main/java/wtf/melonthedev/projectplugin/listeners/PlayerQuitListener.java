package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.RED + "<<" + ChatColor.AQUA + " [Survivalprojekt] " + ChatColor.stripColor(event.getQuitMessage()) + " :(");
    }
}
