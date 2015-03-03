package skinschanger.bungee;

import net.md_5.bungee.api.*;
import skinschanger.shared.utils.*;
import net.md_5.bungee.api.chat.*;
import skinschanger.shared.format.*;
import net.md_5.bungee.api.plugin.*;

public class AdminCommands extends Command
{
    public AdminCommands() {
        super("skina", "skinschanger.admin", new String[] { "skina" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("default")) {
                this.onAdminDefaultCommand(sender, args);
            }
            else if (args[0].equalsIgnoreCase("change")) {
                this.onAdminChangeCommand(sender, args);
            }
            else if (args[0].equalsIgnoreCase("help")) {
                this.onAdminHelpCommand(sender, args);
            }
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skina help&e' for help."));
        }
    }
    
    public void onAdminHelpCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c===========&e[ &aSkinsChanger Admin Help &e]&c===========&e["));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skinadmin change <player> <skinname> &9-&a Changes your skin. &7&o//requires relog"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skinadmin default <player> &9-&a Reverts your default skin &7&o//requires relog"));
        }
    }
    
    public void onAdminChangeCommand(final CommandSender sender, final String[] args) {
        if (args.length == 3) {
            final String name = args[1];
            ProxyServer.getInstance().getScheduler().runAsync((Plugin)Skins.getInstance(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(args[2]);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've changed " + ChatColor.YELLOW + args[1] + ChatColor.AQUA + "'s skin!"));
                        component.setColor(ChatColor.AQUA);
                        sender.sendMessage((BaseComponent)component);
                    }
                    catch (SkinGetUtils.SkinFetchFailedException e) {
                        final TextComponent component = new TextComponent("Skin fetch failed: " + e.getMessage());
                        component.setColor(ChatColor.RED);
                        sender.sendMessage((BaseComponent)component);
                    }
                }
            });
        }
    }
    
    public void onAdminDefaultCommand(final CommandSender sender, final String[] args) {
        if (args.length == 2) {
            final String name = args[1];
            ProxyServer.getInstance().getScheduler().runAsync((Plugin)Skins.getInstance(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(args[1]);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted " + ChatColor.YELLOW + args[1] + ChatColor.AQUA + "'s skin!"));
                        component.setColor(ChatColor.AQUA);
                        sender.sendMessage((BaseComponent)component);
                    }
                    catch (SkinGetUtils.SkinFetchFailedException e) {
                        final TextComponent component = new TextComponent("Skin fetch failed: " + e.getMessage());
                        component.setColor(ChatColor.RED);
                        sender.sendMessage((BaseComponent)component);
                    }
                }
            });
        }
    }
}
