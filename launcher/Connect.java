/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;

/**
 *
 * @author Tereshenko
 */
public class Connect {
    // Получить адрес
    public static String getURL(String path)
    {
        return Launcher.http + Launcher.domain + path;
    }
    
    public static String getURLSc(String script)
    {
        return getURL("/" + Launcher.siteDir + "/" + script );
    }
    
    public static String[] getServerName()
    {
        String[] err = {"Offline"};
        
        try
        {
            String url = runHTTP(getURLSc("servers.php"));
            if (url == null)
            {
                return err;
            }
            else if (url.contains(", "))
            {
                Launcher.servers = url.replaceAll("<br>", "").split("<::>");
                String[] serversName = new String[Launcher.servers.length];
                
                for (int i = 0; i < Launcher.servers.length; i++)
                {
                    serversName[i] = Launcher.servers[i].split(", ")[0];
                }
                return serversName;
            }            
            return err;
        }
        catch(Exception e)
        {
            return err;
        }
    }

    private static String runHTTP(String in_url) {
        System.out.println(in_url);
        
        HttpURLConnection ct = null;
        try
        {
            URL url = new URL(in_url);
            
            Proxy proxy = null;
            
            if (Setup.isUseProxy())
               proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Setup.getAdress(), Setup.getPort()));
            
            ct = (HttpURLConnection)url.openConnection(proxy);
            
            ct.setRequestMethod("GET");
            ct.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            ct.setRequestProperty("Content-Length", "0");
            ct.setRequestProperty("Content-Language", "en-US");

            ct.setUseCaches(false);
            ct.setDoInput(true);
            ct.setDoOutput(true);
            
            ct.connect();
            
            InputStream is = ct.getInputStream();
            StringBuilder response;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is)))
            {
                response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                {
                    response.append(line);
                }
            }
            
            String str = response.toString();
            return str;
        }
        catch(UnknownHostException e)
        {
            return e.toString();
        }
        catch(IOException e)
        {
            return e.toString();
        }        
        finally
        {
            if (ct != null)
                ct.disconnect();
        }
    }
}
