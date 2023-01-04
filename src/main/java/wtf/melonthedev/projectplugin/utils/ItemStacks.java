package wtf.melonthedev.projectplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Different ItemStacks for several old modules (Pet System, Shop System, ...)
 * @deprecated
 */
@Deprecated
public class ItemStacks {

    public static ChatColor colorinfo = ChatColor.AQUA;
    public static ChatColor colorsecondinfo = ChatColor.DARK_AQUA;
    public static ChatColor colorerror = ChatColor.RED;
    public static ItemStack placeholder = createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1);
    public static ItemStack blackplaceholder = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", null, 1);
    public static ItemStack blueplaceholder = createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", null, 1);
    public static ItemStack arrowNext = createItem(Material.ARROW, ChatColor.RESET + "Nächste Seite->", null, 1);
    public static ItemStack arrowBack = createItem(Material.ARROW, ChatColor.RESET + "<-Vorherige Seite", null, 1);
    public static ItemStack arrowUp = createItem(Material.ARROW, ChatColor.RESET + "Go Back", null, 1);

    //SHOP
    public static ItemStack emptySlot = createItem(Material.RED_TERRACOTTA, colorerror + "Here is no Item to buy", null, 1);
    public static ItemStack leaveShop = createItem(Material.DARK_OAK_DOOR, colorerror + "Go Back", null, 1);
    public static ItemStack playerinfos;
    public static ItemStack currencyEmerald = createItem(Material.EMERALD, colorsecondinfo + "Setze die Kaufwährung auf " + colorinfo + "Emerald", null, 1);
    public static ItemStack currencyDiamond = createItem(Material.DIAMOND, colorsecondinfo + "Setze die Kaufwährung auf " + colorinfo + "Diamond", null, 1);
    public static ItemStack buyItem = createItem(Material.FIREWORK_ROCKET, ChatColor.GREEN + "BUY!", null, 1);
    public static ItemStack minus = createItem(Material.CRIMSON_ROOTS, ChatColor.RED + "-", null, 1);
    public static ItemStack plus = createItem(Material.WARPED_ROOTS, ChatColor.GREEN + "+", null, 1);
    public static ItemStack create = createItem(Material.GREEN_BANNER, ChatColor.GREEN + "Create Shop-Entry", null, 1);
    public static ItemStack cancel = createItem(Material.RED_BANNER, ChatColor.RED + "Cancel Shop-Entry", null, 1);
    public static ItemStack removeInfo = createItem(Material.OAK_SIGN, colorerror + "INFO", "Klicke auf ein Item deiner wahl und anschließend auf das Barrier Item unten.", 1);
    public static ItemStack remove = createItem(Material.BARRIER, colorerror + "ITEM ENTFERNEN!", colorerror + "Dies kannst du nicht rückgängig machen.", 1);
    public static ItemStack priceInDiamonds = createItem(Material.DIAMOND, colorinfo + "Preis in Diamonds", colorsecondinfo + "Dies ist der Preis, den man bezahlen muss, wenn man mit der Währung DIAMOND bezahlt.", 1);
    public static ItemStack priceInEmeralds = createItem(Material.EMERALD, colorinfo + "Preis in Emeralds", colorsecondinfo + "Dies ist der Preis, den man bezahlen muss, wenn man mit der Währung EMERALD bezahlt.", 1);

    //LOCKCHEST
    public static ItemStack lockItem = createItem(Material.BARRIER, colorerror + "Lock Container", null, 1);
    public static ItemStack unlockItem = createItem(Material.OAK_DOOR, ChatColor.GREEN + "Unlock Container", null, 1);
    public static ItemStack lockChestEditPerms = createItem(Material.WRITABLE_BOOK, colorinfo + "Trust/Untrust Player", null, 1);
    public static ItemStack lockChestAddPlayer = createItem(Material.NETHER_STAR, ChatColor.GREEN + "Trust Player", null, 1);
    public static ItemStack lockChestRemovePlayer = createItem(Material.BARRIER, ChatColor.RED + "Untrust Player", null, 1);
    public static ItemStack lockChestOfflinePlayer = createItem(Material.CREEPER_HEAD, ChatColor.AQUA + "Select Player by their name", ChatColor.GOLD + "Here you can select also offline players", 1);

    //PLOTVOTE
    public static ItemStack perfect = createItem(Material.NETHERITE_INGOT, ChatColor.GOLD + "PERFECT", null, 1);
    public static ItemStack good =createItem(Material.GOLD_INGOT, ChatColor.GREEN + "GOOD", null, 1);
    public static ItemStack ok = createItem(Material.IRON_INGOT, ChatColor.YELLOW + "OK", null, 1);
    public static ItemStack bad = createItem(Material.BRICK, ChatColor.RED + "BAD", null, 1);
    public static ItemStack poop = createItem(Material.NETHER_BRICK, ChatColor.DARK_RED + "Du hättest auch gleich einen Kackhaufen bauen können", null, 1);

    //PETS
    public static ItemStack selectPetType = createSkull("MHF_Pig", ChatColor.GOLD + "Set Type", null, 1);
    public static ItemStack selectPetSize = createItem(Material.VINE, ChatColor.GOLD + "Set Pet size/age", null, 1);
    public static ItemStack teleportPet = createItem(Material.ENDER_PEARL, ChatColor.GOLD + "Teleport Pet", null, 1);
    public static ItemStack renamePet = createItem(Material.NAME_TAG, ChatColor.GOLD + "Rename Pet", null, 1);
    public static ItemStack spawnPet = createItem(Material.SHEEP_SPAWN_EGG, ChatColor.GOLD + "SPAWN Pet", null, 1);
    public static ItemStack despawnPet = createItem(Material.REDSTONE, ChatColor.GOLD + "DESPAWN Pet", null, 1);
    public static ItemStack tempType = createSkull("MHF_Pig", ChatColor.GOLD + "Type: PIG (temporär)", null, 1);
    public static ItemStack babyAge = createItem(Material.RED_MUSHROOM, ChatColor.GOLD + "Baby Pet", null, 1);
    public static ItemStack adultAge = createItem(Material.CRIMSON_FUNGUS, ChatColor.GOLD + "Adult Pet", null, 1);
    public static ItemStack petSpeed9 = createItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Set FollowSpeed to 9", null, 1);
    public static ItemStack petSpeed8 = createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.DARK_GREEN + "Set FollowSpeed to 8", null, 1);
    public static ItemStack petSpeed7 = createItem(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.YELLOW + "Set FollowSpeed to 7", null, 1);
    public static ItemStack petSpeed6 = createItem(Material.ORANGE_STAINED_GLASS_PANE, ChatColor.GOLD + "Set FollowSpeed to 6", null, 1);
    public static ItemStack petSpeed5 = createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ChatColor.AQUA + "Set FollowSpeed to 5", null, 1);
    public static ItemStack petSpeed4 = createItem(Material.CYAN_STAINED_GLASS_PANE, ChatColor.BLUE + "Set FollowSpeed to 4", null, 1);
    public static ItemStack petSpeed3 = createItem(Material.BLUE_STAINED_GLASS_PANE, ChatColor.DARK_AQUA + "Set FollowSpeed to 3", null, 1);
    public static ItemStack petSpeed2 = createItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.RED + "Set FollowSpeed to 2", null, 1);
    public static ItemStack petSpeed1 = createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_RED + "Set FollowSpeed to 1", null, 1);
    public static ItemStack petSetSpeed = createItem(Material.GOLDEN_BOOTS, ChatColor.GOLD + "Set FollowSpeed",  colorinfo + "Setzt die Geschwindigkeit, in der dir dein Pet folgt.", 1);
    public static ItemStack petSitDown = createItem(Material.ANVIL, ChatColor.GOLD + "Sit Down, PET!", colorinfo + "Setzt dein Pet hin, sodass es dir erst wieder folgt, wenn du es aufstehen lässt.", 1);
    public static ItemStack petSettings = createItem(Material.REDSTONE_TORCH, ChatColor.GOLD + "Settings", null, 1);
    public static ItemStack otherPetThings = createItem(Material.ORANGE_DYE, ChatColor.GOLD + "Others", colorinfo + "Partikel, Sättel, Futter, ...", 1);
    public static ItemStack petOthersOnFire = createItem(Material.FLINT_AND_STEEL, ChatColor.GOLD + "Set your Pet on Fire", null, 1);
    public static ItemStack petOthersJumpBoost = createItem(Material.DIAMOND_BOOTS, ChatColor.GOLD + "Toggle Jump Boost", null, 1);
    public static ItemStack petOthersParticle = createItem(Material.YELLOW_DYE, ChatColor.GOLD + "Particles", null, 1);
    public static ItemStack petOthersItemInHand = createItem(Material.GOLDEN_HOE, ChatColor.GOLD + "Set Item in Hand", null, 1);
    public static ItemStack petOthersSaddle = createItem(Material.SADDLE, ChatColor.GOLD + "Saddle your Pet", null, 1);
    public static ItemStack petOthersPassenger = createItem(Material.MINECART, ChatColor.GOLD + "Add Passenger", null, 1);
    public static ItemStack petOthersFeed = createItem(Material.APPLE, ChatColor.GOLD + "Set Pet Feed", null, 1);
    public static ItemStack petOthersEmpty = createItem(Material.BARRIER, ChatColor.RED + "Coming soon", null, 1);
    public static ItemStack petSilentBell = createItem(Material.BELL, ChatColor.GOLD + "Toggle pet silence", null, 1);

    //MARRY
    public static ItemStack marryAccept = createItem(Material.BEACON, ChatColor.GREEN + "Akzeptiere den Antrag", null, 1);
    public static ItemStack marryDecline = createItem(Material.COAL, ChatColor.DARK_RED + "Lehne den Antrag ab", null, 1);
    public static ItemStack marryInfo = createItem(Material.ENCHANTED_BOOK, ChatColor.GOLD + "Möchten sie heiraten?", null, 1);
    public static ItemStack marryBackGroundYellow = createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", null, 1);
    public static ItemStack marryBackGroundGreen = createItem(Material.GREEN_STAINED_GLASS_PANE, " ", null, 1);
    public static ItemStack marryBackGroundRed = createItem(Material.RED_STAINED_GLASS_PANE, " ", null, 1);
    public static ItemStack marryQuit = createItem(Material.TNT, colorerror + "Lasse dich scheiden!", ChatColor.RED + "Dieser Vorgang kostet 32 Emeralds", 1);
    public static ItemStack marryOverviewInfo = createItem(Material.DARK_OAK_SIGN, colorinfo + "Hier ist die Übersicht deiner Ehe", null, 1);
    public static ItemStack marryTpa = createItem(Material.ENDER_PEARL, colorinfo + "Sende eine TPA", ChatColor.YELLOW + "/tpa <Partner>", 1);
    public static ItemStack marryHome = createItem(Material.DARK_OAK_DOOR, colorinfo + "Teleportiere dich zum Home deines Partners", ChatColor.YELLOW + "/home <Partner>", 1);

    //KITPVP
    public static ItemStack kitPVPkitStandard = createItem(Material.LEATHER_CHESTPLATE, ChatColor.DARK_AQUA + "Standard Kit", ChatColor.GRAY + "Kit contents:\n- Iron Sword\n- Full Leather\n- 64x Stone\n- 10x Golden Carrot", 1);
    public static ItemStack kitPVPkitPro = createItem(Material.IRON_SWORD, ChatColor.AQUA + "Pro Kit", ChatColor.GRAY + "Kit contents:\n- Iron Sword\n- Full Iron\n- 64x Stone\n- 1x Water Bucket\n- Fishing Rod\n- 16x Golden Carrot", 1);
    public static ItemStack kitPVPkitUltra = createItem(Material.COBWEB, ChatColor.LIGHT_PURPLE + "Ultra Kit", ChatColor.GRAY + "Kit contents:\n- Iron Sword\n- Full Iron\n- 64x Stone\n- 1x Water Bucket\n- Fishing Rod\n- 16x Golden Carrot\n- Shield\n- 16x Cobweb", 1);
    public static ItemStack kitPVPkitEpic = createItem(Material.IRON_SWORD, ChatColor.DARK_PURPLE + "Epic Kit", ChatColor.GRAY + "Kit contents:\n- All Ultra-Items\n- Iron Sword + Sharpness I\n- 1x Bow\n- 16x Arrows", 1, Enchantment.DAMAGE_ALL, 1);
    public static ItemStack kitPVPkitSniper = createItem(Material.BOW, ChatColor.GREEN + "Sniper Kit", ChatColor.GRAY + "Kit contents:\n- All Ultra-Items\n- 1x Bow + Infinity\n- Arrow\n- 1x Skeleton Spawn egg", 1, Enchantment.ARROW_INFINITE, 1);
    public static ItemStack kitPVPkitOp = createItem(Material.DIAMOND_SWORD, ChatColor.RED + "OP Kit", ChatColor.GRAY + "Kit contents:\n- Diamond Sword\n- 32x Stone", 1);
    public static ItemStack kitPVPkitPearl = createItem(Material.ENDER_PEARL, ChatColor.DARK_BLUE + "Pearler Kit", ChatColor.GRAY + "Kit contents:\n- All Pro-Items\n- 8x Ender Pearl", 1);

    public static void initializeShopkeeperInfos(Player shopkeeper) {
        if (shopkeeper == null) {
            playerinfos = createItem(Material.CREEPER_HEAD, colorerror + "OFFLINE", colorinfo + "Dieser Spieler ist Offline und deshalb kann kein Profil angezeigt werden :(", 1);
            return;
        }
        playerinfos = createSkull(shopkeeper.getName(), ChatColor.GREEN + "Verkäufer: " + shopkeeper.getName(), null, 1);
        remove.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
    }

    public static ItemStack createItem(Material type, String displayName, String lore, int amount) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        if (lore != null) {
            String[] loreEntrys = lore.split("\n");
            String color = ChatColor.getLastColors(loreEntrys[0]);
            for (int i = 0; i < loreEntrys.length; i++) {
                loreEntrys[i] = color + loreEntrys[i];
            }
            List<String> itemLore = Arrays.asList(loreEntrys);
            meta.setLore(itemLore);
        }
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack createItem(Material type, String displayName, String lore, int amount, Enchantment enchantment, int level) {
        ItemStack item = createItem(type, displayName, lore, amount);
        item.addUnsafeEnchantment(enchantment, level);
        return item;
    }

    public static ItemStack createSkull(String owner, String displayName, String lore, int amount) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        if (lore != null)
            meta.setLore(Collections.singletonList(lore));
        meta.setOwner(owner);
        item.setItemMeta(meta);
        return item;
    }

}
