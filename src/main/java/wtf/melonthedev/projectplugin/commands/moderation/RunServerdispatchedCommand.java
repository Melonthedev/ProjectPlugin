package wtf.melonthedev.projectplugin.commands.moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class RunServerdispatchedCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) return true;
        StringBuilder builder = new StringBuilder();
        for (String str : Arrays.copyOfRange(args, 0, args.length))
            builder.append(str).append(" ");
        Bukkit.dispatchCommand(sender, builder.toString());
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Bukkit.getCommandMap().tabComplete(commandSender, args[args.length - 1]) != null ? Bukkit.getCommandMap().tabComplete(commandSender, args[args.length - 1]).stream().map(s1 -> s1.substring(1)).toList() : null;
    }
}
