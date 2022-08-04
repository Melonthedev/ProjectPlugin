package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.listeners.ActionLogger;

import java.util.*;

public class TempBanCommand implements TabExecutor {

    private Calendar expirationDate;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /tempban <player> (<reason>) (<time>)");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null || target.getName() == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        if (target.isBanned()) {
            sender.sendMessage(ChatColor.RED + "Player is already banned.");
            return true;
        }
        StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            if (isDurationArgument(args[i]))
                setDurationFromString(args[i]);
            else
                reason.append(args[i]).append(" ");
        }
        if (expirationDate == null) {
            expirationDate = Calendar.getInstance();
            expirationDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (target.isOnline() && target.getPlayer() != null)
            target.getPlayer().kick(Component.text("You are banned from Survivalprojekt.\n" + (reason.length() == 0 ? "" : "Reason: " + reason) + "\nExpires: " + expirationDate.toString()));
        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason.toString(), getDuration().getTime(), sender.getName());
        sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been banned for " + reason + ". Ban expires: " + getDuration() + ".");
        ActionLogger.logAction(target.getName(), "was banned", null, sender.getName(), reason.toString(), true);
        expirationDate = null;
        return false;
    }

    public boolean isDurationArgument(String arg) {
        return arg.matches("^[0-99]+[smhdw]$");
    }

    public void setDurationFromString(String str) {
        if (expirationDate == null) expirationDate = Calendar.getInstance();
        if (str.contains("s")) {
            int seconds = Integer.parseInt(str.replace("s", ""));
            expirationDate.add(Calendar.SECOND, seconds);
        } else if (str.contains("m")) {
            int minutes = Integer.parseInt(str.replace("m", ""));
            expirationDate.add(Calendar.MINUTE, minutes);
        } else if (str.contains("h")) {
            int hours = Integer.parseInt(str.replace("h", ""));
            expirationDate.add(Calendar.HOUR, hours);
        } else if (str.contains("d")) {
            int days = Integer.parseInt(str.replace("d", ""));
            expirationDate.add(Calendar.DAY_OF_MONTH, days);
        } else if (str.contains("w")) {
            int weeks = Integer.parseInt(str.replace("w", ""));
            expirationDate.add(Calendar.WEEK_OF_MONTH, weeks);
        }
    }

    public Calendar getDuration() {
        return expirationDate;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    tab.add(player.getName());
            }
        } else {
            if (isDurationArgument(args[args.length - 1]))
                tab.add(args[args.length - 1] + "s");
                tab.add(args[args.length - 1] + "m");
                tab.add(args[args.length - 1] + "h");
                tab.add(args[args.length - 1] + "d");
                tab.add(args[args.length - 1] + "w");
        }
        return tab;
    }
}
