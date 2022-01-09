package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import wtf.melonthedev.projectplugin.Main;

public class ToggleEndAccessibilityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Sorry, DU kannst leider nicht das End aktivieren. :(");
            return true;
        }
        if (Main.getPlugin().isEndAccessible()) {
            sender.sendMessage(ChatColor.RED + "END WURDE BLOCKIERT!");
            Main.getPlugin().setEndAccessible(false);
        } else {
            sender.sendMessage(ChatColor.GREEN + "END WURDE GEÖFFNET!");
            Main.getPlugin().setEndAccessible(true);
        }
        return false;
    }
}
