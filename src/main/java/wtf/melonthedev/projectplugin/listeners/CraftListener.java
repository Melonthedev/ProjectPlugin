package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.projectplugin.modules.Lifesteal;

public class CraftListener implements Listener {

    @EventHandler
    public void onSmith(PrepareSmithingEvent event) {
        if (Lifesteal.isLifestealActive() && Lifesteal.isNetheriteBlocked() && event.getInventory().getInputMineral() != null && event.getInventory().getInputMineral().getType() == Material.NETHERITE_INGOT)
            event.setResult(new ItemStack(Material.AIR));
    }

}
