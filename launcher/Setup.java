/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.lang.management.ManagementFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Tereshenko
 */
public class Setup extends JFrame {
    private ChangeListener listener;
    private JLabel lMem;

    private static String  sAdress = ""; // Адрес прокси сервера
    private static int     iPort   = 0;  // Порт прокси сервера
    private static boolean bProxy  = false;
    private static int     iMemory = 1500;
    
    private int defaultMemory = 1500;
    
    /**
     * Получить адрес прокси
     */
    public static String getAdress()
    {
        return sAdress;
    }

    /**
     * Получить порт
     */
    public static int getPort()
    {
        return iPort;
    }
    
    // Использовать прокси ?
    public static boolean isUseProxy()
    {
        return bProxy;
    }
    
    private JCheckBox  chProxy;    // Прокси чек бокс    
    private JTextField tProxyAdr;   // Поле Прокси АДРЕС
    private JTextField tProxyPort;  // Поле Прокси ПОРТ
    private JSlider    jSlidMem;    // Память
    
    public Setup()
    {
        super("Настройка");
        
        setBounds(100, 100, 800, 330);
        setVisible(true);
        setResizable(false);
        setLayout(null);
        
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long MemSize = os.getTotalPhysicalMemorySize()/1024/1024;

        listener = new ChangeListener()
        {
            public void stateChanged(ChangeEvent event)
            {
                JSlider source = (JSlider) event.getSource();
                lMem.setText("Выделить памяти: " + source.getValue() + "Мб");
            }
        }; 

                
        Object[] header = {"Параметр", "Ваши параметы", "Рекомендуемые"};
        Object[][] data = {{"ОС", System.getProperty("os.name"), "Windows 7 и выше"},
                           {"Разрядность ОС", System.getProperty("os.arch"), "x64"},
                           {"Версия Java", System.getProperty("java.version"), "1.8 и выше"},                           
                           {"Разрядность Java", "x"+ System.getProperty("sun.arch.data.model"), "x64"},
                           {"Память МБ", String.valueOf(MemSize), "2046"}};
        JTable jTable1 = new JTable(data, header);
        jTable1.setFont(new Font("Tahoma", 0, 14));
        jTable1.setRowHeight(20);
        jTable1.setEnabled(false);
        // Параметры таблиц
        JScrollPane jScrollPane3 = new JScrollPane();
        jScrollPane3.setBounds(400, 20, 380, 123);
        jScrollPane3.setViewportView(jTable1);
        
        // папка по умолчанию ==>
        JLabel lDir = new JLabel("Папка по умолчанию");
        lDir.setFont(new Font("Tahoma", 0, 18));
        lDir.setBounds(400, 150, 360, 40);
        
        JTextField tPatch = new JTextField(System.getenv("SYSTEMDRIVE")+File.separator+"Olympus");
        tPatch.setFont(new Font("Tahoma", 0, 18));
        tPatch.setBounds(400, 190, 380, 30);
        tPatch.setEnabled(false);
        
        // папка по умолчанию <==
        
        // память ==>
        lMem = new JLabel("Выделить памяти: " + iMemory + "Мб");
        lMem.setFont(new Font("Tahoma", 0, 18));
        lMem.setBounds(20, 150, 380, 40);
                
        jSlidMem = new JSlider(512, (int)MemSize, iMemory);
        jSlidMem.setBounds(20, 190, 360, 30);
        jSlidMem.setMinorTickSpacing(512);        
        jSlidMem.setPaintLabels(true);
        jSlidMem.setPaintTicks(true);
        jSlidMem.setBorder(javax.swing.BorderFactory.createEtchedBorder());        
        jSlidMem.addChangeListener(listener);
        // память <==
        
        // Прокси ==>
        chProxy = new JCheckBox("Использовать прокси");
        chProxy.setBounds(20, 20, 360, 40);
        chProxy.setFont(new Font("Tahoma", 0, 18));
        
        // Адрес
        JLabel lProxyAdr = new JLabel("Адрес:");
        lProxyAdr.setBounds(40, 60, 360, 40);
        lProxyAdr.setFont(new Font("Tahoma", 0, 18));
        
        // Адрес тест
        tProxyAdr = new JTextField();
        tProxyAdr.setBounds(100, 65, 280, 30);
        tProxyAdr.setFont(new Font("Tahoma", 0, 18));
        tProxyAdr.setText(sAdress);
        
        // Порт
        JLabel lProxyPort = new JLabel("Порт:");
        lProxyPort.setBounds(40, 100, 360, 40);
        lProxyPort.setFont(new Font("Tahoma", 0, 18));
        
        // Порт значение
        tProxyPort = new JTextField();
        tProxyPort.setBounds(100, 105, 60, 30);
        tProxyPort.setFont(new Font("Tahoma", 0, 18));
        tProxyPort.setText( String.valueOf(iPort) );
        // Прокси >==
        
        JButton bSave = new JButton("Сохранить");
        bSave.setFont(new Font("Tahoma", 0, 18));
        bSave.setBounds(20, 240, 360, 40);
        
        JButton bDefault = new JButton("По умолчанию");
        bDefault.setFont(new Font("Tahoma", 0, 18));
        bDefault.setBounds(400, 240, 380, 40);
        
        add(lMem);        
        add(lDir);
        add(tPatch);        
        add(jSlidMem);                
        add(jScrollPane3);        
        add(chProxy);
        add(lProxyAdr);
        add(lProxyPort);
        add(tProxyAdr);
        add(tProxyPort);
        add(bSave);
        add(bDefault);
        
        if (!isUseProxy())
        {
            tProxyAdr.setEnabled(false);
            tProxyPort.setEnabled(false);
        }
        else
        {
            tProxyAdr.setEnabled(true);
            tProxyPort.setEnabled(true);
        }
        
        chProxy.addItemListener   (new ItemListen());
        bDefault.addActionListener(new ActionListen());
        bSave.addActionListener   (new ActionListen());
        
        setVisible(true);
    }
    
    public void setFocus()
    {
        if (chProxy.isSelected())
        {
           tProxyAdr.setEnabled(true);
           tProxyPort.setEnabled(true);                
        }
        else
        {
           tProxyAdr.setEnabled(false);
           tProxyPort.setEnabled(false);
        }        
    }
    
   class ActionListen implements ActionListener
   {
     @Override
      public void actionPerformed(ActionEvent e) 
      {
         switch (e.getActionCommand())
         {
            case "По умолчанию":
            {
               tProxyAdr.setText ("");
               tProxyPort.setText("0");
               jSlidMem.setValue(defaultMemory);
               chProxy.setSelected(false);
               setFocus();
            } break;
            
            case "Сохранить":
            {
               bProxy  = chProxy.isSelected();                              
               if (bProxy)
               {
                  sAdress = tProxyAdr.getText(); // Адрес прокси сервера
                  iPort   = Integer.valueOf(tProxyPort.getText());  // Порт прокси сервера                  
               }
               else
               {
                  sAdress = ""; // Адрес прокси сервера
                  iPort   = 0;  // Порт прокси сервера                  
               }
               iMemory = jSlidMem.getValue();
               
               
            } break;
         }
      }
   }
    
    // Обработчик чек-бокса ПРОКСА
    class ItemListen implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e) 
        {
            setFocus();
        }
    }    
}
