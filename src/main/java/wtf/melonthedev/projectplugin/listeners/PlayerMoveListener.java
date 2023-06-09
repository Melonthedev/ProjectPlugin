package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import wtf.melonthedev.projectplugin.modules.AfkSystem;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()
            || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        AfkSystem.handleAfkModus(event.getPlayer());
    }
}
