package wtf.melonthedev.projectplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.Keybinds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.melonthedev.projectplugin.commands.*;
import wtf.melonthedev.projectplugin.listeners.*;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static HashMap<Player, Location> locations = new HashMap<>();
    public static HashMap<Player, Location> deathlocations = new HashMap<>();
    public static HashMap<Player, Boolean> spawnElytraPlayers = new HashMap<>();
    public static List<HashMap<String, HashMap<Material, Integer>>> collectedValuables = new ArrayList<>();
    private final Component[] infos = new Component[] {
            Component.text(ChatColor.GRAY + "Drücke ").append(Component.keybind("key.sneak")).append(Component.text(ChatColor.GRAY + " um von dieser Insel zu gleiten")),
            Component.text(ChatColor.GRAY + "Chatte mit " + ChatColor.WHITE + "<rainbow>" + ChatColor.GRAY + " um RGB zu schreiben"),
            Component.text(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/status" + ChatColor.GRAY + " um einen Status zu setzen"),
            Component.text(ChatColor.GRAY + "Rechtsklicke sneakend ein " + ChatColor.WHITE + "Schild" + ChatColor.GRAY + " um den Inhalt zu bearbeiten"),
            Component.text(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/r" + ChatColor.GRAY + " um auf eine Privatnachricht zu antworten"),
            Component.text(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/msg" + ChatColor.GRAY + " um Nachrichten sogar an offline Spieler zu senden"),
    };


    @Override
    public void onEnable() {
        plugin = this;
        getLogger().log(Level.INFO, "**********************");
        getLogger().log(Level.INFO, "*** Project Plugin ***");
        getLogger().log(Level.INFO, "*** by Melonthedev ***");
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
        getServer().getPluginManager().registerEvents(new ActionLoggerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        sendSpawnMessage();
        updateTabList();
        stoneCutterDamage();
        handleSusPlayerActivityPerHour();
    }

    @Override
    public void onDisable() {
        savePlayerActivity();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playerListName(Component.text(player.getName()));
            player.displayName(Component.text(player.getName()));
        }
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
                Component.join(JoinConfiguration.noSeparators(), Component.text(ChatColor.GREEN.toString() + ChatColor.BOLD), getMiniMessageComponent("<rainbow>Survivalprojekt 4.0"), Component.text("\n" + ChatColor.RESET + ChatColor.GREEN + "McSurvivalprojekt.de")),
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

    public void stoneCutterDamage() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Player jonbadon = Bukkit.getPlayer("Jonbadon");
            if (jonbadon != null && jonbadon.getLocation().getBlock().getType() == Material.STONECUTTER)
                jonbadon.damage(1);
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
