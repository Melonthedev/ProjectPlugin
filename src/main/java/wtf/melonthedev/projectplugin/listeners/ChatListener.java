package wtf.melonthedev.projectplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import wtf.melonthedev.projectplugin.Main;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.message(Main.getMMComponent(PlainTextComponentSerializer.plainText().serialize(event.message())));
    }
}
