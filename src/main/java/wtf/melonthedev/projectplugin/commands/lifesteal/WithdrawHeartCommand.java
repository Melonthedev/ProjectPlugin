package wtf.melonthedev.projectplugin.commands.lifesteal;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.Lifesteal;

public class WithdrawHeartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return true;
        if (!Lifesteal.isLifestealActive()) {
            player.sendMessage(Lifesteal.prefix+ "Lifesteal is not active!");
            return true;
        }
        int hearts = Main.getPlugin().getConfig().getInt("lifesteal.hearts." + player.getUniqueId(), Lifesteal.getDefaultHeartCount());
        int count;

        if (args.length == 0){
            count = 1;
        } else if (args.length == 1)  {
            try {
                count = Integer.parseInt(args[0]);
            }catch(NumberFormatException exception){
                player.sendMessage(Lifesteal.prefix+ "Syntaxerror: /withdraw " + ChatColor.ITALIC + "Anzahl");
                return true;
            }
        }else {
            player.sendMessage(Lifesteal.prefix + ChatColor.RED + "Syntaxerror: /withdraw " + ChatColor.ITALIC + "Anzahl");
            return true;
        }

        if (count >= hearts){
            player.sendMessage(Lifesteal.prefix + ChatColor.RED + "Error: Du würdest dich selber töten");
            return true;
        }else {
            Lifesteal.withdrawHeartToItem(player, count);
        }
        return false;
    }
}
