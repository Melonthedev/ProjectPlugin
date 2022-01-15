package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

}
