package wtf.melonthedev.projectplugin.commands.pvpcooldown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

public class SkipPvpCooldownCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (Main.isFeatureDisabled("skippablePvpCooldown")) {
            player.sendMessage(ChatColor.RED + "Sorry, skipping the pvpcooldown was disabled for the current project!");
            return true;
        }
        if (!PvpCooldownSystem.pvpCooldowns.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You don't have a pvp cooldown!");
            return true;
        }
        PvpCooldownSystem.pvpCooldowns.get(player.getUniqueId()).disable();
        player.sendMessage(ChatColor.GREEN + "You skipped your pvp cooldown!");
        player.sendMessage(ChatColor.GREEN + "You cannot undo this action!");
        return false;
    }
}
