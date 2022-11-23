package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.JoinMessageCommand;
import wtf.melonthedev.projectplugin.commands.MessageCommand;
import wtf.melonthedev.projectplugin.commands.StatusCommand;
import wtf.melonthedev.projectplugin.utils.AfkSystem;
import wtf.melonthedev.projectplugin.utils.Lifesteal;
import wtf.melonthedev.projectplugin.utils.PvpCooldownSystem;

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


        //FIRST JOIN
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.getServer().broadcast(Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + event.getPlayer().getName() + ", Herzlich Willkommen auf Survivalprojekt!"));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(event.getPlayer(), Sound.ENTITY_GOAT_SCREAMING_AMBIENT, 1.0F, 0.5F));
        }

        //HARDCORE
        //if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) && Main.getPlugin().getConfig().getBoolean("projectActive", false)) {
            PvpCooldownSystem.handleForPlayer(event.getPlayer());
        //}
        Main.getPlugin().handlePlayerJoinSpectatorVisibility(event.getPlayer());
        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) && (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().isDead())) {
            event.getPlayer().playerListName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
            event.getPlayer().displayName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
            event.joinMessage(Component.text(ChatColor.GREEN + ">>" + ChatColor.GRAY + " [Survivalprojekt] " + event.getPlayer().getName() + " " + joinMessages[new Random().nextInt(joinMessages.length)]));
        } else
            event.joinMessage(Component.text(ChatColor.GREEN + ">>" + ChatColor.AQUA + " [Survivalprojekt] " + event.getPlayer().getName() + " " + joinMessages[new Random().nextInt(joinMessages.length)]));
        //STATUS
        if (StatusCommand.statusList.containsKey(event.getPlayer().getName()) && (!Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) || event.getPlayer().getGameMode() != GameMode.SPECTATOR)) {
            StatusCommand.setStatus(event.getPlayer(), Main.getPlugin().getMiniMessageComponent(StatusCommand.statusList.get(event.getPlayer().getName())));
            //StatusCommand.setStatus(event.getPlayer(), Main.getPlugin().translateHexAndCharColorCodes(StatusCommand.statusList.get(event.getPlayer().getName())));
        }

        AfkSystem.handlePlayersSleepingPercentage();
        MessageCommand.handleNewMessages(event.getPlayer());
        JoinMessageCommand.handleJoinMessage(event.getPlayer());
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        Lifesteal.handleLogin(event);
    }
}
