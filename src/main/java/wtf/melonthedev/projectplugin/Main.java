package wtf.melonthedev.projectplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.melonthedev.projectplugin.commands.*;
import wtf.melonthedev.projectplugin.commands.information.ColorCodesCommand;
import wtf.melonthedev.projectplugin.commands.information.PositionCommand;
import wtf.melonthedev.projectplugin.commands.information.TimerCommand;
import wtf.melonthedev.projectplugin.commands.information.WerIstImNetherCommand;
import wtf.melonthedev.projectplugin.commands.lifesteal.GravayardCommand;
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
    public static HashMap<UUID, Location> deathlocations = new HashMap<>();
    public static HashMap<Player, Boolean> spawnElytraPlayers = new HashMap<>();
    public static HashMap<UUID, ItemStack> joinMessages = new HashMap<>();
    private Component[] infos;

    public static String PROJECT_NAME = "Survivalprojekt 4.1";
    public static String DISCORD_INVITE = "discord.gg/AmskHwQSCT";

    @Override
    public void onEnable() {
        plugin = this;
        handleConfig();
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
        getCommand("donators").setExecutor(new DonatorsCommand());
        getCommand("graveyard").setExecutor(new GravayardCommand());
        getCommand("manageplayer").setExecutor(new ManagePlayerCommand());
        getCommand("timer").setExecutor(new TimerCommand());
        //getCommand("votekick").setExecutor(votekickInstance);
        //getCommand("lockchest").setExecutor(lockchestInstance);
        //getCommand("bounty").setExecutor(new BountyCommand());

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
        getServer().getPluginManager().registerEvents(new CraftListener(), this);

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
        StatusCommand.onDisable();
        PvpCooldownSystem.onDisable();
        PlayerActivitySystem.savePlayerActivity();
        TimerSystem.stopTimer();
        Lifesteal.onDisable();
    }


    public void handleConfig() {
        saveDefaultConfig(); // Default Config in resources/config.yml

        // Load Constants
        if (getConfig().getString("projectName") != null) PROJECT_NAME = getConfig().getString("projectName");
        if (getConfig().getString("discordInvite") != null) DISCORD_INVITE = getConfig().getString("discordInvite");
        //Actionbar Messages
        String[] messages = getConfig().getStringList("actionbarmessages").toArray(String[]::new);
        infos = new Component[messages.length];
        for (int i = 0; i < messages.length; i++)
            infos[i] = getMMComponent(messages[i]);
    }

    public static boolean isFeatureDisabled(String feature) {
        return !Main.getPlugin().getConfig().getBoolean("config." + feature + ".enabled", false);
    }

    public void handleHardcoreModus() {
        boolean flag = getConfig().getBoolean("hardcore.enabled", false);
        Bukkit.getOnlinePlayers().forEach(player -> player.getWorld().setHardcore(flag));
    }

    public static void handleFirstJoin(Player player) {
        if (!player.hasPlayedBefore() && !Main.isFeatureDisabled("newPlayerWelcomeMessage")) {
            Bukkit.getServer().broadcast(Component.text(Main.getPlugin().getConfig().getString("config.newPlayerWelcomeMessage.message", "§l§aPlayerName, Herzlich Willkommen auf Survivalprojekt!").replaceFirst("PlayerName", player.getName())));
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p, Sound.ENTITY_GOAT_SCREAMING_AMBIENT, 1.0F, 0.5F));
        }
    }

    public void sendSpawnActionBarMessage() {
        if (isFeatureDisabled("actionBarSpawnMessages")) return;
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
        if (isFeatureDisabled("customTabListInfo")) return;
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(Main::setCustomPlayerListHeader), 0, 80);
    }

    public static void setCustomPlayerListHeader(Player player) {
        if (Main.isFeatureDisabled("customTabListInfo")) return;
        player.sendPlayerListHeaderAndFooter(
                Component.join(JoinConfiguration.noSeparators(), Component.text(ChatColor.GREEN.toString() + ChatColor.BOLD), getMMComponent("<rainbow>" + PROJECT_NAME), Component.text("\n" + ChatColor.RESET + ChatColor.GREEN + "McSurvivalprojekt.de")),
                Component.text(ChatColor.AQUA + "Online: " + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + " | " + ChatColor.AQUA + "TPS: " + Math.round(Main.getPlugin().getServer().getTPS()[0]))
        );
    }

    public boolean isEndAccessible() {
        if (!getConfig().contains("config.endaccessible")) {
            getConfig().set("config.endaccessible", false);
            saveConfig();
        }
        return getConfig().getBoolean("config.endaccessible");
    }

    public void setEndAccessible(boolean accessible) {
        getConfig().set("config.endaccessible", accessible);
        saveConfig();
    }

    public static Component getMMComponent(String message) {
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
        if (isFeatureDisabled("eastereggDamage")) return;
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Player jonbadon = Bukkit.getPlayer("Jonbadon");
            if (jonbadon != null && jonbadon.getLocation().getBlock().getType() == Material.STONECUTTER && !isFeatureDisabled("eastereggDamage.jonbadon"))
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
