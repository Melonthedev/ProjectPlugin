package wtf.melonthedev.projectplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.melonthedev.projectplugin.commands.*;
import wtf.melonthedev.projectplugin.commands.information.ColorCodesCommand;
import wtf.melonthedev.projectplugin.commands.information.PositionCommand;
import wtf.melonthedev.projectplugin.commands.information.WerIstImNetherCommand;
import wtf.melonthedev.projectplugin.commands.lifesteal.LifestealCommand;
import wtf.melonthedev.projectplugin.commands.lifesteal.WithdrawHeartCommand;
import wtf.melonthedev.projectplugin.commands.moderation.*;
import wtf.melonthedev.projectplugin.commands.pvpcooldown.PvpCooldownCommand;
import wtf.melonthedev.projectplugin.commands.pvpcooldown.SkipPvpCooldownCommand;
import wtf.melonthedev.projectplugin.listeners.*;
import wtf.melonthedev.projectplugin.utils.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static HashMap<Player, Location> locations = new HashMap<>();
    public static HashMap<Player, Location> deathlocations = new HashMap<>();
    public static HashMap<Player, Boolean> spawnElytraPlayers = new HashMap<>();
    public static HashMap<UUID, ItemStack> joinMessages = new HashMap<>();
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

        sendSpawnActionBarMessage();
        updateTabList();
        handleEastereggDamages();
        CustomItemSystem.handleCustomRecipes();
        PvpCooldownSystem.handleForAllPlayers();
        PlayerActivitySystem.handleSusPlayerActivityPerHour();
        Lifesteal.init();
    }

    @Override
    public void onDisable() {
        PlayerActivitySystem.savePlayerActivity();
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

    public void sendSpawnActionBarMessage() {
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
                Component.join(JoinConfiguration.noSeparators(), Component.text(ChatColor.GREEN.toString() + ChatColor.BOLD), getMMComponent("<rainbow>" + PROJECT_NAME), Component.text("\n" + ChatColor.RESET + ChatColor.GREEN + "McSurvivalprojekt.de")),
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

    public Component getMMComponent(String message) {
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

    public static Main getPlugin() {
        return plugin;
    }
}
