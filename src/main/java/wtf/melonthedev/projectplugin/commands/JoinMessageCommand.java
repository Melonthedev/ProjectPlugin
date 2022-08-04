package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;

import java.util.Arrays;

public class JoinMessageCommand implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /joinmessage <player> <msg>");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command.");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
            return true;
        }
        Player player = (Player) sender;
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(ChatColor.RED + "You cannot send a joinmessage to " + args[0]);
            return true;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : Arrays.copyOfRange(args, 1, args.length))
            builder.append(s).append(" ");

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.author(player.name());
        bookMeta.title(Component.text("Admin Info to " + target.getName()));
        bookMeta.setGeneration(BookMeta.Generation.ORIGINAL);
        bookMeta.addPages(Component.text(builder.toString()));

        book.setItemMeta(bookMeta);
        Main.joinMessages.put(target.getUniqueId(), book);
        return false;
    }


    public static void handleJoinMessage(Player player) {
        if (!Main.joinMessages.containsKey(player))
            return;
        player.openBook(Main.joinMessages.get(player));
    }
}
