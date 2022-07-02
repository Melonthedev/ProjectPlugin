package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import wtf.melonthedev.projectplugin.Main;

import java.util.Random;

public class ServerPingListener implements Listener {

    String[] motds = new String[] {
            "Hallo",
            "Super :)",
            "Haha",
            "Wird genommen xD",
            "OK brauchen neuen Tread. Dieser wird zu ernst genommen",
            "Brauchen wir überhaupt MOTDs?",
            "Alter nicht euer ernst",
            "Achtung was du schreibst",
            "Das nimmt er alles als MODT.",
            "stebadon pinned a message to this channel.",
            "Hat was...",
            "Ich hasse Melon Client - Jonbadon",
            "Progeto ist heute ganz krass",
            "Joinen! Joinen! Joinen! Joinen!",
            "Ich sehe alles als eine MOTD",
            "Der beste SMP-Server",
            "Der mit riesigem Abstand beste SMP-Server!",
            "Strenggenommen ist es \"the\" SMP - Jonbadon",
            "Hilfe ich bin in einer MOTD's Fabrik gefangen!",
            "https://youtu.be/dQw4w9WgXcQ"
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
        //event.setMotd(ChatColor.GOLD + "Survivalprojekt 4.0" + ChatColor.AQUA + " | Survival SMP \n" +  colors[random.nextInt(colors.length)] + motds[random.nextInt(motds.length)]);
        event.setMotd(ChatColor.GOLD + Main.getPlugin().getServerName() + ChatColor.AQUA + " | Survival SMP \n" +  colors[random.nextInt(colors.length)] + motds[random.nextInt(motds.length)]);
    }

}
