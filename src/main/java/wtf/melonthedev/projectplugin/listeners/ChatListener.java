package wtf.melonthedev.projectplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import wtf.melonthedev.projectplugin.Main;

public class ChatListener implements Listener {

    //@EventHandler
   // public void onChat(AsyncPlayerChatEvent event) {
   //     //event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
   //     event.setMessage(Main.getPlugin().translateHexAndCharColorCodes(event.getMessage()));
   // }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.message(Main.getPlugin().getMMComponent(PlainTextComponentSerializer.plainText().serialize(event.message())));
    }


}
