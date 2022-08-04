package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;

import javax.annotation.Nullable;
import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

public class ActionLogger implements Listener {

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
          Material.BARREL,
          Material.DIAMOND_ORE,
          Material.EMERALD_ORE,
          Material.IRON_ORE,
          Material.COAL_ORE,
          Material.DEEPSLATE_COAL_ORE,
          Material.DEEPSLATE_DIAMOND_ORE,
          Material.DEEPSLATE_IRON_ORE,
          Material.DEEPSLATE_GOLD_ORE,
          Material.GOLD_ORE,
    };

    @EventHandler
    public void onContainerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock() instanceof Container && event.getClickedBlock() instanceof PersistentDataHolder) {
            String owner = getOwner(event.getClickedBlock());
            logAction(event.getPlayer().getName(), "opened", event.getClickedBlock().getLocation(), owner, event.getClickedBlock().getType().toString(), true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getState() instanceof Container && event.getBlockPlaced().getState() instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((Container) event.getBlockPlaced().getState()).getPersistentDataContainer();
            container.set(new NamespacedKey(Main.getPlugin(), "owner"), PersistentDataType.STRING, event.getPlayer().getName());
            event.getBlockPlaced().getState().update();
        }
        blockLog.put(event.getBlockPlaced().getLocation(), event.getPlayer().getName());
        logAction(event.getPlayer().getName(), "placed", event.getBlockPlaced().getLocation(), event.getPlayer().getName(), event.getBlockPlaced().getType().toString(), isValuable(event.getBlockPlaced().getType()));
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String owner = getOwner(event.getBlock());
        logAction(event.getPlayer().getName(), "broke", event.getBlock().getLocation(), owner, event.getBlock().getType().toString(), isValuable(event.getBlock().getType()));
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null || event.getClickedInventory() instanceof PlayerInventory || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;
        String owner = event.getClickedInventory().getLocation() != null ? getOwner(event.getClickedInventory().getLocation().getBlock()) : "unknown";
            logAction(event.getWhoClicked().getName(), "grabbed " + event.getCurrentItem().getAmount(), Objects.requireNonNull(event.getClickedInventory().getLocation()), owner, event.getCurrentItem().getType().toString(), isValuable(event.getCurrentItem().getType()));
    }


    public static void logAction(String player, String action, @Nullable Location location, String owner, String type, boolean important) {
        Date currentDate = new Date();
        String info = "[" + DateFormat.getDateInstance().format(currentDate) + " " + currentDate.getHours() + ":" + currentDate.getMinutes() + "] " + player + " " + action + " " + type + (location == null ? "" : " at X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + " W: " + (location.getWorld() == null ? "unknown" : location.getWorld().getName())) +  " from " + owner;
        if (!Main.getPlugin().getConfig().contains("logging")) {
            Main.getPlugin().getConfig().set("logging", "normal");
            Main.getPlugin().saveConfig();
        }
        String logLevel = Main.getPlugin().getConfig().getString("logging");
        if (logLevel == null) logLevel = "normal";
        if (important || logLevel.equals("detailed"))
            Main.getPlugin().getLogger().info(info);
        Writer output;
        String fileName = "actionlog-" + Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ".txt";
        File logFile = new File(Main.getPlugin().getDataFolder(), fileName);
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) Main.getPlugin().getLogger().log(new LogRecord(Level.SEVERE,"Could not create actionlog-file '" + fileName + "'"));
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

    private String getOwner(Block block) {
        String owner = "unknown";
        if (blockLog.containsKey(block.getLocation()))
            owner = blockLog.get(block.getLocation());
        if (block instanceof Container) {
            PersistentDataContainer container = ((Container) block).getPersistentDataContainer();
            if (NamespacedKey.fromString("owner") != null && container.has(Objects.requireNonNull(NamespacedKey.fromString("owner")), PersistentDataType.STRING))
                owner = container.get(Objects.requireNonNull(NamespacedKey.fromString("owner")), PersistentDataType.STRING);
        }
        return owner;
    }

}

