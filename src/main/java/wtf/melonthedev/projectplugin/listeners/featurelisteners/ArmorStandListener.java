package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorStandListener implements Listener {

    @EventHandler
    public void onArmorStandInteractName(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
        if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAME_TAG)) return;
        ItemStack nametag = event.getPlayer().getInventory().getItemInMainHand();
        if (nametag.getItemMeta() == null) return;
        Component name = nametag.getItemMeta().displayName();
        ArmorStand stand = (ArmorStand) event.getRightClicked();
        if (name == null) return;
        if (stand.customName() != null && name.equals(stand.customName())) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        stand.setCustomNameVisible(true);
        stand.customName(name);
        nametag.setAmount(nametag.getAmount() - 1);
        event.getPlayer().getInventory().setItemInMainHand(nametag);
    }

    @EventHandler
    public void onArmorStandInteractArms(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
        if (!event.getPlayer().isSneaking()) return;
        ArmorStand stand = (ArmorStand) event.getRightClicked();
        stand.setArms(!stand.hasArms());
        event.setCancelled(true);
    }
}
