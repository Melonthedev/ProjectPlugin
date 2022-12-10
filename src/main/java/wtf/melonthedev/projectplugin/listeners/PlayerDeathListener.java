package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.ItemStacks;
import wtf.melonthedev.projectplugin.utils.Lifesteal;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

import java.util.Arrays;
import java.util.Objects;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ActionLogger.logAction(event.getEntity().getName(), "died", event.getEntity().getLocation(), "\"" + PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(event.deathMessage())) + "\"", "", true);

        if (Lifesteal.isLifestealActive()) {
            Lifesteal.removeHeart(event.getPlayer().getUniqueId(), 1);
            if (event.getPlayer().getKiller() != null) Lifesteal.giveHeart(event.getPlayer().getKiller().getUniqueId(), 1);
        }

        if (Main.getPlugin().getConfig().getBoolean("hardcore.giantDeathTitle", false))
            Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getPlugin().getMMComponent("<red>" + event.getPlayer().getName() + " died.</red>"), event.deathMessage() == null ? Component.text("") : Component.text(ChatColor.BLUE + PlainTextComponentSerializer.plainText().serialize(event.deathMessage())))));

        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You died!");
            event.getPlayer().sendMessage(ChatColor.RED + "Your Death Location: X: " + ChatColor.GRAY + event.getPlayer().getLocation().getBlockX() + " Y: " + event.getPlayer().getLocation().getBlockY() + " Z: " + event.getPlayer().getLocation().getBlockZ());
            if (PvpCooldownSystem.pvpCooldowns.containsKey(event.getEntity().getUniqueId()))
                PvpCooldownSystem.pvpCooldowns.get(event.getEntity().getUniqueId()).disable();
            if (event.getKeepInventory()) return;

            ItemStack[] items1 = new ItemStack[27];
            ItemStack[] items2 = new ItemStack[27];
            ItemStack head = ItemStacks.createSkull(event.getEntity().getName(), event.getEntity().getName() + "'s Head", "Was noch übrig von " + event.getEntity().getName() + " ist.", 1);

            if (event.getDrops().isEmpty()) {
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), head);
                return;
            }
            event.getEntity().getLocation().getBlock().setType(Material.CHEST);
            org.bukkit.block.Chest chestb1 = (org.bukkit.block.Chest) event.getEntity().getLocation().getBlock().getState();
            Chest chest1 = (Chest) event.getEntity().getLocation().getBlock().getBlockData();
            chest1.setType(Chest.Type.SINGLE);
            chestb1.setBlockData(chest1);
            chestb1.customName(Component.text(ChatColor.RED + "Inventory of " + event.getEntity().getName()));
            chestb1.update();

            event.getDrops().add(head);
            for (int i = 0; i < event.getDrops().size(); i++) {
                if (i < 27) items1[i] = event.getDrops().get(i);
                else items2[i - 27] = event.getDrops().get(i);
            }
            if (event.getDrops().size() > 27) {
                event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).setType(Material.CHEST);
                org.bukkit.block.Chest chestb2 = (org.bukkit.block.Chest) event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).getState();
                Chest chest2 = (Chest) event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).getBlockData();
                chest2.setType(Chest.Type.RIGHT);
                chest1.setType(Chest.Type.LEFT);
                chestb1.setBlockData(chest1);
                chestb2.setBlockData(chest2);
                chestb1.customName(Component.text(ChatColor.RED + "Inventory of " + event.getEntity().getName()));
                chestb2.customName(Component.text(ChatColor.RED + "Inventory of " + event.getEntity().getName()));
                chestb1.update();
                chestb2.update();
                chestb2.getBlockInventory().setContents(items1);
                chestb1.getBlockInventory().setContents(items2);
            } else {
                chestb1.getBlockInventory().setStorageContents(items1);
                System.out.println(Arrays.toString(items1));
                System.out.println(Arrays.toString(items2));
            }
            event.getDrops().clear();
            return;
        }

        if (event.getEntity().getBedSpawnLocation() != null && !Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)
                && event.getEntity().getBedSpawnLocation().getWorld() == event.getEntity().getLocation().getWorld()
                && event.getEntity().getBedSpawnLocation().distance(event.getEntity().getLocation()) > 4000
                && (event.getEntity().getInventory().contains(Material.NETHERITE_AXE)
                || event.getEntity().getInventory().contains(Material.NETHERITE_SWORD))) {
            Main.deathlocations.put(event.getEntity().getUniqueId(), event.getEntity().getLocation());
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
