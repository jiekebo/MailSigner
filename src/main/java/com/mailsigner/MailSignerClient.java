package com.mailsigner;

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import com.mailsigner.control.Settings;
import com.mailsigner.control.TrayControl;
import com.mailsigner.control.discovery.MulticastClient;
import com.mailsigner.view.TrayView;


public class MailSignerClient {
	
	private static Settings settings;
	private static Logger log = Logger.getLogger(MailSignerClient.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		settings = new Settings();
		settings.loadFromFile(".", "settings.ini");
		settings.readSettings();
		settings.writeSettings();
		
		MulticastClient testClient = new MulticastClient();
		InetAddress serverAddress = testClient.getServerAddress();
		
		TrayControl trayControl = new TrayControl();
		trayControl.connectDB(serverAddress);
				
		TrayView tray = new TrayView(trayControl, serverAddress);
		tray.prepareLaout();
	}

	public static Settings getSettings() {
		return settings;
	}
	
	public static Logger getLog() {
		return log;
	}
	
}
