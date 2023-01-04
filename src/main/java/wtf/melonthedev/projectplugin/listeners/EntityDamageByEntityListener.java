package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.LocationUtils;
import wtf.melonthedev.projectplugin.modules.PvpCooldownSystem;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player && PvpCooldownSystem.pvpCooldowns.containsKey(event.getDamager().getUniqueId()) && Main.getPlugin().getConfig().getBoolean("projectActive", false)) {
            event.setCancelled(true);
            damager.sendActionBar(Component.text(ChatColor.RED + "PvP Cooldown ist aktiv! Überspringe mit /skipPvpCooldown"));
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player damager && PvpCooldownSystem.pvpCooldowns.containsKey(event.getEntity().getUniqueId())  && Main.getPlugin().getConfig().getBoolean("projectActive", false)) {
            event.setCancelled(true);
            damager.sendActionBar(Component.text(ChatColor.RED + "Dieser Spieler ist im PvP Cooldown!"));
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID || event.getCause() == EntityDamageEvent.DamageCause.CUSTOM || event.getCause() == EntityDamageEvent.DamageCause.SUICIDE || event.getCause() == EntityDamageEvent.DamageCause.CRAMMING)
            return;
        if (LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation()) && event.getEntity() instanceof Player)  {
            event.setCancelled(true);
        }
    }

}
