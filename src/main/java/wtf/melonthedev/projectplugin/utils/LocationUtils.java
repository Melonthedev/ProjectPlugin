package wtf.melonthedev.projectplugin.utils;

import org.bukkit.Location;
import wtf.melonthedev.projectplugin.Main;

public class LocationUtils {

    //Utility Class, Cannot be instantiated
    private LocationUtils() {}

    public static boolean isLocationInSpawnArea(Location loc) {
        if (loc.getWorld() == null) return false;
        if (!loc.getWorld().getName().equals(Main.getPlugin().getConfig().getString("spawn.world", "world"))) return false;
        Location relativeSpawnLocation = loc.getWorld().getSpawnLocation();
        relativeSpawnLocation.setY(loc.getY());
        if (loc.getWorld() != relativeSpawnLocation.getWorld()) return false;
        return relativeSpawnLocation.distance(loc) <= Main.getPlugin().getConfig().getInt("spawn.radius", 20);
    }

    public static boolean isLocationNearSpawnArea(Location loc) {
        if (loc.getWorld() == null) return false;
        if (!loc.getWorld().getName().equals(Main.getPlugin().getConfig().getString("spawn.world", "world"))) return false;
        Location relativeSpawnLocation = loc.getWorld().getSpawnLocation();
        relativeSpawnLocation.setY(loc.getY());
        if (loc.getWorld() != relativeSpawnLocation.getWorld()) return false;
        return relativeSpawnLocation.distance(loc) <= 5000;
    }

}
