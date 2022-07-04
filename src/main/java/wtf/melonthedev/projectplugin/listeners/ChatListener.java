package wtf.melonthedev.projectplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import wtf.melonthedev.projectplugin.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    //@EventHandler
   // public void onChat(AsyncPlayerChatEvent event) {
   //     //event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
   //     event.setMessage(Main.getPlugin().translateHexAndCharColorCodes(event.getMessage()));
   // }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.message(Main.getPlugin().getMiniMessageComponent(PlainTextComponentSerializer.plainText().serialize(event.message())));
    }


}
