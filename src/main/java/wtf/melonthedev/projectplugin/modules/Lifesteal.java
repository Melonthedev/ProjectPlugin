package wtf.melonthedev.projectplugin.modules;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import wtf.melonthedev.projectplugin.Main;

import java.util.*;

public class Lifesteal {

    private Lifesteal() {}

    public static final String prefix = ChatColor.GREEN + ChatColor.BOLD.toString() + "[Life" + ChatColor.DARK_RED + ChatColor.BOLD + "Steal] " + ChatColor.RESET;
    public static Objective heartsObjective;

    public static void init() {
        if (!isLifestealActive()) return;
        handleScoreboard();
        Bukkit.getOnlinePlayers().forEach(Lifesteal::validateHearts);
    }

    public static void handleScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        heartsObjective = scoreboard.getObjective("hearts");
        if (heartsObjective == null) heartsObjective = scoreboard.registerNewObjective("hearts", Criteria.DUMMY, Component.text(ChatColor.RED + "❤"));
        heartsObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    public static void onDisable() {
        if (heartsObjective != null) heartsObjective.unregister();
        heartsObjective = null;
    }

    public static boolean isLifestealActive() {
        return Main.getPlugin().getConfig().getBoolean("lifesteal.enabled", false);
    }

    public static boolean isNetheriteBlocked() {
        return Main.getPlugin().getConfig().getBoolean("lifesteal.blocknetherite", true);
    }
    public static boolean isTotemBlocked() {
        return Main.getPlugin().getConfig().getBoolean("lifesteal.blocktotems", true);
    }
    public static boolean isElytraBlocked() {
        return Main.getPlugin().getConfig().getBoolean("lifesteal.blockelytra", true);
    }

    /**
    * Set whether the Lifesteal System is active e.g. if hearts can be crafted
     **/
    public static void setLifestealActive(boolean flag) {
        Main.getPlugin().getConfig().set("lifesteal.enabled", flag);
        Main.getPlugin().saveConfig();
        if (flag) Bukkit.getOnlinePlayers().forEach(Lifesteal::validateHearts);
        else Bukkit.getOnlinePlayers().forEach(Lifesteal::resetHearts);
    }

    public static int getDefaultHeartCount() {
        return 10;
    }

    public static int getRevivedPlayerHeartCount() {
        return 1;
    }

