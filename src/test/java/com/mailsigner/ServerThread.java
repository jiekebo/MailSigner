package com.mailsigner;

import java.io.PrintWriter;

import org.hsqldb.Server;

public class ServerThread implements Runnable {
	
	private volatile boolean stop = false;
	private Server hsqlServer;

	public ServerThread() {
		hsqlServer = null;
		hsqlServer = new Server();

		// HSQLDB prints out a lot of informations when
		// starting and closing, which we don't need now.
		// Normally you should point the setLogWriter
		// to some Writer object that could store the logs.
		hsqlServer.setLogWriter(new PrintWriter(System.out));
		hsqlServer.setSilent(true);

		// The actual database will be named 'xdb' and its
		// settings and data will be stored in files
		// testdb.properties and testdb.script
		hsqlServer.setDatabaseName(0, "mailsignerdb");
		hsqlServer.setDatabasePath(0, "file:data/db");

		// Start the database!
		hsqlServer.start();
	}

	public void requestStop() {
		stop = true;
	}
	
	@Override
	public void run() {
		while(!stop) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hsqlServer.stop();
	}
}
