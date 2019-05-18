package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Register extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
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
	public Register() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblYourPublicKey = new JLabel("Your public key:");
		lblYourPublicKey.setBounds(30, 58, 162, 20);
		contentPane.add(lblYourPublicKey);
		
		JLabel lblYourPeivateKey = new JLabel("Your peivate key:");
		lblYourPeivateKey.setBounds(30, 127, 143, 20);
		contentPane.add(lblYourPeivateKey);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(30, 329, 115, 29);
		contentPane.add(btnSave);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(209, 329, 115, 29);
		contentPane.add(btnBack);
	}

}
