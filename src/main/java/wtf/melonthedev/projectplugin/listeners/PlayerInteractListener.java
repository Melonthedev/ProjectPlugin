package wtf.melonthedev.projectplugin.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import wtf.melonthedev.projectplugin.Main;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            Chunk chunk = event.getPlayer().getWorld().getChunkAt(event.getClickedBlock());
            int cX = chunk.getX() * 16; // make it a world coordinate...
            int cZ = chunk.getZ() * 16;
            World world = chunk.getWorld();
            System.out.println("test");
            for(int x = 0; x < 16; x++)
            {
                for(int z = 0; z < 16; z++)
                {
                    for(int y = 0; y < 256; y++)
                    {
                        world.setBiome(new Location(world, cX + x, y, cZ+ z), Biome.MUSHROOM_FIELDS);
                    }
                }
            }
        }
        if (!event.hasItem()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.FIREWORK_ROCKET) return;
        if (event.getPlayer().getEquipment().getItem(EquipmentSlot.CHEST).getType() == Material.ELYTRA) return;
        event.setCancelled(true);


    }

    @EventHandler
    public void onPortalFrameInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.END_PORTAL_FRAME) return;
        if (Main.getPlugin().isEndAccessible()) return;
        event.setCancelled(true);
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "Das END ist blockiert. Es wird bald geöffnet."));
    }



}
