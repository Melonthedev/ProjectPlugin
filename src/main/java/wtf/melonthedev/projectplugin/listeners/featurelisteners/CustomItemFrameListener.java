package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.CustomItemSystem;

import java.util.Objects;

public class CustomItemFrameListener implements Listener {

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "invisible_item_frame");
        if (event.getItemStack() == null) return;
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

}
