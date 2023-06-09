package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.HardcoreSystem;
import wtf.melonthedev.projectplugin.modules.Lifesteal;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        handleDeathMessage(event);
        handleBigDeathMessage(event);
        Lifesteal.handlePlayerDeath(event.getPlayer());
        HardcoreSystem.handleDeathAndDeathChest(event);
    }

    public void handleDeathMessage(PlayerDeathEvent event) {
        Component deathMessageComponent = event.deathMessage();
        String deathMessage = deathMessageComponent == null ? event.getPlayer().getName() + " died"
                : PlainTextComponentSerializer.plainText().serialize(deathMessageComponent);
        if (deathMessage.contains("tried to swim in lava"))
            event.deathMessage(Component.text(deathMessage.replace("tried to swim in lava", "ging in falschen Gewässern schwimmen (Lava)")));
        if (deathMessage.contains("went up in flames"))
            event.deathMessage(Component.text(deathMessage.replace("went up in flames", "wurde gegrillt (Feuer)")));
        if (deathMessage.contains("burned to death"))
            event.deathMessage(Component.text(deathMessage.replace("burned to death", "hat zu lange auf den Herd gefasst (Fire Tick)")));
        if (deathMessage.contains("fell from a high place"))
            event.deathMessage(Component.text(deathMessage.replace("fell from a high place", "hat versucht zu fliegen (Falldamage)")));
        if (deathMessage.contains("drowned"))
            event.deathMessage(Component.text(deathMessage.replace("drowned", "konnte die Luft nicht anhalten (drown)")));
        if (event.getEntity().getLocation().getBlock().getType() == Material.STONECUTTER)
            event.deathMessage(Component.text(event.getEntity().getName() + " ist in die Säge gelaufen (Stonecutter)"));
    }

    public void handleBigDeathMessage(PlayerDeathEvent event) {
        Component deathMessageComponent = event.deathMessage();
        String deathMessage = deathMessageComponent == null ? event.getPlayer().getName() + " died" : PlainTextComponentSerializer.plainText().serialize(deathMessageComponent);
        if (Main.getPlugin().getConfig().getBoolean("hardcore.giantDeathTitle", false))
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.showTitle(Title.title(Main.getMMComponent("<red>" + event.getPlayer().getName() + " died.</red>"), Component.text(ChatColor.BLUE + deathMessage))));
    }
}
