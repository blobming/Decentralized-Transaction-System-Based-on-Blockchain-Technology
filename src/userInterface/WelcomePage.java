package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import Utilities.Utilities;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class WelcomePage extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private Map<String, String> hostList;
	private Object[][] obj;
	private String[] columnNames =  { "Network Card","IP Address"};
	private TableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomePage frame = new WelcomePage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WelcomePage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWelcomeToSupbank = new JLabel("Welcome to SupBank");
		lblWelcomeToSupbank.setBounds(264, 26, 346, 51);
		contentPane.add(lblWelcomeToSupbank);
		
		JLabel lblPleaseChooseA = new JLabel("Please Choose a network card to connect to the Internet");
		lblPleaseChooseA.setBounds(61, 93, 459, 16);
		contentPane.add(lblPleaseChooseA);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(61, 138, 549, 219);
		contentPane.add(scrollPane);
		
		 
		
        /* 
         * 初始化JTable里面各项的值，设置两个一模一样的实体"赵匡义"学生。 
         */
		hostList = Utilities.getInternetIp();
		obj = new Object[hostList.size()][2];
		int count = 0;
		for(String key:hostList.keySet()) {
			obj[count][0] = key;
			obj[count][1] = hostList.get(key);
			count++;
		}
		tableModel = new DefaultTableModel(obj, columnNames);
        table = new JTable(tableModel);
        /* 
         * 设置JTable的列默认的宽度和高度 
         */  
        TableColumn column = null;
        int colunms = table.getColumnCount();  
        for(int i = 0; i < colunms; i++)  
        {
            column = table.getColumnModel().getColumn(i);  
            /*将每一列的默认宽度设置为100*/  
            column.setPreferredWidth(100);
        }
        
		scrollPane.setViewportView(table);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				BlockChainEntrance blockChainEntrance = new BlockChainEntrance(table.getValueAt(selectedRow, 0).toString());
				try {
					blockChainEntrance.start();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnConnect.setBounds(140, 388, 117, 29);
		contentPane.add(btnConnect);
		
		JButton btnResacan = new JButton("Resacan");
		btnResacan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostList = Utilities.getInternetIp();
				obj = new Object[hostList.size()][2];
				int count = 0;
				for(String key:hostList.keySet()) {
					System.out.println(key);
					obj[count][0] = key;
					obj[count][1] = hostList.get(key);
					count++;
				}
				tableModel = new DefaultTableModel(obj, columnNames);
				table.setModel(tableModel);
			}
		});
		btnResacan.setBounds(336, 388, 117, 29);
		contentPane.add(btnResacan);
        
	}
}
