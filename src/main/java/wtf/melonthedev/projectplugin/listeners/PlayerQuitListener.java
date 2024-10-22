package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.commands.StatusCommand;
import wtf.melonthedev.projectplugin.modules.AfkSystem;
import wtf.melonthedev.projectplugin.modules.PvpCooldownSystem;

import java.util.Random;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (AfkSystem.afkPlayers.contains(player) || AfkSystem.afkTimeoutTasks.containsKey(player)) {
            AfkSystem.afkTimeoutTasks.remove(player);
            AfkSystem.afkPlayers.remove(player);
        }
        PvpCooldownSystem.pauseForPlayer(player);
        //Component quitMsg = event.quitMessage();
        //if (quitMsg == null) return;
        //ChatColor color = (Main.getPlugin().getConfig().getBoolean("hardcore.enabled", true) && player.getGameMode() == GameMode.SPECTATOR) ? ChatColor.GRAY : ChatColor.AQUA;
        //event.quitMessage(Component.text(ChatColor.RED + "<<" + color + " [Survivalprojekt] " + ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(quitMsg)) + " :("));
        String[] quitMessages = Main.getPlugin().getConfig().getStringList("quitmessages").toArray(String[]::new);
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
        Component joinMessage = Main.getMMComponent(((Main.getPlugin().getConfig().getBoolean("hardcore.enabled", true) && player.getGameMode() == GameMode.SPECTATOR) ? "<gray>" : "<#00bdbc>") + quitMessages[new Random().nextInt(quitMessages.length)]).asComponent().replaceText(builder -> builder.matchLiteral("%playerName%").replacement(finalPlayerName));
        event.quitMessage(Component.text("<<", NamedTextColor.RED).appendSpace().append(joinMessage));
    }
}
