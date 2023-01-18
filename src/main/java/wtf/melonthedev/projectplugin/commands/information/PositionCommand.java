package wtf.melonthedev.projectplugin.commands.information;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.ArrayList;
import java.util.List;

public class PositionCommand implements TabExecutor {

    String prefix = ChatColor.AQUA.toString() + ChatColor.BOLD + "[Position] " + ChatColor.RESET + ChatColor.WHITE;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(prefix + "Positions: ");
            for (String name : getAllPositionNames())
                sender.sendMessage("- " + name + " " + CommandUtils.getLocationString(getPosition(name)));
            return true;
        } else if (args.length == 1 && sender instanceof Player player) {
            if (getPosition(args[0]) == null) {
                if (!Main.getPlugin().getConfig().getBoolean("shownSecurityWarning." + player.getUniqueId(), false)) {
                    player.sendMessage(prefix + ChatColor.BOLD + ChatColor.RED + "Vorsicht!" + ChatColor.WHITE + " Du bist dabei eine " + ChatColor.RED + "öffentliche Position" + ChatColor.WHITE + " mit deinen " + ChatColor.RED + "aktuellen Koordinaten" + ChatColor.WHITE + " zu erstellen. Wenn du das wirklich möchtest, führe den Command erneut aus! Du kannst Positionen jederzeit mit " + ChatColor.AQUA + "'/position <Name> delete'" + ChatColor.WHITE + " löschen.");
                    Main.getPlugin().getConfig().set("shownSecurityWarning." + player.getUniqueId(), true);
                    Main.getPlugin().saveConfig();
                    return true;
                }
                if (args[0].contains(".")) {
                    player.sendMessage(prefix + "Points are not allowed!");
                    return true;
                }
                savePosition(args[0], player.getLocation());
                player.sendMessage(prefix + "Saved Position '" + args[0] + "' at " + CommandUtils.getLocationString(player.getLocation()));
                return true;
            }
            Location loc = getPosition(args[0]);
            player.sendMessage(prefix + "Position '" + args[0] + "' - " + CommandUtils.getLocationString(loc));
        } else if (args.length == 2 && getPosition(args[0]) != null && args[1].equalsIgnoreCase("delete")) {
            Main.getPlugin().getConfig().set("positions." + args[0], null);
            Main.getPlugin().saveConfig();
            sender.sendMessage(prefix + "Deleted position '" + args[0] + "'!");
        }
        return false;
    }

    public void savePosition(String name, Location location) {
        Main.getPlugin().getConfig().set("positions." + name + ".x", location.getBlockX());
        Main.getPlugin().getConfig().set("positions." + name + ".y", location.getBlockY());
        Main.getPlugin().getConfig().set("positions." + name + ".z", location.getBlockZ());
        Main.getPlugin().getConfig().set("positions." + name + ".w", location.getWorld().getName());
        Main.getPlugin().saveConfig();
    }

    public Location getPosition(String name) {
        ConfigurationSection posSection = Main.getPlugin().getConfig().getConfigurationSection("positions." + name);
        if (posSection == null) return null;
        World world = Bukkit.getWorld(posSection.getString("w", "world"));
        return new Location(world, posSection.getInt("x"), posSection.getInt("y"), posSection.getInt("z"));
    }

    public List<String> getAllPositionNames() {
        ConfigurationSection section = Main.getPlugin().getConfig().getConfigurationSection("positions");
        if (section == null) return new ArrayList<>();
        return section.getKeys(false).stream().toList();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1)
            tab.addAll(getAllPositionNames());
        else if (args.length == 2)
            tab.add("delete");
        return tab;
    }
}