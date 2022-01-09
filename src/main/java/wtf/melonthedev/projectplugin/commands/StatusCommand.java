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
        String status = ChatColor.translateAlternateColorCodes('&', args[0]);
        if (status.length() > 30) {
            sender.sendMessage(ChatColor.RED + "Dein Status darf nicht länger als 30 Zeichen sein.");
            return true;
        }
        player.setDisplayName("[" + status + ChatColor.RESET + "] " + player.getName());
        player.setPlayerListName("[" + status + ChatColor.RESET + "] " + player.getName());
        ServerPlayer handle = ((CraftPlayer) player).getHandle();
        handle.connection.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, handle));
        handle.listName = Component.nullToEmpty("[" + status + ChatColor.RESET + "] " + player.getName());
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            handle.connection.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, handle));
            handle.connection.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, handle));
        }, 5);
        player.sendMessage(ChatColor.GREEN + "[Status] Dein Status ist nun '" + status + ChatColor.GREEN + "'.");
        return true;
    }
}
