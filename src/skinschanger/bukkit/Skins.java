package skinschanger.bukkit;

import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import java.util.logging.*;
import skinschanger.bukkit.storage.*;
import org.bukkit.command.*;
import skinschanger.bukkit.listeners.*;
import org.bukkit.plugin.*;

public class Skins extends JavaPlugin implements Listener
{
    private static Skins instance;
    private Logger log;
    SkinStorage storage;
    
    public Skins() {
        this.storage = new SkinStorage();
    }
    
    public static Skins getInstance() {
        return Skins.instance;
    }
    
    public void logInfo(final String message) {
        this.log.info(message);
    }
    
    public SkinStorage getSkinStorage() {
        return this.storage;
    }
    
    public void onEnable() {
        Skins.instance = this;
        this.log = this.getLogger();
        this.getCommand("skin").setExecutor((CommandExecutor)new Commands());
        this.getCommand("skina").setExecutor((CommandExecutor)new AdminCommands());
        this.getServer().getPluginManager().registerEvents((Listener)new LoginListener(), (Plugin)this);
    }
    
    public void onDisable() {
        Skins.instance = null;
    }
}
