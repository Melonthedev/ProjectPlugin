package wtf.melonthedev.projectplugin.commands.moderation;

import com.destroystokyo.paper.ClientOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManagePlayerCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Sorry, you don't have permission to use this command!");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /manageplayer <player> <option>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not cached!");
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "uuid" -> sender.sendMessage("UUID of " + target.getName() + ": " + target.getUniqueId());
            case "online" -> sender.sendMessage("Is Online: " + target.isOnline());
        }
        Player targetOnline = target.getPlayer();
        if (targetOnline == null) {
            sender.sendMessage(ChatColor.RED + "Player not online!");
            return true;
        }
        switch (args[1].toLowerCase()) {
            case "respawn" -> targetOnline.spigot().respawn();
            case "ping" -> sender.sendMessage("Ping: " + targetOnline.spigot().getPing());
            case "ip" -> sender.sendMessage("IP: " + targetOnline.spigot().getRawAddress());
            case "viewdistance" -> sender.sendMessage("View Distance: " + targetOnline.getClientOption(ClientOption.VIEW_DISTANCE));
            case "locale" -> sender.sendMessage("Locale: " + targetOnline.getClientOption(ClientOption.LOCALE));
            case "allowserverlisting" -> sender.sendMessage("Allow Server Listing: " + targetOnline.getClientOption(ClientOption.ALLOW_SERVER_LISTINGS));
            case "chatcolorsenabled" -> sender.sendMessage("Chatcolors Enabled: " + targetOnline.getClientOption(ClientOption.CHAT_COLORS_ENABLED));
            case "mainhand" -> sender.sendMessage("Main Hand: " + targetOnline.getClientOption(ClientOption.MAIN_HAND));
            case "textfilteringenabled" -> sender.sendMessage("Text Filtering Enabled: " + targetOnline.getClientOption(ClientOption.TEXT_FILTERING_ENABLED));
            case "skinparts" -> sender.sendMessage("Skin Parts: " + targetOnline.getClientOption(ClientOption.SKIN_PARTS));
            case "killer" -> sender.sendMessage("Killer: " + (targetOnline.getKiller() == null ? "null" : targetOnline.getKiller().getName()));
            case "flying" -> sender.sendMessage("Flying: " + targetOnline.isFlying());
            case "world" -> sender.sendMessage("World: " + targetOnline.getWorld().getName());
            case "location" -> sender.sendMessage("Location: " + targetOnline.getLocation());
            case "gamemode" -> sender.sendMessage("Gamemode: " + targetOnline.getGameMode().name());
            case "client" -> sender.sendMessage("Client: " + targetOnline.getClientBrandName());
            case "boostelytra" -> sender.sendMessage("Boosted with: " + targetOnline.boostElytra(new ItemStack(Material.FIREWORK_ROCKET)).getName());
            case "applyMending" -> sender.sendMessage("Mending applied! Remaining: " + targetOnline.applyMending(100));
            case "xp" -> sender.sendMessage("XP: " + targetOnline.getExp());
            case "health" -> sender.sendMessage("Health: " + targetOnline.getHealth());
            case "foodlevel" -> sender.sendMessage("Food Level: " + targetOnline.getFoodLevel());
            case "hasresourcepack" -> sender.sendMessage("Has Resourcepack: " + targetOnline.hasResourcePack());
            case "dropitem" -> sender.sendMessage("Dropped item: " + targetOnline.dropItem(false));
            case "dropitemstack" -> sender.sendMessage("Dropped stack: " + targetOnline.dropItem(true));
            case "entityid" -> sender.sendMessage("Entity Id: " + targetOnline.getEntityId());
            case "lastdamage" -> sender.sendMessage("Last Damage: " + targetOnline.getLastDamage() + " of " + targetOnline.getLastDamageCause());
            case "deathlocation" -> sender.sendMessage("Death Location: " + targetOnline.getLastDeathLocation());
            case "facing" -> sender.sendMessage("Facing: " + targetOnline.getFacing().name());
            case "reset" -> {
                targetOnline.setHealth(Objects.requireNonNull(targetOnline.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
                targetOnline.setFoodLevel(20);
                sender.sendMessage(ChatColor.GREEN + "Success!");
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            CommandUtils.addOfflinePlayersTabComplete(tab, args[0]);
        } else if (args.length == 2) {
            tab.add("uuid");
            tab.add("online");
            tab.add("respawn");
            tab.add("ping");
            tab.add("ip");
            tab.add("viewdistance");
            tab.add("locale");
            tab.add("allowserverlisting");
            tab.add("chatcolorsenabled");
            tab.add("mainhand");
            tab.add("textfilteringenabled");
            tab.add("skinparts");
            tab.add("killer");
            tab.add("flying");
            tab.add("world");
            tab.add("location");
            tab.add("gamemode");
            tab.add("client");
            tab.add("boostElytra");
            tab.add("applyMending");
            tab.add("xp");
            tab.add("health");
            tab.add("foodlevel");
            tab.add("hasresourcepack");
            tab.add("dropitem");
            tab.add("dropitemstack");
            tab.add("entityid");
            tab.add("lastdamage");
            tab.add("deathlocation");
            tab.add("facing");
            tab.add("reset");
        }
        return tab;
    }
}
