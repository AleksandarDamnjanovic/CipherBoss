package com.aleksandar_damnjanovic.cipher_boss_java;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.aleksandar_damnjanovic.cipher_boss_java.support.CustomDialogFocusListener;
import com.aleksandar_damnjanovic.cipher_boss_java.support.Settings;
import com.aleksandar_damnjanovic.cipher_boss_java.support.Strings;

import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.ActionEvent;

public class CipherBossMainClass extends JFrame {

	private JPanel contentPane;
	private JTextPane textPane;
	public static List<File> dropped;

	public static EncryptDialog encryptDialog;
	public static DecryptDialog decryptDialog;
	public static GenerateKeyDialog generateKeyDialog;
	public static Links links;
	
	public static Strings strings;
	public static Cipher cipher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CipherBossMainClass frame = new CipherBossMainClass();
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
	public CipherBossMainClass() {
		
		try {
			cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		String defaultLanguage = Settings.getSettingValue(Settings.DEFAULT_LANGUAGE);
		
		if(defaultLanguage.equals("")) {
			Settings.writeSetting(Settings.DEFAULT_LANGUAGE, "english");
			defaultLanguage="english";
		}
		
		strings=new Strings(defaultLanguage);
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 356, 645);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);

		Icon encryptIcon = null;
		Icon decryptIcon = null;
		Icon storageIcon = null;
		Icon generateKeyIcon = null;
		Icon chooseDefaultKeyIcon = null;
		Icon manualIcon = null;
		Icon linksIcon = null;
		Icon clearScreenIcon = null;
		Icon exitIcon = null;

