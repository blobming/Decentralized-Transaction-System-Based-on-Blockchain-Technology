package userInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class Homepage extends JFrame {

	private JPanel contentPane;

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
		setBounds(100, 100, 866, 567);
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
		manipulationMenu.add(mani_payItem);
		JMenuItem myAccountItem = new JMenuItem("my account");
		JMenuItem historyItem = new JMenuItem("history");
		JMenuItem logout = new JMenuItem("logout");
		accountMenu.add(myAccountItem);
		accountMenu.add(historyItem);
		accountMenu.add(logout);
		menuBar.add(manipulationMenu);
		menuBar.add(accountMenu);
		
		this.setJMenuBar(menuBar);
		
		JPanel accountPanel = new JPanel();
		accountPanel.setBounds(25, 68, 701, 404);
		accountPanel.setVisible(true);
		contentPane.add(accountPanel);
		accountPanel.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(38, 65, 107, 20);
		accountPanel.add(lblUsername);
		
		JButton btnChangePassword = new JButton("Change password");
		btnChangePassword.setBounds(369, 61, 167, 29);
		accountPanel.add(btnChangePassword);
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setBounds(38, 165, 69, 20);
		accountPanel.add(lblBalance);
		
		JLabel showBalanceLabel = new JLabel("");
		showBalanceLabel.setBounds(146, 165, 69, 20);
		accountPanel.add(showBalanceLabel);
		
		JLabel lblPublicKey = new JLabel("public key:");
		lblPublicKey.setBounds(38, 240, 95, 20);
		accountPanel.add(lblPublicKey);
		
		JLabel showPublabel = new JLabel("");
		showPublabel.setBounds(201, 240, 311, 148);
		accountPanel.add(showPublabel);
		
		JButton btnCopyPublicKey = new JButton("copy Public key");
		btnCopyPublicKey.setBounds(527, 236, 159, 35);
		accountPanel.add(btnCopyPublicKey);
		
		JPanel payPanel = new JPanel();
		payPanel.setBounds(25, 68, 701, 404);
		contentPane.add(payPanel);
		payPanel.setLayout(null);
		payPanel.setVisible(false);
		
		JLabel lblReceiverPublicKey = new JLabel("Receiver public key");
		lblReceiverPublicKey.setBounds(15, 82, 164, 20);
		payPanel.add(lblReceiverPublicKey);
		
		JTextArea ReceivePubtextArea = new JTextArea();
		ReceivePubtextArea.setBounds(206, 82, 397, 100);
		payPanel.add(ReceivePubtextArea);
		
		JLabel AmountLabel = new JLabel("Amount:");
		AmountLabel.setBounds(15, 217, 69, 20);
		payPanel.add(AmountLabel);
		
		JButton btnPayNow = new JButton("Pay Now");
		btnPayNow.setBounds(43, 325, 115, 29);
		payPanel.add(btnPayNow);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(680, 16, 57, 20);
		contentPane.add(lblUser);
		
		JPanel historyPanel = new JPanel();
		historyPanel.setVisible(false);
		historyPanel.setBounds(25, 68, 701, 404);
		contentPane.add(historyPanel);
	}
}

