package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

public class DeathLocationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (Main.isFeatureDisabled("deathLocationCommand")) {
            player.sendMessage(ChatColor.RED + "This command is disabled for the current project.");
            return true;
        }
        if (player.getLastDeathLocation() == null) {
            player.sendMessage(ChatColor.RED + "Sorry, there is no saved deathlocation!");
            return true;
        }
        Location loc = player.getLastDeathLocation();
        player.sendMessage(ChatColor.GOLD + "You died at X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ());
        return false;
    }
}
