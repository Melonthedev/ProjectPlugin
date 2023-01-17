package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import wtf.melonthedev.projectplugin.modules.Lifesteal;

public class PlayerItemHeldListener implements Listener {

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

}
