package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import wtf.melonthedev.projectplugin.Main;

import java.util.HashMap;

public class Lifesteal {

    private Lifesteal() {} // Utility Class, cannot be instantiated

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

    public static void giveHeart(Player player) {
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), getDefaultHeartCount());
        Main.getPlugin().getConfig().set("lifesteal.hearts." + player.getUniqueId(), hearts + 1);
        Main.getPlugin().saveConfig();
    }

    public static void removeHeart(Player player, Integer count) {
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), getDefaultHeartCount());
        Main.getPlugin().getConfig().set("lifesteal.hearts." + player.getUniqueId(), hearts - count);
        Main.getPlugin().saveConfig();
        if (hearts <= 0) eliminatePlayer(player);
    }

    public static void revivePlayer(Player player) {
        Main.getPlugin().getConfig().set("lifesteal.hearts." + player.getUniqueId(), getRevivedPlayerHeartCount());
        Main.getPlugin().saveConfig();
        //TODO: Finish
    }

    public static void eliminatePlayer(Player player) {
        throw new NotImplementedException();
    }

    public static void validateHearts(Player player) {
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), getDefaultHeartCount());
        if (hearts == 0) {
            eliminatePlayer(player);
            return;
        }
        player.setHealthScale(hearts*2);
    }

    public static void handleLogin(AsyncPlayerPreLoginEvent event) {
        if (Main.getPlugin().getConfig().getInt("lifesteal.hearts." + event.getUniqueId(), 20) == 0) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + "Life" + ChatColor.DARK_RED + "Steal\n"+ ChatColor.RESET + "You lost all your hearts.\nAsk your friends to revive you."));
            return;
        }
    }


    /**
     * @return wheather heart was withdrawn successfully
     */
    public static boolean withdrawHeartToItem(Player player, Integer count) {
        removeHeart(player, count);
        ItemStack temp = getHeartItem().clone();
        temp.setAmount(count);
        HashMap<Integer, ItemStack> items = player.getInventory().addItem(temp);
        items.forEach((amount, item) -> player.getWorld().dropItem(player.getLocation(), item));
        return true;
    }

    /**
     * @return wheather heart item was added successfully
     */
    public static boolean addHeartItemToPlayer(Player player, ItemStack heart) {
        heart.setAmount(heart.getAmount() - 1);
        giveHeart(player);
        return true;
    }

    //...

}
