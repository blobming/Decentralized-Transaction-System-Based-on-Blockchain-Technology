package userInterface;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import config.Global;
import obj.Block;
import obj.TXPool;
import obj.Transaction;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransactionPage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7288555578869366188L;
	private JPanel contentPane;
	private JTable table;
	private Object[][] obj;
	private String[] columnNames =  { "Transaciton ID","Hash Code","Timestamp","is Coinbase Transaction"};
	private TableModel tableModel;

	/**
	 * Create the frame.
	 */
	public TransactionPage(String identifyLabel) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel lblWelcomeToSupbank = new JLabel(identifyLabel);
		lblWelcomeToSupbank.setBounds(264, 26, 346, 51);
		contentPane.add(lblWelcomeToSupbank);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(48, 97, 773, 260);
		contentPane.add(scrollPane);
		
		ArrayList<Transaction> transactionList = TXPool.getAll();
		obj = new Object[transactionList.size()][4];
		int count = 0;
		for(Transaction transaction : transactionList) {
			obj[count][0] = transaction.getTxid();
			obj[count][1] = transaction.getHash();
			obj[count][2] = transaction.getTimestamp();
			obj[count][3] = transaction.isCoinBase();
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
		
		JButton btnReturn = new JButton("Return");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Global.homepage.setVisible(true);
			}
		});
		btnReturn.setBounds(58, 369, 117, 29);
		contentPane.add(btnReturn);
	}
	
	public TransactionPage(Block b, BlockPage blockPage) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel lblWelcomeToSupbank = new JLabel("Browser Transaction Details in block "+b.getHashCode());
		lblWelcomeToSupbank.setBounds(264, 26, 346, 51);
		contentPane.add(lblWelcomeToSupbank);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(48, 97, 773, 260);
		contentPane.add(scrollPane);
		
		ArrayList<Transaction> transactionList = b.getBlockBody().transactions;
		obj = new Object[transactionList.size()][4];
		int count = 0;
		for(Transaction transaction : transactionList) {
			obj[count][0] = transaction.getTxid();
			obj[count][1] = transaction.getHash();
			obj[count][2] = transaction.getTimestamp();
			obj[count][3] = transaction.isCoinBase();
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
		
		JButton btnReturn = new JButton("Return");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				blockPage.setVisible(true);
			}
		});
		btnReturn.setBounds(58, 369, 117, 29);
		contentPane.add(btnReturn);
	}

}
