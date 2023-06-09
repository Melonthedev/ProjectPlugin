package wtf.melonthedev.projectplugin.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import wtf.melonthedev.projectplugin.Main;

import java.util.Random;

public class ServerPingListener implements Listener {

    String[] motds = Main.getPlugin().getConfig().getStringList("motds").toArray(String[]::new);
    ChatColor[] colors = new ChatColor[] {
            ChatColor.GRAY, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.WHITE, ChatColor.GOLD, ChatColor.BLUE, ChatColor.DARK_AQUA,
            ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW
    };

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (Main.isFeatureDisabled("customServerMotds")) return;
        Random random = new Random();
        Component randomMotd = Component.text(colors[random.nextInt(colors.length)] + motds[random.nextInt(motds.length)]);
        Component motd = randomMotd;
        if (Main.getPlugin().getConfig().getBoolean("config.customServerMotds.showServerInfos", true)) {
            motd = Component.join(
                    JoinConfiguration.noSeparators(),
                    Component.text(ChatColor.GOLD.toString()), Main.getMMComponent("<rainbow:" + random.nextInt(10) + ">" + Main.PROJECT_NAME),
                    Component.text(ChatColor.AQUA + " | " + Main.PROJECT_TYPE + " \n"), randomMotd);
        }
        event.motd(motd);
    }
}
