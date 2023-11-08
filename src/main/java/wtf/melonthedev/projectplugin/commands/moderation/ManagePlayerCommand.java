package wtf.melonthedev.projectplugin.commands.moderation;

import com.destroystokyo.paper.ClientOption;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.utils.CommandUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /manageplayer <player> <option> (<arguments>)");
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
        if (args.length == 2) {
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
                case "healthscale" -> sender.sendMessage("Health: " + targetOnline.getHealthScale());
                case "foodlevel" -> sender.sendMessage("Food Level: " + targetOnline.getFoodLevel());
                case "hasresourcepack" -> sender.sendMessage("Has Resourcepack: " + targetOnline.hasResourcePack());
                case "dropitem" -> sender.sendMessage("Dropped item: " + targetOnline.dropItem(false));
                case "dropitemstack" -> sender.sendMessage("Dropped stack: " + targetOnline.dropItem(true));
                case "entityid" -> sender.sendMessage("Entity Id: " + targetOnline.getEntityId());
                case "lastdamage" -> sender.sendMessage("Last Damage: " + targetOnline.getLastDamage() + " of " + targetOnline.getLastDamageCause());
                case "deathlocation" -> sender.sendMessage("Death Location: " + targetOnline.getLastDeathLocation());
                case "facing" -> sender.sendMessage("Facing: " + targetOnline.getFacing().name());
                case "flyspeed" -> sender.sendMessage("FlySpeed: " + targetOnline.getFlySpeed());
                case "walkspeed" -> sender.sendMessage("WalkSpeed: " + targetOnline.getWalkSpeed());
                case "wakeup" -> targetOnline.wakeup(true);
                case "isinwaterrainorbubblecolumn" -> sender.sendMessage("IsInWater: " + targetOnline.isInWaterOrRainOrBubbleColumn());
                case "previousgamemode" -> sender.sendMessage("PreviousGameMode: " + targetOnline.getPreviousGameMode() != null ? targetOnline.getPreviousGameMode().name() : "N/A");
                case "resetplayertime" -> targetOnline.resetPlayerTime();
                case "resetplayerweather" -> targetOnline.resetPlayerWeather();
                case "resettitle" -> targetOnline.resetTitle();
                case "reset" -> {
                    targetOnline.setHealth(Objects.requireNonNull(targetOnline.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
                    targetOnline.setFoodLevel(20);
                    sender.sendMessage(ChatColor.GREEN + "Success!");
                }
            }
        } else {
            try {
                switch (args[1].toLowerCase()) {
                    case "setkiller" -> {
                        Player newKiller = args[2].equalsIgnoreCase("null") ? null : Bukkit.getPlayer(args[2]);
                        targetOnline.setKiller(newKiller);
                        sender.sendMessage(ChatColor.GREEN + "Success! Killer set to " + newKiller);
                    }
                    case "sethealth" -> {
                        int newHealth = Integer.parseInt(args[2]);
                        targetOnline.setHealth(newHealth);
                        sender.sendMessage(ChatColor.GREEN + "Success! Health set to " + newHealth);
                    }
                    case "sethealthscale" -> {
                        int newHealthScale = Integer.parseInt(args[2]);
                        targetOnline.setHealthScale(newHealthScale);
                        sender.sendMessage(ChatColor.GREEN + "Success! HealthScale set to " + newHealthScale);
                    }
                    case "setfoodlevel" -> {
                        int newFoodLevel = Integer.parseInt(args[2]);
                        targetOnline.setFoodLevel(newFoodLevel);
                        sender.sendMessage(ChatColor.GREEN + "Success! FoodLevel set to " + newFoodLevel);
                    }
                    case "setflying" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setFlying(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Flying set to " + newState);
                    }
                    case "setallowflight" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setAllowFlight(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! AllowFlight set to " + newState);
                    }
                    case "setexpcooldown" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setExpCooldown(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! ExpCooldown set to " + newValue);
                    }
                    case "setflyspeed" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setFlySpeed(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Flyspeed set to " + newValue);
                    }
                    case "setplayertimerelative" -> {
                        long newValue = Long.parseLong(args[2]);
                        targetOnline.setPlayerTime(newValue, true);
                        sender.sendMessage(ChatColor.GREEN + "Success! Playertime set relative to " + newValue);
                    }
                    case "setplayertime" -> {
                        long newValue = Long.parseLong(args[2]);
                        targetOnline.setPlayerTime(newValue, false);
                        sender.sendMessage(ChatColor.GREEN + "Success! Playertime set relative to " + newValue);
                    }
                    case "setplayerweather" -> {
                        WeatherType newValue = WeatherType.valueOf(args[2].toUpperCase());
                        targetOnline.setPlayerWeather(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Playerweather set to " + newValue.name());
                    }
                    case "setsendviewdistance" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setSendViewDistance(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Sendviewdistance set to " + newValue);
                    }
                    case "setsneaking" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setSneaking(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Sneaking set to " + newState);
                    }
                    case "setsprinting" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setSprinting(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Sprinting set to " + newState);
                    }
                    case "setviewdistance" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setViewDistance(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Viewdistance set to " + newValue);
                    }
                    case "setwalkspeed" -> {
                        float newValue = Float.parseFloat(args[2]);
                        targetOnline.setWalkSpeed(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Walkspeed set to " + newValue);
                    }
                    case "setbodyyaw" -> {
                        float newValue = Float.parseFloat(args[2]);
                        targetOnline.setBodyYaw(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Bodyyaw set to " + newValue);
                    }
                    case "setcanpickupitems" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setCanPickupItems(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! CanPickupItems set to " + newState);
                    }
                    case "setcollidable" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setCollidable(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Collidable set to " + newState);
                    }
                    case "setcustomnamevisible" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setCustomNameVisible(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! CustomNameVisible set to " + newState);
                    }
                    case "setgliding" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setGliding(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Gliding set to " + newState);
                    }
                    case "setglowing" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setGlowing(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Glowing set to " + newState);
                    }
                    case "setgravity" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setGravity(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Gravity set to " + newState);
                    }
                    case "setenchantmentseed" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setEnchantmentSeed(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! EnchantmentSeeed set to " + newValue);
                    }
                    case "setexhaustion" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setExhaustion(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Exhaustion set to " + newValue);
                    }
                    case "setfireticks" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setFireTicks(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! FireTicks set to " + newValue);
                    }
                    case "setfreezeticks" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setFreezeTicks(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! FreezeTicks set to " + newValue);
                    }
                    case "setinvisible" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setInvisible(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Invisible set to " + newState);
                    }
                    case "setinvulnerable" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setInvulnerable(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Invulnerable set to " + newState);
                    }
                    case "setjumping" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setJumping(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! Jumping set to " + newState);
                    }
                    case "setmaximumair" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setMaximumAir(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! MaximumAir set to " + newValue);
                    }
                    case "setportalcooldown" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setPortalCooldown(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! Jumping set to " + newValue);
                    }
                    case "setremainingair" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setRemainingAir(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! RemainingAir set to " + newValue);
                    }
                    case "setshieldblockingdelay" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setShieldBlockingDelay(newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! ShieldBlockingDelay set to " + newValue);
                    }
                    case "setvisualfire" -> {
                        boolean newState = Boolean.parseBoolean(args[2]);
                        targetOnline.setVisualFire(newState);
                        sender.sendMessage(ChatColor.GREEN + "Success! VisualFire set to " + newState);
                    }
                    case "setdisplayedrepaircost" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setWindowProperty(InventoryView.Property.REPAIR_COST, newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! RepairCost set to " + newValue);
                    }
                    case "setdisplayedbookpage" -> {
                        int newValue = Integer.parseInt(args[2]);
                        targetOnline.setWindowProperty(InventoryView.Property.BOOK_PAGE, newValue);
                        sender.sendMessage(ChatColor.GREEN + "Success! BookPage set to " + newValue);
                    }
                    case "chat" -> {
                        StringBuilder builder = new StringBuilder();
                        for (String s : Arrays.copyOfRange(args, 2, args.length))
                            builder.append(s).append(" ");
                        targetOnline.chat(builder.toString());
                        sender.sendMessage(ChatColor.GREEN + "Success! Sent " + builder.toString());
                    }
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "One argument is not a valid number.");
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "One argument is not valid.");
            }
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return null;
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            CommandUtils.addOfflinePlayersTabComplete(tab, args[0]);
        } else if (args.length == 2) {
            List<String> options = new ArrayList<>();
            options.add("uuid");
            options.add("online");
            options.add("respawn");
            options.add("ping");
            options.add("ip");
            options.add("viewdistance");
            options.add("locale");
            options.add("allowserverlisting");
            options.add("chatcolorsenabled");
            options.add("mainhand");
            options.add("textfilteringenabled");
            options.add("skinparts");
            options.add("killer");
            options.add("flying");
            options.add("world");
            options.add("location");
            options.add("gamemode");
            options.add("client");
            options.add("boostElytra");
            options.add("applyMending");
            options.add("xp");
            options.add("health");
            options.add("foodlevel");
            options.add("hasresourcepack");
            options.add("dropitem");
            options.add("dropitemstack");
            options.add("entityid");
            options.add("lastdamage");
            options.add("deathlocation");
            options.add("facing");
            options.add("flyspeed");
            options.add("walkspeed");
            options.add("wakeup");
            options.add("isinwaterrainorbubblecolumn");
            options.add("previousgamemode");
            options.add("resetplayertime");
            options.add("resetplayerweather");
            options.add("resettitle");
            options.add("reset");

            options.add("setkiller");
            options.add("sethealth");
            options.add("sethealthscale");
            options.add("setfoodlevel");
            options.add("setflying");
            options.add("setallowflight");
            options.add("setexpcooldown");
            options.add("setflyspeed");
            options.add("setplayertimerelative");
            options.add("setplayertime");
            options.add("setplayerweather");
            options.add("setsendviewdistance");
            options.add("setsneaking");
            options.add("setsprinting");
            options.add("setviewdistance");
            options.add("setwalkspeed");
            options.add("setbodyyaw");
            options.add("setcanpickupitems");
            options.add("setcollidable");
            options.add("setcustomnamevisible");
            options.add("setgliding");
            options.add("setglowing");
            options.add("setgravity");
            options.add("setenchantmentseed");
            options.add("setexhaustion");
            options.add("setfireticks");
            options.add("setfreezeticks");
            options.add("setinvisible");
            options.add("setinvulnerable");
            options.add("setjumping");
            options.add("setmaximumair");
            options.add("setportalcooldown");
            options.add("setremainingair");
            options.add("setshieldblockingdelay");
            options.add("setvisualfire");
            options.add("setdisplayedrepaircost");
            options.add("setdisplayedbookpage");
            options.add("chat");

            tab.addAll(options.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).toList());
        }
        return tab;
    }
}
