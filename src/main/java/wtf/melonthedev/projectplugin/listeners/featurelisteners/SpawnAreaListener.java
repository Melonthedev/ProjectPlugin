package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class SpawnAreaListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID || event.getCause() == EntityDamageEvent.DamageCause.CUSTOM || event.getCause() == EntityDamageEvent.DamageCause.SUICIDE || event.getCause() == EntityDamageEvent.DamageCause.CRAMMING)
            return;
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation()) && event.getEntity() instanceof Player)  {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block -> {
            if (LocationUtils.isLocationInSpawnArea(block.getLocation())) event.blockList().remove(block);
        });
        if (!LocationUtils.isLocationInSpawnArea(event.getLocation())) return;
        event.blockList().clear();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        if (event.getEntity().getType() == EntityType.DROPPED_ITEM) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(EntityDropItemEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(EntityMoveEvent event) {
        if (!(event.getEntity() instanceof Player) && LocationUtils.isLocationInSpawnArea(event.getTo()))
            event.getEntity().setVelocity(event.getEntity().getFacing().getDirection().multiply(-2));
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void onPortalFrameInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.END_PORTAL_FRAME) return;
        if (Main.getPlugin().isEndAccessible()) return;
        event.setCancelled(true);
        event.getPlayer().sendActionBar(Component.text(ChatColor.AQUA + "Das End ist blockiert. Es wird bald geöffnet."));
    }
}
