package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.text.DecimalFormat;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.aleksandar_damnjanovic.cipher_boss_java.support.Settings;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProcessingWait extends JFrame {

	private JPanel contentPane;
	private String labelText = "";
	private String firstLine = "";
	private SecretKey key = null;
	private boolean encryption = false;
	private JFrame frame;
	JLabel lblNewLabel;
	JButton okCommand;
	boolean runningProcess = false;

	/**
	 * This dialog is responsible for actual encryption of the content. Or in the
	 * case that encryption is done, to show that information.
	 */
	public ProcessingWait() {
		setResizable(false);

		frame = this;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				frame.setAlwaysOnTop(true);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				if (!runningProcess)
					frame.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				CipherBossMainClass.encryptDialog = null;
				CipherBossMainClass.decryptDialog = null;
			}
		});

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

		setTitle("Processing");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 258);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblNewLabel = new JLabel(labelText);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 12, 424, 129);
		lblNewLabel.setFont(font);
		contentPane.add(lblNewLabel);

		okCommand = new JButton("");
		okCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!runningProcess)
					frame.dispose();
			}
		});
		okCommand.setBounds(197, 153, 60, 60);
		okCommand.setIcon(ok);
		contentPane.add(okCommand);

		setVisible(true);
		this.show();
	}

	long fullLength = 0;
	long addition = 0;
	String per = "";

	/**
	 * Procedure addEffect supply ProcessingWait class with parameters for encryption or decryption process
	 * @param firstLine_add text in the first line of the label
	 * @param labelText_add text in the second line of the label
	 * @param key_add sacret key that is going to be used for process. If null indicates that process is over
	 * @param encryption_add if true process will encrypt content, if false decryption is on the way.
	 */
	public void addEffect(String firstLine_add, String labelText_add, SecretKey key_add, boolean encryption_add) {
		firstLine = firstLine_add;
		labelText = labelText_add;
		key = key_add;
		encryption = encryption_add;

		for (File f : CipherBossMainClass.dropped)
			fullLength += f.length();

		if (key != null) {
			runningProcess = true;
			okCommand.setVisible(false);
			if (encryption) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						process(frame, true);
					}
				}).start();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						process(frame, false);
					}
				}).start();
			}
		} else {
			okCommand.setVisible(true);
			labelText = "<html><div style=\"text-align:center;\">" + firstLine + "<br/>" + labelText + "</div></html>";
			lblNewLabel.setText(labelText);
		}
	}

	private void process(JFrame frame, boolean encrypt) {

		int num = 0;
		try {
			FileInputStream fis;
			String outputDirectory = Settings.getSettingValue(Settings.DEFAULT_DIRECTORY);
			if (outputDirectory.equals(""))
				outputDirectory = ".";
			else if (!(new File(outputDirectory).exists()))
				new File(outputDirectory).mkdirs();

			FileOutputStream fos;
			Cipher cipher = CipherBossMainClass.cipher;

			for (File f : CipherBossMainClass.dropped) {
				
				if(encrypt)
					cipher.init(Cipher.ENCRYPT_MODE, key);
				else
					cipher.init(Cipher.DECRYPT_MODE, key);
				
				fis = new FileInputStream(f);
				
				File file=null;
				if(encrypt)
					file = new File(outputDirectory + "/" + f.getName() + "Encrypted by Cipher Boss");
				else
					if(f.getAbsolutePath().contains("Encrypted by Cipher Boss"))
						file=new File(outputDirectory +"/"+f.getName().replace("Encrypted by Cipher Boss", ""));
					else
						file=new File(outputDirectory + "/" +f.getName());

				if (file.exists())
					file=new File(file.getAbsolutePath()+"copy");

				int limit = 10 * 1024 * 1024;
				fis = new FileInputStream(f);
				int position = 0;
				int border = limit;
				if (f.length() > (limit)) {
					fos = new FileOutputStream(file, true);

					while (position < f.length()) {
						byte[] data = null;

						if (f.length() - position > (limit))
							data = new byte[limit];
						else
							data = new byte[(int) f.length() - position];

						addition += data.length;
						double percent = (double) addition / (double) fullLength;
						percent = Double.parseDouble(new DecimalFormat("##.##").format(percent)) * 100;
						per = String.valueOf(new DecimalFormat("##.##").format(percent)) + "%";

						try {
							EventQueue.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									frame.setBounds(100, 100, 450, 180);
									String fileName = f.getName().split("\\.")[0];
									String display = "<html><div style=\"text-align:center;\">" + "Processing <br/>"
											+ fileName + "<br/>" + "Process is at " + per + "</div></html>";
									lblNewLabel.setText(display);
								}
							});
						} catch (InvocationTargetException | InterruptedException e) {
							e.printStackTrace();
						}

						num = fis.read(data);
						data = cipher.update(data);

						fos.write(data, 0, data.length);
						fos.flush();

						if (f.length() - (position + limit) > limit)
							position += limit;
						else if (position == f.length() % limit)
							position = (int) f.length();
						else
							position += limit;

						if (f.length() - position > limit)
							border = position + limit;
						else
							border = (int) f.length();

					}

					fis.close();
					fos.close();

				} else {
					byte[] data = new byte[(int) f.length()];

					fis.read(data, 0, data.length);
					fis.close();

					data = cipher.doFinal(data);

					fos = new FileOutputStream(file);
					fos.write(data, 0, data.length);
					fos.flush();
					fos.close();
				}
			}

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.dispose();
			ProcessingWait wait;
			wait = new ProcessingWait();
			
			if(encryption)
				wait.addEffect("Success", "Encryption process is finished :-)", null, false);
			else
				wait.addEffect("Success", "Decryption process is finished :-)", null, false);
			
		} catch (Exception e) {
			e.printStackTrace();

			ProcessingError processing = null;

			if (e instanceof InvalidKeyException)
				processing = new ProcessingError("Invalid key error");
			else if (e instanceof IllegalBlockSizeException)
				processing = new ProcessingError("Invalid key");
			else
				processing = new ProcessingError("Processing error");

			if (processing != null)
				processing.setVisible(true);

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.dispose();
		}
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		runningProcess = false;
	}

}