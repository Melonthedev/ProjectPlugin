package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.MessageCommand;
import wtf.melonthedev.projectplugin.commands.StatusCommand;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    public static String[] joinMessages = {
      "ist nun online und bleibt das auch hoffentlich",
      "hat sich wiedereinmal hierher verirrt",
      "ist jetzt hier und will nicht sterben",
      "hat sich verklickt aber bleibt",
      "ist nur online um seine Farm zu leeren",
      "ist auf den Server gehoppst",
      "ist jetzt online und will nicht sterben",
      "wollte eigentlich nicht hier sein aber ist jetzt online",
    };

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getPlugin().setCustomPlayerListHeader(event.getPlayer());
        //event.setJoinMessage(ChatColor.GREEN + ">>" + ChatColor.AQUA + " [Survivalprojekt] " + ChatColor.stripColor(event.getJoinMessage()) + " :)");
        event.setJoinMessage(ChatColor.GREEN + ">>" + ChatColor.AQUA + " [Survivalprojekt] " + event.getPlayer().getName() + " " + joinMessages[new Random().nextInt(joinMessages.length)]);

        //STATUS
        if (StatusCommand.statusList.containsKey(event.getPlayer().getName()))
            StatusCommand.setStatus(event.getPlayer(), StatusCommand.statusList.get(event.getPlayer().getName()));

        //FIRST JOIN
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.broadcastMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + event.getPlayer().getName() + ", herzlich willkommen auf Survivalprojekt!");
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_GOAT_SCREAMING_AMBIENT, 1.0F, 0.5F);
        }
        MessageCommand.handleNewMessages(event.getPlayer());
    }
}
