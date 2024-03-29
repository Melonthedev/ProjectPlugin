package wtf.melonthedev.projectplugin.modules;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wtf.melonthedev.projectplugin.Main;

public class CustomItemSystem {

    static NamespacedKey framekey = new NamespacedKey(Main.getPlugin(), "invisible_item_frame");
    static NamespacedKey heartrecipekey = new NamespacedKey(Main.getPlugin(), "heart_recipe");

    public static void handleCustomRecipes() {
        if (Main.isFeatureDisabled("customRecipes")) return;
        //Invisible ItemFrame
        ItemStack itemframe = getInvisibleItemFrameItem();
        ShapedRecipe framerecipe = new ShapedRecipe(framekey, itemframe);
        framerecipe.shape("SSS", "SAS", "SSS");
        framerecipe.setIngredient('S', Material.STICK);
        framerecipe.setIngredient('A', Material.AMETHYST_SHARD);
        if (Main.getPlugin().getConfig().getBoolean("config.customRecipes.invisibleItemFrame", false)) Bukkit.addRecipe(framerecipe);

        //Heart
        ItemStack heartItem = Lifesteal.getHeartItem();
        ShapedRecipe heartrecipe = new ShapedRecipe(heartrecipekey, heartItem);
        heartrecipe.shape("DTD", "NSN", "DED");
        heartrecipe.setIngredient('S', Material.NETHER_STAR);
        heartrecipe.setIngredient('N', Material.NETHERITE_INGOT);
        heartrecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        heartrecipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE);
        heartrecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        if (Lifesteal.isLifestealActive()) Bukkit.addRecipe(heartrecipe);

        Bukkit.getOnlinePlayers().forEach(CustomItemSystem::discoverCustomRecipes);
    }

    public static void discoverCustomRecipes(Player player) {
        if (Main.getPlugin().getConfig().getBoolean("config.customRecipes.invisibleItemFrame", false))
            player.discoverRecipe(framekey);

        if (Lifesteal.isLifestealActive())
            player.discoverRecipe(heartrecipekey);
    }


    public static ItemStack getInvisibleItemFrameItem() {
        ItemStack item = new ItemStack(Material.ITEM_FRAME);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.WHITE + "Invisible Item Frame"));
        meta.getPersistentDataContainer().set(framekey, PersistentDataType.BYTE, (byte) 1);
        meta.addEnchant(Enchantment.CHANNELING, 1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
}
