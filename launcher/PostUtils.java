/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Math.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Tereshenko
 */
public class PostUtils {
   private OutputStream os = null;
   private URLConnection connection;
   private String boundary = "---------------------------" + randomString() + randomString() + randomString();

   private PostUtils(URLConnection connection) throws IOException
   {
      this.connection = connection;
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
   }
   
   private static String randomString()
   {
      return Long.toString(10 * ((long) random()), 36);
   }

   private void connect() throws IOException
   {
      if (os == null) os = connection.getOutputStream();
   }

   private void write(char c) throws IOException
   {
      connect();
      os.write(c);
   }
   
   private void write(String s) throws IOException
   {
      connect();
      os.write(s.getBytes());
   }

   private void writeln(String s) throws IOException
   {
      connect();
      write(s);
      newline();
   }

   private void newline() throws IOException
   {
      connect();
      write("\r\n");
   }

   private void boundary() throws IOException
   {
      write("--");
      write(boundary);
   }

   private InputStream post() throws IOException
   {
      boundary();
      writeln("--");
      os.close();
      return connection.getInputStream();
   }

   private void writeName(String name) throws IOException
   {
      newline();
      write("Content-Disposition: form-data; name=\"");
      write(name);
      write('"');
   }
   
   private static void pipe(InputStream in, OutputStream out) throws IOException
   {
      byte[] buf = new byte[500000];
      int nread;
      synchronized (in)
      {
         while ((nread = in.read(buf, 0, buf.length)) >= 0)
            out.write(buf, 0, nread);
      }
      out.flush();
      buf = null;
   }
  
   private void setParameter(String name, String filename, InputStream is) throws IOException
   {
      boundary();
      writeName(name);
      write("; filename=\"");
      write(filename);
      write('"');
      newline();
      write("Content-Type: ");
      String type = URLConnection.guessContentTypeFromName(filename);
      if (type == null) type = "application/octet-stream";
      writeln(type);
      newline();
      pipe(is, os);
      newline();
   }
   
   private void setParameter(String name, String value) throws IOException
   {
      boundary();
      writeName(name);
      newline();
      newline();
      writeln(value);
   }
    
   private void setParameter(String name, File file) throws IOException
   {
      setParameter(name, file.getPath(), new FileInputStream(file));
   }
   
   private void setParameter(String name, Object object) throws IOException
   {
      if (object instanceof File)
         setParameter(name, (File) object);
      else
         setParameter(name, object.toString());
   }
   
   private void setParameters(Object[] parameters) throws IOException
   {
      if (parameters == null)
            return;
      
      for (int i = 0; i < parameters.length - 1; i += 2)
         setParameter(parameters[i].toString(), parameters[i + 1]);
   }
   
   public InputStream post(Object[] parameters) throws IOException
   {
      setParameters(parameters);
      return post();
   }
   
   // Основная точка входа
   public static InputStream post(URL url, Object[] parameters) throws IOException
   {
      HttpURLConnection ct = null;
      
      if (Setup.isUseProxy())
         ct = (HttpURLConnection)url.openConnection(
               new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Setup.getAdress(), Setup.getPort())));
      else
         ct = (HttpURLConnection)url.openConnection();
      
      return new PostUtils(ct).post(parameters);
   }   
}
