package skinschanger.bungee;

import net.md_5.bungee.api.*;
import skinschanger.shared.utils.*;
import net.md_5.bungee.api.chat.*;
import skinschanger.shared.format.*;
import net.md_5.bungee.api.plugin.*;

public class Commands extends Command
{
    public Commands() {
        super("skin", "skinschanger.use", new String[] { "skin" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("default")) {
                this.onDefaultCommand(sender, args);
            }
            else if (args[0].equalsIgnoreCase("change")) {
                this.onChangeCommand(sender, args);
            }
            else if (args[0].equalsIgnoreCase("help")) {
                this.onHelpCommand(sender, args);
            }
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skin help&e' for help."));
        }
    }
    
    public void onHelpCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c============&e[ &aSkinsChanger Help &e]&c============&e["));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin change <skinname> &9-&a Changes your skin. &7&o//requires relog"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin default &9-&a Reverts your default skin &7&o//requires relog"));
        }
    }
    
    public void onChangeCommand(final CommandSender sender, final String[] args) {
        if (args.length == 2) {
            final String name = sender.getName();
            ProxyServer.getInstance().getScheduler().runAsync((Plugin)Skins.getInstance(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(args[1]);
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've changed your skin! (relog to see the changes)"));
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
    
    public void onDefaultCommand(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            final String name = sender.getName();
            ProxyServer.getInstance().getScheduler().runAsync((Plugin)Skins.getInstance(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        final SkinProfile profile = SkinGetUtils.getSkinProfile(sender.getName());
                        Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                        Skins.getInstance().getSkinStorage().saveData();
                        final TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted your skin! (relog to see the changes)"));
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
