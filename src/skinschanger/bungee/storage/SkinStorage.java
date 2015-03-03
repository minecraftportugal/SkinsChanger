package skinschanger.bungee.storage;

import skinschanger.bungee.*;
import skinschanger.shared.format.*;
import java.io.*;
import net.md_5.bungee.config.*;
import java.util.*;

public class SkinStorage
{
    private LinkedHashMap<String, SkinProfile> skins;
    private final long maxHeldSkinDataNumber = 10000L;
    
    public SkinStorage() {
        this.skins = new LinkedHashMap<String, SkinProfile>(150, 0.75f, true);
    }
    
    public boolean hasLoadedSkinData(final String name) {
        return this.skins.containsKey(name.toLowerCase());
    }
    
    public void addSkinData(final String name, final SkinProfile data) {
        this.skins.put(name.toLowerCase(), data);
    }
    
    public void removeSkinData(final String name) {
        this.skins.remove(name.toLowerCase());
    }
    
    public SkinProfile getLoadedSkinData(final String name) {
        return this.skins.get(name.toLowerCase());
    }
    
    public Map<String, SkinProfile> getSkinData() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends SkinProfile>)this.skins);
    }
    
    public void loadData() {
        int loadedSkins = 0;
        final File datafile = new File(Skins.getInstance().getDataFolder(), "playerdata.yml");
        if (datafile.exists()) {
            try {
                final Configuration configuration = ConfigurationProvider.getProvider((Class)YamlConfiguration.class).load(datafile);
                for (final String name : configuration.getKeys()) {
                    if (loadedSkins >= 10000L) {
                        return;
                    }
                    final long creationDate = configuration.getLong(String.valueOf(name) + ".timestamp");
                    final String propertyname = configuration.getString(String.valueOf(name) + ".propertyname");
                    final String propertyvalue = configuration.getString(String.valueOf(name) + ".propertyvalue");
                    final String propertysignature = configuration.getString(String.valueOf(name) + ".propertysignature");
                    final SkinProfile skinData = new SkinProfile(new SkinProperty(propertyname, propertyvalue, propertysignature), creationDate);
                    this.addSkinData(name, skinData);
                    ++loadedSkins;
                }
            }
            catch (IOException ex) {}
        }
    }
    
    public void saveData() {
        final File datafile = new File(Skins.getInstance().getDataFolder(), "playerdata.yml");
        final Configuration configuration = new Configuration();
        for (final Map.Entry<String, SkinProfile> entry : this.getSkinData().entrySet()) {
            configuration.set(String.valueOf(entry.getKey()) + ".timestamp", (Object)entry.getValue().getCreationDate());
            configuration.set(String.valueOf(entry.getKey()) + ".propertyname", (Object)entry.getValue().getPlayerSkinProperty().getName());
            configuration.set(String.valueOf(entry.getKey()) + ".propertyvalue", (Object)entry.getValue().getPlayerSkinProperty().getValue());
            configuration.set(String.valueOf(entry.getKey()) + ".propertysignature", (Object)entry.getValue().getPlayerSkinProperty().getSignature());
        }
        try {
            datafile.getParentFile().mkdirs();
            datafile.createNewFile();
            ConfigurationProvider.getProvider((Class)YamlConfiguration.class).save(configuration, datafile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
