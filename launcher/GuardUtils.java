/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 *
 * @author Tereshenko
 */
public class GuardUtils {
   
   public static File urltofile(URL url) 
   {
      try 
      {
         return new File(url.toURI());
      } 
      catch (URISyntaxException var2) 
      {
         return new File(url.getPath().replace("file:/", "").replace("file:", ""));
      }
   }
   
   public static String hash(URL url) 
   {
      if (url == null) 
      {
         return "h";
      } 
      else if (urltofile(url).isDirectory()) 
      {
         return "d";
      } 
      else 
      {
         InputStream IS = null;
         DigestInputStream DI = null;
         BufferedInputStream BS = null;
         Formatter F = null;

         try 
         {
            MessageDigest MD = MessageDigest.getInstance("MD5");
            IS = url.openStream();
            BS = new BufferedInputStream(IS);
            DI = new DigestInputStream(BS, MD);

            while (DI.read() != -1) {}

            byte[] Md = MD.digest();
            F = new Formatter();
            byte[] Mi = Md;
            int I = Md.length;

            for (int i = 0; i < I; ++i) 
            {
               byte Bi = Mi[i];
               F.format("%02x", new Object[]{Byte.valueOf(Bi)});
            }

            String str = F.toString();
            return str;
         } 
         catch (Exception e) 
         {
         }            
         finally 
         {
            try 
            {
               IS.close();
               IS = null;
            } 
            catch (Exception e) 
            {
            }

            try 
            {
               DI.close();
               DI = null;
            } 
            catch (Exception e) 
            {
            }

            try 
            {
               BS.close();
               BS = null;
            } catch (Exception e) 
            {
            }

            try 
            {
               F.close();
               F = null;
            } catch (Exception e) 
            {
            }
         }

         return "h";
      }
   }   
}
