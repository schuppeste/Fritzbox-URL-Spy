/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fritzscanner;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Comparator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

public class Fritzscanner extends JPanel {

    public static final String[] columnNames = {
        "Domain", "Zugriffe", "Letzter Zugriff", ""
    };
    Comparator<NetRecord> comp = new NetRecordComparator();
    static JTable table;
    static JScrollPane scroller;
    static InteractiveTableModel tableModel;
    static String fritz;
    static String pw;

    public Fritzscanner() {
        initComponent();
        System.out.println("init");



    }

    public void initComponent() {

        tableModel = new InteractiveTableModel(columnNames);

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setModel(tableModel);
        table.setSurrendersFocusOnKeystroke(false);
        if (!tableModel.hasEmptyRow()) {
            tableModel.addEmptyRow();
        }


        scroller = new javax.swing.JScrollPane(table);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
        TableColumn hidden = table.getColumnModel().getColumn(InteractiveTableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        Renderer ren = new Renderer();
        table.setDefaultRenderer(Long.class, ren);


        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
    }

    public void highlightLastRow(int row) {
        int lastrow = tableModel.getRowCount();
        if (row == lastrow - 1) {
            table.setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            table.setRowSelectionInterval(row + 1, row + 1);
        }

        table.setColumnSelectionInterval(0, 0);
    }

    public static void main(String[] args) {
        try {
		Object[] possibleValues = { "Alles", "LAN", "WLAN" };
		Object selectedValue = JOptionPane.showInputDialog(null,
	
        "PPP OVER?", "Input",

        JOptionPane.INFORMATION_MESSAGE, null,

        possibleValues, possibleValues[0]);
  JPasswordField pwd = new JPasswordField(10);  
    int action = JOptionPane.showConfirmDialog(null, pwd,"Fritzbox Passwort!!",JOptionPane.OK_CANCEL_OPTION);  
    if(action < 0)JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected");  
    else pw =new String(pwd.getPassword()); 
        
              
            if (args.length == 0) {
                System.out.println("fritzurl anhängen");
                System.exit(0);
            } else {
                fritz = args[0];
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            JFrame frame = new JFrame("Fritzdomainscanner");
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    System.exit(0);
                }
            });
            frame.getContentPane().add(new Fritzscanner());
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Connector con = new Connector();
        con.run();
        System.out.println("converlassen");
    }
}
