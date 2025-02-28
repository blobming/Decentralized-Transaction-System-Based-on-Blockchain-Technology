package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.google.gson.Gson;

import config.Global;
import obj.Block;
import obj.UTXOSet;
import utilities.Utilities;

public class BlockPage extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private Object[][] obj;
	private String[] columnNames =  { "preHashCode","hashCode","merkleRootHash","timeStamp","nBits","nonce"};
	private TableModel tableModel;
	private ArrayList<Block> blockList;
	private BlockPage blockPage;
	/**
	 * Create the frame.
	 */

	/**
	 * Create the frame.
	 */
	public BlockPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		blockPage = this;
		
		
		JLabel lblWelcomeToSupbank = new JLabel("Browser BlockChain");
		lblWelcomeToSupbank.setBounds(264, 26, 346, 51);
		contentPane.add(lblWelcomeToSupbank);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(48, 97, 773, 260);
		contentPane.add(scrollPane);
		
		blockList = new ArrayList<Block>();
		for(Block block: UTXOSet.blockchain) {
			blockList.add(block);
		}
		obj = new Object[blockList.size()][6];
		int count = 0;
		for(Block b : blockList) {
			obj[count][0] = b.getPreHashCode();
			obj[count][1] = b.getHashCode();
			obj[count][2] = b.getMerkleRootHash();
			obj[count][3] = b.getTimestamp();
			obj[count][4] = b.getnBits();
			obj[count][5] = b.getNonce();
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
		
		JButton btnDetails = new JButton("Details");
		btnDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				TransactionPage transactionPage = new TransactionPage(blockList.get(selectedRow),blockPage);
				transactionPage.setVisible(true);
				setVisible(false);
			}
		});
		btnDetails.setBounds(75, 381, 117, 29);
		contentPane.add(btnDetails);
		
		JButton btnReturn = new JButton("Return");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Global.homepage.setVisible(true);
			}
		});
		btnReturn.setBounds(223, 381, 117, 29);
		contentPane.add(btnReturn);
	}
	public void UpdateBlockChainValue() {
		blockList = new ArrayList<Block>();
		for(Block block: UTXOSet.blockchain) {
			blockList.add(block);
		}
		obj = new Object[blockList.size()][6];
		int i = 0;
		for(Block b : blockList) {
			obj[i][0] = b.getPreHashCode();
			obj[i][1] = b.getHashCode();
			obj[i][2] = b.getMerkleRootHash();
			obj[i][3] = b.getTimestamp();
			obj[i][4] = b.getnBits();
			obj[i][5] = b.getNonce();
			i++;
		}
		tableModel = new DefaultTableModel(obj, columnNames);
		table.setModel(tableModel);
		
	}

}
