package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Random;

public class ServerPingListener implements Listener {

    String[] motds = new String[] {
      "Der weltweit beste Server",
      "Never gonna give you up!",
      "Bitte nicht Wursten auf dem Server",
      "Spawnelytra update!",
      "null",
      "IllegalArgumentException: Invalid Server: \"hypixel.net\"",
      "All rights reserved!",
      "McSurvivalprojekt.de",
      "Artificial Intelligence usually beats natural stupidity.",
      "Warum? Weil.",
      "Sei nett und schreibe statt \"FUCK!\" lieber \"mist :(\""
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
        event.setMotd(ChatColor.AQUA + "Survivalprojekt 3.0" + " | 1.18 Updated SMP \n" +  colors[random.nextInt(colors.length)] + motds[random.nextInt(motds.length)]);
    }

}
