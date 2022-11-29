package wtf.melonthedev.projectplugin.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;

import java.util.*;

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

    /**
    * Set whether the Lifesteal System is active e.g. if hearts can be crafted
     **/
    public static void setLifestealActive(boolean flag) {
        Main.getPlugin().getConfig().set("lifesteal.enabled", flag);
        Main.getPlugin().saveConfig();
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
        heartmeta.displayName(Component.text(ChatColor.DARK_RED + "Heart"));
        heartmeta.addEnchant(Enchantment.CHANNELING, 1, true);
        heartmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        heartmeta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "heart"), PersistentDataType.BYTE, (byte) 1);
        heart.setItemMeta(heartmeta);
        return heart;
    }

    public static ItemStack getConstructionHeartItem() {
        ItemStack csheart = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        ItemMeta csheartmeta = csheart.getItemMeta();
        NamespacedKey csheartkey = new NamespacedKey(Main.getPlugin(), "construction_heart");
        csheartmeta.displayName(Component.text(ChatColor.WHITE + "Construction Heart"));
        csheartmeta.getPersistentDataContainer().set(csheartkey, PersistentDataType.BYTE, (byte) 1);
        List<Component> lorelist = new ArrayList<>();
        lorelist.add(Component.text("Add Netherstar"));
        lorelist.add(Component.text("in Smithing Table"));
        csheartmeta.lore(lorelist);
        csheart.setItemMeta(csheartmeta);
        return csheart;
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
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, count);
        Main.getPlugin().saveConfig();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) validateHearts(player);
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
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, 0);
        Main.getPlugin().saveConfig();
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) return;
        target.getInventory().clear();
        target.kick(Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + "Life" + ChatColor.DARK_RED + "Steal\n"+ ChatColor.WHITE + "You lost all your hearts.\n" + ChatColor.AQUA + "Ask your friends to revive you.\n" + ChatColor.GOLD + "Don't give up!"), PlayerKickEvent.Cause.BANNED);
    }

    public static void validateHearts(Player player) {
        if (!isLifestealActive()) return;
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), getDefaultHeartCount());
        if (hearts == 0) {
            eliminatePlayer(player.getUniqueId());
            return;
        }
        //player.setHealthScale(hearts*2);
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) == null) player.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(hearts*2);
    }

    public static void handleLogin(AsyncPlayerPreLoginEvent event) {
        if (Main.getPlugin().getConfig().getInt("lifesteal.hearts." + event.getUniqueId(), 20) == 0 && isLifestealActive()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + "Life" + ChatColor.DARK_RED + "Steal\n"+ ChatColor.WHITE + "You lost all your hearts.\n" + ChatColor.AQUA + "Ask your friends to revive you."));
        }
    }

    public static void withdrawHeartToItem(Player player, Integer count) {
        if (!isLifestealActive()) return;
        removeHeart(player.getUniqueId(), count);
        ItemStack temp = getHeartItem().clone();
        temp.setAmount(count);
        HashMap<Integer, ItemStack> items = player.getInventory().addItem(temp);
        items.forEach((amount, item) -> player.getWorld().dropItem(player.getLocation(), item));
    }

    public static void addHeartItemToPlayer(Player player, ItemStack heart) {
        if (!isLifestealActive()) return;
        if (getHeartCount(player.getUniqueId()) >= 20) {
            player.sendMessage(prefix + "You have reached the maximum amount of hearts possible.");
        }
        heart.setAmount(heart.getAmount() - 1);
        giveHeart(player.getUniqueId(), 1);
    }



    //...

}
