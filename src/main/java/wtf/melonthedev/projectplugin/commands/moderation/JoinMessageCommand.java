package wtf.melonthedev.projectplugin.commands.moderation;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.*;

public class JoinMessageCommand implements TabExecutor {

    public static HashMap<UUID, ItemStack> joinMessages = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /joinmessage <player> <msg/reset>");
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command.");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(ChatColor.RED + "You cannot send a joinmessage to " + args[0]);
            return true;
        }
        if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            joinMessages.remove(target.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "You have removed the joinmessage from " + target.getName() + ".");
            return true;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : Arrays.copyOfRange(args, 1, args.length))
            builder.append(s).append(" ");
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setGeneration(BookMeta.Generation.ORIGINAL);
        bookMeta.addPages(Component.text(builder.toString()));
        book.setItemMeta(bookMeta.author(player.name()).title(Component.text("Admin Info to " + target.getName())));
        joinMessages.put(target.getUniqueId(), book);
        player.sendMessage(ChatColor.GREEN + "You have sent a joinmessage to " + target.getName() + ".");
        return false;
    }

    public static void handleJoinMessage(Player player) {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            if (!joinMessages.containsKey(player.getUniqueId()))
                return;
            player.openBook(joinMessages.get(player.getUniqueId()));
            joinMessages.remove(player.getUniqueId());
        }, 10);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1)
            CommandUtils.addOnlinePlayersTabComplete(tab, args[0]);
        else if (args.length == 2 && "reset".startsWith(args[1]))
            tab.add("reset");
        return tab;
    }
}
