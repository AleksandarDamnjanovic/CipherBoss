package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.aleksandar_damnjanovic.cipher_boss_java.support.Settings;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GenerateKeyDialog extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public GenerateKeyDialog() {
		setResizable(false);
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		setTitle("Cipher Boss generate key dialog");
		
		Frame frame=this;
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				CipherBossMainClass.generateKeyDialog=null;				
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
		setBounds(100, 100, 450, 158);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Proceed with key creation process");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(29, 12, 392, 30);
		lblNewLabel.setFont(font);
		contentPane.add(lblNewLabel);
		
		JButton okCommand = new JButton("");
		okCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser chooser=new JFileChooser();
				chooser.setDialogTitle("Select key name and location");
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				chooser.setCurrentDirectory(new File("."));
				
				if(chooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION) {
					try {
						
						File keyFile=chooser.getSelectedFile();
						
						if(!keyFile.getAbsolutePath().endsWith(".key")) 
							keyFile=new File(keyFile.getAbsolutePath()+".key");
						
						SecureRandom secure=SecureRandom.getInstance("SHA1PRNG");
						KeyGenerator generator=KeyGenerator.getInstance("AES");
						generator.init(256,secure);
						SecretKey key=generator.generateKey();
						byte[] keyData=key.getEncoded();
						
						FileOutputStream fos=new FileOutputStream(keyFile);
						fos.write(keyData, 0, keyData.length);
						fos.flush();
						fos.close();
						
						if(Settings.getSettingValue(Settings.DEFAULT_KEY).equals(""))
							Settings.writeSetting(Settings.DEFAULT_KEY, keyFile.getAbsolutePath());
						
						KeyGenerated generated=new KeyGenerated();
						generated.setVisible(true);
						frame.dispose();
						CipherBossMainClass.generateKeyDialog=null;
						
					} catch (NoSuchAlgorithmException | IOException e) {
						e.printStackTrace();
					}	
				}
			}
		});
		okCommand.setToolTipText("Generate");
		okCommand.setBounds(158, 54, 60, 60);
		okCommand.setIcon(ok);
		contentPane.add(okCommand);
		
		JButton cancelCommand = new JButton("");
		cancelCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				CipherBossMainClass.generateKeyDialog=null;
			}
		});
		cancelCommand.setToolTipText("Generate");
		cancelCommand.setBounds(236, 54, 60, 60);
		cancelCommand.setIcon(cancel);
		contentPane.add(cancelCommand);
	}
}