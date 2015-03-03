package skinschanger.bungee.listeners;

import java.lang.reflect.*;
import skinschanger.bungee.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import skinschanger.shared.utils.*;
import net.md_5.bungee.event.*;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.connection.*;
import skinschanger.shared.format.*;

public class LoginListener implements Listener
{
    private Field profileField;
    
    public LoginListener() {
        this.profileField = null;
    }
    
    @EventHandler
    public void onPreLogin(final LoginEvent event) {
        final String name = event.getConnection().getName();
        if (Skins.getInstance().getSkinStorage().hasLoadedSkinData(name) && !Skins.getInstance().getSkinStorage().getLoadedSkinData(name).isTooDamnOld()) {
            Skins.getInstance().logInfo("Skin for player " + name + " is already cached");
            return;
        }
        event.registerIntent((Plugin)Skins.getInstance());
        ProxyServer.getInstance().getScheduler().runAsync((Plugin)Skins.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    final SkinProfile profile = SkinGetUtils.getSkinProfile(name);
                    Skins.getInstance().getSkinStorage().addSkinData(name, profile);
                    Skins.getInstance().logInfo("Skin for player " + name + " was succesfully fetched and cached");
                }
                catch (SkinGetUtils.SkinFetchFailedException e) {
                    Skins.getInstance().logInfo("Skin fetch failed for player " + name + ": " + e.getMessage());
                    return;
                }
                finally {
                    event.completeIntent((Plugin)Skins.getInstance());
                }
                event.completeIntent((Plugin)Skins.getInstance());
            }
        });
    }
    
    @EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        final String name = event.getPlayer().getName();
        if (Skins.getInstance().getSkinStorage().hasLoadedSkinData(name)) {
            try {
                final SkinProperty skinprofile = Skins.getInstance().getSkinStorage().getLoadedSkinData(name).getPlayerSkinProperty();
                final InitialHandler handler = (InitialHandler)event.getPlayer().getPendingConnection();
                final LoginResult.Property[] properties = { new LoginResult.Property(skinprofile.getName(), skinprofile.getValue(), skinprofile.getSignature()) };
                final LoginResult profile = new LoginResult(event.getPlayer().getUniqueId().toString(), properties);
                this.getProfileField().set(handler, profile);
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    private Field getProfileField() throws NoSuchFieldException, SecurityException {
        if (this.profileField == null) {
            (this.profileField = InitialHandler.class.getDeclaredField("loginProfile")).setAccessible(true);
        }
        return this.profileField;
    }
}
