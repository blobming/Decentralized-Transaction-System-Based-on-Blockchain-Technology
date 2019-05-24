package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/*
 * log in
 * 
 */
@SuppressWarnings("serial")
public class LoginWithPwd extends JFrame {

	private JPanel contentPane;
	private JTextField usernameText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWithPwd frame = new LoginWithPwd();
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
	public LoginWithPwd() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 680, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(15, 45, 128, 20);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(15, 138, 69, 20);
		contentPane.add(lblPassword);
		
		usernameText = new JTextField();
		usernameText.setBounds(158, 42, 146, 26);
		contentPane.add(usernameText);
		usernameText.setColumns(10);
		
		JPasswordField passwordText = new JPasswordField();
		passwordText.setBounds(158, 135, 146, 26);
		contentPane.add(passwordText);
		passwordText.setColumns(10);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnLogIn.setBounds(103, 245, 115, 29);
		contentPane.add(btnLogIn);
	}

}
