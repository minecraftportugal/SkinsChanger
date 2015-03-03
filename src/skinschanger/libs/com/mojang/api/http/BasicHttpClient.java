package skinschanger.libs.com.mojang.api.http;

import java.net.*;
import java.io.*;
import java.util.*;

public class BasicHttpClient
{
    private static BasicHttpClient instance;
    
    public static BasicHttpClient getInstance() {
        if (BasicHttpClient.instance == null) {
            BasicHttpClient.instance = new BasicHttpClient();
        }
        return BasicHttpClient.instance;
    }
    
    public String post(final URL url, final HttpBody body, final List<HttpHeader> headers) throws IOException {
        return this.post(url, null, body, headers);
    }
    
    public String post(final URL url, Proxy proxy, final HttpBody body, final List<HttpHeader> headers) throws IOException {
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
        connection.setRequestMethod("POST");
        for (final HttpHeader header : headers) {
            connection.setRequestProperty(header.getName(), header.getValue());
        }
        connection.setConnectTimeout(7000);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        final DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(body.getBytes());
        writer.flush();
        writer.close();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuffer response = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        reader.close();
        return response.toString();
    }
}
