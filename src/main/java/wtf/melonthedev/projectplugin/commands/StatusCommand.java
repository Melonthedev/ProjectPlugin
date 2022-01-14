package wtf.melonthedev.projectplugin.commands;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

public class StatusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry, you cannot use this command in the console.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /status <String: status>");
            return true;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("reset")) {
            player.sendMessage(ChatColor.GREEN + "[Status] Dein Status wurde zurück gesetzt!");
            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getName());
            return true;
        }
        String status = ChatColor.translateAlternateColorCodes('&', args[0]);
        if (status.length() > 30) {
            sender.sendMessage(ChatColor.RED + "Dein Status darf nicht länger als 30 Zeichen sein.");
            return true;
        }
        player.setDisplayName("[" + status + ChatColor.RESET + "] " + player.getName());
        player.setPlayerListName("[" + status + ChatColor.RESET + "] " + player.getName());
        player.sendMessage(ChatColor.GREEN + "[Status] Dein Status ist nun '" + status + ChatColor.GREEN + "'.");
        return true;
    }
}
