package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;

public class ISeeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Main.getPlugin().getMiniMessageComponent("<rainbow><bold>ISEE YOU"));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /isee <player> <textbased: true/false>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player is offline.");
            return true;
        }
        if (sender instanceof Player && !args[1].equalsIgnoreCase("true")) {
            Player player = (Player) sender;
            player.openInventory(target.getInventory());
        } else {
            sender.sendMessage(ChatColor.AQUA + "---- Inventory of " + target.getName() + " -----");


            sender.sendMessage(ChatColor.AQUA + "Armor:");
            for (ItemStack stack : target.getInventory().getArmorContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.AQUA + "- " + stack.getType() + " (" + stack.getEnchantments() + ")");
            }
            sender.sendMessage(ChatColor.AQUA + "Extra:");
            for (ItemStack stack : target.getInventory().getExtraContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.AQUA + "- " + stack.getType() + " " + stack.getAmount() + "x (" + stack.getEnchantments() + ")");
            }
            sender.sendMessage(ChatColor.AQUA + "Storage:");
            for (ItemStack stack : target.getInventory().getStorageContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.AQUA + "- " + stack.getType() + " " + stack.getAmount() + "x (" + stack.getEnchantments() + ")");
            }
        }

        return false;
    }
}
