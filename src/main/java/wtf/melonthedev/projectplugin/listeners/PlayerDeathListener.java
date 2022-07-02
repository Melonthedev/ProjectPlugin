package wtf.melonthedev.projectplugin.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ActionLoggerListener.logAction(event.getEntity(), "died", event.getEntity().getLocation(), "\"" + event.getDeathMessage() + "\"", "");
        if (event.getEntity().getBedSpawnLocation() != null
                && event.getEntity().getBedSpawnLocation().distance(event.getEntity().getLocation()) > 4000
                && (event.getEntity().getInventory().contains(Material.NETHERITE_AXE)
                || event.getEntity().getInventory().contains(Material.NETHERITE_SWORD))) {
            event.setDeathMessage(event.getDeathMessage() + " und muss jetzt seeehhhr weit laufen :/");
            TextComponent component = new TextComponent(ChatColor.AQUA + "Nicht aufgeben! Aber wehe du klickst das hier an!");
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathlocation"));
            event.getEntity().spigot().sendMessage(component);
        }
        if (event.getDeathMessage() == null) return;
        if (event.getDeathMessage().contains("tried to swim in lava"))
            event.setDeathMessage(event.getDeathMessage().replace("tried to swim in lava", "ging in falschen Gewässern schwimmen (Lava)"));
        if (event.getDeathMessage().contains("went up in flames"))
            event.setDeathMessage(event.getDeathMessage().replace("went up in flames", "wurde gegrillt (Feuer)"));
        if (event.getDeathMessage().contains("burned to death"))
            event.setDeathMessage(event.getDeathMessage().replace("burned to death", "hat zu lange auf den Herd gefasst (Fire Tick)"));
        if (event.getDeathMessage().contains("fell from a high place"))
            event.setDeathMessage(event.getDeathMessage().replace("fell from a high place", "hat versucht zu fliegen (Falldamage)"));
        if (event.getDeathMessage().contains("drowned"))
            event.setDeathMessage(event.getDeathMessage().replace("drowned", "konnte die Luft nicht anhalten (drown)"));
        if (event.getEntity().getLocation().getBlock().getType() == Material.STONECUTTER)
            event.setDeathMessage(event.getEntity().getName() + " ist in die Säge gelaufen (Stonecutter)");
    }


}
