package wtf.melonthedev.projectplugin.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.melonthedev.projectplugin.Main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class StatusConfiguration {

    private static File file;
    private static FileConfiguration configuration;

    public static void init() {
        if (!Main.getPlugin().getDataFolder().exists()) {
            try {
                Main.getPlugin().getDataFolder().createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Could not create config folder.");
            }
        }
        file = new File(Main.getPlugin().getDataFolder(), "status.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Could not create config file.");
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfig() {
        return configuration;
    }

    public static void saveConfig() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config file.");
        }
    }

    public static void reloadConfig() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }
}
