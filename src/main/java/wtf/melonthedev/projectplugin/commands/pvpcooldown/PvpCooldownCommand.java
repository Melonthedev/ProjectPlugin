package wtf.melonthedev.projectplugin.commands.pvpcooldown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

import java.util.ArrayList;
import java.util.List;

public class PvpCooldownCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final String prefix = ChatColor.BLUE + ChatColor.BOLD.toString() + "[Pvp" + ChatColor.WHITE + ChatColor.BOLD + "Cooldown] " + ChatColor.RESET;
        if (!sender.isOp()) {
            sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to use this command!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /pvpCooldown <start | startForAll | stop | stopForAll>");
            sender.sendMessage(prefix + ChatColor.RED + "Example: /pvpCooldown start " + ChatColor.ITALIC + "<Player> <Time in Min>");
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("start")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(prefix + ChatColor.RED + "This target was not found.");
                return true;
            }
            try {
                int minutes = Integer.parseInt(args[2]);
                if (minutes <= 0) {
                    sender.sendMessage(prefix + "Minutes must be above 0!");
                    return true;
                }
                PvpCooldownSystem.startForPlayer(target.getUniqueId(), minutes);
                sender.sendMessage(prefix + ChatColor.GREEN + "Success! Started PvP Cooldown of " + target.getName() + " for " + minutes + " Minutes.");
            } catch (NumberFormatException e) {
                sender.sendMessage(prefix + ChatColor.RED + "Syntax: /pvpCooldown <start | startForAll | stop | stopForAll>");
                sender.sendMessage(prefix + ChatColor.RED + "Example: /pvpCooldown start " + ChatColor.ITALIC + "<Player> <Time in Min>");
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("startForAll")) {
                try {
                    int minutes = Integer.parseInt(args[1]);
                    if (minutes <= 0) {
                        sender.sendMessage(prefix + "Minutes must be above 0!");
                        return true;
                    }
                    Bukkit.getOnlinePlayers().forEach(player -> PvpCooldownSystem.startForPlayer(player.getUniqueId(), minutes));
                    sender.sendMessage(prefix + ChatColor.GREEN + "Success! Started PvP Cooldown of all players for " + minutes + " Minutes.");
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefix + ChatColor.RED + "Syntax: /pvpCooldown <start | startForAll | stop | stopForAll>");
                    sender.sendMessage(prefix + ChatColor.RED + "Example: /pvpCooldown start " + ChatColor.ITALIC + "<Player> <Time in Min>");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(prefix + ChatColor.RED + "This player was not found.");
                    return true;
                }
                PvpCooldownSystem.disableForPlayer(target.getUniqueId());
                sender.sendMessage(prefix + ChatColor.GREEN + "Success! Stopped PvP Cooldown for " + target.getName());
            } else {
                sender.sendMessage(prefix + ChatColor.RED + "Syntax: /pvpCooldown <start | startForAll | stop | stopForAll>");
                sender.sendMessage(prefix + ChatColor.RED + "Example: /pvpCooldown start " + ChatColor.ITALIC + "<Player> <Time in Min>");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("stopForAll")) {
            PvpCooldownSystem.disableForAllPlayers();
            sender.sendMessage(prefix + ChatColor.GREEN + "Success! Stopped PvP Cooldown for all players.");
        } else {
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /pvpCooldown <start | startForAll | stop | stopForAll>");
            sender.sendMessage(prefix + ChatColor.RED + "Example: /pvpCooldown start " + ChatColor.ITALIC + "<Player> <Time in Min>");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("start");
            tab.add("startForAll");
            tab.add("stop");
            tab.add("stopForAll");
        } else if (args.length == 2) {
            Bukkit.getOnlinePlayers().forEach(p -> tab.add(p.getName()));
        }
        return tab;
    }
}
