package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.moderation.JoinMessageCommand;
import wtf.melonthedev.projectplugin.commands.MessageCommand;
import wtf.melonthedev.projectplugin.commands.StatusCommand;
import wtf.melonthedev.projectplugin.modules.*;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        handleJoinMessage(event);
        event.getPlayer().setSleepingIgnored(false);
        Main.handleFirstJoin(event.getPlayer());
        Main.setCustomPlayerListHeader(event.getPlayer());
        Lifesteal.handleJoin(event.getPlayer());
        TimerSystem.handleAutoShow(event.getPlayer());
        StatusCommand.handlePlayerJoin(event.getPlayer());
        MessageCommand.handleNewMessages(event.getPlayer());
        CustomItemSystem.discoverCustomRecipes(event.getPlayer());
        PvpCooldownSystem.handleForPlayer(event.getPlayer());
        JoinMessageCommand.handleJoinMessage(event.getPlayer());
    }

    public void handleJoinMessage(PlayerJoinEvent event) {
        if (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", false) && (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().isDead())) {
            event.getPlayer().playerListName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
            event.getPlayer().displayName(Component.text(ChatColor.GRAY + "[Spectator] " + event.getPlayer().getName()));
            if (!Main.isFeatureDisabled("customJoinMessages")) event.joinMessage(getRandomJoinMessage(event.getPlayer(), true));
        } else if (!Main.isFeatureDisabled("customJoinMessages")) event.joinMessage(getRandomJoinMessage(event.getPlayer(), false));
    }

    public Component getRandomJoinMessage(Player player, boolean spectator) {
        String[] joinMessages = Main.getPlugin().getConfig().getStringList("joinmessages").toArray(String[]::new);
        return Component.text(ChatColor.GREEN + ">>" + (spectator ? ChatColor.GRAY : ChatColor.AQUA) + " [Survivalprojekt] " + player.getName() + " " + joinMessages[new Random().nextInt(joinMessages.length)]);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        Lifesteal.handleLogin(event);
    }

}
