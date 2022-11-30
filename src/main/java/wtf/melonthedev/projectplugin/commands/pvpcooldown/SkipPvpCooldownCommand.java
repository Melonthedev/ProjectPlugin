package wtf.melonthedev.projectplugin.commands.pvpcooldown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

import java.util.List;

public class SkipPvpCooldownCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!PvpCooldownSystem.pvpCooldowns.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You don't have a pvp cooldown!");
            return true;
        }
        PvpCooldownSystem.pvpCooldowns.get(player.getUniqueId()).disable();
        player.sendMessage(ChatColor.GREEN + "You skipped your pvp cooldown!");
        player.sendMessage(ChatColor.GREEN + "You cannot undo this action!");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
