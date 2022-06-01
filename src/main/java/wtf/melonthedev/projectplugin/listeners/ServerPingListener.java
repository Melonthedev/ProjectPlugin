package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Random;

public class ServerPingListener implements Listener {

    String[] motds = new String[] {
            "Hallo",
            "Super :)",
            "Haha",
            "Wird genommen xD",
            "OK brauchen neuen Tread. Dieser wird zu ernst genommen",
            "Brauchen wir überhaupt MOTDs?"
    };
    ChatColor[] colors = new ChatColor[] {
            ChatColor.GRAY,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.RED,
            ChatColor.WHITE,
            ChatColor.GOLD,
            ChatColor.BLUE,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_GREEN,
            ChatColor.DARK_PURPLE,
            ChatColor.DARK_RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW
    };

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        Random random = new Random();
        event.setMotd(ChatColor.GOLD + "Survivalprojekt 4.0" + ChatColor.AQUA + " | Survival SMP \n" +  colors[random.nextInt(colors.length)] + motds[random.nextInt(motds.length)]);
    }

}
