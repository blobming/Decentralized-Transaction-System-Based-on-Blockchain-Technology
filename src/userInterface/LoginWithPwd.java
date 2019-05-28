package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import config.Global;
import database.SQLDB;
import obj.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
		SQLDB.connSqlDB();
		LoginWithPwd frame = new LoginWithPwd();
		frame.setVisible(true);
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
				String username = usernameText.getText();
				String password = new String(passwordText.getPassword());
				User user = SQLDB.getUserByUsername(username, password);
				if(user == null) {
					JOptionPane.showMessageDialog(LoginWithPwd.getFrames()[0], "wrong username or password", "Wrong!", JOptionPane.WARNING_MESSAGE);
				}else {
					Global.user = user;
					NetworkCardPage welcomePage = new NetworkCardPage();
					welcomePage.setVisible(true);
					setVisible(false);
				}
			}
		});
		btnLogIn.setBounds(103, 245, 115, 29);
		contentPane.add(btnLogIn);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
				login.setVisible(true);
				setVisible(false);
			}
		});
		btnBack.setBounds(294, 245, 115, 29);
		contentPane.add(btnBack);
	}
}
