package skinschanger.shared.utils;

import skinschanger.libs.com.mojang.api.profiles.*;
import skinschanger.shared.format.*;
import java.nio.charset.*;
import skinschanger.shared.utils.apacheutils.*;
import skinschanger.libs.org.json.simple.*;
import java.net.*;
import java.io.*;
import skinschanger.libs.org.json.simple.parser.*;
import java.util.*;

public class DataUtils
{
    private static final String skullbloburl = "https://sessionserver.mojang.com/session/minecraft/profile/";
    
    public static Profile getProfile(final String nick) throws SkinGetUtils.SkinFetchFailedException {
        final HttpProfileRepository repo = new HttpProfileRepository("minecraft");
        final Profile[] profiles = repo.findProfilesByNames(nick);
        if (profiles.length >= 1) {
            return profiles[0];
        }
        throw new SkinGetUtils.SkinFetchFailedException(SkinGetUtils.SkinFetchFailedException.Reason.NO_PREMIUM_PLAYER);
    }
    
    public static SkinProperty getProp(final String id) throws IOException, ParseException, SkinGetUtils.SkinFetchFailedException {
        final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id + "?unsigned=false");
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setUseCaches(false);
        final InputStream is = connection.getInputStream();
        final String result = IOUtils.toString(is, StandardCharsets.UTF_8);
        IOUtils.closeQuietly(is);
        final JSONArray properties = ((HashMap<K, JSONArray>)new JSONParser().parse(result)).get("properties");
        for (int i = 0; i < properties.size(); ++i) {
            final JSONObject property = ((ArrayList<JSONObject>)properties).get(i);
            final String name = ((HashMap<K, String>)property).get("name");
            final String value = ((HashMap<K, String>)property).get("value");
            final String signature = ((HashMap<K, String>)property).get("signature");
            if (name.equals("textures")) {
                return new SkinProperty(name, value, signature);
            }
        }
        throw new SkinGetUtils.SkinFetchFailedException(SkinGetUtils.SkinFetchFailedException.Reason.NO_SKIN_DATA);
    }
}
