package wtf.melonthedev.projectplugin.commands.moderation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VelocityCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /velocity <player> <velocityX> <velocityY> <velocityZ>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not online.");
            return true;
        }
        try {
            float velocityX = Float.parseFloat(args[1]);
            float velocityY = Float.parseFloat(args[2]);
            float velocityZ = Float.parseFloat(args[3]);
            target.setVelocity(target.getVelocity().setX(velocityX).setY(velocityY).setZ(velocityZ));
            sender.sendMessage(ChatColor.GREEN + "Success!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /velocity <player> <velocityX> <velocityY> <velocityZ>");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
