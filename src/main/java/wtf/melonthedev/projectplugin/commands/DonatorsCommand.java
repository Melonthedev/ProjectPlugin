package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonatorsCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command.");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GOLD + "Survivalprojekt Donators:");
            List<UUID> donators = getDonators();
            donators.forEach(uuid -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                sender.sendMessage(ChatColor.GOLD + "- " + (offlinePlayer.getName() == null ? uuid.toString() : offlinePlayer.getName()));
            });
            return true;
        } else if (args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player was not found!");
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "add" -> {
                    Main.getPlugin().getConfig().set("donators." + target.getUniqueId(), true);
                    Main.getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Success! " + target.getName() + " is now a donator!");
                    return true;
                }
                case "remove" -> {
                    Main.getPlugin().getConfig().set("donators." + target.getUniqueId(), null);
                    Main.getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Success! " + target.getName() + " is no longer a donator!");
                    return true;
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Syntaxerror: /donators <add/remove/list>");
        return false;
    }

    public static List<UUID> getDonators() {
        List<UUID> donators = new ArrayList<>();
        ConfigurationSection donatorsection = Main.getPlugin().getConfig().getConfigurationSection("donators");
        if (donatorsection == null) return donators;
        for (String donator : donatorsection.getKeys(false)) {
            UUID uuid;
            try {
                uuid = UUID.fromString(donator);
            } catch (IllegalArgumentException e) {
                continue;
            }
            donators.add(uuid);
        }
        return donators;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("list");
            tab.add("add");
            tab.add("remove");
        } else if (args.length == 2)
            tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        return tab;
    }
}
