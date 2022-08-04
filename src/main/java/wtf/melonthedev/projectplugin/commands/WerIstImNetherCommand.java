package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WerIstImNetherCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp() && !Arrays.asList(Main.donators).contains(sender.getName())) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hey, es sieht so aus als hättest du nicht zu der Finanzierung des Servers beigetragen. Das ist nicht schlimm, aber damit dieser Command nicht zu viel Power hat, dürfen ihn nur die Unterstützer verwenden. Vielen Dank für dein Verständnis.");
            sender.sendMessage(ChatColor.GRAY + "Du kannst dich zu jeder Zeit dazu bereit erklären, den Server zu unterstützen, dazu schreibe einfach jemanden aus dem Team an.");
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Wenn du diese Nachricht erhälst, obwohl du ein Unterstützer bist, wende dich bitte schnellstmöglich an uns.");
            return true;
        }
        sender.sendMessage(Component.text(ChatColor.GOLD + "----- Wer ist im Nether????? -----"));
        List<String> netherPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getWorld().getName().equals("world_nether")) netherPlayers.add(player.getName());
        if (netherPlayers.isEmpty())
            sender.sendMessage(Component.text(ChatColor.GOLD + ChatColor.BOLD.toString() + "Momentan ist niemand im Nether. :)"));
        else {
            sender.sendMessage(Component.text(ChatColor.GOLD + "Momentan sind folgende Spieler im Nether:"));
            for (String player : netherPlayers)
                sender.sendMessage(Component.text(ChatColor.GOLD + player));
        }
        sender.sendMessage(Component.text(ChatColor.GOLD + "----------------------------------"));
        return false;
    }

}
