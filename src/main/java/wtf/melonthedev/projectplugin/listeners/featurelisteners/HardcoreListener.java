package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HardcoreListener implements Listener {


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        validatePlayerMovement(event.getPlayer(), event.getTo());
    }


    HashMap<UUID, Integer> inBlockIndex = new HashMap<>();
    public void validatePlayerMovement(Player player, Location to) {
        if (!(player.getGameMode() == GameMode.SPECTATOR && Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false))) return;

        List<Entity> el = player.getNearbyEntities(100, 100, 100);
        if (!el.stream().anyMatch(e -> e instanceof Player) && !LocationUtils.isLocationInSpawnArea(to)) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }

        if (to.getBlock().getType() != Material.AIR && to.getBlock().getType() != Material.VOID_AIR && to.getBlock().getType() != Material.CAVE_AIR && to.getBlock().getType() != Material.WATER)  {
            boolean isFreeBlockInArea = false;
            int raduis = 1;
            Block middle = to.getBlock();
            for (int x = raduis; x >= -raduis; x--) {
                for (int y = raduis; y >= -raduis; y--) {
                    for (int z = raduis; z >= -raduis; z--) {
                        if (middle.getRelative(x, y, z).getType() != Material.AIR
                                && middle.getRelative(x, y, z).getType() != Material.VOID_AIR
                                && middle.getRelative(x, y, z).getType() != Material.CAVE_AIR
                                && middle.getRelative(x, y, z).getType() != Material.WATER) continue;
                        isFreeBlockInArea = true;
                        break;
                    }
                }
            }

            if (inBlockIndex.containsKey(player.getUniqueId()) && inBlockIndex.get(player.getUniqueId()) > 3 && !isFreeBlockInArea) {
                player.teleport(to.add(0, 50, 0));
                inBlockIndex.put(player.getUniqueId(), 0);
                return;
            }
            //event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().setY(-2).multiply(-0.3));
            player.leaveVehicle();
            player.setVelocity(player.getVelocity().setY(1.5).multiply(new Vector(-0.3, 1,-0.3)));
            inBlockIndex.put(player.getUniqueId(), inBlockIndex.getOrDefault(player.getUniqueId(), 0) + 1);
        } else inBlockIndex.remove(player.getUniqueId());
    }

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
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)
                && Main.getPlugin().getConfig().getBoolean("hardcore.blockSpectatorTeleport", false)
                && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Sorry, but you can't do that!");
        }
    }

}
