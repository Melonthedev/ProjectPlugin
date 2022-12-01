package wtf.melonthedev.projectplugin.utils;

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
    static NamespacedKey csheartrecipekey = new NamespacedKey(Main.getPlugin(), "construction_heart_recipe");

    public static void handleCustomRecipes() {
        //Invisible ItemFrame
        ItemStack itemframe = new ItemStack(Material.ITEM_FRAME);
        ItemMeta framemeta = itemframe.getItemMeta();
        framemeta.displayName(Component.text(ChatColor.WHITE + "Invisible Item Frame"));
        framemeta.getPersistentDataContainer().set(framekey, PersistentDataType.BYTE, (byte) 1);
        framemeta.addEnchant(Enchantment.CHANNELING, 1,true);
        framemeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemframe.setItemMeta(framemeta);
        ShapedRecipe framerecipe = new ShapedRecipe(framekey, itemframe);
        framerecipe.shape("SSS", "SAS", "SSS");
        framerecipe.setIngredient('S', Material.STICK);
        framerecipe.setIngredient('A', Material.AMETHYST_SHARD);

        Bukkit.addRecipe(framerecipe);

        //Construction Heart
        ItemStack csheart = Lifesteal.getConstructionHeartItem();

        ShapedRecipe csheartrecipe = new ShapedRecipe(csheartrecipekey, csheart);
        csheartrecipe.shape("RNR", "DHD", "RTR");
        csheartrecipe.setIngredient('R', Material.REDSTONE);
        csheartrecipe.setIngredient('N', Material.NETHERITE_INGOT);
        csheartrecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        csheartrecipe.setIngredient('H', Material.HEART_OF_THE_SEA);
        csheartrecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        Bukkit.addRecipe(csheartrecipe);

        //Heart
        NamespacedKey heartrecipekey = new NamespacedKey(Main.getPlugin(), "heart_recipe");
        ItemStack heart = Lifesteal.getHeartItem();
        //SmithingRecipe heartrecipe = new SmithingRecipe(heartrecipekey, Lifesteal.getHeartItem(),
        //        new RecipeChoice.ExactChoice(Lifesteal.getConstructionHeartItem()),
        //        new RecipeChoice.MaterialChoice(Material.NETHER_STAR));

        //Bukkit.addRecipe(heartrecipe);

        Bukkit.getOnlinePlayers().forEach(CustomItemSystem::discoverCustomRecipes);
    }

    public static void discoverCustomRecipes(Player player) {
        player.discoverRecipe(framekey);
        player.discoverRecipe(csheartrecipekey);
    }


}
