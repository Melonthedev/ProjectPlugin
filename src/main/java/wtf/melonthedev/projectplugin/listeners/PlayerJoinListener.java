package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.JoinMessageCommand;
import wtf.melonthedev.projectplugin.commands.MessageCommand;
import wtf.melonthedev.projectplugin.commands.StatusCommand;
import wtf.melonthedev.projectplugin.utils.AfkSystem;

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
        event.joinMessage(Component.text(ChatColor.GREEN + ">>" + ChatColor.AQUA + " [Survivalprojekt] " + event.getPlayer().getName() + " " + joinMessages[new Random().nextInt(joinMessages.length)]));

        //STATUS
        if (StatusCommand.statusList.containsKey(event.getPlayer().getName()))
            StatusCommand.setStatus(event.getPlayer(), Main.getPlugin().getMiniMessageComponent(StatusCommand.statusList.get(event.getPlayer().getName())));
            //StatusCommand.setStatus(event.getPlayer(), Main.getPlugin().translateHexAndCharColorCodes(StatusCommand.statusList.get(event.getPlayer().getName())));

        //FIRST JOIN
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.getServer().broadcast(Component.text(ChatColor.BOLD.toString() + ChatColor.GREEN + event.getPlayer().getName() + ", Herzlich Willkommen auf Survivalprojekt!"));
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_GOAT_SCREAMING_AMBIENT, 1.0F, 0.5F);
        }

        //HARDCORE
        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) && (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().isDead())) {
            event.getPlayer().playerListName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
            event.getPlayer().displayName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
        }
        if (Main.getPlugin().getConfig().getBoolean("hardcore.pvpCooldown", false)) {
            event.getPlayer().showBossBar(Main.getPlugin().bar);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "PvP Cooldown ist aktiv!"));
        }
        Main.getPlugin().handlePlayerJoinSpectatorVisibility(event.getPlayer());

        AfkSystem.handlePlayersSleepingPercentage();
        MessageCommand.handleNewMessages(event.getPlayer());
        JoinMessageCommand.handleJoinMessage(event.getPlayer());
    }
}
