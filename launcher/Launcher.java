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
   private JComboBox        cServer;
   private JPasswordField fPassword;
   private JTextField        fLogin;
   private ConfigUtils configUtils;
   private byte[]         secredKey;

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

      cServer = new JComboBox(getServerName());
      cServer.setFont(new Font("Tahoma", 0, 18));
      cServer.setBounds(180, 13, 200, 40);
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

      bSetup.addActionListener(e->{ new Setup(); } );
      bAutor.addActionListener(new ActionListen());

      setVisible(true);

      configUtils = new ConfigUtils();
      configUtils.load();
      // String pass = configUtils.getPropertyString("password");

      // Получить уникальный ключ для шифрования.
      secredKey = Crypto.getKey();
      //String pass = Crypto.encrypt("Филипп_кукушка123", secredKey);
      //System.out.println(pass);
      //System.out.println(Crypto.decrypt(pass, secredKey));
   }
    
    class ActionListen implements ActionListener
    {

      @Override
      public void actionPerformed(ActionEvent e) 
      {         
         String sLogin    = Crypto.encrypt(fLogin.getText(), secredKey);
         String sPassword = Crypto.encrypt(new String(fPassword.getPassword()), secredKey);
         String sServer   = Crypto.encrypt((String) cServer.getSelectedItem(), secredKey);

         configUtils.setPropertyString("Server",   sServer);
         configUtils.setPropertyString("Login",    sLogin);
         configUtils.setPropertyString("Password", sPassword);
         configUtils.flush();
      }
    }
}