package skinschanger.bukkit.storage;

import skinschanger.bukkit.*;
import skinschanger.shared.format.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.*;
import java.util.*;
import java.io.*;

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
        final FileConfiguration data = (FileConfiguration)YamlConfiguration.loadConfiguration(datafile);
        final ConfigurationSection cs = data.getConfigurationSection("");
        if (cs == null) {
            return;
        }
        for (final String name : cs.getKeys(false)) {
            if (loadedSkins >= 10000L) {
                return;
            }
            final long creationDate = cs.getLong(String.valueOf(name) + ".timestamp");
            final String propertyname = cs.getString(String.valueOf(name) + ".propertyname");
            final String propertyvalue = cs.getString(String.valueOf(name) + ".propertyvalue");
            final String propertysignature = cs.getString(String.valueOf(name) + ".propertysignature");
            final SkinProfile skinData = new SkinProfile(new SkinProperty(propertyname, propertyvalue, propertysignature), creationDate);
            this.addSkinData(name, skinData);
            ++loadedSkins;
        }
    }
    
    public void saveData() {
        final File datafile = new File(Skins.getInstance().getDataFolder(), "playerdata.yml");
        final FileConfiguration data = (FileConfiguration)new YamlConfiguration();
        for (final Map.Entry<String, SkinProfile> entry : this.getSkinData().entrySet()) {
            data.set(String.valueOf(entry.getKey()) + ".timestamp", (Object)entry.getValue().getCreationDate());
            data.set(String.valueOf(entry.getKey()) + ".propertyname", (Object)entry.getValue().getPlayerSkinProperty().getName());
            data.set(String.valueOf(entry.getKey()) + ".propertyvalue", (Object)entry.getValue().getPlayerSkinProperty().getValue());
            data.set(String.valueOf(entry.getKey()) + ".propertysignature", (Object)entry.getValue().getPlayerSkinProperty().getSignature());
        }
        try {
            data.save(datafile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
