package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import wtf.melonthedev.projectplugin.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        event.setMessage(Main.getPlugin().translateHexAndCharColorCodes(event.getMessage()));
    }


}
