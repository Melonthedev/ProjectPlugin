package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.AfkSystem;
import wtf.melonthedev.projectplugin.modules.PvpCooldownSystem;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (AfkSystem.afkPlayers.contains(event.getPlayer()) || AfkSystem.afkTimeoutTasks.containsKey(event.getPlayer())) {
            AfkSystem.afkTimeoutTasks.remove(event.getPlayer());
            AfkSystem.afkPlayers.remove(event.getPlayer());
        }
        PvpCooldownSystem.pauseForPlayer(event.getPlayer());
        Component quitMsg = event.quitMessage();
        if (quitMsg == null) return;
        ChatColor color = (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", true) && event.getPlayer().getGameMode() == GameMode.SPECTATOR) ? ChatColor.GRAY : ChatColor.AQUA;
        event.quitMessage(Component.text(ChatColor.RED + "<<" + color + " [Survivalprojekt] " + ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(quitMsg)) + " :("));
    }
}
