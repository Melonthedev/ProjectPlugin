package wtf.melonthedev.projectplugin.utils;

import org.bukkit.Location;
import wtf.melonthedev.projectplugin.Main;

public class LocationUtils {

    private LocationUtils() {} //Utility Class, cannot be instantiated

    public static boolean isLocationInSpawnArea(Location loc) {
        Location relativeSpawnLocation = getRelativeSpawnLocation(loc);
        if (relativeSpawnLocation == null) return false;
        return relativeSpawnLocation.distance(loc) <= Main.getPlugin().getConfig().getInt("spawn.radius", 20);
    }

    public static boolean isLocationNearSpawnArea(Location loc) {
        Location relativeSpawnLocation = getRelativeSpawnLocation(loc);
        if (relativeSpawnLocation == null) return false;
        return relativeSpawnLocation.distance(loc) <= 5000;
    }

    public static Location getRelativeSpawnLocation(Location loc) {
        if (loc.getWorld() == null || !loc.getWorld().getName().equals(Main.getPlugin().getConfig().getString("spawn.world", "world"))) return null;
        Location relativeSpawnLocation = loc.getWorld().getSpawnLocation();
        relativeSpawnLocation.setY(loc.getY());
        if (loc.getWorld() != relativeSpawnLocation.getWorld()) return null;
        return relativeSpawnLocation;
    }

}
