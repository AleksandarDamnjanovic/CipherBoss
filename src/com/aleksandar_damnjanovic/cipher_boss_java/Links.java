package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Links extends JFrame {

	private JPanel contentPane;

	public Links() {
		setTitle("Links");
		setResizable(false);

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		Icon ok = null;
		Font font = null;
		try {
			this.setIconImage(ImageIO.read(new File("res/Images/lock_icon.png")));
			ok = new ImageIcon("res/Images/ok_icon.png");
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/Raleway-Bold.ttf")).deriveFont(18.0f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 112);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel licence = new JLabel("Licence");
		licence.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				try {
					Desktop.getDesktop().browse(new URI("https://choosealicense.com/licenses/unlicense/"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		licence.setCursor(new Cursor(Cursor.HAND_CURSOR));
		licence.setFont(font);
		licence.setHorizontalAlignment(SwingConstants.CENTER);
		licence.setBounds(166, 12, 106, 21);

		contentPane.add(licence);
		
		JLabel androidApp = new JLabel("Android version of Cipher Boss");
		androidApp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				try {
					Desktop.getDesktop().browse(new URI("https://play.google.com/store/apps/details?id=com.aleksandar_damnjanovic.cipherboss"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		androidApp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		androidApp.setFont(font);
		androidApp.setHorizontalAlignment(SwingConstants.CENTER);
		androidApp.setBounds(51, 45, 337, 21);
		contentPane.add(androidApp);

		this.setVisible(true);

	}
}