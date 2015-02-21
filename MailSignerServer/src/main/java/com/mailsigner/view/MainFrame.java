package com.mailsigner.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class MainFrame extends JFrameCenter implements ActionListener {
	
	private static final long serialVersionUID = 2292455670620751734L;
	private JMenuItem mntmAdd;
	private JMenuItem mntmRemove;
	private JMenuItem mntmRename;
	private JMenuItem mntmSave;
	private JMenuItem mntmQuit;
	private JMenuItem mntmCut;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JMenuItem mntmSettings;
	private JMenuItem mntmAbout;
	private JTabbedPane tabbedPane;
	
	private static SignatureTab signatureTab = new SignatureTab();
	private static UserTab userTab = new UserTab();
	
	private SettingsDialog settingsDialog;
	private JPanel panel;
	private static JProgressBar progressBar;
	private Component verticalStrut;
	private static JLabel toolTip;
	
	/**
	 * Create the frame.
	 */
	public MainFrame(){
		setTitle("MailSigner");
		
		settingsDialog = new SettingsDialog();
		
		prepareLayout();
		
		progressBar.setVisible(false);
	}
	
	/**
	 * Creates the main frame's layout
	 */
	public void prepareLayout() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 556);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab("Signature", signatureTab);
		tabbedPane.addTab("User", userTab);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		toolTip = new JLabel("");
		panel.add(toolTip);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		
		verticalStrut = Box.createVerticalStrut(20);
		panel.add(verticalStrut);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmAdd = new JMenuItem("Add");
		mntmAdd.addActionListener(this);
		mntmAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK, true));
		mnFile.add(mntmAdd);
		
		mntmRemove = new JMenuItem("Remove");
		mntmRemove.addActionListener(this);
		mntmRemove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK, true));
		mnFile.add(mntmRemove);
		
		mntmRename = new JMenuItem("Rename");
		mntmRename.addActionListener(this);
		mntmRename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK, true));
		mnFile.add(mntmRename);
		
		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(this);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, true));
		mnFile.add(mntmSave);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(this);
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK, true));
		mnFile.add(mntmQuit);
		
		JMenu mnNewMenu = new JMenu("Edit");
		menuBar.add(mnNewMenu);
		
		mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(this);
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, true));
		mnNewMenu.add(mntmCut);
		
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(this);
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, true));
		mnNewMenu.add(mntmCopy);
		
		mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(this);
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, true));
		mnNewMenu.add(mntmPaste);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		mntmSettings = new JMenuItem("Settings");
		mntmSettings.addActionListener(this);
		mnNewMenu.add(mntmSettings);
		
		JMenu mnNewMenu_1 = new JMenu("Help");
		menuBar.add(mnNewMenu_1);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(this);
		mnNewMenu_1.add(mntmAbout);
	}
	
	public static void setToolTip(String tip) {
		toolTip.setText(tip);
	}
	
	public static void clearToolTip() {
		toolTip.setText("");
	}
	
	public static void setProgressBar(int percent) {
		progressBar.setValue(percent);
	}
	
	public static void showProgressBar() {
		progressBar.setVisible(true);
	}
	
	public static void hideProgressBar() {
		progressBar.setVisible(false);
	}
	
	public static void toggleProgressBar() {
		progressBar.setVisible(!progressBar.isVisible());
		setProgressBar(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedTab = tabbedPane.getSelectedIndex();
		if(e.getSource() == mntmAdd) {
			switch(selectedTab) {
				case 0: signatureTab.addSignatureAction() ;break;
				case 1: userTab.addUser(); break;
			}
		}
		
		if(e.getSource() == mntmRemove) {
			switch(selectedTab) {
				case 0: signatureTab.removeSignatureAction(); break;
				case 1: userTab.removeUser(); break;
			}
		}
		
		if(e.getSource() == mntmRename) {
			switch(selectedTab) {
				case 0: signatureTab.renameSignatureAction(); break;
				case 1: userTab.renameUser(); break;
			}
		}
		
		if(e.getSource() == mntmSave) {
			switch(selectedTab) {
				case 0: signatureTab.saveSignatureAction(false); break;
				case 1: break;
			}
		}
		
		if(e.getSource() == mntmQuit) {
			System.exit(0);
		}
		
		if(e.getSource() == mntmCopy) {
			switch(selectedTab) {
				case 0: break;
				case 1: break;
			}
		}
		
		if(e.getSource() == mntmCut) {
			switch(selectedTab) {
				case 0: break;
				case 1: break;
			}
		}
		
		if(e.getSource() == mntmPaste) {
			switch(selectedTab) {
				case 0: break;
				case 1: break;
			}
		}
		
		if(e.getSource() == mntmSettings) {
			settingsDialog.setVisible(true);
		}
		
		if(e.getSource() == mntmAbout) {
			//TODO: To be implemented
		}
	}

	public static SignatureTab getSignatureTab() {
		return signatureTab;
	}

	public static UserTab getUserTab() {
		return userTab;
	}
}