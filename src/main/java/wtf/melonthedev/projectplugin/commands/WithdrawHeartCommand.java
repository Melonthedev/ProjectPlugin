package wtf.melonthedev.projectplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.Lifesteal;

public class WithdrawHeartCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + p.getUniqueId(), Lifesteal.getDefaultHeartCount());
        Integer count;

        if (args.length == 0){
            count = 1;
        } else if (args.length == 1)  {
            try {
                count = Integer.valueOf(args[0]);
            }catch(NumberFormatException exception){
                p.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "[Life" + ChatColor.DARK_RED + "Steal]" + ChatColor.RESET.toString() + ChatColor.RED + "Syntaxerror: /withdraw " + ChatColor.ITALIC + "Anzahl");
                return true;
            }
        }else {
            p.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "[Life" + ChatColor.DARK_RED + "Steal]" + ChatColor.RESET.toString() + ChatColor.RED + "Syntaxerror: /withdraw " + ChatColor.ITALIC + "Anzahl");
            return true;
        }

        if (count >= hearts){
            p.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "[Life" + ChatColor.DARK_RED + "Steal]" + ChatColor.RESET.toString() + ChatColor.RED + "Error: Du würdest dich selber töten");
            return true;
        }else {
            Lifesteal.withdrawHeartToItem(p, count);
        }
        return false;
    }
}
