package wtf.melonthedev.projectplugin.listeners.featurelisteners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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

/**
 * @deprecated in favor of Log4Minecraft
 */
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
            Material.WITHER_ROSE
    };

    private static final EntityType[] valuableEntities = {
            EntityType.MINECART_CHEST,
            EntityType.MINECART,
            EntityType.HORSE,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.DROPPED_ITEM,
            EntityType.VILLAGER,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ELDER_GUARDIAN,
            EntityType.SNOWMAN,
            EntityType.MUSHROOM_COW,
            EntityType.IRON_GOLEM,
            EntityType.ARMOR_STAND,
            EntityType.WOLF,
            EntityType.OCELOT,
            EntityType.PAINTING,
            EntityType.WITHER,
            EntityType.PLAYER,
            EntityType.CAT,
            EntityType.BEE,
            EntityType.DOLPHIN,
            EntityType.DONKEY,
            EntityType.FOX,
            EntityType.LLAMA,
            EntityType.MULE,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.POLAR_BEAR,
            EntityType.SHULKER,
            EntityType.WANDERING_TRADER,
            EntityType.TRADER_LLAMA,
            EntityType.AXOLOTL,
            EntityType.ALLAY,
            EntityType.STRIDER,
            EntityType.TADPOLE,
            EntityType.FROG,
            EntityType.GOAT,
            EntityType.TURTLE,
            EntityType.WARDEN
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
        String owner = "unknown";
        if (event.getClickedInventory().getLocation() != null && event.getClickedInventory().getLocation().getBlock() != null  && event.getClickedInventory().getLocation().getBlock().getType() != null) {
            owner = getOwner(event.getClickedInventory().getLocation().getBlock());
        }
        logAction(event.getWhoClicked().getName(), "grabbed " + event.getCurrentItem().getAmount(), Objects.requireNonNull(event.getClickedInventory().getLocation()), owner, event.getCurrentItem().getType().toString(), isValuable(event.getCurrentItem().getType()));
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (isValuable(event.getRightClicked().getType())) logAction(event.getPlayer().getName(), "interacted with", event.getRightClicked().getLocation(), "unknown", event.getRightClicked().getType().toString(), isValuable(event.getRightClicked().getType()));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER || event.getEntity().getType() == EntityType.VILLAGER)
            return;
        logAction(event.getEntity().getName(), "died", event.getEntity().getLocation(), event.getEntity().getKiller() == null ? "unknown" : event.getEntity().getKiller().toString(), "", isValuable(event.getEntity().getType()), null, isValuable(event.getEntity().getType()));
    }

    public static void logAction(String player, String action, @Nullable Location location, String owner, String type, boolean important) {
        logAction(player, action, location, owner, type, important, null);
    }

    public static void logAction(String player, String action, @Nullable Location location, String owner, String type, boolean important, String details) {
        logAction(player, action, location, owner, type, important, details, true);
    }

    public static void logAction(String player, String action, @Nullable Location location, String owner, String type, boolean important, String details, boolean logToFile) {
        Date currentDate = new Date();
        String info = "[" + DateFormat.getDateInstance().format(currentDate) + " " + (currentDate.getHours() < 10 ? "0" + currentDate.getHours() : currentDate.getHours()) + ":" + (currentDate.getMinutes() < 10 ? "0" + currentDate.getMinutes() : currentDate.getMinutes()) + "] "
                + player + " "
                + action + " "
                + type
                + (location == null ? "" : " at X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + " W: " + (location.getWorld() == null ? "unknown" : location.getWorld().getName()))
                +  " from " + owner
                + (details != null ? " - " + details : "");
        if (!Main.getPlugin().getConfig().contains("logging")
                || (!Objects.equals(Main.getPlugin().getConfig().getString("config.logging"), "normal")
                && !Objects.equals(Main.getPlugin().getConfig().getString("config.logging"), "detailed")
                && !Objects.equals(Main.getPlugin().getConfig().getString("config.logging"), "off"))) {
            Main.getPlugin().getConfig().set("config.logging", "normal");
            Main.getPlugin().saveConfig();
        }
        String logLevel = Main.getPlugin().getConfig().getString("config.logging");
        if (logLevel == null) logLevel = "normal";
        if ((important && logLevel.equals("normal")) || logLevel.equals("detailed"))
            Main.getPlugin().getLogger().info(info);
        if (!logToFile)
            return;
        Writer output;
        String fileName = "actionlog-" + Calendar.getInstance().get(Calendar.YEAR) +
                "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10 ? "0" + Calendar.getInstance().get(Calendar.MONTH) + 1 : Calendar.getInstance().get(Calendar.MONTH) + 1) +
                "-" + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10 ? "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + ".txt";
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
    private boolean isValuable(EntityType entityType) {
        return Arrays.stream(valuableEntities).collect(Collectors.toList()).contains(entityType);
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

