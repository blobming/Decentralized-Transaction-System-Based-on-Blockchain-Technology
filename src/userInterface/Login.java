package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Login extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 841, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSupbank = new JLabel("SupBank");
		lblSupbank.setBounds(48, 16, 69, 20);
		contentPane.add(lblSupbank);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setBounds(26, 116, 115, 29);
		contentPane.add(btnRegister);
		
		JButton btnLogInWith_1 = new JButton("log in with pub&private key");
		btnLogInWith_1.setBounds(26, 348, 274, 29);
		contentPane.add(btnLogInWith_1);
		
		JButton btnLogWithUsernamepassword = new JButton("log with username&password");
		btnLogWithUsernamepassword.setBounds(26, 229, 274, 29);
		contentPane.add(btnLogWithUsernamepassword);
	}
}
