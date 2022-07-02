package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class EntityListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getLocation())) return;
        event.blockList().clear();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        event.setCancelled(true);
    }
}
