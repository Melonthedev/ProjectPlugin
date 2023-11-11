package wtf.melonthedev.projectplugin.commands.moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TargetCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        if (args[1].equalsIgnoreCase("untarget")) {
            for (Entity entity : target.getNearbyEntities(100, 100, 100)) {
                if (!(entity instanceof Mob mob)) continue;
                mob.setTarget(null);
            }
            sender.sendMessage("Mobs are no longer targeting " + target.getName());
            return true;
        }

        EntityType type = args[1].equals("*") ? null : EntityType.valueOf(args[1]);
        int radius = Integer.parseInt(args[2]);
        int max = Integer.parseInt(args[3]);

        int count = 0;
        StringBuilder sb = new StringBuilder();

        for (Entity entity : target.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof Mob mob)) continue;
            if (type == null || entity.getType() == type) {
                mob.setTarget(target);
                sb.append(entity.getType().name()).append(", ");
                count++;
                if (count >= max) break;
            }
        }
        sb.append("are now targeting ").append(target.getName());
        sender.sendMessage(sb.toString());
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(player -> tab.add(player.getName()));
            return tab;
        } else if (args.length == 2) {
            tab.add("untarget");
            tab.add("*");
            for (EntityType value : EntityType.values()) {
                tab.add(value.name());
            }
        } else if (args.length == 3) {
            tab.add("5");
            tab.add("10");
            tab.add("30");
            tab.add("50");
            tab.add("100");
        } else if (args.length == 4) {
            tab.add("1");
            tab.add("2");
            tab.add("5");
            tab.add("4");
            tab.add("5");
            tab.add("10");
            tab.add("100");
        }
        return tab;
    }
}
