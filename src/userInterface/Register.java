package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Register extends JFrame {

	//Todo: 限制用户名和密码长度
	private JPanel contentPane;
	private JTextField usernameText;
	private JTextField textField;

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
		lblYourPublicKey.setBounds(30, 168, 162, 20);
		contentPane.add(lblYourPublicKey);
		
		JLabel lblYourPeivateKey = new JLabel("Your private key:");
		lblYourPeivateKey.setBounds(30, 245, 143, 20);
		contentPane.add(lblYourPeivateKey);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//check username exist
				//generate user & save into db
				//存文件
			}
		});
		btnSave.setBounds(30, 329, 115, 29);
		contentPane.add(btnSave);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(209, 329, 115, 29);
		contentPane.add(btnBack);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(30, 44, 101, 20);
		contentPane.add(lblUsername);
		
		usernameText = new JTextField();
		usernameText.setBounds(305, 41, 146, 26);
		contentPane.add(usernameText);
		usernameText.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(30, 107, 69, 20);
		contentPane.add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(305, 104, 146, 26);
		contentPane.add(textField);
		textField.setColumns(10);
	}

}