		Font font = null;
		try {
			exitIcon = new ImageIcon("res/Images/exit_icon.png");
			clearScreenIcon = new ImageIcon("res/Images/clear_icon.png");
			linksIcon = new ImageIcon("res/Images/links_icon.png");
			manualIcon = new ImageIcon("res/Images/manual_icon.png");
			chooseDefaultKeyIcon = new ImageIcon("res/Images/set_default_key_icon.png");
			generateKeyIcon = new ImageIcon("res/Images/generate_icon.png");
			storageIcon = new ImageIcon("res/Images/storage_icon.png");
			encryptIcon = new ImageIcon("res/Images/lock_icon.png");
			decryptIcon = new ImageIcon("res/Images/unlock_icon.png");
			this.setIconImage(ImageIO.read(new File("res/Images/lock_icon.png")));
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/Raleway-Bold.ttf")).deriveFont(18.0f);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 329, 450);
		contentPane.add(scrollPane);

		textPane = new JTextPane();

		textPane.setBackground(new Color(80, 80, 80));
		textPane.setForeground(new Color(5, 170, 5));
		textPane.setEditable(false);

		textPane.setFont(font);

		textPane.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent event) {
				event.acceptDrop(DnDConstants.ACTION_LINK);
				try {
					dropped = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					textPane.setText("");
					for (File f : dropped) {
						addToTerminal("Cipher Boss-->\nPath: " + f.getAbsolutePath() + "\nFile size: "
								+ formatedFileSize((int) f.length()) + "\n");
					}
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		scrollPane.setViewportView(textPane);

		JButton encryptCommand = new JButton("");
		encryptCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(cipher==null) {
					addToTerminal("Cipher cannot be initialized. Please contact application developer.");
					return;
				}
				
				if(dropped==null) {
					addToTerminal("You must have content in order to proceed!");
					return;
				}
				
				if(Settings.getSettingValue(Settings.DEFAULT_KEY).equals("")) {
					addToTerminal("You must set default key in order to proceed!");
					return;
				}
				
				if (dialogs()) {
					encryptDialog = new EncryptDialog();
					openDialogHandler(encryptDialog);
				}
			}
		});
		encryptCommand.setBounds(12, 472, 60, 60);
		encryptCommand.setToolTipText("Encrypt");
		encryptCommand.setIcon(encryptIcon);

		contentPane.add(encryptCommand);

		JButton decryptCommand = new JButton("");
		decryptCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dialogs()) {
					decryptDialog = new DecryptDialog();
					openDialogHandler(decryptDialog);
				}
			}
		});

		decryptCommand.setBounds(74, 472, 60, 60);
		decryptCommand.setToolTipText("Decrypt");
		decryptCommand.setIcon(decryptIcon);

		contentPane.add(decryptCommand);

		JButton storageCommand = new JButton("");
		storageCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dialogs()) {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File("."));
					chooser.setDialogTitle("Select default output directory");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					if (chooser.showOpenDialog(CipherBossMainClass.this) == JFileChooser.APPROVE_OPTION) {
						Settings.writeSetting(Settings.DEFAULT_DIRECTORY, chooser.getSelectedFile().getAbsolutePath());
						addToTerminal("Default directory set to " + chooser.getSelectedFile().getAbsolutePath());
					}
				}
			}
		});
		storageCommand.setToolTipText("Default output directory");
		storageCommand.setBounds(146, 472, 60, 60);
		storageCommand.setIcon(storageIcon);
		contentPane.add(storageCommand);

		JButton generateCommand = new JButton("");
		generateCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dialogs()) {
					generateKeyDialog = new GenerateKeyDialog();
					openDialogHandler(generateKeyDialog);
				}
			}
		});
		generateCommand.setToolTipText("Generate key");
		generateCommand.setBounds(219, 472, 60, 60);
		generateCommand.setIcon(generateKeyIcon);
		contentPane.add(generateCommand);

		JButton chooseDefaultKey = new JButton("");
		chooseDefaultKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dialogs()) {
					FileFilter filter=new FileNameExtensionFilter("Key files","key");	
					JFileChooser chooser=new JFileChooser();
					chooser.setFileFilter(filter);
					chooser.setDialogType(JFileChooser.OPEN_DIALOG);
					chooser.setDialogTitle("Choose default key");
					chooser.setCurrentDirectory(new File("."));
					
					if(chooser.showOpenDialog(CipherBossMainClass.this)==JFileChooser.APPROVE_OPTION)
						Settings.writeSetting(Settings.DEFAULT_KEY, chooser.getSelectedFile().getAbsolutePath());
					
					addToTerminal("You have selected new default key");
				}
			}
		});
		chooseDefaultKey.setToolTipText("Choose default key");
		chooseDefaultKey.setBounds(281, 472, 60, 60);
		chooseDefaultKey.setIcon(chooseDefaultKeyIcon);
		contentPane.add(chooseDefaultKey);

		JButton manualCommand = new JButton("");
		manualCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dialogs()) {
					textPane.setText("");
					addToTerminal(strings.getManual());
				}
			}
		});
		manualCommand.setToolTipText("Manual");
		manualCommand.setBounds(12, 544, 60, 60);
		manualCommand.setIcon(manualIcon);
		contentPane.add(manualCommand);

		JButton linksCommand = new JButton("");
		linksCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dialogs())
					links=new Links();
			}
		});
		linksCommand.setToolTipText("Links");
		linksCommand.setBounds(74, 544, 60, 60);
		linksCommand.setIcon(linksIcon);
		contentPane.add(linksCommand);

		JButton clearCommand = new JButton("");
		clearCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dialogs())
					textPane.setText("");
			}
		});
		clearCommand.setToolTipText("Clear display");
		clearCommand.setBounds(219, 544, 60, 60);
		clearCommand.setIcon(clearScreenIcon);
		contentPane.add(clearCommand);

		JButton exitCommand = new JButton("");
		exitCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dialogs())
					System.exit(0);
			}
		});
		exitCommand.setToolTipText("Exit");
		exitCommand.setBounds(281, 544, 60, 60);
		exitCommand.setIcon(exitIcon);
		contentPane.add(exitCommand);

		this.setTitle("Cipher Boss");

	}

	/**
	 * Ads one more line to terminal window
	 * 
	 * @param text
	 */
	private void addToTerminal(String text) {

		int limit = 27;
		String finalText = "";
		if (text.length() > limit) {
			int previous = 0;
			for (int i = 0; i < text.length(); i += limit) {
				int next;
				if (previous + limit < text.length())
					next = previous + limit;
				else
					next = text.length();
				String line = text.substring(previous, next);
				if (!line.contains(" "))
					finalText += line + (next != text.length() && !line.contains("\n") ? "\n" : "");
				else
					finalText += line;
				previous = next;
			}
		} else {
			finalText = text;
		}

		textPane.setText(textPane.getText() + finalText+"\n");
	}

	/**
	 * Returns formated file size
	 * 
	 * @param size
	 * @return
	 */
	private String formatedFileSize(int size) {

		float length = (float) size;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String sufix = "";

		if (length >= (1024 * 1024 * 1024)) {
			length = length / (1024 * 1024 * 1024);
			sufix = "GB";
		} else if (length >= (1024 * 1024)) {
			length = length / (1024 * 1024);
			sufix = "MB";
		} else if (length >= 1024) {
			length = length / 1024;
			sufix = "KB";
		} else {
			sufix = "bytes";
		}

		return df.format(length) + " " + sufix;
	}

	/**
	 * Check if any of existing dialogs are open.
	 * 
	 * @return
	 */
	private boolean dialogs() {

		if (encryptDialog != null)
			return false;

		if (decryptDialog != null)
			return false;

		if (generateKeyDialog != null)
			return false;

		return true;
	}

	private void openDialogHandler(Frame frame) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.addWindowFocusListener(new CustomDialogFocusListener(frame));
				frame.setVisible(true);
			}
		});
	}

}