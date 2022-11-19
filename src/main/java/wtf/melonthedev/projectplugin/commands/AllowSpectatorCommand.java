package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;

public class AllowSpectatorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false)) {
            sender.sendMessage(ChatColor.GREEN + "Hardcore mode is not enabled so you cannot use this command!");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /allowspectator <player>");
            return true;
        }
        OfflinePlayer target = Main.getPlugin().getServer().getOfflinePlayerIfCached(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' was not found!");
            return true;
        }




        return false;
    }

}
