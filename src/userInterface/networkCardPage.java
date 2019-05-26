package userInterface;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import config.Global;
import utilities.Utilities;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class networkCardPage extends JFrame {

	private static final long serialVersionUID = -4603485177943160649L;
	private JPanel contentPane;
	private JTable table;
	private Map<String, String> hostList;
	private Object[][] obj;
	private String[] columnNames =  { "Network Card","IP Address"};
	private TableModel tableModel;
	/**
	 * Create the frame.
	 */
	public networkCardPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 587);
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
        TableColumn column = null;
        int colunms = table.getColumnCount();  
        for(int i = 0; i < colunms; i++)  
        {
            column = table.getColumnModel().getColumn(i);  
            column.setPreferredWidth(100);
        }
        
		scrollPane.setViewportView(table);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				try {
					Global.blockChainMainThread = new BlockChainMainThread(table.getValueAt(selectedRow, 0).toString());
					Global.blockChainMainThread.start();
					setVisible(false);
					Homepage homepage = new Homepage();
					homepage.setVisible(true);
				} catch (Exception e1) {
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
		
		JButton btnViewBlock = new JButton("View Block");
		btnViewBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BlockPage blockPage = new BlockPage();
				blockPage.setVisible(true);
			}
		});
		btnViewBlock.setBounds(493, 388, 117, 29);
		contentPane.add(btnViewBlock);
	}
}
