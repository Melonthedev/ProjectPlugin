package wtf.melonthedev.projectplugin.commands.moderation;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.util.*;
import java.util.stream.Collectors;

public class ISeeCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Main.getMMComponent("<rainbow><bold>ISEE " + (args.length > 0 ? args[0] : "YOU")));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /isee <player> (<text/armor/ec>)");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player is offline.");
            return true;
        }

        if (args.length == 1) {
            Player player = (Player) sender;
            player.openInventory(target.getInventory());
            return true;
        }

        if (sender instanceof Player player && args[1].equalsIgnoreCase("ec")) {
            player.openInventory(target.getEnderChest());
        } else if (sender instanceof Player player && args[1].equalsIgnoreCase("armor")) {
            Inventory inv = Bukkit.createInventory(null, 27, Component.text("Armor of " + target.getName()));
            Arrays.stream(target.getInventory().getArmorContents()).filter(Objects::nonNull).toList().forEach(inv::addItem);
            Arrays.stream(target.getInventory().getExtraContents()).filter(Objects::nonNull).toList().forEach(inv::addItem);
            player.openInventory(inv);
        } else if (args[1].equalsIgnoreCase("text")) {
            sender.sendMessage(ChatColor.GOLD + "---- Inventory of " + target.getName() + " -----");
            sender.sendMessage(ChatColor.AQUA + "Armor:");
            for (ItemStack stack : target.getInventory().getArmorContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.AQUA + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.AQUA + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
            }
            sender.sendMessage(ChatColor.BLUE + "Extra:");
            for (ItemStack stack : target.getInventory().getExtraContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.BLUE + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.BLUE + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
            }
            sender.sendMessage(ChatColor.GREEN + "Storage:");
            for (ItemStack stack : target.getInventory().getStorageContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.GREEN + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "EnderChest:");
            for (ItemStack stack : target.getEnderChest().getStorageContents()) {
                if (stack == null) continue;
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + ChatColor.LIGHT_PURPLE + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
            }
            sender.sendMessage(ChatColor.GOLD + "----------------------------------");
        }
        return false;
    }


    public String getEnchantmentsString(Map<Enchantment, Integer> list) {
        if (list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("(");
        list.forEach((enchantment, integer) -> sb.append(StringUtils.capitalize(enchantment.getKey().asString().toLowerCase().replaceAll("minecraft:", ""))).append(" ").append(integer).append(", "));
        return sb.substring(0, sb.length() - 2) + ")";
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length == 2) {
            tab.add("text");
            tab.add("ec");
            tab.add("armor");
        }
        return tab;
    }
}
