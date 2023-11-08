package wtf.melonthedev.projectplugin.commands.moderation;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.*;

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

        if (args.length == 1 && sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(target.getInventory());
            return true;
        }

        if (sender instanceof Player player && args[1].equalsIgnoreCase("ec")) {
            player.openInventory(target.getEnderChest());
        } else if (sender instanceof Player player && args[1].equalsIgnoreCase("armor")) {
            Inventory inv = Bukkit.createInventory(null, 9, Component.text("Armor of " + target.getName()));
            Arrays.stream(target.getInventory().getArmorContents()).filter(Objects::nonNull).toList().forEach(inv::addItem);
            Arrays.stream(target.getInventory().getExtraContents()).filter(Objects::nonNull).toList().forEach(inv::addItem);
            player.openInventory(inv);
        } else if (args[1].equalsIgnoreCase("text") || !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.GOLD + "---- Inventory of " + target.getName() + " -----");
            sender.sendMessage(ChatColor.AQUA + "Armor:");
            printTextInv(target.getInventory().getArmorContents(), sender, ChatColor.YELLOW);
            sender.sendMessage(ChatColor.BLUE + "Extra:");
            printTextInv(target.getInventory().getExtraContents(), sender, ChatColor.BLUE);
            sender.sendMessage(ChatColor.GREEN + "Storage:");
            printTextInv(target.getInventory().getStorageContents(), sender, ChatColor.GREEN);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "EnderChest:");
            printTextInv(target.getEnderChest().getStorageContents(), sender, ChatColor.LIGHT_PURPLE);
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

    public void printTextInv(ItemStack[] stacks, CommandSender sender, ChatColor color) {
        for (ItemStack stack : stacks) {
            if (stack == null) continue;
            sender.sendMessage(color + "- " + ChatColor.YELLOW + stack.getAmount() + "x " + color + StringUtils.capitalize(stack.getType().getKey().asString().replaceAll("minecraft:", "").toLowerCase().replaceAll("_", " ")) + " " + getEnchantmentsString(stack.getEnchantments()));
            appendShulkerInfos(stack, sender);
        }
    }

    public void appendShulkerInfos(ItemStack stack, CommandSender sender) {
        if (!(stack.getItemMeta() instanceof BlockStateMeta)
                || !(((BlockStateMeta)stack.getItemMeta()).getBlockState() instanceof ShulkerBox))
            return;
        Inventory shulkerInv = ((ShulkerBox)((BlockStateMeta) stack.getItemMeta()).getBlockState()).getInventory();
        if (shulkerInv.isEmpty()) return;
        sender.sendMessage(ChatColor.DARK_PURPLE + "-- Contents of shulker:");
        printTextInv(shulkerInv.getContents(), sender, ChatColor.DARK_PURPLE);
        sender.sendMessage(ChatColor.DARK_PURPLE + "--");

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            CommandUtils.addOnlinePlayersTabComplete(tab, args[0]);
        } else if (args.length == 2) {
            tab.add("text");
            tab.add("ec");
            tab.add("armor");
        }
        return tab;
    }
}
