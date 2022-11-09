package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.AfkSystem;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        validatePlayerMovement(event.getPlayer(), event.getTo());
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        AfkSystem.handleAfkModus(event.getPlayer());
    }

    HashMap<UUID, Integer> inBlockIndex = new HashMap<>();
    public void validatePlayerMovement(Player player, Location to) {
        if (to.getBlock().getType() != Material.AIR && to.getBlock().getType() != Material.VOID_AIR && to.getBlock().getType() != Material.CAVE_AIR && player.getGameMode() == GameMode.SPECTATOR && Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false))  {
            boolean isFreeBlockInArea = false;
            int raduis = 1;
            Block middle = to.getBlock();
            for (int x = raduis; x >= -raduis; x--) {
                for (int y = raduis; y >= -raduis; y--) {
                    for (int z = raduis; z >= -raduis; z--) {
                        if (middle.getRelative(x, y, z).getType() != Material.AIR
                                && middle.getRelative(x, y, z).getType() != Material.VOID_AIR
                                && middle.getRelative(x, y, z).getType() != Material.CAVE_AIR) continue;
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


}
