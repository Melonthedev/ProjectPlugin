package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;

import java.util.HashMap;
import java.util.List;

public class CheckSusPlayerActivityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /checksusplayeractivity <player>");
            return true;
        }
        if (!Main.getPlugin().getLatestPlayerActivityEntry().containsKey(args[0])) {
            sender.sendMessage(ChatColor.RED + "No data or invalid player!");
            return true;
        }
        sender.sendMessage(ChatColor.AQUA + "----- PlayerActivity of Player " + args[0] + ": ----- ");
        for (HashMap<String, HashMap<Material, Integer>> entry : Main.collectedValuables) {
           if (!entry.containsKey(args[0])) {
               sender.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "No Data");
           } else {
               StringBuilder stringBuilder = new StringBuilder();
               HashMap<Material, List<Integer>> minedValues = new HashMap<>();
               for (Material material : entry.get(args[0]).keySet()) {
                   int mined = entry.get(args[0]).get(material);
                   //if (!minedValues.containsKey(material))
                   //    minedValues.put(material, new ArrayList<>());
                   //  minedValues.get(material).add(mined);
                   ChatColor color = material == Material.DIAMOND_ORE || material == Material.DEEPSLATE_DIAMOND_ORE ? mined < 50 ? ChatColor.GREEN : mined < 100 ? ChatColor.YELLOW : ChatColor.RED
                           : material == Material.EMERALD_ORE || material == Material.DEEPSLATE_EMERALD_ORE ? mined < 50 ? ChatColor.GREEN : mined < 100 ? ChatColor.YELLOW : ChatColor.RED
                           : material == Material.IRON_ORE || material == Material.DEEPSLATE_IRON_ORE ? mined < 100 ? ChatColor.GREEN : mined < 150 ? ChatColor.YELLOW : ChatColor.RED
                           : material == Material.COAL_ORE || material == Material.DEEPSLATE_COAL_ORE ? mined < 150 ? ChatColor.GREEN : mined < 200 ? ChatColor.YELLOW : ChatColor.RED
                           : material == Material.GOLD_ORE || material == Material.DEEPSLATE_GOLD_ORE ? mined < 50 ? ChatColor.GREEN : mined < 100 ? ChatColor.YELLOW : ChatColor.RED
                           : material == Material.ANCIENT_DEBRIS ? mined < 50 ? ChatColor.GREEN : mined < 70 ? ChatColor.YELLOW : ChatColor.RED
                           : ChatColor.AQUA;
                   if (sender.isOp())
                       stringBuilder.append(ChatColor.AQUA).append(material.name()).append("/h").append(": ").append(color).append(mined).append("\n");
                   else
                       stringBuilder.append(ChatColor.AQUA).append(material.name()).append(": ").append(color).append(color == ChatColor.GREEN ? "Nicht SuS!!!" : color == ChatColor.YELLOW ? "Schon bissl SuS!!!" : color == ChatColor.RED ? "Richtig SuS!!!" : "-").append("\n");
               }
               sender.sendMessage(stringBuilder.toString());
           }
        }
        sender.sendMessage(ChatColor.AQUA + "-------------------------------------------");
        return false;
    }
}
