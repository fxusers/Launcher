/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import static launcher.Connect.getServerName;

/**
 *
 * @author Tereshenko
 */
public class Launcher {
    public static final String domain   = "www.ozonemc.ru"; // Домен сайта
    public static final String  siteDir = "minecrafttest";  // Папка с файлами лаунчера на сайте
    public static final String http     = "http://";        // Протокол подключения https:// если есть ssl сертификат
    public static final String baseconf = "Olympus";
    public static String servers[]={};
    public static final String key2 = "2222222222222222"; //16 Character Key Ключ пост запросов
    public static final String key1 = "1111111111111111";
    public static final String masterVersion = "5566"; //Версия лаунчера

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainPanel mp = new MainPanel();
    }
    
}

class MainPanel extends JFrame
{
   public static String current_server = "Offline";
   private static JComboBox        cServer;
   private static JPasswordField fPassword;
   private static JTextField        fLogin;
   private static ConfigUtils  configUtils;
   private static Setup              setup;
   private static byte[]  secredKey;

   public MainPanel()
   {
      super("Лаунчер");

      setBounds(100, 100, 800, 290);
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLayout(null);

      // ==> Выбор сервера
      JLabel label = new JLabel("Выберите сервер:");
      label.setBounds(20, 18, 150, 30);
      label.setFont(new Font("Tahoma", 0, 18)); // NOI18N

      cServer = new JComboBox();
      cServer.setFont(new Font("Tahoma", 0, 18));
      cServer.setBounds(180, 13, 200, 40);
      cServer.addItem(new String("Offline"));
      // <== Выбор сервера

      fLogin = new JTextField("Логин");
      fLogin.setFont(new Font("Tahoma", 0, 18));
      fLogin.setBounds(20, 80, 175, 40);

      fPassword = new JPasswordField("Пароль");
      fPassword.setFont(new Font("Tahoma", 0, 18));
      fPassword.setBounds(200, 80, 180, 40);

      JButton bAutor = new JButton("Авторизация");
      bAutor.setFont(new Font("Tahoma", 0, 18));
      bAutor.setBounds(20, 140, 360, 40);

      JButton bSetup = new JButton("Настройка");
      bSetup.setFont(new Font("Tahoma", 0, 18));
      bSetup.setBounds(20, 200, 360, 40);

      JScrollPane jScrollPane1 = new JScrollPane();
      jScrollPane1.setBounds(400, 13, 380, 230);

      JTextPane jTextPane1 = new JTextPane();
      jTextPane1.setFont(new Font("Tahoma", 0, 18));        
      jTextPane1.setText("Описание сервера");
      jScrollPane1.setViewportView(jTextPane1);        

      add(label);
      add(cServer);
      add(fLogin);
      add(fPassword);
      add(bAutor);
      add(bSetup);
      add(jScrollPane1);

      jTextPane1.setEditable(false);

      bSetup.addActionListener(e->{ setup.setVisible(true); } );
      bAutor.addActionListener(new ActionListen());
      setVisible(true);
      
      secredKey   = Crypto.getKey();
      setup       = new Setup();
      configUtils = new ConfigUtils();

      LoadSetup();
      
      // Получить список серверов
      for (String a: getServerName())
      {
         cServer.addItem(a);
      }
      
      if (!current_server.matches("Offline") )
         cServer.setSelectedItem(current_server);
   }
   
   public static void LoadSetup()
   {
      configUtils.load();
      
      current_server = Crypto.decrypt(configUtils.getPropertyString("Server"), secredKey);
      if (current_server == null)
         current_server = "Offline";
      cServer.setSelectedItem(current_server);
      
      String Login = Crypto.decrypt(configUtils.getPropertyString("Login"), secredKey);
      if (Login.trim().length() == 0)
         fLogin.setText("Логин");
      else
         fLogin.setText(Login);
      
      String Password = Crypto.decrypt(configUtils.getPropertyString("Password"), secredKey);
      if (Password.trim().length() == 0)
         fPassword.setText("Пароль");
      else
         fPassword.setText(Password);
      
      setup.setProxy(Crypto.decrypt(configUtils.getPropertyString("Proxy"), secredKey).matches("1"));
      setup.setAdress(Crypto.decrypt(configUtils.getPropertyString("Adress"), secredKey));
      
      try
      {
         setup.setPort(Integer.parseInt(Crypto.decrypt(configUtils.getPropertyString("Port"), secredKey)));
      }
      catch (NumberFormatException e)
      {
         setup.setPort(0);
      }
      
      try
      {
         setup.setMemory(Integer.parseInt(Crypto.decrypt(configUtils.getPropertyString("Memory"), secredKey)));
      }
      catch (NumberFormatException e)
      {
         setup.setMemory(setup.getMemory());
      }      
   }

   public static void saveChanges()
   {
      String sLogin;
      if (fLogin.getText().trim().length() == 0)
         sLogin    = Crypto.encrypt("Логин", secredKey);
      else
         sLogin    = Crypto.encrypt(fLogin.getText(), secredKey);

      String sPassword;
      if (fPassword.getPassword()[0] != 0)
         sPassword = Crypto.encrypt(new String(fPassword.getPassword()), secredKey);
      else
         sPassword = Crypto.encrypt("Пароль", secredKey);
      
      String sServer   = Crypto.encrypt((String) cServer.getSelectedItem(), secredKey);
      String sProxy    = Crypto.encrypt(setup.isUseProxy() ? "1" : "0", secredKey);
      String sAdress   = Crypto.encrypt(setup.getAdress(), secredKey);
      String sPort     = Crypto.encrypt(String.valueOf(setup.getPort()), secredKey);
      String sMemory   = Crypto.encrypt(String.valueOf(setup.getMemory()), secredKey);

      configUtils.setPropertyString("Server",   sServer);
      configUtils.setPropertyString("Login",    sLogin);
      configUtils.setPropertyString("Password", sPassword);
      configUtils.setPropertyString("Proxy",    sProxy);
      configUtils.setPropertyString("Adress",   sAdress);
      configUtils.setPropertyString("Port",     sPort);
      configUtils.setPropertyString("Memory",   sMemory);
      configUtils.flush();         
   }
    
   class ActionListen implements ActionListener
   {
     @Override
      public void actionPerformed(ActionEvent e) 
      {
         saveChanges();
         Auth.auth();
      }
   }

   public static String getClientName()
   {
      return ((String) cServer.getSelectedItem()).replaceAll(" ", "");
   }
   
   public static String getLogin()
   {
      return fLogin.getText();
   }
  
   public static String getPassword()
   {
      return new String(fPassword.getPassword());
   }
}