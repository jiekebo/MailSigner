package com.mailsigner.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mailsigner.MailSigner;

public class DatabaseUtil {

	String url;
	private Connection con;
	private static Logger log = MailSigner.getLog();

	public DatabaseUtil() {
		try {
			url = new String("jdbc:hsqldb:hsql://localhost:9001/mailsignerdb");
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the default fields into the database
	 */
	public void insertDefaultFields() {
		try {
			con = DriverManager.getConnection(url);
			String insertDefaultFieldsStatement = 
					"INSERT INTO Field (idField, Label, Code, Protected) VALUES (NULL, ?, ?, 1);";
			PreparedStatement statement = con.prepareStatement(insertDefaultFieldsStatement);
			Map<String, String> defaultFields = Util.readPropertiesFileAsMap(MailSigner.getSettings().getDefaultFieldsFile(), ":");
			Set<Entry<String,String>> entries = defaultFields.entrySet();
			for (Entry<String, String> entry : entries) {
				statement.setString(1, entry.getKey());
				statement.setString(2, entry.getValue());
				statement.executeUpdate();
				log.debug("Inserting default field " + entry.getKey() + " with value " + entry.getValue() + " : START/STOP");
			}
			con.close();
		} catch (SQLException e) {
			log.error("An sql exception occured", e);
		} catch (Exception e) {
			log.error("General error", e);
		}
	}
	
	/**
	 * Back up the database
	 */
	public void backupDatabase() {
		//TODO: write method for backing up data
	}
	
	public void resetDatabase() {
		try {
			con = DriverManager.getConnection(url);
			BufferedReader br = new BufferedReader(new FileReader("db/db.sql"));
			String line;
			while ((line = br.readLine()) != null) {
				PreparedStatement prepareStatement = con.prepareStatement(line);
				prepareStatement.executeUpdate();
			}
			con.close();
		} catch (FileNotFoundException e) {
			log.error("Database schema file could not be found", e);
		} catch (SQLException e) {
			log.error("An sql exception occured", e);
		} catch (IOException e) {
			log.error("Error reading database schema file", e);
		}
	}
}
