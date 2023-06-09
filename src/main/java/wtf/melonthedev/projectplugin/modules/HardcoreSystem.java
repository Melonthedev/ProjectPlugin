package wtf.melonthedev.projectplugin.modules;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.projectplugin.Main;

import java.util.List;

public class HardcoreSystem {

    public static void handleHardcoreModus() {
        boolean isEnabled = Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false);
        Bukkit.getOnlinePlayers().forEach(player -> player.getWorld().setHardcore(isEnabled));
    }

    public static void handleDeathAndDeathChest(PlayerDeathEvent event) {
        if (!Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) return;
        event.getPlayer().sendMessage(ChatColor.RED + "You died!");
        event.getPlayer().sendMessage(ChatColor.RED + "Your Death Location: X: "
                + ChatColor.GRAY + event.getPlayer().getLocation().getBlockX()
                + " Y: " + event.getPlayer().getLocation().getBlockY()
                + " Z: " + event.getPlayer().getLocation().getBlockZ());
        if (PvpCooldownSystem.pvpCooldowns.containsKey(event.getEntity().getUniqueId()))
            PvpCooldownSystem.pvpCooldowns.get(event.getEntity().getUniqueId()).disable();
        if (event.getKeepInventory()) return;
        createDeathChest(event.getDrops(), event.getPlayer());
    }

    public static void createDeathChest(List<ItemStack> items, Player player) {
        ItemStack[] items1 = new ItemStack[27];
        ItemStack[] items2 = new ItemStack[27];
        Component chestTitle = Component.text(ChatColor.RED + "Inventory of " + player.getName());

        if (items.isEmpty()) {
            Block block = player.getLocation().getBlock();
            block.setType(Material.PLAYER_HEAD);
            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
            skull.update();
            return;
        }
        player.getLocation().getBlock().setType(Material.CHEST);
        org.bukkit.block.Chest chestb1 = (org.bukkit.block.Chest) player.getLocation().getBlock().getState();
        Chest chest1 = (Chest) player.getLocation().getBlock().getBlockData();
        chest1.setType(Chest.Type.SINGLE);
        chestb1.setBlockData(chest1);
        chestb1.customName(chestTitle);
        chestb1.update();

        Block block = player.getLocation().getBlock().getRelative(BlockFace.UP);
        block.setType(Material.PLAYER_HEAD);
        Skull skull = (Skull) block.getState();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        skull.update();

        for (int i = 0; i < items.size(); i++) {
            if (i < 27) items1[i] = items.get(i);
            else items2[i - 27] = items.get(i);
        }
        if (items.size() >= 27) {
            chestb1.getBlockInventory().setStorageContents(items1);
            items.clear();
            return;
        }

        player.getLocation().getBlock().getRelative(BlockFace.EAST).setType(Material.CHEST);
        org.bukkit.block.Chest chestb2 = (org.bukkit.block.Chest) player.getLocation().getBlock().getRelative(BlockFace.EAST).getState();
        Chest chest2 = (Chest) player.getLocation().getBlock().getRelative(BlockFace.EAST).getBlockData();
        chest2.setType(Chest.Type.RIGHT);
        chest1.setType(Chest.Type.LEFT);
        chestb2.setBlockData(chest2);
        chestb2.customName(chestTitle);
        chestb1.update();
        chestb2.update();
        chestb2.getBlockInventory().setContents(items1);
        chestb1.getBlockInventory().setContents(items2);
        items.clear();
    }

}
