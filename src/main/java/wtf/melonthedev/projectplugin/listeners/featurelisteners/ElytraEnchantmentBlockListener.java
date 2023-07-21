package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.projectplugin.Main;

public class ElytraEnchantmentBlockListener implements Listener {


    @EventHandler
    public void onCombine(PrepareAnvilEvent event) {
        if (!Main.isFeatureDisabled("blockElytraEnchantments")
                && event.getInventory().getFirstItem() != null
                && event.getInventory().getFirstItem().getType() == Material.ELYTRA
                && event.getInventory().getSecondItem() != null
                && event.getInventory().getSecondItem().getType() == Material.ENCHANTED_BOOK) {
            event.getInventory().setRepairCost(100);
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

}
