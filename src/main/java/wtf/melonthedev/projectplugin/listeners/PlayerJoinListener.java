package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.melonthedev.projectplugin.Main;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getPlugin().setCustomPlayerListHeader(event.getPlayer());
        //event.setJoinMessage(ChatColor.GREEN + ">>" + ChatColor.AQUA + " [Survivalprojekt] " + ChatColor.stripColor(event.getJoinMessage()) + " :)");
        event.setJoinMessage(ChatColor.GREEN + ">>" + ChatColor.AQUA + " [Survivalprojekt] " + event.getPlayer().getName() + Main.getPlugin().translateHexAndCharColorCodes(" &#54f6fb#j&#54f2fa#o&#54edfb#i&#54e9fb#n&#54e5fb#e&#54e0fb#d &#54d7fb#t&#55d3fb#h&#55cffb#e &#55c6fb#g&#55c1fb#a&#55bdfb#m&#55b9fb#e &#55b0fb#:&#56abfb#)"));
    }
}
