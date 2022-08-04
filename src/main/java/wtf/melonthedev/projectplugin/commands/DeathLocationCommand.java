package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

public class DeathLocationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Main.deathlocations.containsKey(player)) {
                Location loc = Main.deathlocations.get(player);
                player.sendMessage(loc.getX() + " " + loc.getY() + " " + loc.getZ());
            } else
                player.sendMessage("Sorry, nur für Notfälle ;)");
        }
        return false;
    }
}
