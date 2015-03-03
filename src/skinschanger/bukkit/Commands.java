package skinschanger.bukkit;

import java.util.concurrent.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import skinschanger.shared.utils.*;
import skinschanger.shared.format.*;

public class Commands implements CommandExecutor
{
    private ExecutorService executor;
    
    public Commands() {
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("change")) {
                return this.onChangeCommand(sender, cmd, label, args);
            }
            if (args[0].equalsIgnoreCase("default")) {
                return this.onDefaultCommand(sender, cmd, label, args);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return this.onHelpCommand(sender, cmd, label, args);
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skin help&e' for help."));
        return true;
    }
    
    public boolean onHelpCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("skinschanger.use")) {
            sender.sendMessage("You don't have permission to do this");
            return true;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c============&e[ &aSkinsChanger Help &e]&c============&e["));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin change <skinname> &9-&a Changes your skin. &7&o//requires relog"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin default &9-&a Reverts your default skin &7&o//requires relog"));
        return false;
    }
    
    public boolean onChangeCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("skinschanger.use")) {
            sender.sendMessage("You don't have permission to do this");
            return true;
        }
        if (args.length == 2) {
            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    final String name = sender.getName();
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(args[1]);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou've changed your skin! (relog to see the changes)"));
                    }
                    catch (SkinGetUtils.SkinFetchFailedException e) {
                        sender.sendMessage(ChatColor.RED + "Skin fetch failed: " + e.getMessage());
                    }
                }
            });
        }
        return false;
    }
    
    public boolean onDefaultCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("skinschanger.use")) {
            sender.sendMessage("You don't have permission to do this");
            return true;
        }
        if (args.length == 1) {
            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    final String name = sender.getName();
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(name);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted your skin! (relog to see the changes)"));
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
