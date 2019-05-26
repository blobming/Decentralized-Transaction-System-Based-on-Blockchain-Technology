package userInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Security.SecretKey;
import config.Global;
import database.SQLDB;
import obj.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class LoginWithKey extends JFrame {
	
	private JPanel contentPane;
	private JTextField publicKeyPath;
	private JTextField privateKeyPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWithKey frame = new LoginWithKey();
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
	public LoginWithKey() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 737, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPublicKeyPath = new JLabel("Public key path:");
		lblPublicKeyPath.setBounds(15, 71, 145, 20);
		contentPane.add(lblPublicKeyPath);
		
		publicKeyPath = new JTextField();
		publicKeyPath.setBounds(209, 68, 313, 26);
		publicKeyPath.setEditable(false);
		contentPane.add(publicKeyPath);
		publicKeyPath.setColumns(10);
		
		JButton btnChoose = new JButton("choose");
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();
			    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    int value = jfc.showSaveDialog(jfc);
			    if(value == JFileChooser.APPROVE_OPTION) {
			    	File file = jfc.getSelectedFile();
			    	publicKeyPath.setText(file.getAbsolutePath());
			    }
			}
		});
		btnChoose.setBounds(585, 67, 115, 29);
		contentPane.add(btnChoose);
		
		JLabel lblPrivateKeyPath = new JLabel("Private key path:");
		lblPrivateKeyPath.setBounds(15, 160, 133, 20);
		contentPane.add(lblPrivateKeyPath);
		
		privateKeyPath = new JTextField();
		privateKeyPath.setBounds(209, 157, 313, 26);
		privateKeyPath.setEditable(false);
		contentPane.add(privateKeyPath);
		privateKeyPath.setColumns(10);
		
		JButton btnChoose_1 = new JButton("choose");
		btnChoose_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();
			    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    int value = jfc.showSaveDialog(jfc);
			    if(value == JFileChooser.APPROVE_OPTION) {
			    	File file = jfc.getSelectedFile();
				    privateKeyPath.setText(file.getAbsolutePath());
			    }
			    
			}
		});
		btnChoose_1.setBounds(585, 156, 115, 29);
		contentPane.add(btnChoose_1);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User user =	checkKey(); 
				if(user != null) {
					Global.user = user;
					networkCardPage welcomePage = new networkCardPage();
					welcomePage.setVisible(true);
					setVisible(false);
				}else {
					JOptionPane.showMessageDialog(LoginWithKey.getFrames()[0], "wrong private& public key", "Wrong!", JOptionPane.WARNING_MESSAGE);
				}	
			}
		});
		btnLogIn.setBounds(254, 278, 115, 29);
		contentPane.add(btnLogIn);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
				login.setVisible(true);
				setVisible(false);
			}
		});
		btnBack.setBounds(455, 278, 115, 29);
		contentPane.add(btnBack);
	}
	
	public User checkKey() {
		//read public key & pivate key from file
        File publickeyFile = new File(publicKeyPath.getText());   
        InputStreamReader publickeyreader;
        StringBuilder publickeyBuilder = new StringBuilder();
		try {
			publickeyreader = new InputStreamReader(new FileInputStream(publickeyFile));
			BufferedReader publickeybr = new BufferedReader(publickeyreader);  
			String line = "";    
            while ((line = publickeybr.readLine()) != null) {  
            	publickeyBuilder.append(line); 
            }  
            publickeybr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		String publicKeyString = publickeyBuilder.toString();
		System.out.println("publickey:" + publicKeyString);
		File privatekeyFile = new File(privateKeyPath.getText());   
        InputStreamReader privatekeyreader;
        StringBuilder privateKeyBuilder = new StringBuilder();
		try {
			privatekeyreader = new InputStreamReader(new FileInputStream(privatekeyFile));
			BufferedReader privatekeybr = new BufferedReader(privatekeyreader);  
			String line = "";    
            while ((line = privatekeybr.readLine()) != null) {  
            	privateKeyBuilder.append(line); 
            }  
            privatekeybr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		String privateKeyfromFile = privateKeyBuilder.toString();
		System.out.println("privatekey:" + privateKeyfromFile);
		
		///////////////
		User user = SQLDB.getUserByKey(publicKeyString);
		if(user == null || !user.getPrivateKey().equals(privateKeyfromFile))	return null;
		
		byte[] cipherText = SecretKey.encrypt(user.getUsername().getBytes(), publicKeyString); 
        String tempStr = Base64.getEncoder().encodeToString(cipherText);
        byte[] plainText = SecretKey.decrypt(Base64.getDecoder().decode(tempStr),privateKeyfromFile);
        
        if(!user.getUsername().equals(new String(plainText)))	return null;
        
        return user;    
	}
}
