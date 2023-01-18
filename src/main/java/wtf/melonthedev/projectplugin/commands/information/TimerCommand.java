package wtf.melonthedev.projectplugin.commands.information;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.modules.TimerSystem;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimerCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have the permissions to use this command.");
            return true;
        }
        // /timer start/stop/pause/resume/color/showtoall/showtoplayer

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "start" -> {
                    TimerSystem.startTimer();
                    TimerSystem.showToAll();
                }
                case "stop" -> TimerSystem.stopTimer();
                case "pause" -> TimerSystem.pauseTimer();
                case "resume" -> TimerSystem.resumeTimer();
                case "showtoall" -> TimerSystem.showToAll();
            }
            sender.sendMessage(ChatColor.GREEN + "Success!");
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "showtoplayer" -> {
                    OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Player was not found!");
                        return true;
                    }
                    TimerSystem.showToPlayer(player.getUniqueId());
                }
                case "autoshow" -> {
                    try {
                        boolean autoshow = Boolean.parseBoolean(args[1]);
                        TimerSystem.setAutoShow(autoshow);
                        if (autoshow) sender.sendMessage(ChatColor.GREEN + "Timer now gets shown to new joined players!");
                        else sender.sendMessage(ChatColor.GREEN + "Timer no longer gets shown to new joined players! Add them manually by doing '/timer showToPlayer <Player>'");
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + "Please use 'true' or 'false'");
                    }
                }
                case "prefix" -> {
                    //try {
                    //    ChatColor color = ChatColor.valueOf(args[1].toUpperCase());
                    //    TimerSystem.setColor(color);
                    //    sender.sendMessage(ChatColor.GREEN + "Success! The color of the timer is now set to " + args[1].toUpperCase() + "!");
                    //} catch (IllegalArgumentException e) {
                    //    sender.sendMessage(ChatColor.RED + "Please use a valid color from the tabcomplete!");
                    //}
                    TimerSystem.setPrefix(args[1]);
                    sender.sendMessage(ChatColor.GREEN + "Success! The prefix of the timer is now set to '" + args[1] + ChatColor.GREEN + "'!");
                }
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("start");
            tab.add("stop");
            tab.add("pause");
            tab.add("resume");
            tab.add("showToAll");
            tab.add("showToPlayer");
            tab.add("autoShow");
            tab.add("prefix");
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                //case "color" -> tab.addAll(Arrays.stream(ChatColor.values()).map(Enum::name).collect(Collectors.toList()));
                case "showtoplayer" -> CommandUtils.addOnlinePlayers(tab, args[1]);
                case "autoshow" -> {
                    tab.add("true");
                    tab.add("false");
                }
            }
        }
        return tab;
    }
}
