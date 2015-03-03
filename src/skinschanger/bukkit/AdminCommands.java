package skinschanger.bukkit;

import java.util.concurrent.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import skinschanger.shared.utils.*;
import skinschanger.shared.format.*;

public class AdminCommands implements CommandExecutor
{
    private ExecutorService executor;
    
    public AdminCommands() {
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("change")) {
                return this.onAdminChangeCommand(sender, cmd, label, args);
            }
            if (args[0].equalsIgnoreCase("default")) {
                return this.onAdminDefaultCommand(sender, cmd, label, args);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return this.onAdminHelpCommand(sender, cmd, label, args);
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skina help&e' for help."));
        return true;
    }
    
    public boolean onAdminHelpCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("skinschanger.admin")) {
            sender.sendMessage("You don't have permission to do this");
            return true;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c===========&e[ &aSkinsChanger Admin Help &e]&c===========&e["));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skinadmin change <player> <skinname> &9-&a Changes your skin. &7&o//requires relog"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skinadmin default <player> &9-&a Reverts your default skin &7&o//requires relog"));
        return false;
    }
    
    public boolean onAdminChangeCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("skinschanger.admin")) {
            sender.sendMessage("You don't have permission to do this");
            return true;
        }
        if (args.length == 3) {
            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    final String name = args[1];
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(args[2]);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou've changed " + ChatColor.YELLOW + args[1] + ChatColor.AQUA + "'s skin!"));
                    }
                    catch (SkinGetUtils.SkinFetchFailedException e) {
                        sender.sendMessage(ChatColor.RED + "Skin fetch failed: " + e.getMessage());
                    }
                }
            });
        }
        return false;
    }
    
    public boolean onAdminDefaultCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("skinschanger.admin")) {
            sender.sendMessage("You don't have permission to do this");
            return true;
        }
        if (args.length == 2) {
            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    final String name = args[1];
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(name);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted " + ChatColor.YELLOW + args[1] + ChatColor.AQUA + "'s skin!"));
                    }
                    catch (SkinGetUtils.SkinFetchFailedException e) {
                        sender.sendMessage(ChatColor.RED + "Skin fetch failed: " + e.getMessage());
                    }
                }
            });
        }
        return false;
    }
}
