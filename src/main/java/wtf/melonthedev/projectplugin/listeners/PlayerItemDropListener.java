package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.Lifesteal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerItemDropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack stack = event.getItemDrop().getItemStack();
        NamespacedKey heartKey = new NamespacedKey(Main.getPlugin(), "heart");
        if (stack.getType() != Material.HEART_OF_THE_SEA && !stack.getItemMeta().getPersistentDataContainer().has(heartKey)) return;
        List<Entity> nearbyItems = event.getItemDrop().getNearbyEntities(2, 2, 2);
        ItemStack secondItem = null;
        for (Entity entity : nearbyItems) {
            if (!(entity instanceof Item)) continue;
            ItemStack stack2 = ((Item) entity).getItemStack();
            if ((stack2.getType() == Material.HEART_OF_THE_SEA && stack.getItemMeta().getPersistentDataContainer().has(heartKey))
                    || (stack2.getItemMeta().getPersistentDataContainer().has(heartKey) && stack.getType() == Material.HEART_OF_THE_SEA)) secondItem = stack2;
        }
        if (secondItem == null) return;
        HashMap<Location, UUID> locations = Lifesteal.getGraves();
        ItemStack finalSecondItem = secondItem;
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            for (Map.Entry<Location, UUID> entry : locations.entrySet()) {
                //System.out.println(entry.getKey().distance(event.getItemDrop().getLocation()));
                if (entry.getKey().distance(event.getItemDrop().getLocation()) > 3) continue;
                UUID uuid = entry.getValue();
                if (uuid == null) continue;
                if (Lifesteal.getHeartCount(event.getPlayer().getUniqueId()) <= 1) continue;
                if (Main.getPlugin().getConfig().getBoolean("lifesteal.willReviveOnJoin." + uuid, false)) continue;
                event.getPlayer().sendMessage(Lifesteal.prefix + ChatColor.GOLD + "Your revive was successful. Ask the player to join!");
                Lifesteal.removeHeart(event.getPlayer().getUniqueId(), 1);
                Lifesteal.unblockPlayer(uuid);
                stack.setAmount(stack.getAmount() - 1);
                finalSecondItem.setAmount(finalSecondItem.getAmount() - 1);
                entry.getKey().getWorld().strikeLightningEffect(entry.getKey());
                return;
            }
        }, 10);
    }
}
