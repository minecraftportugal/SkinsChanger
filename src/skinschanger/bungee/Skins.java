package skinschanger.bungee;

import java.util.logging.*;
import skinschanger.bungee.storage.*;
import skinschanger.bungee.listeners.*;
import net.md_5.bungee.api.plugin.*;

public class Skins extends Plugin
{
    private static Skins instance;
    private Logger log;
    private SkinStorage storage;
    
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
        this.storage.loadData();
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new LoginListener());
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Commands());
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new AdminCommands());
    }
    
    public void onDisable() {
        this.storage.saveData();
        Skins.instance = null;
    }
}
