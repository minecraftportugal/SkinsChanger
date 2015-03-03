package skinschanger.libs.com.mojang.api.profiles;

import skinschanger.libs.com.mojang.api.http.*;
import java.net.*;
import skinschanger.libs.org.json.simple.*;
import java.io.*;
import skinschanger.libs.org.json.simple.parser.*;
import java.util.*;

public class HttpProfileRepository
{
    private static final int PROFILES_PER_REQUEST = 100;
    private final String agent;
    private BasicHttpClient client;
    
    public HttpProfileRepository(final String agent) {
        this(agent, BasicHttpClient.getInstance());
    }
    
    public HttpProfileRepository(final String agent, final BasicHttpClient client) {
        this.agent = agent;
        this.client = client;
    }
    
    public Profile[] findProfilesByNames(final String... names) {
        final List<Profile> profiles = new ArrayList<Profile>();
        try {
            final List<HttpHeader> headers = new ArrayList<HttpHeader>();
            headers.add(new HttpHeader("Content-Type", "application/json"));
            final int namesCount = names.length;
            int start = 0;
            int i = 0;
            do {
                int end = 100 * (i + 1);
                if (end > namesCount) {
                    end = namesCount;
                }
                final String[] namesBatch = Arrays.copyOfRange(names, start, end);
                final HttpBody body = getHttpBody(namesBatch);
                final Profile[] result = this.post(this.getProfilesUrl(), body, headers);
                profiles.addAll(Arrays.asList(result));
                start = end;
                ++i;
            } while (start < namesCount);
        }
        catch (Exception ex) {}
        return profiles.toArray(new Profile[profiles.size()]);
    }
    
    private URL getProfilesUrl() throws MalformedURLException {
        return new URL("https://api.mojang.com/profiles/" + this.agent);
    }
    
    private Profile[] post(final URL url, final HttpBody body, final List<HttpHeader> headers) throws IOException, ParseException {
        final String response = this.client.post(url, body, headers);
        final JSONArray jsonProfiles = (JSONArray)new JSONParser().parse(response);
        final Profile[] profiles = new Profile[jsonProfiles.size()];
        for (int i = 0; i < jsonProfiles.size(); ++i) {
            final JSONObject jsonProfile = ((ArrayList<JSONObject>)jsonProfiles).get(i);
            final String id = ((HashMap<K, String>)jsonProfile).get("id");
            final String name = ((HashMap<K, String>)jsonProfile).get("name");
            profiles[i] = new Profile(id, name);
        }
        return profiles;
    }
    
    private static HttpBody getHttpBody(final String... namesBatch) {
        return new HttpBody(JSONArray.toJSONString(Arrays.asList(namesBatch)));
    }
}
