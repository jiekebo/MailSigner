package com.mailsigner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;

import com.mailsigner.util.StringEncrypter.EncryptionException;


public class Settings implements Serializable {
	private static final long serialVersionUID = -7677225061050053260L;
	private Properties props;
	
	/* Set the default values for the settings file */
	private String host 					= "";
	private String user 					= "";
	private String pass 					= "";
	private String domain 					= "";
	private int timeout 					= 3600;
	private String datapath 				= "./data";
	private String databaseLocation			= "file:" + datapath + "/db";
	private String defaultFieldsFile		= "defaultFields.ini";
	private String login 					= "";
	private String loginpassword 			= "";
	private String companyName				= "";
	private String firstName				= "";
	private String lastName					= "";
	private int userLimit					= 0;
	
	/* Labels for fields in settings file */
	private String hostLabel 				= "adHost";
	private String userLabel 				= "adUser";
	private String passLabel 				= "adPass";
	private String domainLabel 				= "adDomain";
	private String timeoutLabel 			= "adTimeout";
	private String datapathLabel			= "dataDirectory";
	private String databaseLocationLabel	= "databaseLocation";
	private String defaultFieldsFileLabel	= "defaultFieldsFile";
	private String loginLabel 				= "login";
	private String loginpasswordLabel 		= "loginPassword";
	
	String encryptionKey = "ILoveHaiyanForever123456";
	
	private StringEncrypter stringEncrypter;
	
	/**
	 * Default constructor
	 */
	public Settings() {
		props = new Properties();
		try {
			stringEncrypter = new StringEncrypter("DES", encryptionKey);
		} catch (EncryptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Load settings from a file into the properties object
	 * @param props Properties object
	 * @param dir Directory of the settings file
	 * @param file Filename of the settings file
	 */
	public void loadFromFile(String dir, String file){
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
	public void saveToFile(String dir, String file){
		File propsFile = new File(dir, file);
		
		try {
			OutputStream propsStream = new FileOutputStream(propsFile);
			props.store(propsStream, "Settings for MailSigner, do not edit!");
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
	public void readSettings(){
		host = props.getProperty(hostLabel);
		user = props.getProperty(userLabel);
		pass = stringEncrypter.decryptWrapper(props.getProperty(passLabel));
		timeout = Integer.parseInt(props.getProperty(timeoutLabel));
		domain = props.getProperty(domainLabel);
		datapath = props.getProperty(datapathLabel);
		databaseLocation = props.getProperty(databaseLocationLabel);
		defaultFieldsFile = props.getProperty(defaultFieldsFileLabel);
		login = props.getProperty(loginLabel);
		loginpassword = stringEncrypter.decryptWrapper(props.getProperty(loginpasswordLabel));
	}
	
	/**
	 * Writes the settings from this objects private members into the properties object
	 * @param props Properties object
	 */
	public void writeSettings(){
		props.put(hostLabel, host);
		props.put(userLabel, user);
		props.put(passLabel, stringEncrypter.encryptWrapper(pass));
		props.put(domainLabel, domain);
		props.put(timeoutLabel, (new Integer(timeout)).toString());
		props.put(datapathLabel, datapath);
		props.put(databaseLocationLabel, databaseLocation);
		props.put(defaultFieldsFileLabel, defaultFieldsFile);
		props.put(loginLabel, login);
		props.put(loginpasswordLabel, stringEncrypter.encryptWrapper(loginpassword));
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getDefaultFieldsFile() {
		return defaultFieldsFile;
	}

	public void setDefaultFieldsFile(String defaultFieldsFile) {
		this.defaultFieldsFile = defaultFieldsFile;
	}

	public void setDatabaseLocation(String databaseLocation) {
		this.databaseLocation = databaseLocation;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDatapath() {
		return datapath;
	}

	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLoginpassword() {
		return loginpassword;
	}

	public void setLoginpassword(String loginpassword) {
		this.loginpassword = loginpassword;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(int userLimit) {
		this.userLimit = userLimit;
	}
	
	public String getDatabaseLocation() {
		return databaseLocation;
	}
}
