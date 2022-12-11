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
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.Lifesteal;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class SpawnElytraListener implements Listener {

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getEquipment().getItem(EquipmentSlot.CHEST) != null
                && player.getEquipment().getItem(EquipmentSlot.CHEST).getType() == Material.ELYTRA) {
            if (Lifesteal.isLifestealActive()) {
                event.setCancelled(true);
            }
            return;
        }
        if (player.getLocation().getY() > 20 && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && LocationUtils.isLocationNearSpawnArea(player.getLocation()) && Main.spawnElytraPlayers.getOrDefault(player, false)) {
            event.setCancelled(true);
            return;
        }
        Main.spawnElytraPlayers.remove(player);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.AIR || block.getRelative(BlockFace.DOWN).getType() != Material.AIR || event.getPlayer().isFlying() || !LocationUtils.isLocationInSpawnArea(event.getPlayer().getLocation())) return;
        event.getPlayer().setGliding(true);
        Main.spawnElytraPlayers.put(event.getPlayer(), true);
    }
}
