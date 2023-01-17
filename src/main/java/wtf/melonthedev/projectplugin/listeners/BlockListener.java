package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import wtf.melonthedev.projectplugin.modules.Lifesteal;
import wtf.melonthedev.projectplugin.modules.PlayerActivitySystem;

import java.util.HashMap;

public class BlockListener implements Listener {

    @EventHandler
    public void onCraft(PrepareSmithingEvent event) {
        //if (event.getInventory().getInputEquipment().getItemMeta().getDisplayName().equals(Lifesteal.getConstructionHeartItem().getItemMeta().getDisplayName()) //UNSAFE
        ////        && event.getInventory().getInputMineral().getType() == Material.NETHER_STAR
        //        && Lifesteal.isLifestealActive()) {
         //   event.setResult(Lifesteal.getHeartItem());
        //}
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        /*Material material = event.getBlock().getType();
        if (material == Material.DIAMOND_ORE
                || material == Material.EMERALD_ORE
                || material == Material.IRON_ORE
                || material == Material.COAL_ORE
                || material == Material.DEEPSLATE_COAL_ORE
                || material == Material.DEEPSLATE_DIAMOND_ORE
                || material == Material.DEEPSLATE_IRON_ORE
                || material == Material.DEEPSLATE_GOLD_ORE
                || material == Material.DEEPSLATE_EMERALD_ORE
                || material == Material.GOLD_ORE
                || material == Material.ANCIENT_DEBRIS
                || material == Material.NETHER_QUARTZ_ORE) {
            if (!PlayerActivitySystem.getLatestPlayerActivityEntry().containsKey(event.getPlayer().getName()))
                PlayerActivitySystem.getLatestPlayerActivityEntry().put(event.getPlayer().getName(), new HashMap<>());
            PlayerActivitySystem.getLatestPlayerActivityEntry().get(event.getPlayer().getName()).put(material, PlayerActivitySystem.getLatestPlayerActivityEntry().get(event.getPlayer().getName()).getOrDefault(material, 0) + 1);
        }*/
    }
}
