package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

public class SpectateStebadonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.getName().equalsIgnoreCase("stebadon")) {
            sender.sendMessage(ChatColor.RED + "Du bist leider nicht dazu berechtigt, diesen Command zu verwenden!");
            return true;
        }
        if (Main.isFeatureDisabled("spectateStebadonEasteregg")) {
            sender.sendMessage(ChatColor.RED + "Dieser Command ist momentan nicht verfügbar! Ich weiß, traurig. Aber naja.");
            return true;
        }
        if (!(sender instanceof Player player)) return true;
        if (Bukkit.getPlayer("stebadon") == null) {
            sender.sendMessage(ChatColor.RED + "Hey ey sorry aber stebadon ist grad net online ¯\\_(ツ)_/¯");
            return true;
        }
        Player target = Bukkit.getPlayer("stebadon");
        if (Main.locations.containsKey(player) && Main.locations.get(player) != null) {
            player.setSpectatorTarget(null);
            player.teleport(Main.locations.get(player));
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.AQUA + "Du beobachtest nun nicht mehr den mit flying machine weg getragenen stebadon.");
            Main.locations.remove(player);
            return true;
        }
        Main.locations.put(player, player.getLocation());
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(target);
        runTeleportloop(player);
        return false;
    }


    public void runTeleportloop(Player player) {
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), () -> {
            if (!Main.locations.containsKey(player)) return;
            if (Main.isFeatureDisabled("spectateStebadonEasteregg")) return;
            if (player != null && player.getSpectatorTarget() == null) {
                player.setSpectatorTarget(null);
                player.teleport(Main.locations.get(player));
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.AQUA + "Du beobachtest nun nicht mehr den mit flying machine weg getragenen stebadon.");
                Main.locations.remove(player);
            }
        }, 60, 0);
    }
}
