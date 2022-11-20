package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import wtf.melonthedev.projectplugin.Main;

public class Lifesteal {

    private Lifesteal() {} // Utility Class, cannot be instantiated

    public static void init() {
        //TODO: Load infos from config for players, show messages, ...

    }

    public static boolean isLifesteal() {
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
        throw new NotImplementedException();
    }

    public static void removeHeart(Player player) {
        throw new NotImplementedException();
    }

    public static void revivePlayer(Player player) {
        throw new NotImplementedException();

    }

    public static void eliminatePlayer(Player player) {
        throw new NotImplementedException();

    }

    public static void handleJoin(Player player) {
        // Hier schauen ob spieler noch leben etc
        // Called from JoinListener
        throw new NotImplementedException();
    }


    /**
     * @return wheather heart was withdrawn successfully
     */
    public static boolean withdrawHeartToItem(Player player) {
        throw new NotImplementedException();
    }

    /**
     * @return wheather heart item was added successfully
     */
    public static boolean addHeartItemToPlayer(Player player) {
        throw new NotImplementedException();
    }

    //...

}
