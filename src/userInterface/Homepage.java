package userInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import config.Global;
import database.SQLDB;
import obj.Block;
import obj.BlockBody;
import obj.Blockchain;
import obj.TXPool;
import obj.Transaction;
import obj.UTXOSet;
import obj.User;
import utilities.Utilities;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Homepage extends JFrame {

	private JPanel contentPane;
	private JPanel historyPanel;
	private JPanel accountPanel;
	private JPanel payPanel;
	private JTextArea pubKeyText;
	private JTextField amountText;
	public JLabel showBalanceLabel;
	private JTextArea ReceivePubtextArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Homepage frame = new Homepage();
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
	public Homepage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 869, 656);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JMenuBar menuBar = new JMenuBar();
		
		JMenu manipulationMenu = new JMenu("Manipulation");
		manipulationMenu.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		JMenu accountMenu = new JMenu("Account");
		accountMenu.setFont(new Font("Segoe UI Historic", Font.PLAIN, 25));
		JMenuItem mani_payItem = new JMenuItem("pay");
		mani_payItem.addActionListener(new MenuActionListener());
		manipulationMenu.add(mani_payItem);
		JMenuItem myAccountItem = new JMenuItem("my account");
		myAccountItem.addActionListener(new MenuActionListener());
		JMenuItem historyItem = new JMenuItem("history");
		historyItem.addActionListener(new MenuActionListener());
		JMenuItem logoutItem = new JMenuItem("logout");
		logoutItem.addActionListener(new MenuActionListener());
		accountMenu.add(myAccountItem);
		accountMenu.add(historyItem);
		accountMenu.add(logoutItem);
		menuBar.add(manipulationMenu);
		menuBar.add(accountMenu);
		
		this.setJMenuBar(menuBar);
		
		accountPanel = new JPanel();
		accountPanel.setBounds(25, 68, 701, 480);
		accountPanel.setVisible(true);
		
		payPanel = new JPanel();
		payPanel.setBounds(25, 68, 701, 404);
		contentPane.add(payPanel);
		payPanel.setLayout(null);
		payPanel.setVisible(false);
		
		JLabel lblReceiverPublicKey = new JLabel("Receiver public key HASH");
		lblReceiverPublicKey.setBounds(15, 82, 164, 20);
		payPanel.add(lblReceiverPublicKey);
		
		JLabel AmountLabel = new JLabel("Amount:");
		AmountLabel.setBounds(15, 217, 69, 20);
		payPanel.add(AmountLabel);
		
		JButton btnPayNow = new JButton("Pay Now");
		btnPayNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pay();
			}
		});
		btnPayNow.setBounds(43, 325, 115, 29);
		payPanel.add(btnPayNow);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(183, 59, 479, 119);
		scrollPane.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		payPanel.add(scrollPane);
		
		
		ReceivePubtextArea = new JTextArea();
		scrollPane.setViewportView(ReceivePubtextArea);
		ReceivePubtextArea.setLineWrap(true);
		
		amountText = new JTextField();
		amountText.setBounds(148, 214, 146, 26);
		payPanel.add(amountText);
		amountText.setColumns(10);
		contentPane.add(accountPanel);
		accountPanel.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(38, 65, 107, 20);
		accountPanel.add(lblUsername);
		
		JButton btnChangePassword = new JButton("Change password");
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePwd change = new ChangePwd();
				change.setVisible(true);
				setVisible(false);
			}
		});
		btnChangePassword.setBounds(369, 61, 167, 29);
		accountPanel.add(btnChangePassword);
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setBounds(38, 165, 69, 20);
		accountPanel.add(lblBalance);
		showBalanceLabel = new JLabel(""+ UTXOSet.getBalance(Global.user.getPubkey()));
		showBalanceLabel.setBounds(146, 165, 69, 20);
		accountPanel.add(showBalanceLabel);
		
		JLabel lblPublicKey = new JLabel("public key:");
		lblPublicKey.setBounds(38, 240, 95, 20);
		accountPanel.add(lblPublicKey);
		
		JButton btnCopyPublicKey = new JButton("copy Public key");
		btnCopyPublicKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
				Transferable trans = new StringSelection(pubKeyText.getText()); 
				clipboard.setContents(trans, null);
				JOptionPane.showMessageDialog(Homepage.getFrames()[0], "copy successfully", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		btnCopyPublicKey.setBounds(527, 236, 159, 35);
		accountPanel.add(btnCopyPublicKey);
		
//		JButton btnSyncBlocks = new JButton("Sync Blocks");
//		btnSyncBlocks.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Global.blockChainMainThread.peerNetwork.broadcast("HEIGHT "+ UTXOSet.blockchain.getHeight());
//				//更新balance & tempBalance
//			}
//		});
//		btnSyncBlocks.setBounds(0, 435, 117, 29);
//		accountPanel.add(btnSyncBlocks);
		
		pubKeyText = new JTextArea(Global.user.getPubkey());
		pubKeyText.setLineWrap(true);
		pubKeyText.setEditable(false);
		pubKeyText.setBounds(151, 240, 361, 148);
		accountPanel.add(pubKeyText);
		
		JLabel usernameLabel = new JLabel(Global.user.getUsername());
		usernameLabel.setBounds(176, 65, 159, 20);
		accountPanel.add(usernameLabel);
		
		JLabel label = new JLabel("");
		label.setBounds(167, 165, 69, 20);
		accountPanel.add(label);
		
		JButton btnMining = new JButton("Mining");
		btnMining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Transaction> txList = TXPool.gatherTransaction();
				txList.add(Transaction.newCoinbaseTx(Global.user.getPubkey()));
				if(txList.size()<=Global.minBlockTxNum) {
					JOptionPane.showMessageDialog(Homepage.getFrames()[0], "Currently We donot have enough transaction", "Wrong!", JOptionPane.WARNING_MESSAGE);
				}else {
					BlockBody body = new BlockBody(TXPool.gatherTransaction());
					Block block = Blockchain.MiningBlock(new Block(body, 0, UTXOSet.blockchain.tip, new Date()));
					System.out.println("==================Successfully Generate new Block! "+new Date()+"===============================");
					System.out.println(new Gson().toJson(block));
					System.out.println("==================================================================================");
					Global.blockChainMainThread.peerNetwork.broadcast("BLOCK "+ Base64.getEncoder().encodeToString(Utilities.toByteArray(block)));
					JOptionPane.showMessageDialog(Homepage.getFrames()[0], "Successfully Generate new Block!", "", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnMining.setBounds(304, 435, 117, 29);
		accountPanel.add(btnMining);
		
		JButton btnSyncTransactions = new JButton("Sync Transactions");
		btnSyncTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.blockChainMainThread.peerNetwork.broadcast("SYNC_TRANSACTION");
			}
		});
		btnSyncTransactions.setBounds(146, 435, 146, 29);
		accountPanel.add(btnSyncTransactions);
		
		historyPanel = new JPanel();
		historyPanel.setVisible(false);
		historyPanel.setBounds(25, 68, 701, 404);
		contentPane.add(historyPanel);
		
	}
	class MenuActionListener implements ActionListener {
		  public void actionPerformed(ActionEvent e) {
		    if(e.getActionCommand().equals("pay")) {
		    	ReceivePubtextArea.setText("");
		    	amountText.setText("");
		    	payPanel.setVisible(true);
		    	historyPanel.setVisible(false);
		    	accountPanel.setVisible(false);
		    }else if(e.getActionCommand().equals("history")) {
		    	payPanel.setVisible(false);
		    	historyPanel.setVisible(true);
		    	accountPanel.setVisible(false);
		    }else if(e.getActionCommand().equals("my account")) {
		    	payPanel.setVisible(false);
		    	historyPanel.setVisible(false);
		    	accountPanel.setVisible(true);
		    }else if(e.getActionCommand().equals("logout")) {
		    	int n = JOptionPane.showOptionDialog(Homepage.getFrames()[0], "Are you sure to leave?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
	    		        new Object[] { "Yes", "No" }, JOptionPane.YES_OPTION);
	    	    if (n == JOptionPane.YES_OPTION) {
	    	    	System.exit(0);
	    	    } 
		    }
		  }
	}
	private void Pay() {
		if(!Utilities.checkAmount(amountText.getText())) {
			JOptionPane.showMessageDialog(Homepage.getFrames()[0], "Invalid amount", "Wrong!", JOptionPane.WARNING_MESSAGE);
			return;
		}
		double amount = Double.parseDouble(amountText.getText());
		if(amount > Double.parseDouble(showBalanceLabel.getText())) {
			JOptionPane.showMessageDialog(Homepage.getFrames()[0], "Insufficient account balance", "Wrong!", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String payeePubkeyHash = ReceivePubtextArea.getText();
		
		if(payeePubkeyHash.equals(Utilities.hashKeyForDisk(Global.user.getPubkey()))) {
			JOptionPane.showMessageDialog(Homepage.getFrames()[0], "Invalid transfer", "Wrong!", JOptionPane.WARNING_MESSAGE);
			return;
		}
		double balance = Double.parseDouble(showBalanceLabel.getText()) - amount;
		showBalanceLabel.setText("" + balance);
		Transaction transaction = Transaction.createTransaction(Global.user.getPubkey(), Global.user.getPrivateKey(), payeePubkeyHash, amount, new Date());
		UTXOSet.addTempTX(transaction);
		Global.txDB.put(transaction.getTxid(), transaction);
		Global.blockChainMainThread.peerNetwork.broadcast("TRANSACTION "+ Base64.getEncoder().encodeToString(Utilities.toByteArray(transaction)));
		JOptionPane.showMessageDialog(Homepage.getFrames()[0], "Pay successfully!", "", JOptionPane.INFORMATION_MESSAGE);
		ReceivePubtextArea.setText("");
    	amountText.setText("");
	}
}