package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class SpawnElytraListener implements Listener {

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {//LocationUtils.isLocationInSpawnArea(entity.getLocation()) &&
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (player.getEquipment() != null && player.getEquipment().getItem(EquipmentSlot.CHEST) != null && player.getEquipment().getItem(EquipmentSlot.CHEST).getType() == Material.ELYTRA) return;
        if (player.getLocation().getY() > 20 && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.AIR || block.getRelative(BlockFace.DOWN).getType() != Material.AIR || event.getPlayer().isFlying() || !LocationUtils.isLocationInSpawnArea(event.getPlayer().getLocation())) return;
        event.getPlayer().setGliding(true);
    }
}
