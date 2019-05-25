package userInterface;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Security.KeyValuePairs;
import database.SQLDB;
import utilities.Utilities;

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Button;
import java.awt.Dialog;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Register extends JFrame {

	//Todo: 在Utilities中限制用户名和密码长度
	private JPanel contentPane;
	private JTextField usernameText;
	private JTextField pwdText;
	private Dialog errorMessage;
	private Label errorLabel;
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
		KeyValuePairs pair = new KeyValuePairs();
		SQLDB.connSqlDB();
		
		errorMessage = new Dialog(this, "Error", true);
		errorMessage.setBounds(400, 200, 350, 150);
		errorMessage.setLayout(new FlowLayout());
		errorLabel = new Label();
        Button okBut = new Button("OK");
        okBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                errorMessage.setVisible(false);
            }
        });
        errorMessage.add(errorLabel);
        errorMessage.add(okBut);
        
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 558);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Utilities.checkUsername(usernameText.getText()) && Utilities.checkPwd(pwdText.getText())) {
					if(SQLDB.checkUsername(usernameText.getText())) {
						//generate user & save into db
						RegisterSuc suc = new RegisterSuc(pair, usernameText.getText(), pwdText.getText());
						suc.setVisible(true);
						setVisible(false);
					}else {
						//message
						errorLabel.setText("Duplicate Username");
						errorMessage.setVisible(true);
					}
				}else {
					System.out.println("123");
					errorLabel.setText("Username must be email Address\nAnd Password must be longer than 6 and shorter than 20");
					errorMessage.setVisible(true);
				}
			}
		});
		btnSave.setBounds(43, 246, 115, 29);
		contentPane.add(btnSave);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
				login.setVisible(true);
				setVisible(false);
			}
		});
		btnBack.setBounds(257, 246, 115, 29);
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
		
		pwdText = new JPasswordField();
		pwdText.setBounds(305, 104, 146, 26);
		contentPane.add(pwdText);
		pwdText.setColumns(10);
	}
}
