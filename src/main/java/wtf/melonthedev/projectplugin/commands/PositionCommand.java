package wtf.melonthedev.projectplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import wtf.melonthedev.projectplugin.Main;

import java.util.*;

public class PositionCommand implements TabExecutor {

    FileConfiguration config = Main.getPlugin().getConfig();
    String prefix = ChatColor.AQUA.toString() + ChatColor.BOLD + "[Position] " + ChatColor.RESET + ChatColor.AQUA;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        switch (args.length) {
            case 0:
                player.sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Oh Gott, dieser Command ist sehr verbuggt, wird wahrscheinlich nicht funktionieren.");
                player.sendMessage(prefix + "Deine Positions:" + getLocationsString(getLocations(player)));
                player.sendMessage(prefix + "Öffentliche Positions:" + getLocationsString(getPublicLocations()));
                return true;
            case 1:
                Location loc = getLocation(player, args[0]);
                if (loc == null) {
                    if (getPublicLocations().containsKey(args[0])) {
                        loc = getPublicLocations().get(args[0]);
                        player.sendMessage(prefix + "Öffentliche Position " + args[0] + ": x: " + loc.getBlockX() + ", y: " + loc.getBlockY() + ", z: " + loc.getBlockZ() + ", world: " + Objects.requireNonNull(loc.getWorld()).getName() + ".");
                        return true;
                    }
                    addLocation(player, args[0], player.getLocation());
                    player.sendMessage(prefix + "Position " + args[0] + " wurde erstellt bei x: " + player.getLocation().getBlockX() + ", y:" + player.getLocation().getBlockY() + ", z: " + player.getLocation().getBlockZ() + ", world: " + Objects.requireNonNull(player.getLocation().getWorld()).getName() + ".");
                    return true;
                }
                player.sendMessage(prefix + "Position " + args[0] + ": x: " + loc.getBlockX() + ", y: " + loc.getBlockY() + ", z: " + loc.getBlockZ() + ", world: " + Objects.requireNonNull(loc.getWorld()).getName() + ".");
                return true;
            case 2:
                if (args[1].equalsIgnoreCase("delete")) {
                    if (getLocations(player).containsKey(args[0])) {
                        deleteLocation(player, args[0]);
                        player.sendMessage(prefix + "Die Position " + args[0] + " wurde gelöscht.");
                        return true;
                    }
                    if (getPublicLocations().containsKey(args[0])) {
                        if (player.getUniqueId() == getPublicLocationOwner(args[0]) || player.isOp()) {
                            deletePublicLocation(args[0]);
                            player.sendMessage(prefix + "Die Position " + args[0] + " wurde gelöscht.");
                            return true;
                        }
                        player.sendMessage(ChatColor.RED + "Du musst die Position erstellt haben um sie löschen zu können.");
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + "Du besitzt keine Position mit dem Namen " + args[0] + ".");
                    return true;
                } else if (args[1].equalsIgnoreCase("public")) {
                    if (getLocations(player).containsKey(args[0])) {
                        player.sendMessage(ChatColor.RED + "Du besitzt schon eine Position mit dem Namen " + args[0] + ".");
                        return true;
                    }
                    addPublicLocation(args[0], player.getLocation(), player.getUniqueId());
                    player.sendMessage(prefix + "Öffentliche Position " + args[0] + " wurde erstellt bei x: " + player.getLocation().getBlockX() + ", y:" + player.getLocation().getBlockY() + ", z: " + player.getLocation().getBlockZ() + ", world: " + Objects.requireNonNull(player.getLocation().getWorld()).getName() + ".");
                    return true;
                }
            default:
                player.sendMessage(ChatColor.RED + "Syntaxerror: /position | /position <String: name> | /position <String: name> delete/public");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }


    public void deleteLocation(Player player, String position) {
        ConfigurationSection positions = config.getConfigurationSection("position." + player.getUniqueId());
        if (positions == null) positions = config.createSection("positions." + player.getUniqueId());
        positions.set(position, null);
        Main.getPlugin().saveConfig();
    }
    public void addLocation(Player player, String position, Location location) {
        ConfigurationSection positions = config.getConfigurationSection("position." + player.getUniqueId());
        if (positions == null) positions = config.createSection("positions." + player.getUniqueId());
        positions.set(position + ".x", location.getX());
        positions.set(position + ".y", location.getY());
        positions.set(position + ".z", location.getZ());
        positions.set(position + ".world", Objects.requireNonNull(location.getWorld()).getName());
        Main.getPlugin().saveConfig();
    }

    public void deletePublicLocation(String position) {
        ConfigurationSection positions = config.getConfigurationSection("position.public");
        if (positions == null) positions = config.createSection("positions.public");
        positions.set(position, null);
        Main.getPlugin().saveConfig();
    }
    public void addPublicLocation(String position, Location location, UUID owner) {
        ConfigurationSection positions = config.getConfigurationSection("position.public");
        if (positions == null) positions = config.createSection("positions.public");
        positions.set(position + ".x", location.getX());
        positions.set(position + ".y", location.getY());
        positions.set(position + ".z", location.getZ());
        positions.set(position + ".world", Objects.requireNonNull(location.getWorld()).getName());
        positions.set(position + ".owner", owner);
        Main.getPlugin().saveConfig();
    }

    public UUID getPublicLocationOwner(String position) {
        ConfigurationSection positions = config.getConfigurationSection("position.public");
        if (positions == null) positions = config.createSection("positions.public");
        String uuid = positions.getString(position + ".owner");
        if (uuid == null) return null;
        return UUID.fromString(uuid);
    }

    public Location getLocation(Player player, String position) {
        HashMap<String, Location> locations = getLocations(player);
        if (!locations.containsKey(position)) {
            HashMap<String, Location> locationsPublic = getPublicLocations();
            if (!locationsPublic.containsKey(position))
                return null;
            return locationsPublic.get(position);
        }
        return locations.get(position);
    }

    public HashMap<String, Location> getLocations(Player player) {
        HashMap<String, Location> locations = new HashMap<>();
        ConfigurationSection positions = config.getConfigurationSection("position." + player.getUniqueId());
        if (positions == null) positions = config.createSection("positions." + player.getUniqueId());
        for (String position : positions.getKeys(false))
            locations.put(position, getLocationFromConfig(positions, position));
        return locations;
    }

    public String getLocationsString(HashMap<String, Location> locations) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Location> entry : locations.entrySet()) {
            String coords = "x: " + entry.getValue().getBlockX() + ", y: " + entry.getValue().getBlockY() + ", z: " + entry.getValue().getBlockZ() + ", world: " + Objects.requireNonNull(entry.getValue().getWorld()).getName();
            builder.append("\n").append("   - ").append(entry.getKey()).append(": ").append(coords);
        }
        return builder.toString();
    }

    public HashMap<String, Location> getPublicLocations() {
        HashMap<String, Location> locations = new HashMap<>();
        ConfigurationSection positions = config.getConfigurationSection("positions.public");
        if (positions == null) positions = config.createSection("positions.public");
        for (String position : positions.getKeys(false))
            locations.put(position, getLocationFromConfig(positions, position));
        return locations;
    }

    public Location getLocationFromConfig(ConfigurationSection positions, String position) {
        double x = positions.getDouble(position + ".x");
        double y = positions.getDouble(position + ".y");
        double z = positions.getDouble(position + ".z");
        String world = positions.getString(position + ".world");
        if (world == null) return null;
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

}
