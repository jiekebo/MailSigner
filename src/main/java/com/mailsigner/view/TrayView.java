package com.mailsigner.view;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.mailsigner.control.TrayControl;
import com.mailsigner.model.persistence.Usersignature;

public class TrayView implements ActionListener, MouseListener, ItemListener {
	private PopupMenu menu;
	private TrayIcon ti;
	private MenuItem quit;
	private MenuItem redeploy;
	private MenuItem clipboard;
	private PopupMenu formatMenu;
	private CheckboxMenuItem html;
	private CheckboxMenuItem rtf;
	private CheckboxMenuItem txt;
	private TrayControl trayControl;
	private InetAddress serverAddress;
	private PopupMenu signatureMenu;
	private ArrayList<CheckboxMenuItem> formatCheckboxGroup;
	private HashMap<CheckboxMenuItem, Usersignature> signatureCheckboxGroup;
	private Set<Usersignature> signatures;
	private Usersignature selectedSignature;
	private Integer selectedFormat;

	public TrayView(TrayControl trayControl, InetAddress serverAddress) {
		signatures = trayControl.loadUsersSignatures();
		this.trayControl = trayControl;
		this.serverAddress = serverAddress;

	}
	
	public void prepareLaout() {
		menu = new PopupMenu();
		
		
		menu.add("Login: " + trayControl.getUserLogin() + " Computer: " + trayControl.getComputerName());		
		if(serverAddress != null) {
			menu.add("Server located at: " + serverAddress.toString());
		} else {
			menu.add("Server not available!");
		}
		
		
		menu.addSeparator();
		
		
		redeploy = new MenuItem("Deploy Outlook signatures");
		redeploy.addActionListener(this);
		menu.add(redeploy);
		
		
		menu.addSeparator();
		
		
		signatureMenu = new PopupMenu("Select signature");
		menu.add(signatureMenu);
		
		signatureCheckboxGroup = new HashMap<CheckboxMenuItem, Usersignature>();
		
		formatMenu = new PopupMenu("Select signature format");
		menu.add(formatMenu);
		
		html = new CheckboxMenuItem("HTML Signature");
		html.addItemListener(this);
		rtf = new CheckboxMenuItem("RTF Signature");
		rtf.addItemListener(this);
		txt = new CheckboxMenuItem("TXT Signature");
		txt.addItemListener(this);
		
		formatCheckboxGroup = new ArrayList<CheckboxMenuItem>();
		
		formatCheckboxGroup.add(html);
		formatCheckboxGroup.add(rtf);
		formatCheckboxGroup.add(txt);		
		
		formatMenu.add(html);
		formatMenu.add(rtf);
		formatMenu.add(txt);
		
		clipboard = new MenuItem("Copy signature to clipboard");
		clipboard.addActionListener(this);
		menu.add(clipboard);

		
		menu.addSeparator();
		
		
		quit = new MenuItem("Quit Mailsigner");
		quit.addActionListener(this);
		menu.add(quit);
		
		// Get the system tray functionality
		SystemTray st = SystemTray.getSystemTray();
		
		// And add a tray icon to it
		Image icon = Toolkit.getDefaultToolkit().getImage("assets/198.png");
		ti = new TrayIcon(icon, "Mailsigner", menu);
		try {
			st.add(ti);
			ti.addActionListener(this);
			ti.addMouseListener(this);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == redeploy) {
			trayControl.deploy();
		}
		if(e.getSource() == clipboard) {
			if(selectedSignature == null || selectedFormat == null) {
				return;
			}
			trayControl.clipboard(selectedSignature, selectedFormat);
		}
		if(e.getSource() == quit) {
			System.exit(0);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//TODO: at every click on the icon, signatures are reloaded. Maybe this
		// can be done using the udp-multicast, with messaging to clients about
		// updates made
		if(e.getSource() == ti) {
			if(!trayControl.isConnected()) {
				return;
			}
			Set<Usersignature> newSignatures = trayControl.loadUsersSignatures();
			signatures = newSignatures;
			trayControl.loadUsersSignatures();
			signatureMenu.removeAll();
			signatureCheckboxGroup.clear();
			for (Usersignature usersignature : signatures) {
				if(usersignature.getEnabled() == 0) {
					continue;
				}
				// Working the garbage collector here!
				CheckboxMenuItem signatureCheckbox = new CheckboxMenuItem(usersignature.getSignature().getTitle());
				signatureMenu.add(signatureCheckbox);
				signatureCheckbox.addItemListener(this);
				// Match the new signature on the signatures title (ensured by gui unique title).
				if(selectedSignature != null && 
						usersignature.getSignature().getTitle() == selectedSignature.getSignature().getTitle()) {
					signatureCheckbox.setState(true);
				}
				signatureCheckboxGroup.put(signatureCheckbox, usersignature);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(formatCheckboxGroup.contains(e.getSource())) {
			for (CheckboxMenuItem item : formatCheckboxGroup) {
				if(item != e.getSource()) {
					item.setState(false);
				} else {
					selectedFormat = formatCheckboxGroup.indexOf(item);
				}
			}
		} else {
			Set<Entry<CheckboxMenuItem, Usersignature>> entrySet = signatureCheckboxGroup.entrySet();
			for (Entry<CheckboxMenuItem, Usersignature> entry : entrySet) {
				if(entry.getKey() != e.getSource()) {
					entry.getKey().setState(false);
				} else {
					selectedSignature = entry.getValue();
				}
			}
		}
	}
}
