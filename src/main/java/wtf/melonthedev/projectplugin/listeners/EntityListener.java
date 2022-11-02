package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

import java.util.Objects;

public class EntityListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (!LocationUtils.isLocationInSpawnArea(event.getLocation())) return;
        event.blockList().clear();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
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
        ((ItemFrame) event.getEntity()).setVisible(false);
        event.getEntity().getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler
    public void onHangingDestroy(HangingBreakEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "invisible_item_frame");
        if (!event.getEntity().getPersistentDataContainer().has(key) || event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.BYTE) != 1)
            return;
        event.setCancelled(true);
        event.getEntity().remove();
        ItemStack item = new ItemStack(Material.ITEM_FRAME);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.WHITE + "Invisible Item Frame"));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        ItemFrame frame = (ItemFrame) event.getEntity();
        event.getEntity().getWorld().dropItem(event.getEntity().getLocation().add(0, -0.3, 0).add(frame.getFacing().getDirection().multiply(0.2)), item);
    }

}
