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
            "stebadon pinned a message to this channel."
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
        event.setMotd(ChatColor.GOLD + Main.getPlugin().translateHexAndCharColorCodes("&#fed900#S&#fecd00#u&#fec100#r&#feb500#v&#fea900#i&#fe9d00#v&#fe9100#a&#fe8500#l&#fe7900#p&#fd6c00#r&#fd6000#o&#fd5400#j&#fd4800#e&#fd3c00#k&#fd3000#t &#fd1800#4&#fd0c00#.&#fd0100#0") + ChatColor.AQUA + " | Survival SMP \n" +  colors[random.nextInt(colors.length)] + motds[random.nextInt(motds.length)]);
    }

}
