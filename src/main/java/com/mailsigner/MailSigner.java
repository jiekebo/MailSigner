package com.mailsigner;

import java.awt.EventQueue;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.UIManager;
import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.mailsigner.control.SignatureController;
import com.mailsigner.control.UserController;
import com.mailsigner.control.discovery.MulticastServer;
import com.mailsigner.control.runnable.ServerThread;
import com.mailsigner.control.ws.MailSignerServiceImpl;
import com.mailsigner.util.DatabaseUtil;
import com.mailsigner.util.Settings;
import com.mailsigner.view.LoginFrame;
import com.mailsigner.view.MainFrame;

public class MailSigner {

	private static Settings settings;
	private static MainFrame mainFrame;
	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static EntityTransaction entr;
	
	private static UserController userController;
	private static SignatureController signatureController;
	
	private static Logger log = Logger.getLogger(MailSigner.class);

	// Commmand line arguments
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Parameters param = new Parameters();
		new JCommander(param, args);
		
		// Load settings
		settings = new Settings();
		settings.loadFromFile(".", "settings.ini");
		settings.readSettings();
		
		// Detect if database exists
		File databaseFile = new File(settings.getDatapath());
		boolean databaseExists = databaseFile.isDirectory();
		
		// Start database
		ServerThread hsqlThread = new ServerThread();
		Thread serverThread = new Thread(hsqlThread);
		serverThread.start();
				
		// Start persistence
		emf = Persistence.createEntityManagerFactory("MailSignerJPA");
		em = emf.createEntityManager();
		entr = em.getTransaction();
		
		DatabaseUtil dbl = new DatabaseUtil();
		
		// Create the database, does nothing if it already exists.
		if(!param.isResetDatabase()) {
			dbl.createDatabase();
		}

		// If the database files were not found, database has just been created
		// thus default fields must be inserted here.
		if(!databaseExists) {
			dbl.insertDefaultFields();
		}
		
		// Reset database on command line parameter
		if(param.isResetDatabase()) {
			dbl.resetDatabase();
		}		

		// Start udp multicast server
		MulticastServer multicastServer = new MulticastServer();
		Thread multicastThread = new Thread(multicastServer);
		multicastThread.setDaemon(true);
		multicastThread.start();
		
		// Start webservice endpoint
		Endpoint.publish("http://localhost:9999/ws/mailsigner", new MailSignerServiceImpl());
		
		// Stop here if servermode is requested, otherwise, create the gui
		if(param.isServer()) {
			return;
		}

		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						signatureController = new SignatureController();
						userController = new UserController();
						UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
						LoginFrame loginFrame = new LoginFrame();
						loginFrame.setVisible(!param.isDeveloperMode());
						if (param.isDeveloperMode()) {
							mainFrame = new MainFrame();
							mainFrame.setVisible(true);
							settings.setUserLimit(9999);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InterruptedException e) {
			log.error("Gui thread interrupted", e);
		} catch (InvocationTargetException e) {
			log.error("Invocation target exception", e);
		}
	}

	public static Settings getSettings() {
		return settings;
	}
	
	public static Logger getLog() {
		return log;
	}

	public static void writeProperties() {
		settings.writeSettings();
		settings.saveToFile(".", "settings.ini");
	}

	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	public static EntityManager getEm() {
		return em;
	}

	public static EntityTransaction getEntr() {
		return entr;
	}
	
	public static UserController getUserController() {
		return userController;
	}
	
	public static SignatureController getSignatureController() {
		return signatureController;
	}

}
