package wtf.melonthedev.projectplugin.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import wtf.melonthedev.projectplugin.Main;

public class Lifesteal {

    private Lifesteal() {} // Utility Class, cannot be instantiated

    public static void init() {
        //TODO: Load infos from config for players, show messages, ...

    }

    public static boolean isLifesteal() {
        return Main.getPlugin().getConfig().getBoolean("lifesteal.enabled", false);
    }

    public static int getDefaultHeartCount() {
        return 10;
    }

    public static int getRevivedPlayerHeartCount() {
        return 3;
    }

    public static ItemStack getHeartItem() {
        return new ItemStack(Material.FERMENTED_SPIDER_EYE);
    }

    public static void giveHeart(Player player) {
        throw new NotImplementedException();
    }

    public static void removeHeart(Player player) {
        throw new NotImplementedException();
    }

    public static void revivePlayer(Player player) {
        throw new NotImplementedException();

    }

    public static void eliminatePlayer(Player player) {
        throw new NotImplementedException();

    }

    public static void handleJoin(Player player) {
        // Hier schauen ob spieler noch leben etc
        // Called from JoinListener
        throw new NotImplementedException();
    }


    /**
     * @return wheather heart was withdrawn successfully
     */
    public static boolean withdrawHeartToItem(Player player) {
        throw new NotImplementedException();
    }

    /**
     * @return wheather heart item was added successfully
     */
    public static boolean addHeartItemToPlayer(Player player) {
        throw new NotImplementedException();
    }

    //...

}
