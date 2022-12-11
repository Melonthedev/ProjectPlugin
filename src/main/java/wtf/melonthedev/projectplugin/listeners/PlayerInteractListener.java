package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.Lifesteal;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onFireworkInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getItem() == null) return;
        if ((event.getPlayer().getEquipment().getItem(EquipmentSlot.CHEST) == null
                || event.getPlayer().getEquipment().getItem(EquipmentSlot.CHEST).getType() != Material.ELYTRA)
                && event.getItem().getType() == Material.FIREWORK_ROCKET)
            event.setCancelled(true);
    }

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
    public void onPortalFrameInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.END_PORTAL_FRAME) return;
        if (Main.getPlugin().isEndAccessible()) return;
        event.setCancelled(true);
        event.getPlayer().sendActionBar(Component.text(ChatColor.AQUA + "Das End ist blockiert. Es wird bald geöffnet."));
    }

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
