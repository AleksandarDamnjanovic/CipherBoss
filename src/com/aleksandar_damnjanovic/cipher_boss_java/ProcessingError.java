package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProcessingError extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ProcessingError(String labelText) {
		
		labelText="<html><div style=\"text-align:center;\">"
				+ "Imposible to continue<br/>"+labelText+"</div></html>";
		
		Frame frame=this;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				frame.setAlwaysOnTop(true);
			}
			@Override
			public void windowClosed(WindowEvent e) {
				CipherBossMainClass.encryptDialog=null;
			}
		});
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		Icon ok=null;
		Font font=null;
		try {
			this.setIconImage(ImageIO.read(new File("res/Images/lock_icon.png")));
			ok=new ImageIcon("res/Images/ok_icon.png");
			font=Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/Raleway-Bold.ttf")).deriveFont(18.0f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
		setTitle("Processing Error");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 222);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(labelText);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 12, 424, 68);
		lblNewLabel.setFont(font);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnNewButton.setIcon(ok);
		btnNewButton.setBounds(195, 107, 60, 60);
		contentPane.add(btnNewButton);
	}
}