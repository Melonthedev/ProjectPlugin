package wtf.melonthedev.projectplugin.modules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.type.Chest;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import wtf.melonthedev.projectplugin.Main;

public class HardcoreSystem {

    public static void handleHardcoreModus() {
        boolean flag = Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false);
        Bukkit.getOnlinePlayers().forEach(player -> player.getWorld().setHardcore(flag));
    }

    public static void handleDeathAndDeathChest(PlayerDeathEvent event) {
        Component deathMessageComponent = event.deathMessage();
        String deathMessage = deathMessageComponent == null ? event.getPlayer().getName() + " died"
                : PlainTextComponentSerializer.plainText().serialize(deathMessageComponent);
        if (Main.getPlugin().getConfig().getBoolean("hardcore.giantDeathTitle", false))
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.showTitle(Title.title(Main.getMMComponent("<red>" + event.getPlayer().getName() + " died.</red>"),
                            Component.text(ChatColor.BLUE + deathMessage))));

        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You died!");
            event.getPlayer().sendMessage(ChatColor.RED + "Your Death Location: X: "
                    + ChatColor.GRAY + event.getPlayer().getLocation().getBlockX()
                    + " Y: " + event.getPlayer().getLocation().getBlockY()
                    + " Z: " + event.getPlayer().getLocation().getBlockZ());
            if (PvpCooldownSystem.pvpCooldowns.containsKey(event.getEntity().getUniqueId()))
                PvpCooldownSystem.pvpCooldowns.get(event.getEntity().getUniqueId()).disable();
            if (event.getKeepInventory()) return;
            ItemStack[] items1 = new ItemStack[27];
            ItemStack[] items2 = new ItemStack[27];
            //ItemStack head = ItemStacks.createSkull(event.getEntity().getName(), event.getEntity().getName() + "'s Head", "Was noch übrig von " + event.getEntity().getName() + " ist.", 1);

            if (event.getDrops().isEmpty()) {
                //event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), head);
                Block block = event.getEntity().getLocation().getBlock();
                block.setType(Material.PLAYER_HEAD);
                Skull skull = (Skull) block.getState();
                skull.setOwningPlayer(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()));
                skull.update();
                return;
            }
            event.getEntity().getLocation().getBlock().setType(Material.CHEST);
            org.bukkit.block.Chest chestb1 = (org.bukkit.block.Chest) event.getEntity().getLocation().getBlock().getState();
            Chest chest1 = (Chest) event.getEntity().getLocation().getBlock().getBlockData();
            chest1.setType(Chest.Type.SINGLE);
            chestb1.setBlockData(chest1);
            chestb1.customName(Component.text(ChatColor.RED + "Inventory of " + event.getEntity().getName()));
            chestb1.update();

            //event.getDrops().add(head);
            Block block = event.getEntity().getLocation().getBlock().getRelative(BlockFace.UP);
            block.setType(Material.PLAYER_HEAD);
            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()));
            skull.update();

            for (int i = 0; i < event.getDrops().size(); i++) {
                if (i < 27) items1[i] = event.getDrops().get(i);
                else items2[i - 27] = event.getDrops().get(i);
            }
            if (event.getDrops().size() >= 27) {
                chestb1.getBlockInventory().setStorageContents(items1);
                event.getDrops().clear();
                return;
            }
            event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).setType(Material.CHEST);
            org.bukkit.block.Chest chestb2 = (org.bukkit.block.Chest) event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).getState();
            Chest chest2 = (Chest) event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).getBlockData();
            chest2.setType(Chest.Type.RIGHT);
            chest1.setType(Chest.Type.LEFT);
            chestb1.setBlockData(chest1);
            chestb2.setBlockData(chest2);
            chestb1.customName(Component.text(ChatColor.RED + "Inventory of " + event.getEntity().getName()));
            chestb2.customName(Component.text(ChatColor.RED + "Inventory of " + event.getEntity().getName()));
            chestb1.update();
            chestb2.update();
            chestb2.getBlockInventory().setContents(items1);
            chestb1.getBlockInventory().setContents(items2);
            event.getDrops().clear();
            return;
        }
    }

}
