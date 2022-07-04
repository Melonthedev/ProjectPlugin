package wtf.melonthedev.projectplugin.utils;

import org.bukkit.Location;

public class LocationUtils {

    //Utility Class, Cannot be instantiated
    private LocationUtils() {}

    public static boolean isLocationInSpawnArea(Location loc) {
        if (loc.getWorld() == null) return false;
        if (!loc.getWorld().getName().equals("world")) return false;
        Location relativeSpawnLocation = loc.getWorld().getSpawnLocation();
        relativeSpawnLocation.setY(loc.getY());
        if (loc.getWorld() != relativeSpawnLocation.getWorld()) return false;
        return relativeSpawnLocation.distance(loc) <= 20;
    }

}
