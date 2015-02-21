package com.mailsigner;

import com.beust.jcommander.Parameter;

public class Parameters {
	@Parameter(names = "-d", description = "Start the developer mode, bypasses the login screen. Remove this in final build.")
	private boolean developerMode = false;
	@Parameter(names = "-r", description = "Reset the database to initial state.")
	private boolean resetDatabase = false;
	@Parameter(names = "-s", description = "Start in servermode, no gui.")
	private boolean server = false;
	
	public boolean isDeveloperMode() {
		return developerMode;
	}
	public boolean isResetDatabase() {
		return resetDatabase;
	}
	public boolean isServer() {
		return server;
	}
}
