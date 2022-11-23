package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;

import java.util.HashMap;
import java.util.UUID;

public class Lifesteal {

    private Lifesteal() {} // Utility Class, cannot be instantiated

    public static final String prefix = ChatColor.GREEN + ChatColor.BOLD.toString() + "[Life" + ChatColor.DARK_RED + ChatColor.BOLD + "Steal] " + ChatColor.RESET;

    public static void init() {
        //TODO: Load infos from config for players, show messages, ...

        if (!isLifestealActive()) return;
        Bukkit.getOnlinePlayers().forEach(Lifesteal::validateHearts);
    }

    public static boolean isLifestealActive() {
        return Main.getPlugin().getConfig().getBoolean("lifesteal.enabled", false);
    }

    public static int getDefaultHeartCount() {
        return 10;
    }

    public static int getRevivedPlayerHeartCount() {
        return 3;
    }

    public static ItemStack getHeartItem() {
        ItemStack heart = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        ItemMeta heartmeta = heart.getItemMeta();
        heartmeta.displayName(Component.text(ChatColor.DARK_RED + "Herz"));
        heartmeta.addEnchant(Enchantment.CHANNELING, 1, true);
        heartmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        heartmeta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "heart"), PersistentDataType.BYTE, (byte) 1);
        heart.setItemMeta(heartmeta);
        return heart;
    }

    public static void giveHeart(UUID uuid, Integer count) {
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + uuid, getDefaultHeartCount());
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, hearts + count);
        Main.getPlugin().saveConfig();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            validateHearts(player);
            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "+" + count + " Heart");
        }
    }

    public static void removeHeart(UUID uuid, Integer count) {
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + uuid, getDefaultHeartCount());
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, hearts - count);
        Main.getPlugin().saveConfig();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            validateHearts(player);
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "-" + count + " Heart");
        }
        if (hearts <= 0) eliminatePlayer(uuid);
    }

    public static void setHeartCount(UUID uuid, Integer count){
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + uuid, getDefaultHeartCount());
        Main.getPlugin().getConfig().set("lifesteal.herarts." + uuid, count);
        Main.getPlugin().saveConfig();
        if (hearts <= 0) eliminatePlayer(uuid);
    }

    public static int getHeartCount(UUID uuid) {
        return Main.getPlugin().getConfig().getInt("lifesteal.hearts." + uuid, getDefaultHeartCount());
    }

    public static void revivePlayer(UUID uuid) {
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, getRevivedPlayerHeartCount());
        Main.getPlugin().saveConfig();
        //TODO: Finish
    }

    public static void eliminatePlayer(UUID uuid) {
        //TODO
    }

    public static void validateHearts(Player player) {
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), getDefaultHeartCount());
        if (hearts == 0) {
            eliminatePlayer(player.getUniqueId());
            return;
        }
        player.setHealthScale(hearts*2);
    }

    public static void handleLogin(AsyncPlayerPreLoginEvent event) {
        if (Main.getPlugin().getConfig().getInt("lifesteal.hearts." + event.getUniqueId(), 20) == 0 && isLifestealActive()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + "Life" + ChatColor.DARK_RED + "Steal\n"+ ChatColor.RESET + "You lost all your hearts.\nAsk your friends to revive you."));
        }
    }

    public static void withdrawHeartToItem(Player player, Integer count) {
        removeHeart(player.getUniqueId(), count);
        ItemStack temp = getHeartItem().clone();
        temp.setAmount(count);
        HashMap<Integer, ItemStack> items = player.getInventory().addItem(temp);
        items.forEach((amount, item) -> player.getWorld().dropItem(player.getLocation(), item));
    }

    public static void addHeartItemToPlayer(Player player, ItemStack heart) {
        heart.setAmount(heart.getAmount() - 1);
        giveHeart(player.getUniqueId(), 1);
    }



    //...

}
