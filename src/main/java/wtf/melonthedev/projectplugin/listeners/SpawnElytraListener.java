package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class SpawnElytraListener implements Listener {

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        Entity entity = event.getEntity();
        if (LocationUtils.isLocationInSpawnArea(entity.getLocation()) && entity.getLocation().getY() > 20 && entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.AIR || block.getRelative(BlockFace.DOWN).getType() != Material.AIR || event.getPlayer().isFlying()) return;
        event.getPlayer().setGliding(true);
    }
}
