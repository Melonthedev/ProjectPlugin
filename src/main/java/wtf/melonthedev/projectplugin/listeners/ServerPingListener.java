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

    /*String[] motds = new String[] {
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
    };*/
    String[] motds = Main.getPlugin().getConfig().getStringList("motds").toArray(String[]::new);
            /*new String[] {
            "GUYS MOTD TIME",
            "Auch anständige?",
            "Tantal0s ist wieder am griefen",
            "MelonClient ist scheiße - Jonbadon",
            "wieder mal die alten?",
            "Joris ist klein",
            "Mobbing in den MOTD's?",
            "Ja",
            "Gerne",
            "Kann ich machen - Tantal0s",
            "Aber ich will hier nd eherenlos werden",
            "MOTD Channel: ALLES wird genommen",
            "imagine man liked seine eigenen vorschläge",
            "Sorry, zu lang für sie",
            "Also die Serverliste",
            "schade",
            "aber eig random genug",
            "Tantalos für die mündliche 1 in Geschi???",
            "Lancelot ist mal wieder AFK",
            "Die Qualität dieser MOTD's, 1A",
            "Kritisch...",
            "aber wahr",
            "Joris sucht immer noch sein Pigstep",
            "Das ist doch geil!",
            "Steffen ist schon tot",
            "und hat es verdient",
            "Destreuer findet schnitzelbrötchen besser als Freunde",
            "https://youtu.be/dQw4w9WgXcQ"
    };*/
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
