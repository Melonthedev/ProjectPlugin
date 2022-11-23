package wtf.melonthedev.projectplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import wtf.melonthedev.projectplugin.commands.*;
import wtf.melonthedev.projectplugin.listeners.*;
import wtf.melonthedev.projectplugin.utils.Lifesteal;
import wtf.melonthedev.projectplugin.utils.LocationUtils;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static HashMap<Player, Location> locations = new HashMap<>();
    public static HashMap<Player, Location> deathlocations = new HashMap<>();
    public static HashMap<Player, Boolean> spawnElytraPlayers = new HashMap<>();
    public static HashMap<UUID, ItemStack> joinMessages = new HashMap<>();
    public static String[] donators = new String[] {
            "Progeto",
            "El_Crafter",
            "Groß_Gandhini",
            "Jonbadon",
            "Soro",
            "Sprengmeister444",
            "stebadon",
            "Melonthedev"
    }; // TODO: Handle per API to not be hardcoded
    public static List<HashMap<String, HashMap<Material, Integer>>> collectedValuables = new ArrayList<>();
    private final Component[] infos = new Component[] {
            Component.text(ChatColor.GRAY + "Drücke ").append(Component.keybind("key.sneak")).append(Component.text(ChatColor.GRAY + " um von dieser Insel zu gleiten")),
            Component.text(ChatColor.GRAY + "Chatte mit " + ChatColor.WHITE + "<rainbow>" + ChatColor.GRAY + " um RGB zu schreiben"),
            Component.text(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/status" + ChatColor.GRAY + " um einen Status zu setzen"),
            Component.text(ChatColor.GRAY + "Rechtsklicke sneakend ein " + ChatColor.WHITE + "Schild" + ChatColor.GRAY + " um den Inhalt zu bearbeiten"),
            Component.text(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/r" + ChatColor.GRAY + " um auf eine Privatnachricht zu antworten"),
            Component.text(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/msg" + ChatColor.GRAY + " um Nachrichten sogar an offline Spieler zu senden"),
    };


    public static String PROJECT_NAME = "Survivalprojekt 4.1";
    public static String DISCORD_INVITE = "discord.gg/AmskHwQSCT";

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().log(Level.INFO, "**********************");
        getLogger().log(Level.INFO, "*** Project Plugin ***");
        getLogger().log(Level.INFO, "*** by Melonthedev ***");
        getLogger().log(Level.INFO, "**** and Stebadon  ****");
        getLogger().log(Level.INFO, "**********************");

        //COMMAND REGISTRATION
        getCommand("status").setExecutor(new StatusCommand());
        getCommand("position").setExecutor(new PositionCommand());
        getCommand("colorcodes").setExecutor(new ColorCodesCommand());
        getCommand("toggleendaccessibility").setExecutor(new ToggleEndAccessibilityCommand());
        getCommand("message").setExecutor(new MessageCommand());
        getCommand("reply").setExecutor(new ReplyCommand());
        getCommand("spectatestebadon").setExecutor(new SpectateStebadonCommand());
        getCommand("logoutput").setExecutor(new LogOutputCommand());
        getCommand("bounty").setExecutor(new BountyCommand());
        getCommand("deathlocation").setExecutor(new DeathLocationCommand());
        getCommand("checksusplayeractivity").setExecutor(new CheckSusPlayerActivityCommand());
        getCommand("afk").setExecutor(new AfkCommand());
        getCommand("weristimnether").setExecutor(new WerIstImNetherCommand());
        getCommand("tempban").setExecutor(new TempBanCommand());
        getCommand("joinmessage").setExecutor(new JoinMessageCommand());
        getCommand("isee").setExecutor(new ISeeCommand());
        getCommand("hardcore").setExecutor(new HardCoreCommand());
        getCommand("survivalprojekt").setExecutor(new SurvivalprojektCommand());
        getCommand("skippvpcooldown").setExecutor(new SkipPvpCooldownCommand());
        getCommand("withdraw").setExecutor(new WithdrawHeartCommand());
        getCommand("lifesteal").setExecutor(new LifestealCommand());
        getCommand("pvpcooldown").setExecutor(new PvpCooldownCommand());
        //getCommand("votekick").setExecutor(votekickInstance);
        //getCommand("lockchest").setExecutor(lockchestInstance);

        //LISTENER REGISTRATION
        //getServer().getPluginManager().registerEvents(votekickInstance, this);
        //getServer().getPluginManager().registerEvents(lockchestInstance, this);
        getServer().getPluginManager().registerEvents(new SpawnElytraListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new SignEditListener(), this);
        getServer().getPluginManager().registerEvents(new ServerPingListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new ActionLogger(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);

        sendSpawnMessage();
        updateTabList();
        handleEastereggDamages();
        handleSusPlayerActivityPerHour();
        handleCustomRecpies();
        PvpCooldownSystem.handleForAllPlayers();
        Lifesteal.init();
    }

    @Override
    public void onDisable() {
        savePlayerActivity();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playerListName(Component.text(player.getName()));
            player.displayName(Component.text(player.getName()));
            if (PvpCooldownSystem.pvpCooldowns.containsKey(player.getUniqueId()))
                player.hideBossBar(PvpCooldownSystem.pvpCooldowns.get(player.getUniqueId()).getBar());
        }
    }

    public void handleHardcoreModus() {
        boolean flag = getConfig().getBoolean("hardcore.enabled", false);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getWorld().setHardcore(flag);
            player.getWorld().setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        });
    }

    public void handleCustomRecpies() {
        //Invisible ItemFrame
        ItemStack item = new ItemStack(Material.ITEM_FRAME);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(this, "invisible_item_frame");
        meta.displayName(Component.text(ChatColor.WHITE + "Invisible Item Frame"));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        meta.addEnchant(Enchantment.CHANNELING, 1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("SSS", "SAS", "SSS");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('A', Material.AMETHYST_SHARD);

        Bukkit.addRecipe(recipe);
    }

    public void handleSeeSpectators() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam("players") != null) return;
        scoreboard.registerNewTeam("players");
        Team players = scoreboard.getTeam("players");
        if (players == null) return;
        Bukkit.getOnlinePlayers().forEach(players::addPlayer);
        players.setCanSeeFriendlyInvisibles(true);
    }

    public void handlePlayerJoinSpectatorVisibility(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam("players") == null) handleSeeSpectators();
        Team players = scoreboard.getTeam("players");
        if (players == null) return;
        players.addPlayer(player);
    }

    public void sendSpawnMessage() {
        AtomicInteger i = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!LocationUtils.isLocationInSpawnArea(player.getLocation())) continue;
                player.sendActionBar(infos[i.get()]);
            }
            i.getAndIncrement();
            if (i.get() >= infos.length) i.set(0);
        }, 0, 80);
    }

    public void updateTabList() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) setCustomPlayerListHeader(player);
        }, 0, 80);
    }

    public void setCustomPlayerListHeader(Player player) {
        player.sendPlayerListHeaderAndFooter(
                Component.join(JoinConfiguration.noSeparators(), Component.text(ChatColor.GREEN.toString() + ChatColor.BOLD), getMiniMessageComponent("<rainbow>" + PROJECT_NAME), Component.text("\n" + ChatColor.RESET + ChatColor.GREEN + "McSurvivalprojekt.de")),
                Component.text(ChatColor.AQUA + "Online: " + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + " | " + ChatColor.AQUA + "TPS: " + Math.round(getServer().getTPS()[0]))
        );
    }

    public boolean isEndAccessible() {
        if (!getConfig().contains("config.isendaccessible")) {
            getConfig().set("config.isendaccessible", false);
            saveConfig();
        }
        return getConfig().getBoolean("config.isendaccessible");
    }

    public void setEndAccessible(boolean accessible) {
        getConfig().set("config.isendaccessible", accessible);
        saveConfig();
    }

    public Component getMiniMessageComponent(String message) {
        MiniMessage mm = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.reset())
                        .resolver(StandardTags.hoverEvent())
                        .resolver(StandardTags.rainbow())
                        .resolver(StandardTags.translatable())
                        .resolver(StandardTags.transition())
                        .resolver(StandardTags.keybind())
                        .resolver(StandardTags.font())
                        .build())
                .build();
        return mm.deserialize(message);
    }

    public String translateHexAndCharColorCodes(String message)
    {
        final Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})" + "#");
        Matcher matcher = hexPattern.matcher(message);
        char COLOR_CHAR = ChatColor.COLOR_CHAR;
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public void handleEastereggDamages() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Player jonbadon = Bukkit.getPlayer("Jonbadon");
            if (jonbadon != null && jonbadon.getLocation().getBlock().getType() == Material.STONECUTTER)
                jonbadon.damage(1);

            Player tantalos = Bukkit.getPlayer("Tantal0s");
            if (tantalos != null && (tantalos.getInventory().getItemInMainHand().getType() == Material.TNT || tantalos.getInventory().getItemInOffHand().getType() == Material.TNT))
                tantalos.damage(1);
        }, 0, 1);
    }

    public void handleSusPlayerActivityPerHour() {
        new BukkitRunnable() {
            @Override
            public void run() {
                collectedValuables.add(new HashMap<>());
            }
        }.runTaskTimer(this, 0, 72000); // 1h
    }

    public void savePlayerActivity() {
        getConfig().set("playeractivity", collectedValuables);
    }
    public HashMap<String, HashMap<Material, Integer>> getLatestPlayerActivityEntry() {
        return collectedValuables.get(collectedValuables.size() - 1);
    }

    public static Main getPlugin() {
        return plugin;
    }
}
