package wtf.melonthedev.projectplugin.commands;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.TileState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.ItemStacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LockchestCommand implements CommandExecutor, Listener {

    Block block;
    static String serverprefix = "[LockChest] ";
    static ChatColor colorerror = ChatColor.RED;
    static ChatColor colorinfo = ChatColor.AQUA;
    static List<Player> inInv = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (args.length != 0) {
            p.sendMessage(colorerror + serverprefix + "Syntaxerror: /lockchest");
            return true;
        }
        Block block = p.getTargetBlock(null, 4);
        if (!(block.getState() instanceof Container)) {
            p.sendMessage(colorerror + serverprefix + "Du musst einen Container anschauen");
            return true;
        }
        openMainPage(p);
        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        //GENERAL
        if (e.getClickedInventory() == null) return;
        if (Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.PLAYER) return;
        if (!e.getView().getTitle().startsWith(colorinfo + "LockChest")) return;
        Player player = (Player) e.getWhoClicked();
        if (inInv.contains(player)) e.setCancelled(true);
        String title = e.getView().getTitle();
        int slot = e.getSlot();
        Block block = player.getTargetBlock(null, 4);
        ItemStack currentItem = e.getCurrentItem();
        if (currentItem == null) return;

        //CLICK EVENTS
        if (title.equals(colorinfo + "LockChest: Select Mode")) {
            if (slot == 11) lockChest(player);
            else if (slot == 15) unlockChest(player);
            else if (slot == 22) {
                if (!isLocked(block)) {
                    player.closeInventory();
                    player.sendMessage(colorerror + serverprefix + "Dieser Container ist nicht verschlossen.");
                    return;
                }
                if (!hasAccess(block, player)) {
                    player.closeInventory();
                    player.sendMessage(colorerror + serverprefix + "Dieser Container ist nicht von dir verschlossen.");
                    return;
                }
                this.block = block;
                openSelectPlayerInventory(player);
            }
        } else if (title.equals(colorinfo + "LockChest: Select Trust Methode")) {
            OfflinePlayer target = getSkullOwner(Objects.requireNonNull(e.getInventory().getItem(4)));
            if (slot == 11) {
                addChestAccess(block, target);
                player.closeInventory();
                player.sendMessage(colorinfo + serverprefix + "Du hast " + target.getName() + " zugriff auf den Container gewährt.");
            } else if (slot == 15) {
                removeChestAccess(block, target);
                player.closeInventory();
                player.sendMessage(colorinfo + serverprefix + target.getName() + " kann nun nicht mehr auf den Container zugreifen.");
            }
            else if (slot == 22) {
                this.block = block;
                openSelectPlayerInventory(player);
            }
        } else if (title.equals(colorinfo + "LockChest: Select Player")) {
            if (slot == 22) openMainPage(player);
            if (currentItem.getType() == Material.PLAYER_HEAD) {
                openTrustUpdateInventory(player, getSkullOwner(currentItem));
            } else if (currentItem.getType() == Material.CREEPER_HEAD) {
                player.closeInventory();
                player.sendMessage(colorinfo + serverprefix + "Please paste the name of the player in the chat");
                //config.set(player.getName() + ".waitForInput.status", true);
                //config.set(player.getName() + ".waitForInput.waitForLockChestTrustName", true);
                //Main.getPlugin().saveConfig();
            }
        }
    }

    private void openSelectPlayerInventory(Player player) {
        Inventory trustInv = Bukkit.createInventory(null, 27, colorinfo + "LockChest: Select Player");
        for (int i = 0; i < trustInv.getSize(); i++) trustInv.setItem(i, ItemStacks.placeholder);
        trustInv.setItem(10, ItemStacks.blackplaceholder);
        trustInv.setItem(11, ItemStacks.blackplaceholder);
        trustInv.setItem(12, ItemStacks.blackplaceholder);
        trustInv.setItem(13, ItemStacks.blackplaceholder);
        trustInv.setItem(14, ItemStacks.blackplaceholder);
        trustInv.setItem(15, ItemStacks.blackplaceholder);
        trustInv.setItem(16, ItemStacks.blackplaceholder);
        int currentSlot = 10;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Objects.requireNonNull(trustInv.getItem(currentSlot)).isSimilar(ItemStacks.blackplaceholder)) return;
            if (block != null && hasAccess(block, p)) continue;
            trustInv.setItem(currentSlot, ItemStacks.createSkull(p.getName(), colorinfo + p.getName(), null, 1));
            currentSlot++;
        }
        trustInv.setItem(23, ItemStacks.lockChestOfflinePlayer);
        trustInv.setItem(22, ItemStacks.arrowUp);
        player.openInventory(trustInv);
        inInv.add(player);
    }

    public static void openTrustUpdateInventory(Player player, OfflinePlayer target) {
        Inventory trustInv = Bukkit.createInventory(null, 27, colorinfo + "LockChest: Select Trust Methode");
        for (int i = 0; i < trustInv.getSize(); i++) trustInv.setItem(i, ItemStacks.placeholder);
        trustInv.setItem(11, ItemStacks.lockChestAddPlayer);
        trustInv.setItem(15, ItemStacks.lockChestRemovePlayer);
        trustInv.setItem(4, ItemStacks.createSkull(target.getName(), colorinfo + "Target: " + target.getName(), null, 1));
        trustInv.setItem(22, ItemStacks.arrowUp);
        player.openInventory(trustInv);
        inInv.add(player);
    }

    public void unlockChest(Player player) {
        Block block = player.getTargetBlock(null, 4);
        setContainer(block, player, "unlock");
        if (block.getState() instanceof Chest) {
            Container container = (Container) block.getState();
            container.setLock(null);
        }
    }

    public void lockChest(Player player) {
        Block block = player.getTargetBlock(null, 4);
        setContainer(block, player, "lock");
    }

    private void openMainPage(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, colorinfo + "LockChest: Select Mode");
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, ItemStacks.placeholder);
        inv.setItem(11, ItemStacks.lockItem);
        inv.setItem(15, ItemStacks.unlockItem);
        inv.setItem(22, ItemStacks.lockChestEditPerms);
        player.openInventory(inv);
        inInv.add(player);
    }

    public static void setContainer(Block block, Player player, String mode) {
        if (!(block.getState() instanceof Container)) {
            player.sendMessage(colorerror + serverprefix + "Du musst einen Container anschauen");
            player.closeInventory();
            return;
        }
        //PERSISTENT DATA CONTAINER
        if (!(block.getState() instanceof TileState)) return;
        TileState state = (TileState) block.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "locked-chests");
        //MODE
        switch (mode) {
            case "lock":
                if (container.has(key, PersistentDataType.STRING)) {
                    player.sendMessage(colorerror + serverprefix + "Dieser Behälter ist schon verschlossen.");
                    player.closeInventory();
                    return;
                }
                container.set(key, PersistentDataType.STRING, player.getUniqueId().toString());
                player.sendMessage(colorinfo + serverprefix + "Du hast den Behälter abgeschlossen. Unlocke ihn mit '/lockchest' > Unlock Container");
                break;
            case "unlock":
                if (!isLocked(block)) {
                    player.sendMessage(colorerror + serverprefix + "Dieser Behälter ist nicht verschlossen.");
                    player.closeInventory();
                    return;
                }
                if (!hasAccess(block, player)) {
                    player.sendMessage(colorerror + serverprefix + "Dieser Behälter wurde nicht von dir verschlossen.");
                    player.closeInventory();
                    return;
                }
                container.remove(key);
                player.sendMessage(colorinfo + serverprefix + "Du hast den Behälter freigegeben.");
                player.closeInventory();
                break;
        }
        state.update();
        player.closeInventory();
    }

    public static OfflinePlayer getSkullOwner(ItemStack skullItem) {
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
        assert meta != null;
        return meta.getOwningPlayer();
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        inInv.remove((Player) event.getPlayer());
    }

    public static boolean isLocked(Block container) {
        if (!(container.getState() instanceof TileState)) return false;
        TileState state = (TileState) container.getState();
        PersistentDataContainer dataContainer = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "locked-chests");
        return dataContainer.has(key, PersistentDataType.STRING);
    }

    public static boolean hasAccess(Block container, Player player) {
        if (!isLocked(container)) return false;
        PersistentDataContainer dataContainer = ((TileState) container.getState()).getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "locked-chests");
        String[] value = Objects.requireNonNull(dataContainer.get(key, PersistentDataType.STRING)).split(",");
        List<String> permissionedPlayers = Arrays.asList(value);
        return permissionedPlayers.contains(player.getUniqueId().toString());
    }

    public static void addChestAccess(Block container, OfflinePlayer player) {
        if (!isLocked(container)) return;
        if (!(container.getState() instanceof TileState)) return;
        TileState state = (TileState) container.getState();
        PersistentDataContainer dataContainer = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "locked-chests");
        String[] value = Objects.requireNonNull(dataContainer.get(key, PersistentDataType.STRING)).split(",");
        List<String> permissionedPlayers = Arrays.asList(value);
        if (permissionedPlayers.contains(player.getUniqueId().toString())) return;
        String newValue = Objects.requireNonNull(dataContainer.get(key, PersistentDataType.STRING)) + "," + player.getUniqueId();
        dataContainer.set(key, PersistentDataType.STRING, newValue);
        state.update();
    }

    public static void removeChestAccess(Block container, OfflinePlayer player) {
        if (!isLocked(container)) return;
        if (!(container.getState() instanceof TileState)) return;
        TileState state = (TileState) container.getState();
        PersistentDataContainer dataContainer = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "locked-chests");
        String oldValue = Objects.requireNonNull(dataContainer.get(key, PersistentDataType.STRING));
        String value = oldValue.replace("," + player.getUniqueId(), "");
        dataContainer.set(key, PersistentDataType.STRING, value);
        state.update();
    }
    
}