    public static ItemStack getHeartItem() {
        ItemStack heart = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        ItemMeta heartmeta = heart.getItemMeta();
        heartmeta.displayName(Component.text(ChatColor.DARK_RED + "Heart"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.AQUA + "Right-click to consume"));
        heartmeta.lore(lore);
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
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, count);
        Main.getPlugin().saveConfig();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) validateHearts(player);
        if (hearts <= 0) eliminatePlayer(uuid);
    }

    public static int getHeartCount(UUID uuid) {
        return Main.getPlugin().getConfig().getInt("lifesteal.hearts." + uuid, getDefaultHeartCount());
    }

    public static void handleJoin(Player player) {
        validateHearts(player);
        if (Main.getPlugin().getConfig().getBoolean("lifesteal.willReviveOnJoin." + player.getUniqueId(), false))
            revivePlayer(player);
    }

    public static void revivePlayer(Player player) {
        //TODO: Teleport to grave, applie slow falling, add achivement sound, add achivement postmortal, summon particles, remove head from grave
        Location location = getGraveLocationOfPlayer(player.getUniqueId());
        Main.getPlugin().getConfig().set("lifesteal.willReviveOnJoin." + player.getUniqueId(), null);
        Main.getPlugin().saveConfig();
        unassignGrave(player.getUniqueId());
        player.spigot().respawn();
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*2, 255));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*6, 255));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*20, 255));
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 0.9f));
        Bukkit.broadcast(Component.join(JoinConfiguration.noSeparators(), Component.text(Lifesteal.prefix), Main.getMMComponent("<rainbow>" + player.getName() + " was revived!")));
        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 100);
        if (location != null)
            player.teleport(location.add(0.5, 30, 0.5));
    }

    public static String getGraveIdOfPlayer(UUID uuid) {
        ConfigurationSection section = Main.getPlugin().getConfig().getConfigurationSection("graveyardpositions");
        if (section == null) section = Main.getPlugin().getConfig().createSection("graveyardpositions");
        Set<String> positions = section.getKeys(false);
        ConfigurationSection finalSection = section;
        return positions.stream().filter(s -> Objects.equals(finalSection.getString(s + ".owner"), uuid.toString())).findFirst().orElse(null);
    }

    public static Location getGraveLocationOfPlayer(UUID uuid) {
        String id = getGraveIdOfPlayer(uuid);
        ConfigurationSection section = Main.getPlugin().getConfig().getConfigurationSection("graveyardpositions");
        if (id == null) return null;
        assert section != null;
        int x = section.getInt(id + ".x");
        int y = section.getInt(id + ".y");
        int z = section.getInt(id + ".z");
        String world = section.getString(id + ".world");
        if (world == null) return null;
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static HashMap<Location, UUID> getGraves() {
        ConfigurationSection section = Main.getPlugin().getConfig().getConfigurationSection("graveyardpositions");
        if (section == null) section = Main.getPlugin().getConfig().createSection("graveyardpositions");
        Set<String> positions = section.getKeys(false);
        HashMap<Location, UUID> graves = new HashMap<>();
        ConfigurationSection finalSection = section;
        positions.forEach(s -> {
            int x = finalSection.getInt(s + ".x");
            int y = finalSection.getInt(s + ".y");
            int z = finalSection.getInt(s + ".z");
            String world = finalSection.getString(s + ".world");
            String owner = finalSection.getString(s + ".owner");
            if (world == null) return;
            World w = Bukkit.getWorld(world);
            UUID uuid = owner == null ? null : UUID.fromString(owner);
            Location location = new Location(w, x, y, z);
            graves.put(location, uuid);
        });
        return graves;
    }

    public static void assignGrave(UUID uuid) {
        for (Map.Entry<Location, UUID> entry : getGraves().entrySet()) {
            if (entry.getValue() != null) continue;
            Location l = entry.getKey();
            Main.getPlugin().getConfig().set("graveyardpositions." + l.getWorld().getName() + l.getBlockX() + l.getBlockY() + l.getBlockZ() + ".owner", uuid.toString());
            Main.getPlugin().saveConfig();

            Block block = l.getBlock().getRelative(BlockFace.UP);
            block.setType(Material.PLAYER_HEAD);
            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            skull.update();
            return;
        }
    }

    public static void unassignGrave(UUID uuid) {
        for (Map.Entry<Location, UUID> entry : getGraves().entrySet()) {
            if (entry.getValue() == null) continue;
            if (!Objects.equals(entry.getValue().toString(), uuid.toString())) continue;
            Location l = entry.getKey();
            Main.getPlugin().getConfig().set("graveyardpositions." + l.getWorld().getName() + l.getBlockX() + l.getBlockY() + l.getBlockZ() + ".owner", null);
            Main.getPlugin().saveConfig();
            l.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
            return;
        }
    }

    public static void unblockPlayer(UUID uuid) {
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, getRevivedPlayerHeartCount());
        Main.getPlugin().getConfig().set("lifesteal.willReviveOnJoin." + uuid, true);
        Main.getPlugin().saveConfig();
    }

    public static void eliminatePlayer(UUID uuid) {
        Main.getPlugin().getConfig().set("lifesteal.hearts." + uuid, 0);
        Main.getPlugin().saveConfig();
        assignGrave(uuid);
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) return;
        target.getInventory().clear();
        target.kick(Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + "Life" + ChatColor.DARK_RED + "Steal\n" + ChatColor.WHITE + "You lost all your hearts.\n" + ChatColor.AQUA + "Ask your friends to revive you.\n" + ChatColor.GOLD + "Don't give up!"), PlayerKickEvent.Cause.BANNED);
    }

    public static void validateHearts(Player player) {
        if (!isLifestealActive()) return;
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), getDefaultHeartCount());
        if (hearts == 0) {
            eliminatePlayer(player.getUniqueId());
            return;
        }
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) == null) player.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(hearts*2);
        if (heartsObjective == null) handleScoreboard();
        heartsObjective.getScore(player).setScore(hearts);
    }

    public static void resetHearts(Player player) {
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) == null) player.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
        if (heartsObjective != null) heartsObjective.unregister();
        heartsObjective = null;
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
            return;
        }
        heart.setAmount(heart.getAmount() - 1);
        giveHeart(player.getUniqueId(), 1);
    }
}
