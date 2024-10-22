package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
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
        handleFirstJoin(event.getPlayer());
        event.getPlayer().setSleepingIgnored(false);
        Main.setCustomPlayerListHeader(event.getPlayer());
        Lifesteal.handleJoin(event.getPlayer());
        TimerSystem.handleAutoShow(event.getPlayer());
        StatusCommand.handlePlayerJoin(event.getPlayer());
        MessageCommand.handleNewMessages(event.getPlayer());
        CustomItemSystem.discoverCustomRecipes(event.getPlayer());
        PvpCooldownSystem.handleForPlayer(event.getPlayer());
        JoinMessageCommand.handleJoinMessage(event.getPlayer());
    }

    public static void handleFirstJoin(Player player) {
        if (!player.hasPlayedBefore() && !Main.isFeatureDisabled("newPlayerWelcomeMessage")) {
            Bukkit.getServer().broadcast(Component.text(Main.getPlugin().getConfig().getString("config.newPlayerWelcomeMessage.message",
                    "§l§aPlayerName, Herzlich Willkommen auf Survivalprojekt!").replaceFirst("%playerName%", player.getName())));
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p, Sound.ENTITY_GOAT_SCREAMING_AMBIENT, 1.0F, 0.5F));
        }
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
        Component playerName = Component.text(player.getName(), NamedTextColor.AQUA);
        Component status = StatusCommand.getStatus(player);
        if (status != null) {
            Component playerNameWithStatus = Component.text("[", NamedTextColor.DARK_AQUA).append(status).append(Component.text("]", NamedTextColor.DARK_AQUA)).appendSpace().append(Component.text(player.getName(), NamedTextColor.AQUA));
            if (Main.getPlugin().getConfig().getBoolean("config.customJoinMessages.usePlayerNameWithStatus", false)) {
                playerName = playerNameWithStatus;
            } else {
                playerName = playerName.hoverEvent(playerNameWithStatus);
            }
        }
        playerName = playerName.clickEvent(ClickEvent.callback(audience -> ((Player)audience).chat(player.getName() + " <3")));
        Component finalPlayerName = playerName;
        Component joinMessage = Main.getMMComponent((spectator ? "<gray>" : "<#00bdbc>") + joinMessages[new Random().nextInt(joinMessages.length)]).asComponent().replaceText(builder -> builder.matchLiteral("%playerName%").replacement(finalPlayerName));
        return Component.text(">>", NamedTextColor.GREEN).appendSpace().append(joinMessage);

        //return Component.text(ChatColor.GREEN + ">>" + (spectator ? ChatColor.GRAY : ChatColor.AQUA) + " [Survivalprojekt] " + player.getName() + " " + joinMessages[new Random().nextInt(joinMessages.length)]);
    }
}
