package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        /*if (event.getBlock().getType() == Material.ANCIENT_DEBRIS) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Sorry heute ist netherite farm verbot damit wir über 2TPS bleiben :)");
        }*/ //TODO BLOCK FOR CONFIG
    }
}
