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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ActionLoggerListener implements Listener {

    public static final HashMap<Location, String> blockLog = new HashMap<>();
    private static final Material[] valuables = {
      Material.DIAMOND,
      Material.EMERALD,
      Material.GOLD_INGOT,
      Material.IRON_INGOT,
      Material.NETHERITE_INGOT,
      Material.DIAMOND_BLOCK,
      Material.EMERALD_BLOCK,
      Material.GOLD_BLOCK,
      Material.IRON_BLOCK,
      Material.NETHERITE_BLOCK,
      Material.BEACON,
      Material.NETHER_STAR,
      Material.ENCHANTED_GOLDEN_APPLE,
      Material.ELYTRA,
      Material.TRIDENT,
      Material.SHULKER_BOX,
      Material.SHULKER_SHELL,
      Material.TOTEM_OF_UNDYING,
      Material.HEART_OF_THE_SEA,
      Material.CONDUIT,
      Material.PLAYER_HEAD,
      Material.PLAYER_WALL_HEAD,
      Material.ZOMBIE_HEAD,
      Material.CREEPER_HEAD,
      Material.SKELETON_SKULL,
      Material.WITHER_SKELETON_SKULL,
      Material.WITHER_SKELETON_WALL_SKULL,
      Material.DRAGON_HEAD,
      Material.TNT,
      Material.SLIME_BLOCK,
      Material.HONEY_BLOCK,
      Material.CHEST,
      Material.TRAPPED_CHEST,
      Material.BARREL
    };

    @EventHandler
    public void onContainerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock() instanceof Container && event.getClickedBlock() instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((Container) event.getClickedBlock()).getPersistentDataContainer();
            String owner = "unknown";
            if (NamespacedKey.fromString("owner") != null && container.has(Objects.requireNonNull(NamespacedKey.fromString("owner")), PersistentDataType.STRING)) {
                owner = container.get(Objects.requireNonNull(NamespacedKey.fromString("owner")), PersistentDataType.STRING);
            }
            logAction(event.getPlayer(), "opened", event.getClickedBlock().getLocation(), owner, event.getClickedBlock().getType().toString());
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getState() instanceof Container && event.getBlockPlaced().getState() instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((Container) event.getBlockPlaced().getState()).getPersistentDataContainer();
            container.set(new NamespacedKey(Main.getPlugin(), "owner"), PersistentDataType.STRING, event.getPlayer().getName());
            event.getBlockPlaced().getState().update();
            blockLog.put(event.getBlockPlaced().getLocation(), event.getPlayer().getName());
            logAction(event.getPlayer(), "placed", event.getBlockPlaced().getLocation(), event.getPlayer().getName(), event.getBlockPlaced().getType().toString());
        }
        if (isValuable(event.getBlockPlaced().getType())) {
            blockLog.put(event.getBlockPlaced().getLocation(), event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String owner = "unknown";
        if (blockLog.containsKey(event.getBlock().getLocation())) {
            owner = blockLog.get(event.getBlock().getLocation());
        }
        Material type = event.getBlock().getType();
        if (event.getBlock() instanceof Container || type == Material.DIAMOND_BLOCK
                || type == Material.EMERALD_BLOCK
                || type == Material.NETHERITE_BLOCK
                || type == Material.IRON_BLOCK
                || type == Material.GOLD_BLOCK
                || type == Material.DIAMOND_ORE
                || type == Material.EMERALD_ORE
                || type == Material.IRON_ORE
                || type == Material.COAL_ORE
                || type == Material.DEEPSLATE_COAL_ORE
                || type == Material.DEEPSLATE_DIAMOND_ORE
                || type == Material.DEEPSLATE_IRON_ORE
                || type == Material.DEEPSLATE_GOLD_ORE
                || type == Material.GOLD_ORE) {
            logAction(event.getPlayer(), "broke", event.getBlock().getLocation(), owner, type.toString());
        }
        if (Arrays.stream(valuables).collect(Collectors.toList()).contains(event.getBlock().getType())) {
            logAction(event.getPlayer(), "broke", event.getBlock().getLocation(), owner, type.toString());
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null || event.getClickedInventory() instanceof PlayerInventory || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;
        if (isValuable(event.getCurrentItem().getType())) {
            String owner = "unknown";
            if (blockLog.containsKey(event.getClickedInventory().getLocation())) {
                owner = blockLog.get(event.getClickedInventory().getLocation());
            }
            logAction((Player) event.getWhoClicked(), "grabbed " + event.getCurrentItem().getAmount(), Objects.requireNonNull(event.getClickedInventory().getLocation()), owner, event.getCurrentItem().getType().toString());
        }
    }


    public static void logAction(Player player, String action, Location location, String owner, String type) {
        String worldName = location.getWorld() == null ? "unknown" : location.getWorld().getName();
        String info = player.getName() + " " + action + " " + type + " at X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + " W: " + worldName +  " from " + owner;
        if (!Main.getPlugin().getConfig().contains("logging")) {
            Main.getPlugin().getConfig().set("logging", true);
            Main.getPlugin().saveConfig();
        }
        if (Main.getPlugin().getConfig().getBoolean("logging")) {
            Main.getPlugin().getLogger().info(info);
        }
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
            output = new BufferedWriter(new FileWriter(logFile, true));
            output.append(info).append("\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValuable(Material material) {
        return Arrays.stream(valuables).collect(Collectors.toList()).contains(material);
    }

}

