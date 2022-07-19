package wtf.melonthedev.projectplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.melonthedev.projectplugin.Main;

import java.util.ArrayList;
import java.util.List;

public class ColorCodesCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChatColor colorinfo = ChatColor.AQUA;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("colors")) {
                sender.sendMessage(colorinfo + "---------- Color Codes ----------");
                sender.sendMessage(colorinfo + "You can use the following colors in chat: ");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_RED + "DARK_RED: <dark_red>");
                sender.sendMessage(colorinfo + "-" + ChatColor.RED + "RED (aka. coral): <red>");
                sender.sendMessage(colorinfo + "-" + ChatColor.GOLD + "GOLD (aka. orange): <gold>");
                sender.sendMessage(colorinfo + "-" + ChatColor.YELLOW + "YELLOW: <yellow>");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_GREEN + "DARK GREEN: <dark_green>");
                sender.sendMessage(colorinfo + "-" + ChatColor.GREEN + "GREEN (aka. lime): <green>");
                sender.sendMessage(colorinfo + "-" + ChatColor.AQUA + "AQUA: <aqua>");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_AQUA + "DARK AQUA: <dark_aqua>");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_BLUE + "DARK BLUE: <dark_blue>");
                sender.sendMessage(colorinfo + "-" + ChatColor.BLUE + "BLUE: <blue>");
                sender.sendMessage(colorinfo + "-" + ChatColor.LIGHT_PURPLE + "LIGHT PURPLE (aka. pink): <light_purple>");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_PURPLE + "DARK PURPLE: <dark_purple>");
                sender.sendMessage(colorinfo + "-" + ChatColor.WHITE + "WHITE: <white>");
                sender.sendMessage(colorinfo + "-" + ChatColor.GRAY + "GRAY: <gray>");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_GRAY + "DARK GRAY: <dark_gray>");
                sender.sendMessage(colorinfo + "-" + ChatColor.BLACK + "BLACK: <black>");
                sender.sendMessage(ChatColor.GRAY + "Du kannst auch HEX Farben verwenden! Schreiben einfach '<#' dann den HEX Code und am Ende des HEX Code wieder '>'!");
                sender.sendMessage(colorinfo + "---------------------------------");
                return true;
            } else if (args[0].equalsIgnoreCase("legacy")) {
                sender.sendMessage(colorinfo + "---------- Color Codes ----------");
                sender.sendMessage(ChatColor.RED + "!!! These codes are outdated and will no longer work. !!! ");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_RED + "DARK_RED: &4");
                sender.sendMessage(colorinfo + "-" + ChatColor.RED + "RED (aka. coral): &c");
                sender.sendMessage(colorinfo + "-" + ChatColor.GOLD + "GOLD (aka. orange): &6");
                sender.sendMessage(colorinfo + "-" + ChatColor.YELLOW + "YELLOW: &e");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_GREEN + "DARK GREEN: &2");
                sender.sendMessage(colorinfo + "-" + ChatColor.GREEN + "GREEN (aka. lime): &a");
                sender.sendMessage(colorinfo + "-" + ChatColor.AQUA + "AQUA: &b");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_AQUA + "DARK AQUA: &3");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_BLUE + "DARK BLUE: &1");
                sender.sendMessage(colorinfo + "-" + ChatColor.BLUE + "BLUE: &9");
                sender.sendMessage(colorinfo + "-" + ChatColor.LIGHT_PURPLE + "LIGHT PURPLE (aka. pink): &d");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_PURPLE + "DARK PURPLE: &5");
                sender.sendMessage(colorinfo + "-" + ChatColor.WHITE + "WHITE: &f");
                sender.sendMessage(colorinfo + "-" + ChatColor.GRAY + "GRAY: &7");
                sender.sendMessage(colorinfo + "-" + ChatColor.DARK_GRAY + "DARK GRAY: &8");
                sender.sendMessage(colorinfo + "-" + ChatColor.BLACK + "BLACK: &0");
                sender.sendMessage(colorinfo + "---------------------------------");
            } else if (args[0].equalsIgnoreCase("extras")) {
                sender.sendMessage(colorinfo + "---------- Color Codes Extras ----------");
                sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(colorinfo + "- RAINBOW: <rainbow> - "), Main.getPlugin().getMiniMessageComponent("<rainbow>Really long example")));
                sender.sendMessage(colorinfo + "- BOLD: <bold> or <b> - " + ChatColor.BOLD + " Example");
                sender.sendMessage(colorinfo + "- ITALIC: <italic> or <em> or <i> - " + ChatColor.ITALIC + " Example");
                sender.sendMessage(colorinfo + "- UNDERLINED: <underlined> or <u> - " + ChatColor.UNDERLINE + " Example");
                sender.sendMessage(colorinfo + "- STRIKETHROUGH: <strikethrough> or <st> - " + ChatColor.STRIKETHROUGH + " Example");
                sender.sendMessage(colorinfo + "- OBFUSCATED: <obfuscated> or <obf> - " + ChatColor.MAGIC + " Example");
                sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(colorinfo + "- HOVER: <hover:show_text:'test'> - "), Main.getPlugin().getMiniMessageComponent("<aqua><hover:show_text:'test'>Example (hover me)")));
                sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(colorinfo + "- KEYBIND: <key:key.jump> - "), Main.getPlugin().getMiniMessageComponent("<aqua>Example: Your jump key is <key:key.jump>")));
                sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(colorinfo + "- GRADIENT: <gradient:[color1]:[color...]> - "), Main.getPlugin().getMiniMessageComponent("<gradient:green:blue>Really long example")));
                sender.sendMessage(Component.join(JoinConfiguration.noSeparators(), Component.text(colorinfo + "- FONT: <font:[key]> - "), Main.getPlugin().getMiniMessageComponent("Example: <font:uniform>Uniform <font:alt>Alt")));
                sender.sendMessage(colorinfo + "---------------------------------");
            }
            /*for (ChatColor color : ChatColor.values()) {
                if (!args[0].equalsIgnoreCase(color.name())) continue;
                sender.sendMessage(colorinfo + "Der Farbcode für " + args[0] + " ist <" + color.name() + ">");
                sender.sendMessage(color + "So wird es bei Benutzung aussehen :)");
                sender.sendMessage(colorinfo + "-------------------------------");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Diese Farbe wurde nicht gefunden: '" + args[0] + "'. Such deine Farbe in der Liste: /colorcodes");*/
        } else {
            sender.sendMessage(colorinfo + "---------- Color Codes ----------");
            sender.sendMessage(colorinfo + "Type '/colorcodes colors' to see all normal colors.");
            sender.sendMessage(colorinfo + "Type '/colorcodes extras' to see all extra formatting options.");
            sender.sendMessage(colorinfo + "Type '/colorcodes legacy' to see all legacy colorcodes.");
            sender.sendMessage(colorinfo + "---------------------------------");
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("colors");
            tab.add("extras");
            tab.add("legacy");
        }
        return tab;
    }
}
