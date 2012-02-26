package com.mailsigner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HSQLDBRequestThread implements Runnable {

	@Override
	public void run() {
		Connection connection = null;
		// We have here two 'try' blocks and two 'finally'
		// blocks because we have two things to close
		// after all - HSQLDB server and connection
		try {
			System.out.println("Waiting a while...");
//			Thread.sleep(500);
			System.out.println("Sending query!");
			// Getting a connection to the newly started database
			Class.forName("org.hsqldb.jdbcDriver");
			// Default user of the HSQLDB is 'sa'
			// with an empty password
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
			
			// Here we run a few SQL statements to see if
			// everything is working.
			// We first drop an existing 'testtable' (supposing
			// it was there from the previous run), create it
			// once again, insert some data and then read it
			// with SELECT query.
			// connection.prepareStatement("drop table testtable;").execute();
			// connection.prepareStatement("create table testtable ( id INTEGER, "
			// + "name VARCHAR(24));").execute();
			// connection.prepareStatement("insert into testtable(id, name) "
			// + "values (1, 'testvalue');").execute();
			ResultSet rs = connection.prepareStatement("SELECT * FROM field;").executeQuery();
			
			// Checking if the data is correct
			while (rs.next()) {
				System.out.println("Id: " + rs.getInt(1) + " Name: " + rs.getString(2));
			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Closing the connection
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	}

}
