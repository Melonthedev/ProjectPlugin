package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) {
            event.setDamage(0.1);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID || event.getCause() == EntityDamageEvent.DamageCause.CUSTOM || event.getCause() == EntityDamageEvent.DamageCause.SUICIDE || event.getCause() == EntityDamageEvent.DamageCause.CRAMMING)
            return;
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

}
