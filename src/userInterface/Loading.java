package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import config.Global;
import obj.UTXOSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Loading extends JFrame {

	private JPanel contentPane;
	public int currentProgress = 0;
	public int maxHeight;
	public Timer timer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Loading frame = new Loading();
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
	public Loading() {
		int originalHeight = UTXOSet.blockchain.getHeight();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 862, 487);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDownloadingAllThe = new JLabel("Downloading all the blocks, please wait....");
		lblDownloadingAllThe.setBounds(156, 47, 434, 20);
		contentPane.add(lblDownloadingAllThe);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(currentProgress);
        progressBar.setStringPainted(true);

		progressBar.setBounds(48, 176, 706, 30);
		
		contentPane.add(progressBar);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.homepage.setVisible(true);
				setVisible(false);
			}
		});
		btnNext.setVisible(false);
		btnNext.setBounds(309, 338, 115, 29);
		contentPane.add(btnNext);
		
		timer = new Timer(1000, new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
//				currentProgress+= 20; 
				if (progressBar.getValue() == 100) {
					btnNext.setVisible(true);
					timer.stop();
				}
				System.out.println(currentProgress + "\t"+ maxHeight);
				int value = maxHeight == 0 ? 100 : (int)(currentProgress*1.00 / (maxHeight - originalHeight)*100); 
				progressBar.setValue(value);
			} 
		});
		timer.start();
		
	}
}
