package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.List;

public class HardCoreCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final String prefix = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "[Hard" + ChatColor.WHITE + ChatColor.BOLD + "Core] " + ChatColor.RESET;
        if (!sender.isOp()) {
            sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to use this command!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(prefix + ChatColor.RED + "Syntax: /hardcore <toggleHardcoreMode|toggleGiantDeathTitle|togglePvpCooldown|startPvpCooldown>");
            return true;
        }
        if (args.length == 1) {
            switch (args[0]) {
                case "toggleHardcoreMode":
                    Main.getPlugin().getConfig().set("hardcore.enabled", !Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false));
                    Main.getPlugin().saveConfig();
                    Main.getPlugin().handleHardcoreModus();
                    sender.sendMessage(prefix + ChatColor.RED + "You have " + (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) ? "enabled" : "disabled") + " the hardcore mode!");
                    return true;
                case "toggleGiantDeathTitle":
                    Main.getPlugin().getConfig().set("hardcore.giantDeathTitle", !Main.getPlugin().getConfig().getBoolean("hardcore.giantDeathTitle", false));
                    Main.getPlugin().saveConfig();
                    sender.sendMessage(prefix + ChatColor.RED + "Giant Title is now set to " + Main.getPlugin().getConfig().getBoolean("hardcore.giantDeathTitle", false));
                    return true;
                case "togglePvpCooldown":
                    Main.getPlugin().getConfig().set("hardcore.pvpCooldown", !Main.getPlugin().getConfig().getBoolean("hardcore.pvpCooldown", false));
                    Main.getPlugin().saveConfig();
                    Main.getPlugin().handlePvpCooldown(false);
                    sender.sendMessage(prefix + ChatColor.RED + "PvP Cooldown is now set to " + Main.getPlugin().getConfig().getBoolean("hardcore.pvpCooldown", false));
                    return true;
                case "startPvpCooldown":
                    Main.getPlugin().getConfig().set("hardcore.pvpCooldown", true);
                    Main.getPlugin().saveConfig();
                    Main.getPlugin().handlePvpCooldown(true);
                    sender.sendMessage(prefix + ChatColor.RED + "You started the PvP Cooldown!");
                    return true;
                case "stopPvpCooldown":
                    Main.getPlugin().getConfig().set("hardcore.pvpCooldown", false);
                    Main.getPlugin().saveConfig();
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.hideBossBar(Main.getPlugin().bar);
                        p.getWorld().setPVP(true);
                        p.showTitle(Title.title(Component.text(ChatColor.RED + "PvP is now enabled!"), Component.empty()));
                    });
                    Main.getPlugin().runnable.cancel();
                    sender.sendMessage(prefix + ChatColor.RED + "You stopped the PvP Cooldown!");
                    return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("revivePlayer")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(prefix + ChatColor.RED + "This player was not found!");
                    return true;
                }
                target.setGameMode(GameMode.SURVIVAL);
                target.setHealth(20);
                target.setFoodLevel(20);
                target.setHealthScale(20);
                target.setSilent(false);
                target.setAffectsSpawning(true);
                target.setInvulnerable(false);
                target.setCollidable(true);
                target.setSleepingIgnored(false);
                target.playerListName(target.name());
                target.displayName(target.name());
                target.sendMessage(prefix + ChatColor.GREEN + "You have been revived!");
                sender.sendMessage(prefix + ChatColor.RED + "You revived " + target.getName() + "!");
                return true;
            }
        } else sender.sendMessage(prefix + ChatColor.RED + "Syntax: /hardcore <toggleHardcoreMode|toggleGiantDeathTitle|togglePvpCooldown|startPvpCooldown>");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("toggleGiantDeathTitle");
            tab.add("togglePvpCooldown");
            tab.add("startPvpCooldown");
            tab.add("toggleHardcoreMode");
            tab.add("revivePlayer");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("revivePlayer"))
                Bukkit.getOnlinePlayers().forEach(p -> tab.add(p.getName()));
        }
        return tab;
    }
}
