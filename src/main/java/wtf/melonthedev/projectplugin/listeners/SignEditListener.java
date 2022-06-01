package wtf.melonthedev.projectplugin.listeners;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import wtf.melonthedev.projectplugin.utils.LocationUtils;

public class SignEditListener implements Listener {

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!(event.getClickedBlock().getState() instanceof Sign)) return;
        Sign sign = (Sign) event.getClickedBlock().getState();
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        if (LocationUtils.isLocationInSpawnArea(event.getClickedBlock().getLocation())) return;
        sign.setEditable(true);
        player.openSign(sign);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Sign sign = (Sign) event.getBlock().getState();
        sign.setEditable(true);
        /*for (int i = 0; i < event.getLines().length; i++) {
            String line = event.getLine(i);
            if (line == null) continue;
            event.setLine(i, line);
        }*/
    }

}
