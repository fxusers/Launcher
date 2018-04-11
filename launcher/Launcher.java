/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import java.io.File;
import java.lang.management.ManagementFactory;
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

/**
 *
 * @author Tereshenko
 */
public class Launcher {
    public static final String domain   = "www.ozonemc.ru"; // Домен сайта
    public static final String  siteDir = "minecrafttest";  // Папка с файлами лаунчера на сайте
    public static final String http     = "http://";        // Протокол подключения https:// если есть ssl сертификат

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
    private ChangeListener listener;
    private JLabel label3;

    public MainPanel()
    {
        super("Launcher Hitech");
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long MemSize = os.getTotalPhysicalMemorySize()/1024/1024;

        listener = new ChangeListener()
   {
            public void stateChanged(ChangeEvent event)
            {
      JSlider source = (JSlider) event.getSource();
      label3.setText("Выделить памяти: " + source.getValue() + "Мб");
            }
   }; 
        
        setBounds(100, 100, 800, 370);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setBackground(new Color(0, 153, 153));
        
        JLabel label = new JLabel("Выберите сервер:");
        label.setBounds(20, 18, 150, 30);
        label.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        
        JComboBox jComboBox1 = new JComboBox<>();
        jComboBox1.setFont(new Font("Tahoma", 0, 18));
        jComboBox1.setBounds(180, 13, 200, 40);
        //jComboBox1.setModel(new DefaultComboBoxModel<>(Connect.getServerName()));

        JTextField fLogin = new JTextField("Логин");
        fLogin.setFont(new Font("Tahoma", 0, 18));
        fLogin.setBounds(20, 80, 175, 40);
                
        JTextField fPassword = new JPasswordField("Пароль");
        fPassword.setFont(new Font("Tahoma", 0, 18));
        fPassword.setBounds(200, 80, 180, 40);
        
        JButton button = new JButton("Авторизация");
        button.setFont(new Font("Tahoma", 0, 18));
        button.setBounds(20, 140, 360, 40);
        
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setBounds(400, 13, 380, 170);
        
        JTextPane jTextPane1 = new JTextPane();
        jTextPane1.setFont(new Font("Tahoma", 0, 18));        
        jTextPane1.setText("Описание сервера");
        jScrollPane1.setViewportView(jTextPane1);        
        
        JScrollPane jScrollPane3 = new JScrollPane();
        jScrollPane3.setBounds(20, 200, 360, 123);
        
        Object[] header = {"Имя параметра", "Ваши параметы", "Рекомендуемые"};
        Object[][] data = {{"ОС", System.getProperty("os.name"), "Windows 7 и выше"},
                           {"Разрядность ОС", System.getProperty("os.arch"), "x64"},
                           {"Версия Java", System.getProperty("java.version"), "1.8 и выше"},                           
                           {"Разрядность Java", "x"+ System.getProperty("sun.arch.data.model"), "x64"},
                           {"Память МБ", String.valueOf(MemSize), "2046"}};
        JTable jTable1 = new JTable(data, header);
        jTable1.setFont(new Font("Tahoma", 0, 14));
        jTable1.setRowHeight(20);
        jTable1.setEnabled(false);
        jScrollPane3.setViewportView(jTable1);
        
        JLabel label2 = new JLabel("Папка по умолчанию");
        label2.setFont(new Font("Tahoma", 0, 18));
        label2.setBounds(400, 185, 360, 40);
        
        JTextField patch2 = new JTextField(System.getenv("SYSTEMDRIVE")+File.separator+"Olympus");
        patch2.setFont(new Font("Tahoma", 0, 18));
        patch2.setBounds(400, 220, 380, 30);
        
        label3 = new JLabel("Выделить памяти: " + 1500 + "Мб");
        label3.setFont(new Font("Tahoma", 0, 18));
        label3.setBounds(400, 250, 380, 40);
        
        JSlider jSlider1 = new JSlider(512, (int)MemSize, 1500);
        jSlider1.setBounds(400, 280, 380, 40);
        jSlider1.setMinorTickSpacing(512);        
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setBorder(javax.swing.BorderFactory.createEtchedBorder());        
        jSlider1.addChangeListener(listener);
                
        add(label);
        add(jComboBox1);
        add(fLogin);
        add(fPassword);
        add(button);
        add(jScrollPane1);
        add(jScrollPane3);
        add(label2);
        add(patch2);
        add(label3);
        add(jSlider1);
        setVisible(true);
        
        jTextPane1.setEditable(false);
        jTextPane1.setText(Connect.getServerName()[0] );
        
               
        
    }
}