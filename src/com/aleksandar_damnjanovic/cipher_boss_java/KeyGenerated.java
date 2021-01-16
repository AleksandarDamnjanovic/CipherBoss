package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class KeyGenerated extends JFrame {

	private JPanel contentPane;

	public KeyGenerated() {
		Frame frame=this;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				frame.dispose();
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
		
		setResizable(false);
		setTitle("Key confirmation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 328, 176);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Key created successfully");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(40, 24, 249, 15);
		lblNewLabel.setFont(font);
		contentPane.add(lblNewLabel);
		
		JButton confirmButton = new JButton("");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		confirmButton.setBounds(132, 61, 60, 60);
		confirmButton.setIcon(ok);
		contentPane.add(confirmButton);
	}
}