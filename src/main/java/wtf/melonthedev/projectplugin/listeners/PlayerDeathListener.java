package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import wtf.melonthedev.projectplugin.Main;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ActionLoggerListener.logAction(event.getEntity(), "died", event.getEntity().getLocation(), "\"" + event.getDeathMessage() + "\"", "");
        if (event.getEntity().getBedSpawnLocation() != null
                && event.getEntity().getBedSpawnLocation().getWorld() == event.getEntity().getLocation().getWorld()
                && event.getEntity().getBedSpawnLocation().distance(event.getEntity().getLocation()) > 4000
                && (event.getEntity().getInventory().contains(Material.NETHERITE_AXE)
                || event.getEntity().getInventory().contains(Material.NETHERITE_SWORD))) {
            Main.deathlocations.put(event.getEntity(), event.getEntity().getLocation());
            event.deathMessage(Component.join(JoinConfiguration.noSeparators(), event.deathMessage(), Component.text(" und muss jetzt seeehhhr weit laufen :/")));
            Component component = Component.text(ChatColor.AQUA + "Nicht aufgeben! Aber wehe du klickst das hier an!");
            component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/deathlocation"));
            event.getEntity().sendMessage(component);
        }
        Component deathMsg = event.deathMessage();
        if (deathMsg == null) return;
        if (PlainTextComponentSerializer.plainText().serialize(deathMsg).contains("tried to swim in lava"))
            event.deathMessage(Component.text(PlainTextComponentSerializer.plainText().serialize(deathMsg).replace("tried to swim in lava", "ging in falschen Gewässern schwimmen (Lava)")));
        if (PlainTextComponentSerializer.plainText().serialize(deathMsg).contains("went up in flames"))
            event.deathMessage(Component.text(PlainTextComponentSerializer.plainText().serialize(deathMsg).replace("went up in flames", "wurde gegrillt (Feuer)")));
        if (PlainTextComponentSerializer.plainText().serialize(deathMsg).contains("burned to death"))
            event.deathMessage(Component.text(PlainTextComponentSerializer.plainText().serialize(deathMsg).replace("burned to death", "hat zu lange auf den Herd gefasst (Fire Tick)")));
        if (PlainTextComponentSerializer.plainText().serialize(deathMsg).contains("fell from a high place"))
            event.deathMessage(Component.text(PlainTextComponentSerializer.plainText().serialize(deathMsg).replace("fell from a high place", "hat versucht zu fliegen (Falldamage)")));
        if (PlainTextComponentSerializer.plainText().serialize(deathMsg).contains("drowned"))
            event.deathMessage(Component.text(PlainTextComponentSerializer.plainText().serialize(deathMsg).replace("drowned", "konnte die Luft nicht anhalten (drown)")));
        if (event.getEntity().getLocation().getBlock().getType() == Material.STONECUTTER)
            event.deathMessage(Component.text(event.getEntity().getName() + " ist in die Säge gelaufen (Stonecutter)"));
    }
}
