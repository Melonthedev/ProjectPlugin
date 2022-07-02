package wtf.melonthedev.projectplugin.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BountyCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        if (args.length == 1) {

        }
        /*Player player = (Player) sender;
        switch (args.length) {
            case 0:
                openBountyInterface(player);
                break;
            case 1:
                if (args[0].equalsIgnoreCase("remove")) {
                    openRemoveBountyInterface(player);
                    return true;
                }
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    assert target != null;
                    /*if (target.getName().equals(player.getName())) {
                        player.sendMessage(ChatColor.RED + "You cannot bounty yourself.");
                        return true;
                    }*//*
                    openSetBountyInterface(player, target);
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Syntaxerror: /bounty remove/<player>/<>");
                break;
        }*/
        sender.sendMessage(ChatColor.AQUA + "Hi, some commands will either be implemented at some point of Survivalprojekt or not be implemented at all. For more infos ping me on discord.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("remove");
            for (Player player : sender.getServer().getOnlinePlayers()) {
                tab.add(player.getName());
            }
        }
        return tab;
    }

    public void openSetBountyInterface(Player player, Player target) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        assert bookMeta != null;
        BaseComponent builder = new ComponentBuilder(ChatColor.BOLD.toString() + ChatColor.AQUA + "Diamonds").create()[0];
        builder.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        builder.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounty set " + target.getName() + " diamond"));
        bookMeta.setTitle(ChatColor.BOLD.toString() + ChatColor.GOLD + "Bounty");
        bookMeta.setAuthor(player.getName());
        bookMeta.addPage(ChatColor.BOLD.toString() + ChatColor.GOLD + "Set Bounty on " + target.getName() + ChatColor.RESET + "\n\n" + "Choose the type of bounty: \n" + "");
        book.setItemMeta(bookMeta);
        player.openBook(book);
    }

    public void openRemoveBountyInterface(Player player) {

    }
    public void openBountyInterface(Player player) {

    }
}
