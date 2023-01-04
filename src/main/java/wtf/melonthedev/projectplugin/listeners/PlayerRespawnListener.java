package wtf.melonthedev.projectplugin.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import wtf.melonthedev.projectplugin.Main;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) {
            event.getPlayer().showTitle(Title.title(Component.text(ChatColor.RED + "You died!"), Component.text(ChatColor.GRAY + "You won't respawn!")));
            event.getPlayer().sendMessage(Main.getMMComponent("<yellow>If you want to spectate your team or friends,"));
            event.getPlayer().sendMessage(Main.getMMComponent("<yellow>run <aqua>/allowheadpickup PlayerName</aqua><yellow> and"));
            event.getPlayer().sendMessage(Main.getMMComponent("<yellow>ask them to pick your head up at the <light_purple>graveyard</light_purple><yellow> at spawn!"));
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().setSilent(true);
            event.getPlayer().setAffectsSpawning(false);
            event.getPlayer().setSleepingIgnored(true);
            event.getPlayer().playerListName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
            event.getPlayer().displayName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE) && Main.getPlugin().getConfig().getBoolean("hardcore.blockSpectatorTeleport", false)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Sorry, but you can't do that!");
        }
    }

}
