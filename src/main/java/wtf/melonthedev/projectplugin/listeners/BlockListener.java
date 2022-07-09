package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import wtf.melonthedev.projectplugin.Main;

import java.util.HashMap;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        if (material == Material.DIAMOND_ORE
                || material == Material.EMERALD_ORE
                || material == Material.IRON_ORE
                || material == Material.COAL_ORE
                || material == Material.DEEPSLATE_COAL_ORE
                || material == Material.DEEPSLATE_DIAMOND_ORE
                || material == Material.DEEPSLATE_IRON_ORE
                || material == Material.DEEPSLATE_GOLD_ORE
                || material == Material.GOLD_ORE
                || material == Material.ANCIENT_DEBRIS
                || material == Material.NETHER_QUARTZ_ORE) {
            if (!Main.getPlugin().getLatestPlayerActivityEntry().containsKey(event.getPlayer().getName())) {
                Main.getPlugin().getLatestPlayerActivityEntry().put(event.getPlayer().getName(), new HashMap<>());
            }
            Main.getPlugin().getLatestPlayerActivityEntry().get(event.getPlayer().getName()).put(material, Main.getPlugin().getLatestPlayerActivityEntry().get(event.getPlayer().getName()).getOrDefault(material, 0) + 1);
        }
    }
}
