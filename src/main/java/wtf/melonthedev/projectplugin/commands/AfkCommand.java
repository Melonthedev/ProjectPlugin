package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.AfkSystem;

public class AfkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        if (player.getGameMode() == GameMode.SPECTATOR && Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) {
            player.sendMessage(ChatColor.RED + "You can't use this command in spectator mode!");
            return true;
        }
        if (AfkSystem.isAfk(player)) AfkSystem.disableAfkMode(player);
        else AfkSystem.enableAfkModus(player);
        return false;
    }
}
