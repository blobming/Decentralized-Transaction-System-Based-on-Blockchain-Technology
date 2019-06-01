package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.SQLDB;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
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
		String strPath = "./DataFile";  
		File file = new File(strPath);
		if(!file.exists()){  
		    file.mkdirs();  
		}
		SQLDB.connSqlDB();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 841, 467);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSupbank = new JLabel("SupBank");
		lblSupbank.setBounds(48, 16, 69, 20);
		contentPane.add(lblSupbank);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Register register = new Register();
				register.setVisible(true);
				setVisible(false);
			}
		});
		btnRegister.setBounds(26, 116, 115, 29);
		contentPane.add(btnRegister);
		
		JButton btnLogInWith_1 = new JButton("log in with pub&private key");
		btnLogInWith_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginWithKey loginWithKey = new LoginWithKey();
				loginWithKey.setVisible(true);
				setVisible(false);
			}
		});
		btnLogInWith_1.setBounds(433, 229, 274, 29);
		contentPane.add(btnLogInWith_1);
		
		JButton btnLogWithUsernamepassword = new JButton("log with username&password");
		btnLogWithUsernamepassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginWithPwd loginwithpwd = new LoginWithPwd();
				loginwithpwd.setVisible(true);
				setVisible(false);
			}
		});
		btnLogWithUsernamepassword.setBounds(26, 229, 274, 29);
		contentPane.add(btnLogWithUsernamepassword);
	}
}
