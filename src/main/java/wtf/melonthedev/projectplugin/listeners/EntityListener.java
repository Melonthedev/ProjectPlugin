package wtf.melonthedev.projectplugin.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.CustomItemSystem;
import wtf.melonthedev.projectplugin.modules.Lifesteal;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

import java.util.Objects;

public class EntityListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block -> {
            if (LocationUtils.isLocationInSpawnArea(block.getLocation())) event.blockList().remove(block);
        });
        if (!LocationUtils.isLocationInSpawnArea(event.getLocation())) return;
        event.blockList().clear();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        if (event.getEntity().getType() == EntityType.DROPPED_ITEM) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(EntityDropItemEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(EntityMoveEvent event) {
        if (!(event.getEntity() instanceof Player) && LocationUtils.isLocationInSpawnArea(event.getTo())) event.getEntity().setVelocity(event.getEntity().getFacing().getDirection().multiply(-2));
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getEntity().getLocation())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "invisible_item_frame");
        if (!Objects.requireNonNull(event.getItemStack()).hasItemMeta()
                || !event.getItemStack().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)
                || event.getItemStack().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.BYTE) != 1
                || !(event.getEntity() instanceof ItemFrame)) return;
        event.getEntity().setGlowing(true);
        event.getEntity().getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "invisible_item_frame");
        if (!event.getEntity().getPersistentDataContainer().has(key) || event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.BYTE) != 1)
            return;
        event.setCancelled(true);
        ItemStack item = CustomItemSystem.getInvisibleItemFrameItem();
        ItemFrame frame = (ItemFrame) event.getEntity();
        frame.getWorld().dropItem(frame.getLocation().add(0, -0.3, 0).add(frame.getFacing().getDirection().multiply(0.2)), item);
        frame.remove();
    }

    @EventHandler
    public void onItemFrameChange(PlayerItemFrameChangeEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "invisible_item_frame");
        if (!event.getItemFrame().getPersistentDataContainer().has(key) || event.getItemFrame().getPersistentDataContainer().get(key, PersistentDataType.BYTE) != 1)
            return;
        boolean flag = false;
        if (event.getAction() == PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE)
            return;
        if (event.getAction() == PlayerItemFrameChangeEvent.ItemFrameChangeAction.REMOVE)
            flag = true;
        event.getItemFrame().setGlowing(flag);
        event.getItemFrame().setVisible(flag);
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

}
