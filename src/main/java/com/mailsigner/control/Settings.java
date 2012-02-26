package com.mailsigner.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

public class Settings implements Serializable {
	private static final long serialVersionUID = -2157098047271954413L;
	private Properties props;
	
	/* Set the default values for the settings file */
	private String outlookDirs					= "\\Application Data\\Microsoft\\Signatures;" +
												  "\\Application Data\\Microsoft\\Signaturer;" +
												  "\\AppData\\Roaming\\Microsoft\\Signatures;" +
												  "\\AppData\\Roaming\\Microsoft\\Signaturer";
	private ArrayList<String> outlookDirsArray 	= new ArrayList<String>();
	
	/* Labels for fields in settings file */
	private String outlookDirsLabel				= "outlookDirs";
	
	/**
	 * Default constructor
	 */
	public Settings() {
		props = new Properties();
	}
	
	/**
	 * Load settings from a file into the properties object
	 * @param props Properties object
	 * @param dir Directory of the settings file
	 * @param file Filename of the settings file
	 */
	public void loadFromFile(String dir, String file) {
		File propsFile = new File(dir, file);

		try {
			InputStream propsStream = new FileInputStream(propsFile);
			props.load(propsStream);
		} catch (FileNotFoundException e) {
			try {
				propsFile.createNewFile();
				writeSettings();
				saveToFile(dir, file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves settings to a file from a properties object
	 * @param props Properties object
	 * @param dir Directory of the settings file
	 * @param file Filename of the settings file
	 */
	public void saveToFile(String dir, String file) {
		File propsFile = new File(dir, file);

		try {
			OutputStream propsStream = new FileOutputStream(propsFile);
			props.store(propsStream, "Settings for MailSignerClient, do not edit!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the settings from the properties into this objects private members
	 * @param props Properties object
	 */
	public void readSettings() {
		outlookDirs = props.getProperty(outlookDirsLabel);
		outlookDirsArray = readArray(outlookDirs);
	}
	
	/**
	 * Writes the settings from this objects private members into the properties object
	 * @param props Properties object
	 */
	public void writeSettings() {
		if(outlookDirsArray.size() > 0) {
			outlookDirs = writeArray(outlookDirsArray);
		}
		props.put(outlookDirsLabel, outlookDirs);
	}
	
	/**
	 * 
	 * @param arrayString
	 * @return
	 */
	private ArrayList<String> readArray(String arrayString) {
		String[] array = arrayString.split(";");
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String string : array) {
			arrayList.add(string);
		}
		return arrayList;
	}
	
	/**
	 * 
	 * @param array
	 * @return
	 */
	private String writeArray(ArrayList<String> array) {
		StringBuilder sb = new StringBuilder();
		for (String string : array) {
			sb.append(string);
			sb.append(";");
		}
		sb.deleteCharAt(sb.lastIndexOf(";"));
		return sb.toString();
	}
	
	public ArrayList<String> getOutlookDirsArray() {
		return outlookDirsArray;
	}
	
	public void setOutlookDirsArray(ArrayList<String> outlookDirsArray) {
		this.outlookDirsArray = outlookDirsArray;
	}
}
