package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.modules.PvpCooldownSystem;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SurvivalprojektCommand implements TabExecutor {

    public CommandSender sender;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || !sender.isOp()) {
            sender.sendMessage(Main.getMMComponent("<gold>You are playing on </gold><rainbow>" + Main.PROJECT_NAME + " " + (Main.getPlugin().getConfig().getBoolean("showProjectType", false) ? Main.PROJECT_TYPE : "") + "</rainbow><gold>!"));
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Discord: " + Main.DISCORD_INVITE);
            sender.sendMessage(ChatColor.GREEN + "Website: McSurvivalprojekt.de");
            sender.sendMessage(ChatColor.AQUA + "Developer: Melonthedev#1848");
            sender.sendMessage(ChatColor.AQUA + "Co-Developer: stebadon#1189");
            sender.sendMessage(ChatColor.YELLOW + "Admin: stebadon#1189");
            return true;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "startProject" -> {
                    this.sender = sender;
                    sender.sendMessage(ChatColor.GREEN + "Starting Project...");
                    Main.getPlugin().getConfig().set("projectActive", false);
                    Main.getPlugin().saveConfig();
                    runStartAction();
                }
                case "setupProject" -> {
                    Main.getPlugin().getConfig().set("projectActive", false);
                    Main.getPlugin().saveConfig();
                    Bukkit.getWorlds().get(0).getWorldBorder().setCenter(Bukkit.getWorlds().get(0).getSpawnLocation());
                    Bukkit.getWorlds().get(0).getWorldBorder().setWarningDistance(0);
                    Bukkit.getWorlds().get(0).getWorldBorder().setSize(Main.getPlugin().getConfig().getInt("spawn.border_width", 16));
                    sender.sendMessage(ChatColor.GREEN + "Worldborder set to " + Main.getPlugin().getConfig().getInt("spawn.border_width", 16) + " blocks!");
                    Main.getPlugin().setEndAccessible(false);
                    sender.sendMessage(ChatColor.GREEN + "Blocked End Access!");
                    PvpCooldownSystem.resetAllPvpCooldowns();
                    sender.sendMessage(ChatColor.GREEN + "Reset PvP Cooldowns!");
                    sender.sendMessage(ChatColor.GREEN + "Done!");
                }
                case "stopProject" -> {
                    Main.getPlugin().getConfig().set("projectActive", false);
                    Main.getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Stopped Project!");
                    Bukkit.getWorlds().get(0).getWorldBorder().setSize(59999968);
                    sender.sendMessage(ChatColor.GREEN + "Reset Worldborder!");
                    Main.getPlugin().setEndAccessible(true);
                    sender.sendMessage(ChatColor.GREEN + "Unblocked End Access!");
                    sender.sendMessage(ChatColor.GREEN + "Done!");
                }
                case "startProjectInstantly" -> {
                    this.sender = sender;
                    runStartAction();
                }
            }
        }
        return false;
    }


    public String[] startActions = Main.getPlugin().getConfig().getStringList("startcommand").toArray(String[]::new);
    public int actionIndex = 0;

    public void runStartAction() {
        if (actionIndex >= startActions.length) {
            Main.getPlugin().getConfig().set("projectActive", true);
            Main.getPlugin().saveConfig();
            Bukkit.getOnlinePlayers().forEach(Audience::clearTitle);
            Main.getPlugin().saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Done!");
            return;
        }
        String action = startActions[actionIndex];
        actionIndex++;
        switch (action) {
            case "showProjectname" -> {
                Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getMMComponent("<rainbow>" + Main.PROJECT_NAME + (Main.getPlugin().getConfig().getBoolean("showProjectType", false) ? " " + Main.PROJECT_TYPE : "")), Component.empty(), Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(3000), Duration.ofMillis(1000)))));
                Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this::runStartAction, 5*20);
            }
            case "startCountdown" -> startCountdown();
            case "showExtra" -> showExtra();
            case "expandWorldborder" -> {
                Bukkit.getWorlds().get(0).getWorldBorder().setWarningDistance(5);
                Bukkit.getWorlds().get(0).getWorldBorder().setSize(Main.getPlugin().getConfig().getInt("worldborderSize"));
                sender.sendMessage(ChatColor.GREEN + "Worldborder set to " + Main.getPlugin().getConfig().getInt("worldborderSize") + " blocks!");
                runStartAction();
            }
            case "shrinkenWorldborder" -> {
                Bukkit.getWorlds().get(0).getWorldBorder().setCenter(Bukkit.getWorlds().get(0).getSpawnLocation());
                Bukkit.getWorlds().get(0).getWorldBorder().setWarningDistance(0);
                Bukkit.getWorlds().get(0).getWorldBorder().setSize(Main.getPlugin().getConfig().getInt("spawn.border_radius", 16));
                sender.sendMessage(ChatColor.GREEN + "Worldborder set to " + Main.getPlugin().getConfig().getInt("spawn_border_radius", 16) + " blocks!");
                runStartAction();
            }
            case "resetFood" -> {
                Bukkit.getOnlinePlayers().forEach(player -> player.setFoodLevel(20));
                sender.sendMessage(ChatColor.GREEN + "Reset Hunger!");
                runStartAction();
            }
            case "resetHealth" -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) == null) player.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                });
                sender.sendMessage(ChatColor.GREEN + "Reset Health!");
                runStartAction();
            }
            case "blockEndAccess" -> {
                Main.getPlugin().setEndAccessible(false);
                sender.sendMessage(ChatColor.GREEN + "Blocked End Access!");
                runStartAction();
            }
            case "resetTime" -> {
                Bukkit.getWorlds().get(0).setTime(0);
                sender.sendMessage(ChatColor.GREEN + "Reset Time!");
                runStartAction();
            }
            case "resetTotalWorldTime" -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setStatistic(Statistic.TOTAL_WORLD_TIME, 0);
                });
                sender.sendMessage(ChatColor.GREEN + "Reset Stats!");
                runStartAction();
            }
            case "resetPvpCooldowns" -> {
                PvpCooldownSystem.startForAllPlayers(Main.getPlugin().getConfig().getInt("pvpCooldownTime"));
                sender.sendMessage(ChatColor.GREEN + "Started PvP Cooldown!");
                runStartAction();
            }
        }
    }

    public void startCountdown(){
        int startValue = Main.getPlugin().getConfig().getInt("countdowntime");    // Startwert für den ersten Zahlenwert
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + "Startet in " + startValue + "..."), Main.getMMComponent("<rainbow>" + Main.PROJECT_NAME + (Main.getPlugin().getConfig().getBoolean("showProjectType", false) ? " " + Main.PROJECT_TYPE : "") + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))),40);
        for (int i = startValue-1, j = 0; i >= 0; i--, j++) {
            int count = i;
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + Integer.toString(count)), Main.getMMComponent("<rainbow>" + Main.PROJECT_NAME + (Main.getPlugin().getConfig().getBoolean("showProjectType", false) ? " " + Main.PROJECT_TYPE : "") + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500)))));
                if (count == 0) runStartAction();
            }, 80 + 40 * j);
        }
    }

    public void showExtra(){
        String extra = Main.getPlugin().getConfig().getString("extraText");
        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.empty(), Main.getMMComponent("<gold>" + extra + "</gold>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500)))));
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this::runStartAction, 2*20);
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("startProject");
            tab.add("setupProject");
            tab.add("stopProject");
        }
        return tab;
    }
}
