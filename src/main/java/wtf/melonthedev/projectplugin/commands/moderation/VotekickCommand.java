package wtf.melonthedev.projectplugin.commands.moderation;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.melonthedev.projectplugin.Main;

import java.util.*;

public class VotekickCommand implements TabExecutor, Listener {

    int cooldown = 0; //Minutes until the next votekick can be executed
    int banCooldown = 0; //Minutes until the ban from target ends
    int requiredVotes; //Contains how many votes are missing to get a result
    boolean voting = false; //True if there is a running vote
    HashMap<CommandSender, Boolean> votes = new HashMap<>(); //Players/Senders who voted with their vote
    String targetName; //Name of target
    Player target; //Target Player who will be kicked
    CommandSender leader; //The sender who started the votekick
    String prefix = ChatColor.AQUA + "" + ChatColor.BOLD + "[VoteKick] " + ChatColor.RESET + ChatColor.AQUA;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //INTERFACE MODE
        if (args.length == 0 && voting && target != null && sender instanceof Player) {
            Inventory inv = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Vote for kicking " + target.getName());
            for (int i = 0; i < 27; i++) {
                ItemStack placeholder = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
                ItemMeta meta = placeholder.getItemMeta();
                Objects.requireNonNull(meta).setDisplayName(" ");
                placeholder.setItemMeta(meta);
                inv.setItem(i, placeholder);
            }
            ItemStack yes = new ItemStack(Material.GREEN_CONCRETE);
            ItemStack no = new ItemStack(Material.RED_CONCRETE);
            ItemMeta meta = yes.getItemMeta();
            Objects.requireNonNull(meta).setDisplayName("Yes");
            yes.setItemMeta(meta);
            meta = no.getItemMeta();
            Objects.requireNonNull(meta).setDisplayName("No");
            no.setItemMeta(meta);
            inv.setItem(15, yes);
            inv.setItem(11, no);
            Player player = (Player) sender;
            player.openInventory(inv);
        } else if (args.length == 0 && !voting) {
            if (cooldown > 0) {
                sender.sendMessage(prefix + "Ein votekick kann erst wieder in " + cooldown + " Minuten ausgeführt werden.");
                return true;
            }
            sender.sendMessage(prefix + "Es läuft gerade kein Votekick. Starte einen mit '/votekick <Player>'");
        } else if (args.length == 1 && args[0].equals("yes")) {
            if (!voting) {
                sender.sendMessage(prefix + "Es läuft gerade kein Votekick. Starte einen mit '/votekick <Player>'");
                return true;
            }
            vote("yes", sender);
        } else if (args.length == 1 && args[0].equals("no")) {
            if (!voting) {
                sender.sendMessage(prefix + "Es läuft gerade kein Votekick. Starte einen mit '/votekick <Player>'");
                return true;
            }
            vote("no", sender);
        } else if (args.length == 1 && args[0].equals("info")) {
            sender.sendMessage(prefix + ChatColor.UNDERLINE + "Votekick:" + ChatColor.RESET + ChatColor.AQUA + "\nVotekick ist eine Funktion, mit der normale Spieler einen anderen Spieler für 5 Minuten kicken können. Votekick läuft nach dem Start 5 Minuten. Nach dem Ergebniss gibt es einen Votekick-cooldown für 30 minuten.");
        } else if (args.length == 1 && args[0].equals("cancel")) {
            if (sender == leader || sender.isOp()) {
                resetVotekick();
                sender.sendMessage(prefix + "Du hast den aktuellen Votekick beendet.");
                return true;
            }
            sender.sendMessage(prefix + "Du musst der Leader sein, um den Votekick zu beenden.");
        } else if (args.length == 1 && Bukkit.getPlayer(args[0]) != null) { //Create Votekick eg /votekick Player
            if (cooldown > 0) {
                sender.sendMessage(prefix + "Ein votekick kann erst wieder in " + cooldown + " Minuten ausgeführt werden.");
                return true;
            }
            if (voting) {
                sender.sendMessage(prefix + "Du kannst erst einen neuen Votekick ausführen, wenn der aktuelle beendet ist.");
                return true;
            }
            target = Bukkit.getPlayer(args[0]);
            assert target != null;
            targetName = target.getName();
            leader = sender;
            banCooldown = 5;
            voting = true;
            cooldown = 30;
            TextComponent component = new TextComponent(prefix + sender.getName() + " will " + target.getName() + " votekicken. ");
            TextComponent yes = new TextComponent("" + ChatColor.GREEN + ChatColor.BOLD + "[Yes] ");
            TextComponent no = new TextComponent("" + ChatColor.RED + ChatColor.BOLD + "[No]");
            component.addExtra(yes);
            component.addExtra(no);
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/votekick yes"));
            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/votekick no"));
            Bukkit.spigot().broadcast(component);
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                if (!voting) return;
                endVotekickWithResult();
            }, 6000);
        } else sender.sendMessage(prefix + ChatColor.RED + "Syntaxerror: /votekick | /votekick <yes/no> | /votekick <OnlinePlayer> | /votekick info");
        return false;
    }

    public void vote(String vote, CommandSender sender) {
        if (vote.equalsIgnoreCase("yes")) {
            votes.put(sender, true);
            sender.sendMessage(prefix + "Du hast für den votekick gevotet!");

            return;
        }
        votes.put(sender, false);
        sender.sendMessage(prefix + "Du hast gegen den votekick gevotet!");
    }

    public void endVotekickWithResult() {
        StringBuilder yesVotes = new StringBuilder();
        StringBuilder noVotes = new StringBuilder();
        for (Map.Entry<CommandSender, Boolean> entry : votes.entrySet()) {
            if (entry.getValue()) yesVotes.append("-").append(entry.getKey().getName()).append("\n    ");
            if (!entry.getValue()) noVotes.append("-").append(entry.getKey().getName()).append("\n    ");
        }
        Bukkit.broadcastMessage(prefix + "Votekick Ergebnis: \n    " + ChatColor.GREEN + "YES:\n    " + yesVotes  + ChatColor.RED + "NO:\n    " + noVotes);
        Bukkit.broadcastMessage(prefix + ChatColor.BOLD + "\n" + target.getName() + " wird für 5 Minuten gekickt.");
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            target.kickPlayer("Du wurdest für 5 Minuten gevotekickt.\nDu kannst in " + banCooldown + " Minuten wieder joinen.");
            startBanCountdown();
            resetVotekick();
        }, 60);
    }

    private void startBanCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (banCooldown <= 0 || !voting) {
                    cancel();
                    return;
                }
                banCooldown--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 1200);
    }

    public void resetVotekick() {
        target = null;
        leader = null;
        voting = false;
        cooldown = 0;
        banCooldown = 0;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (targetName == null) return;
        if (!event.getPlayer().getName().equals(targetName)) return;
        if (banCooldown <= 0) return;
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Du wurdest für 5 Minuten gevotekickt.\nDu kannst in " + banCooldown + " Minuten wieder joinen.");
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().startsWith(ChatColor.AQUA + "Vote for kicking "))) return;
        event.setCancelled(true);
        if (event.getClickedInventory() != null && event.getClickedInventory() == event.getInventory()) {
            if (event.getSlot() == 11) {
                vote("no", event.getWhoClicked());
                event.getWhoClicked().closeInventory();
            }
            else if (event.getSlot() == 15) {
                vote("yes", event.getWhoClicked());
                event.getWhoClicked().closeInventory();
            }
            else if (event.getSlot() == 16) {
                endVotekickWithResult();
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (voting) {
            tab.add("yes");
            tab.add("no");
            if (sender == leader || sender.isOp()) tab.add("cancel");
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) tab.add(player.getName());
            tab.add("info");
        }
        return tab;
    }
}
