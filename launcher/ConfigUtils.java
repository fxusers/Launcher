/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author Tereshenko
 */
public class ConfigUtils 
{
   private File config; // основной файл
   private String fileName = "launcher.cfg";
   private HashMap<String,String> contents;  // Кеш
   private final String[] params = {"Server", "Login", "Password", "Proxy", "Adress", "Port", "Memory"};
 
   // получить имя файла
   private File getConfigName()
   {
      String home = System.getProperty("user.home", "");
      String path = File.separator + Launcher.baseconf + File.separator + fileName;
      
      String osName = System.getProperty("os.name").toLowerCase();

      if (osName.contains("win")) 
      {
         String appData = System.getenv("SYSTEMDRIVE");
         if (appData != null) 
            return new File(appData + path);
         else 
            return new File(home + path);
      }
      return null;
   }
   
   // Залить из архива в файл
   public void load()
   {
      if (config.exists())
      {
         loadHashMap();
         return;
      }
     
      try
      {
         config.createNewFile();
/* Это не нужно, у нас в архиве пустой файл
         // Извлечь файл из архива
         InputStream input = getClass().getResourceAsStream(fileName);
         if (input != null)
         {
            FileOutputStream output = null;

            // Создать каталоги ?
            config.getParentFile().mkdirs();
            output = new FileOutputStream(config);
            byte[] buff = new byte[8192];
            int length = 0;
            while ((length = input.read(buff)) > 0)
            {
               output.write(buff, 0, length);
            }
         }*/
      }
      catch (FileNotFoundException e)
      {
         System.out.println(e);
      }
      catch (IOException e)
      {
         System.out.println(e);
      }         
   }
   
   // Хеширование
   private HashMap<String, String> loadHashMap()
   {
      HashMap<String, String> result = new HashMap<String, String>();
      
      BufferedReader rd = null;
      try
      {
         // Читаем файл
         rd = new BufferedReader(new FileReader(config));
         
         String line; int i = 0;
         while ((line = rd.readLine()) != null)
         {
            if (line.isEmpty())
               continue;
            
            result.put(params[i], line); i++;
            continue;
         }         
      }
      catch(IOException e)
      {
         
      }
      finally
      {
         try
         {
            if (rd != null)
               rd.close();
         }
         catch(IOException e)
         {
         
         }
      }

      return result;
   }
   
   // Проверка наличия в кеше значения
   public Boolean checkProperty(String property)
   {
      if (contents == null)
         contents = loadHashMap();
      
      return (contents.get(property) == null ? false : true);
   }
   
   // Запись в файл информации
   public void flush()
   {
      try
      {
         if (config.exists()) config.delete();
         if (contents.size() == 0)
            return;

         config.createNewFile();
         BufferedWriter writer = new BufferedWriter(new FileWriter(config));
         for (int i = 0; i < params.length; i ++)
         {
            String line = contents.get(params[i]);
            if (line == null)
            {
               writer.append("\n");
               continue; 
            }
            writer.append(line);
            writer.append("\n");
         }
         writer.flush();
         writer.close();
         contents = null;
      } 
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   public String getPropertyString(String property)
   {
      if (!checkProperty(property))
         return null;
      
      return contents.get(property);
   }
   
   public void setPropertyString(String type, String property)
   {
      if ((contents != null) && (contents.get(type) != null))
         contents.remove(type);
      
      if (contents == null)
         contents = new HashMap<String, String>();

      contents.put(type, property);
   }
   
   public ConfigUtils()
   {
      config = getConfigName();
   }   
}