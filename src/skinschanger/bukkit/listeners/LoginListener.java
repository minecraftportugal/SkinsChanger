package skinschanger.bukkit.listeners;

import skinschanger.bukkit.storage.*;
import skinschanger.bukkit.*;
import skinschanger.shared.utils.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.entity.*;
import skinschanger.shared.format.*;

public class LoginListener implements Listener
{
    SkinStorage storage;
    
    public LoginListener() {
        this.storage = new SkinStorage();
    }
    
    public SkinStorage getSkinStorage() {
        return this.storage;
    }
    
    @EventHandler
    public void onPreLoginEvent(final AsyncPlayerPreLoginEvent event) {
        final String name = event.getName();
        Skins.getInstance().getSkinStorage().loadData();
        if (Skins.getInstance().getSkinStorage().hasLoadedSkinData(name) && !Skins.getInstance().getSkinStorage().getLoadedSkinData(name).isTooDamnOld()) {
            Skins.getInstance().logInfo("Skin for player " + name + " is already cached");
            return;
        }
        try {
            final SkinProfile profile = SkinGetUtils.getSkinProfile(name);
            Skins.getInstance().getSkinStorage().addSkinData(name, profile);
            Skins.getInstance().logInfo("Skin for player " + name + " was succesfully fetched and cached");
        }
        catch (SkinGetUtils.SkinFetchFailedException e) {
            Skins.getInstance().logInfo("Skin fetch failed for player " + name + ": " + e.getMessage());
        }
    }
    
    @EventHandler
    public void onLoginEvent(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String name = event.getPlayer().getName();
        Skins.getInstance().getSkinStorage().loadData();
        if (Skins.getInstance().getSkinStorage().hasLoadedSkinData(player.getName())) {
            final SkinProperty skinproperty = Skins.getInstance().getSkinStorage().getLoadedSkinData(player.getName()).getPlayerSkinProperty();
            final WrappedGameProfile wrappedprofile = WrappedGameProfile.fromPlayer(player);
            final WrappedSignedProperty wrappedproperty = WrappedSignedProperty.fromValues(skinproperty.getName(), skinproperty.getValue(), skinproperty.getSignature());
            if (!wrappedprofile.getProperties().containsKey((Object)wrappedproperty.getName())) {
                wrappedprofile.getProperties().put((Object)wrappedproperty.getName(), (Object)wrappedproperty);
            }
        }
    }
}
