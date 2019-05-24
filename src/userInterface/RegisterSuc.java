package userInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Security.KeyValuePairs;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class RegisterSuc extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//for test
					RegisterSuc frame = new RegisterSuc(new KeyValuePairs(), "aa");
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
	public RegisterSuc(KeyValuePairs pair, String username) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblYourRegistrationIs = new JLabel("Your registration is succeesful!");
		lblYourRegistrationIs.setBounds(37, 35, 263, 39);
		contentPane.add(lblYourRegistrationIs);
		
		JLabel lblUsername = new JLabel("Username:"+ username);
		lblUsername.setBounds(37, 124, 160, 20);
		contentPane.add(lblUsername);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Login login = new Login();
				login.setVisible(true);
				setVisible(false);
			}
		});
		btnOk.setBounds(325, 412, 115, 29);
		contentPane.add(btnOk);
		
		JLabel lblPublicKey_1 = new JLabel("public key:");
		lblPublicKey_1.setBounds(37, 204, 94, 20);
		contentPane.add(lblPublicKey_1);
		
		JTextArea pubicKeyText = new JTextArea(pair.getPublicKey());
		pubicKeyText.setBounds(185, 204, 557, 78);
		pubicKeyText.setEditable(false);
		contentPane.add(pubicKeyText);
		
		JLabel lblPrivateKey = new JLabel("private key:");
		lblPrivateKey.setBounds(37, 325, 120, 20);
		contentPane.add(lblPrivateKey);
		
		JTextArea privateKeyText = new JTextArea(pair.getPublicKey());
		privateKeyText.setBounds(185, 325, 557, 71);
		privateKeyText.setEditable(false);
		contentPane.add(privateKeyText);
		
		JButton btnSaveIntoFile = new JButton("Save into File");
		btnSaveIntoFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 JFileChooser jfc=new JFileChooser();
				    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				    int result = jfc.showOpenDialog(jfc);
				    if(result == JFileChooser.APPROVE_OPTION) {
				    	  File file=jfc.getSelectedFile();
				    	  String publicKeyPath = String.format("%s\\pub%d.txt", file.getAbsolutePath(),System.currentTimeMillis());
						    String privateKeyPath = String.format("%s\\pri%d.txt", file.getAbsolutePath(),System.currentTimeMillis());
						    BufferedWriter out1;
						    BufferedWriter out2;
						    try {
						    	out1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(publicKeyPath,true)));
								out1.write(pair.getPublicKey());
								out1.close();
								
								out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(privateKeyPath,true)));
								out2.write(pair.getPrivateKey());
								out2.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
				    }	    
			}
		});
		btnSaveIntoFile.setBounds(112, 412, 168, 29);
		contentPane.add(btnSaveIntoFile);
	}
}
