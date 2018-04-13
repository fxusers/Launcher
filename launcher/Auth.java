/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static launcher.Launcher.*;
import static launcher.MainPanel.*;

/**
 *
 * @author Tereshenko
 */
public class Auth {
   public static void auth()
   {
      try {
         String buildUrl = Connect.buildUrl("launcher.php");
         
         String answer2 = 
         execute(buildUrl,
            new Object[] {
               "action",
               Crypto.encrypt("auth:" + getClientName() +
                  ":"     + getLogin()      +
                  ":"     + getPassword()   +
                  ":"     +
                  GuardUtils.hash(Auth.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL())
                  +":"+getPassword(),
                  Launcher.key2.getBytes()),
            });
         
         send(answer2);
         String answer = null;
         try 
         {
            answer = Crypto.decrypt(answer2, Launcher.key1.getBytes());
         } 
         catch (Exception e) 
         {
         }
         
         if (answer == null)
         {
            send("Ошибка подключения");
            return;
         }
         else if(answer.contains("badlauncher<$>"))
         {
            send(answer.replace("badlauncher<$>_", "" ));
            return;
         }
         else if(answer.contains("errorLogin<$>"))
         {
            JOptionPane.showMessageDialog(null, "Проверьте логин/пароль", "Ошибка авторизации", javax.swing.JOptionPane.ERROR_MESSAGE, null);
            return;
         } 
         else if(answer.contains("errorsql<$>"))
         {
            send("Ошибка sql");
            return;
         } 
         else if(answer.contains("client<$>"))
         {
            send("Ошибка: "+answer.replace("client<$>", "клиент")+" не найден");
            return;
         } 
         else if(answer.contains("temp<$>"))
         {
            send("Подождите перед следущей попыткой ввода");
            return;
         }
         else if(answer.contains("badhash<$>"))
         {
            send("Ошибка: Неподдерживаемый способ хеширования");
            return;
         } 
         else if(answer.split("<br>").length != 4)
         {
            send(answer);
            return;
         } 
         
         String version = answer.split("<br>")[0].split("<:>")[0];
         if(!version.equals(Launcher.masterVersion))
         {
            //Frame.main.setUpdateComp(version);
            return;
         }
         send("Logging in successful");
      }
      catch (URISyntaxException ex)
      {
         Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
      } 
      catch (MalformedURLException ex) 
      {
         Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
      
   public static String execute(String surl, Object[] params)
   {
      try
      {
         URL url = new URL(surl);

         InputStream is = PostUtils.post(url, params);
         BufferedReader rd = new BufferedReader(new InputStreamReader(is));

         StringBuffer response = new StringBuffer();
         String line;
         while ((line=rd.readLine()) != null)
         { 
            response.append(line); 
         }
         rd.close();

         String str1 = response.toString().trim();
         return str1;
      } 
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null, e, "Ошибка авторизации", javax.swing.JOptionPane.ERROR_MESSAGE, null);
         return null;
      }
   }
   
   public static void send(String msg)
   {
      System.out.println("[Launcher thread/INFO]: "+msg);
   }   
}
