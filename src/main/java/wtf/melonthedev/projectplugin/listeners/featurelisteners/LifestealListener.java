package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.Lifesteal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LifestealListener implements Listener {

    @EventHandler
    public void onHeartItemInteract(PlayerInteractEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "heart");
        if (!event.hasItem() || event.getItem() == null) return;
        if (event.getItem().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)
                && event.getItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.BYTE) == 1
                && Lifesteal.isLifestealActive() && event.getAction().isRightClick())
            Lifesteal.addHeartItemToPlayer(event.getPlayer(), event.getItem());
    }

    @EventHandler
    public void onEntityRescue(EntityResurrectEvent event) {
        if (Lifesteal.isLifestealActive() && Lifesteal.isTotemBlocked()) event.setCancelled(true);
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        if (Lifesteal.isLifestealActive() && Lifesteal.isElytraBlocked() && event.getSlotType() == PlayerArmorChangeEvent.SlotType.CHEST && event.getNewItem() != null && event.getNewItem().getType() == Material.ELYTRA) {
            event.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
            event.getPlayer().getInventory().addItem(event.getNewItem());
        }
    }

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
                //Lifesteal.removeHeart(event.getPlayer().getUniqueId(), 1);
                Lifesteal.unblockPlayer(uuid);
                stack.setAmount(stack.getAmount() - 1);
                finalSecondItem.setAmount(finalSecondItem.getAmount() - 1);
                entry.getKey().getWorld().strikeLightningEffect(entry.getKey());
                return;
            }
        }, 10);
    }

    @EventHandler
    public void onItemChange(PlayerItemHeldEvent event) {
        if (Lifesteal.isLifestealActive()
                && Lifesteal.isTotemBlocked()
                && event.getPlayer().getInventory().getItem(event.getNewSlot()) != null
                && event.getPlayer().getInventory().getItem(event.getNewSlot()).getType() == Material.TOTEM_OF_UNDYING) {
            event.getPlayer().sendActionBar(Component.text(ChatColor.RED + ChatColor.BOLD.toString() + "TOTEM IST DEAKTIVIERT!!!"));
        }
    }

    @EventHandler
    public void onOffhand(PlayerSwapHandItemsEvent event) {
        if (Lifesteal.isLifestealActive()
                && Lifesteal.isTotemBlocked()
                && event.getOffHandItem() != null
                && event.getOffHandItem().getType() == Material.TOTEM_OF_UNDYING) {
            event.getPlayer().sendActionBar(Component.text(ChatColor.RED + ChatColor.BOLD.toString() + "TOTEM IST DEAKTIVIERT!!!"));
        }
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        Lifesteal.handleLogin(event);
    }

}
