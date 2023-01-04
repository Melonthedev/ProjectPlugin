package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import wtf.melonthedev.projectplugin.modules.Lifesteal;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

import java.util.HashMap;

public class SpawnElytraListener implements Listener {

    public HashMap<Player, Boolean> spawnElytraPlayers = new HashMap<>();

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getEquipment().getItem(EquipmentSlot.CHEST).getType() == Material.ELYTRA) {
            if (Lifesteal.isLifestealActive() && Lifesteal.isElytraBlocked()) event.setCancelled(true);
            return;
        }
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
                && LocationUtils.isLocationNearSpawnArea(player.getLocation())
                && spawnElytraPlayers.getOrDefault(player, false)
                && player.getLocation().getY() > 20) {
            event.setCancelled(true);
            return;
        }
        spawnElytraPlayers.remove(player);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.AIR
                || block.getRelative(BlockFace.DOWN).getType() != Material.AIR
                || event.getPlayer().isFlying()
                || !LocationUtils.isLocationInSpawnArea(event.getPlayer().getLocation())) return;
        event.getPlayer().setGliding(true);
        spawnElytraPlayers.put(event.getPlayer(), true);
    }
}
