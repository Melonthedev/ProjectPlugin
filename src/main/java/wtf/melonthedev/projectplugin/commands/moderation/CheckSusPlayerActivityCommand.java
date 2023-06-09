package wtf.melonthedev.projectplugin.commands.moderation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * @deprecated in favor of Log4Minecraft
 */
public class CheckSusPlayerActivityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /checksusplayeractivity <player>");
            return true;
        }
        sender.sendMessage(ChatColor.AQUA + "----- PlayerActivity of Player " + args[0] + ": ----- ");
        HashMap<Material, List<Integer>> minedValues = new HashMap<>();
        /*for (HashMap<String, HashMap<Material, Integer>> entry : PlayerActivitySystem.collectedValuables) {
           if (!entry.containsKey(args[0])) {
               sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.ITALIC + "No Data for this hour!");
               continue;
           }
            for (Material material : entry.get(args[0]).keySet()) {
                int mined = entry.get(args[0]).get(material);
                if (!minedValues.containsKey(material))
                    minedValues.put(material, new ArrayList<>());
                  minedValues.get(material).add(mined);
            }
        }*/
        for (Material material : minedValues.keySet()) {
            List<Integer> minedList = minedValues.get(material);
            StringBuilder stringBuilder = new StringBuilder();
            if (sender.isOp()) stringBuilder.append(ChatColor.AQUA).append(material.name()).append("/h: ");
            else stringBuilder.append(ChatColor.AQUA).append(material.name()).append(": ");
            for (int mined : minedList) {
                ChatColor color = material == Material.DIAMOND_ORE || material == Material.DEEPSLATE_DIAMOND_ORE ? mined < 35 ? ChatColor.GREEN : mined < 50 ? ChatColor.YELLOW : ChatColor.RED
                        : material == Material.EMERALD_ORE || material == Material.DEEPSLATE_EMERALD_ORE ? mined < 30 ? ChatColor.GREEN : mined < 50 ? ChatColor.YELLOW : ChatColor.RED
                        : material == Material.IRON_ORE || material == Material.DEEPSLATE_IRON_ORE ? mined < 100 ? ChatColor.GREEN : mined < 150 ? ChatColor.YELLOW : ChatColor.RED
                        : material == Material.COAL_ORE || material == Material.DEEPSLATE_COAL_ORE ? mined < 150 ? ChatColor.GREEN : mined < 200 ? ChatColor.YELLOW : ChatColor.RED
                        : material == Material.GOLD_ORE || material == Material.DEEPSLATE_GOLD_ORE ? mined < 50 ? ChatColor.GREEN : mined < 100 ? ChatColor.YELLOW : ChatColor.RED
                        : material == Material.ANCIENT_DEBRIS ? mined < 30 ? ChatColor.GREEN : mined < 45 ? ChatColor.YELLOW : ChatColor.RED
                        : material == Material.NETHER_QUARTZ_ORE ? mined < 180 ? ChatColor.GREEN : mined < 300 ? ChatColor.YELLOW : ChatColor.RED
                        : ChatColor.AQUA;
                if (sender.isOp()) stringBuilder.append(ChatColor.AQUA).append(color).append(mined).append(ChatColor.AQUA).append(" / ");
                else stringBuilder.append(ChatColor.AQUA).append(color).append(color == ChatColor.GREEN ? "Nicht SuS!!!" : color == ChatColor.YELLOW ? "Schon bissl SuS!!!" : color == ChatColor.RED ? "SUSSY BAKA!!!" : "-").append(" / ");
            }
            sender.sendMessage(stringBuilder.substring(0, stringBuilder.length() - 3));
        }
        sender.sendMessage(ChatColor.AQUA + "-------------------------------------------");
        return false;
    }
}
