package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SurvivalprojektCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.getPlugin().getMiniMessageComponent("<gold>You are playing on </gold><rainbow>" + Main.PROJECT_NAME + "</rainbow><gold>!"));
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Discord: " + Main.DISCORD_INVITE);
            sender.sendMessage(ChatColor.GREEN + "Website: McSurvivalprojekt.de");
            sender.sendMessage(ChatColor.AQUA + "Developer: Melonthedev#1848");
            sender.sendMessage(ChatColor.YELLOW + "Admin: stebadon#1189");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have the permission to use this part of the command! Try it without arguments ;)");
            return true;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "startProject":
                    sender.sendMessage(ChatColor.GREEN + "Starting Project...");
                    Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Component.text(ChatColor.GREEN + "Starting in 10..."), Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(3000), Duration.ofMillis(1000)))));
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Component.text(ChatColor.GREEN + "Starting in 9..."), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Component.text(ChatColor.GREEN + "Starting in 8..."), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Component.text(ChatColor.GREEN + "Starting in 7..."), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 2);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Component.text(ChatColor.GREEN + "Starting in 6..."), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 3);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + "5"), Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 4);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + "4"), Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 5);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + "3"), Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 6);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + "2"), Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 7);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(ChatColor.RED + "1"), Main.getPlugin().getMiniMessageComponent("<rainbow>" + Main.PROJECT_NAME + "</rainbow>"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(500))))), 80 + 40 * 8);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                        Main.getPlugin().handlePvpCooldown(true);
                        sender.sendMessage(ChatColor.GREEN + "Started PvP Cooldown!");
                        Bukkit.getWorlds().get(0).getWorldBorder().setWarningDistance(5);
                        Bukkit.getWorlds().get(0).getWorldBorder().setSize(59999968);
                        sender.sendMessage(ChatColor.GREEN + "Reset Worldborder!");
                        sender.sendMessage(ChatColor.GREEN + "Done!");
                    }, 80 + 40 * 9);
                    break;
                case "setupProject":
                    Bukkit.getWorlds().get(0).getWorldBorder().setCenter(Bukkit.getWorlds().get(0).getSpawnLocation());
                    Bukkit.getWorlds().get(0).getWorldBorder().setWarningDistance(0);
                    Bukkit.getWorlds().get(0).getWorldBorder().setSize(16);
                    sender.sendMessage(ChatColor.GREEN + "Worldborder set to 16 blocks!");
                    sender.sendMessage(ChatColor.GREEN + "Done!");
                    break;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("startProject");
            tab.add("setupProject");
        }
        return tab;
    }
}
