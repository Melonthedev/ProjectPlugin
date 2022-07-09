package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
        Component quitMsg = event.quitMessage();
        if (quitMsg == null) return;
        event.quitMessage(Component.text(ChatColor.RED + "<<" + ChatColor.AQUA + " [Survivalprojekt] " + ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(quitMsg)) + " :("));
    }
}
