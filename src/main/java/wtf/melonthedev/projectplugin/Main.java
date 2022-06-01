package wtf.melonthedev.projectplugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.Keybinds;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import wtf.melonthedev.projectplugin.commands.*;
import wtf.melonthedev.projectplugin.listeners.*;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static HashMap<Player, Location> locations = new HashMap<>();
    private final BaseComponent[][] infos = new BaseComponent[][] {
        new ComponentBuilder(ChatColor.GRAY + "Drücke ")
                .append(new KeybindComponent(Keybinds.SNEAK))
                .append(ChatColor.GRAY + " um diese Insel herunter zu gleiten")
                .create(),
        new ComponentBuilder(ChatColor.GRAY + "Benutze" + ChatColor.WHITE + " /position " + ChatColor.GRAY + "um dir Positionen zu speichern").create(),
        new ComponentBuilder(ChatColor.GRAY + "Chatte mit " + ChatColor.WHITE + "&" + ChatColor.GRAY + " um die Schriftfarbe zu ändern").create(),
        new ComponentBuilder(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/status" + ChatColor.GRAY + " um einen Status zu setzen").create(),
        new ComponentBuilder(ChatColor.GRAY + "Rechtsklicke sneakend ein " + ChatColor.WHITE + "Schild" + ChatColor.GRAY + " um den Inhalt zu bearbeiten").create(),
        new ComponentBuilder(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/kopfgeld" + ChatColor.GRAY + " um das Kopfgeldmenü zu öffnen").create(),
        new ComponentBuilder(ChatColor.GRAY + "Rechtsklicke sneakend einen " + ChatColor.WHITE + "Armorstand" + ChatColor.GRAY + " um ihn zu bearbeiten").create(),
        new ComponentBuilder(ChatColor.GRAY + "Benutze " + ChatColor.WHITE + "/r" + ChatColor.GRAY + " um auf eine privatnachricht zu antworten").create(),
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
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new ActionLoggerListener(), this);

        sendSpawnMessage();
        updateTabList();
    }

    @Override
    public void onDisable() {

    }

    public void sendSpawnMessage() {
        AtomicInteger i = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!LocationUtils.isLocationInSpawnArea(player.getLocation())) continue;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, infos[i.get()]);
            }
            i.getAndIncrement();
            if (i.get() >= infos.length) i.set(0);
        }, 0, 80);
    }

    public void updateTabList() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) player.setPlayerListHeaderFooter(
                    ChatColor.GREEN.toString() + ChatColor.BOLD + "Survivalprojekt 4.0\n" + ChatColor.RESET + ChatColor.GRAY + "McSurvivalprojekt.de",
                    ChatColor.AQUA + "Online: " + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + " | " + ChatColor.AQUA + "TPS: " + (int) MinecraftServer.getServer().recentTps[0]
                );
        }, 0, 80);
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

    public static Main getPlugin() {
        return plugin;
    }
}
