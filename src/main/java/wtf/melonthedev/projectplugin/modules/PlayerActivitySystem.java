package wtf.melonthedev.projectplugin.modules;

import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @deprecated in favor of Log4Minecraft
 */
@Deprecated
public class PlayerActivitySystem {

    public static List<HashMap<String, HashMap<Material, Integer>>> collectedValuables = new ArrayList<>();

    public static void handleSusPlayerActivityPerHour() {
        new BukkitRunnable() {
            @Override
            public void run() {
                collectedValuables.add(new HashMap<>());
            }
        }.runTaskTimer(Main.getPlugin(), 0, 72000); // 1h
    }

    public static void savePlayerActivity() {
        //Main.getPlugin().getConfig().set("playeractivity", collectedValuables);
        //Main.getPlugin().saveConfig();
    }
    public static HashMap<String, HashMap<Material, Integer>> getLatestPlayerActivityEntry() {
        return collectedValuables.get(collectedValuables.size() - 1);
    }


}
