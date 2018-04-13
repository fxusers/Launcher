/*
 * Класс для шифрования / расшифровки данных
 */
package launcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import javax.swing.JOptionPane;

/**
 *
 * @author Tereshenko
 */
public class Crypto {
   // Уникальный ключ компьютера (Версия БИОС + МакАдреса)
   public static byte[] getKey()
   {
      byte[] thedigest = null;
      String serial = "";      
      try
      {
         // Версия биоса
         Process process = Runtime.getRuntime().exec(new String[] { "wmic", "bios", "get", "BIOSVersion " });
         process.getOutputStream().close();
         Scanner sc = new Scanner(process.getInputStream());
         while (sc.hasNext())
         {
            serial += sc.next();
         }
      }
      catch (IOException ex)
      {
         serial = "123";
      }

      Enumeration<NetworkInterface> nis;
      try 
      {
         nis = NetworkInterface.getNetworkInterfaces();
         while (nis.hasMoreElements())
         {
            NetworkInterface ni = nis.nextElement();
            serial += ni.toString();
         }
      } 
      catch (SocketException ex) 
      {
         serial = "456";
      }

      try 
      {
         thedigest = MessageDigest.getInstance("MD5").digest(serial.getBytes("UTF-8"));
      } 
      catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) 
      {
         Logger.getLogger(Crypto.class.getName()).log(Level.SEVERE, null, ex);
         return null;
      }

      return thedigest;
   }
   // Зашифровать строку
   public static String encrypt(String input, byte[] key)
   {
      if (input == null)
         return null;

      byte[] crypted = null;
      try
      {
         SecretKeySpec skey = new SecretKeySpec(key, "AES");
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, skey);
         crypted = cipher.doFinal(input.getBytes());
      }
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null, e, "Ошибка шифрования", javax.swing.JOptionPane.ERROR_MESSAGE, null);
         System.exit(0);
      }
      // Строка в формате BASE64
      return new String(new sun.misc.BASE64Encoder().encode(crypted));
   }

   // Попытка дешифровки (Шифрованная строка и ключ)
   public static String decrypt(String input, byte[] key)
   {
      if (input == null)
         return "";
      
      byte[] output = null;
      
      try
      {
         SecretKeySpec skey = new SecretKeySpec(key, "AES");
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         cipher.init(Cipher.DECRYPT_MODE, skey);
         
         // В BASE64 преобразуем
         output = cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(input));
      }
      catch(NoSuchPaddingException | 
            NoSuchAlgorithmException | 
            IllegalBlockSizeException | 
            InvalidKeyException |
            BadPaddingException |
            NullPointerException |
            IOException e )
      {
         JOptionPane.showMessageDialog(null, e, "Ошибка дешифрования", javax.swing.JOptionPane.ERROR_MESSAGE, null);
         System.out.println(e);
         System.exit(0);
      }
      
      return new String(output);
   }
}