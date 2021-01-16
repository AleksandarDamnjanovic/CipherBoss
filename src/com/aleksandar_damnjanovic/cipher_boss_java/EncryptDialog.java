package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.aleksandar_damnjanovic.cipher_boss_java.support.Settings;

import javax.swing.JLabel;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class EncryptDialog extends JFrame {

	private JPanel contentPane;
	
	/**
	 * Only purpose of this dialog is to decide are you going to continue with encryption of not.
	 * If you decide to continue ProcessingWait class is going to take the rest of the job
	 */
	public EncryptDialog() {
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		setResizable(false);
		
		final Frame frame=this;
		this.setTitle("Cipher Boss encryption dialog");
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				CipherBossMainClass.encryptDialog=null;				
			}
		});
		
		Icon ok=null;
		Icon cancel=null;
		Font font=null;
		try {
			this.setIconImage(ImageIO.read(new File("res/Images/lock_icon.png")));
			ok=new ImageIcon("res/Images/ok_icon.png");
			cancel=new ImageIcon("res/Images/cancel_icon.png");
			font=Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/Raleway-Bold.ttf")).deriveFont(18.0f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 294, 171);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAreYouSure = new JLabel("<html>Are you sure that you want<br/> to continue with encryption?</html>");
		lblAreYouSure.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblAreYouSure.setBounds(12, 12, 268, 46);
		lblAreYouSure.setFont(font);
		contentPane.add(lblAreYouSure);
		
		JButton okCommand = new JButton("");
		okCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File keyFile=new File(Settings.getSettingValue(Settings.DEFAULT_KEY));
				if(!keyFile.exists()) {
					ProcessingError processing=new ProcessingError("There is no key file");
					processing.setVisible(true);
					frame.dispose();
					return;
				}
				
				byte[] keyData=new byte[(int)keyFile.length()];
				try {
					FileInputStream fis=new FileInputStream(keyFile);
					fis.read(keyData, 0, keyData.length);
					fis.close();
					
					SecretKey key=new SecretKeySpec(keyData,"AES");
					
					frame.setVisible(false);
					
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							ProcessingWait wait=new ProcessingWait();
							wait.addEffect("Please wait","Encryption process is running",key, true);
						}
					});
					
				} catch (IOException e) {
					e.printStackTrace();
					
					ProcessingError processing=null;
					
					if(e instanceof IOException)
						processing=new ProcessingError("Loading content error");
					
					if(processing!=null)
						processing.setVisible(true);
					frame.dispose();
					
					return;
				}
			}
		});
		okCommand.setToolTipText("Proceed with encryption");
		okCommand.setBounds(85, 70, 60, 60);
		okCommand.setIcon(ok);
		contentPane.add(okCommand);
		
		JButton cancelCommand = new JButton("");
		cancelCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				CipherBossMainClass.encryptDialog=null;
			}
		});
		cancelCommand.setToolTipText("Cancel");
		cancelCommand.setBounds(152, 70, 60, 60);
		cancelCommand.setIcon(cancel);
		contentPane.add(cancelCommand);
	}
	
}