package com.mailsigner;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

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
		
		// TODO: Make the webservice poll and reconnect.
		URL serviceLocation = null;
		try {
			serviceLocation = new URL("http:/" + serverAddress + ":2563/ws/mailsigner/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		trayControl.connectWebservice(serviceLocation, "http://ws.control.mailsigner.com/", "MailSignerServiceImplService");
		
		TrayView tray = new TrayView(trayControl, serverAddress);
		tray.prepareLayout();
	}

	public static Settings getSettings() {
		return settings;
	}
	
	public static Logger getLog() {
		return log;
	}
	
}
