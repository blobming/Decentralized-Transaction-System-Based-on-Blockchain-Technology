package userInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import config.Global;
import obj.UTXOSet;

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
import java.awt.event.ActionEvent;

public class Homepage extends JFrame {

	private JPanel contentPane;
	private JPanel historyPanel;
	private JPanel accountPanel;
	private JPanel payPanel;
	private JTextArea pubKeyText;

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
		
		JLabel lblReceiverPublicKey = new JLabel("Receiver public key");
		lblReceiverPublicKey.setBounds(15, 82, 164, 20);
		payPanel.add(lblReceiverPublicKey);
		
		JLabel AmountLabel = new JLabel("Amount:");
		AmountLabel.setBounds(15, 217, 69, 20);
		payPanel.add(AmountLabel);
		
		JButton btnPayNow = new JButton("Pay Now");
		btnPayNow.setBounds(43, 325, 115, 29);
		payPanel.add(btnPayNow);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(183, 59, 479, 119);
		scrollPane.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		payPanel.add(scrollPane);
		
		
		JTextArea ReceivePubtextArea = new JTextArea();
		scrollPane.setViewportView(ReceivePubtextArea);
		ReceivePubtextArea.setLineWrap(true);
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
		
		JLabel showBalanceLabel = new JLabel(""+UTXOSet.getBalance(Global.user.getPubkey()));
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
				JOptionPane.showMessageDialog(Register.getFrames()[0], "copy successfully", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		btnCopyPublicKey.setBounds(527, 236, 159, 35);
		accountPanel.add(btnCopyPublicKey);
		

		JButton btnSyncBlocks = new JButton("Sync Blocks");
		btnSyncBlocks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.blockChainMainThread.peerNetwork.broadcast("HEIGHT "+ UTXOSet.blockchain.getHeight());
			}
		});
		btnSyncBlocks.setBounds(0, 435, 117, 29);
		accountPanel.add(btnSyncBlocks);
		
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
		
		historyPanel = new JPanel();
		historyPanel.setVisible(false);
		historyPanel.setBounds(25, 68, 701, 404);
		contentPane.add(historyPanel);
		
	}
	class MenuActionListener implements ActionListener {
		  public void actionPerformed(ActionEvent e) {
		    if(e.getActionCommand().equals("pay")) {
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
		    }
		  }
	}
}

