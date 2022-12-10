package wtf.melonthedev.projectplugin.commands.moderation;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ISeeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Main.getMMComponent("<rainbow><bold>ISEE " + (args.length > 0 ? args[0] : "YOU")));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /isee <player> <text/armor/ec>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player is offline.");
            return true;
        }
        if (sender instanceof Player && args[1].equalsIgnoreCase("ec")) {
            Player player = (Player) sender;
            player.openInventory(target.getEnderChest());
        } else if (sender instanceof Player && args[1].equalsIgnoreCase("armor")) {
            Player player = (Player) sender;
            Inventory inv = Bukkit.createInventory(null, 27, Component.text("Armor of " + target.getName()));
            Arrays.stream(target.getInventory().getArmorContents()).filter(Objects::nonNull).collect(Collectors.toList()).forEach(inv::addItem);
            Arrays.stream(target.getInventory().getExtraContents()).filter(Objects::nonNull).collect(Collectors.toList()).forEach(inv::addItem);
            player.openInventory(inv);
        } else if (sender instanceof Player && !args[1].equalsIgnoreCase("text")) {
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
