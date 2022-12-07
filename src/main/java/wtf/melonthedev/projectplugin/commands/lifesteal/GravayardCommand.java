package wtf.melonthedev.projectplugin.commands.lifesteal;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.List;

public class GravayardCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(ChatColor.RED + "Error: You don't have the permissions to do /graveyard");
            return true;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Error: You are not a Player");
        }
        Player p = (Player) sender;
        if (!(args.length == 3)){
            p.sendMessage(ChatColor.RED + "Error: do /graveyard" + ChatColor.ITALIC + "x y z");
        }
        try {
            Integer temp = Integer.parseInt(args[0]);
            Integer temp1 = Integer.parseInt(args[1]);
            Integer temp2 = Integer.parseInt(args[2]);
        }catch (NumberFormatException exception){
            p.sendMessage(ChatColor.RED + "Error: do /graveyard" + ChatColor.ITALIC + "x y z");
        }
        String x = args[0];
        String y = args[1];
        String z = args[2];
        String world = p.getWorld().getName();
        String id = x+y+z;

        Main.getPlugin().getConfig().set("graveyard." + id + ".x", x);
        Main.getPlugin().getConfig().set("graveyard." + id + ".y", y);
        Main.getPlugin().getConfig().set("graveyard." + id + ".z", z);
        Main.getPlugin().getConfig().set("graveyard." + id + ".world", world);
        Main.getPlugin().getConfig().set("graveyard" + id + ".uuid", null);

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (sender instanceof Player){
            if (args.length == 1) {
                tab.add(String.valueOf(((Player) sender).getTargetBlock(5).getX()));
            }
            if (args.length == 2){
                tab.add(String.valueOf(((Player) sender).getTargetBlock(5).getY()));
            }
            if (args.length == 3){
                tab.add(String.valueOf(((Player) sender).getTargetBlock(5).getZ()));
            }
        }

        return tab;
    }

}
