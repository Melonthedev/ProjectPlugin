package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;

import java.io.*;
import java.util.Objects;
import java.util.logging.Level;

public class ActionLoggerListener implements Listener {

    @EventHandler
    public void onContainerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock() instanceof Container && event.getClickedBlock() instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((Container) event.getClickedBlock()).getPersistentDataContainer();
            String owner = "unknown";
            if (NamespacedKey.fromString("owner") != null && container.has(Objects.requireNonNull(NamespacedKey.fromString("owner")), PersistentDataType.STRING)) {
                owner = container.get(Objects.requireNonNull(NamespacedKey.fromString("owner")), PersistentDataType.STRING);
            }
            logAction(event.getPlayer(), event.getClickedBlock().getLocation(), owner, event.getClickedBlock().getType().toString());
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced() instanceof Container && event.getBlockPlaced() instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((Container) event.getBlockPlaced()).getPersistentDataContainer();
            container.set(new NamespacedKey(Main.getPlugin(), "owner"), PersistentDataType.STRING, event.getPlayer().getName());
            event.getBlockPlaced().getState().update();
            logAction(event.getPlayer(), event.getBlockPlaced().getLocation(), event.getPlayer().getName(), event.getBlockPlaced().getType().toString());
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        if (event.getBlock() instanceof Container || type == Material.DIAMOND_BLOCK || type == Material.EMERALD_BLOCK || type == Material.NETHERITE_BLOCK || type == Material.IRON_BLOCK || type == Material.GOLD_BLOCK) {
            logAction(event.getPlayer(), event.getBlock().getLocation(), "", type.toString());
        }
    }


    public void logAction(Player player, Location location, String owner, String type) {
        String info =  player.getName() + " interact at " + location + " with " + type + " from " + owner + ".";
        Main.getPlugin().getLogger().log(Level.INFO, info);
        Writer output;
        File logFile = new File(Main.getPlugin().getDataFolder(), "actionlog.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            output = new BufferedWriter(new FileWriter(logFile));
            output.append(info);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

