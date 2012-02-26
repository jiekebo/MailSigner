package com.mailsigner.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.mailsigner.MailSignerClient;
import com.mailsigner.model.CompletedSignature;
import com.mailsigner.model.HtmlSelection;
import com.mailsigner.model.RtfSelection;
import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.ComputerPK;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Userfield;
import com.mailsigner.model.persistence.Usersignature;

public class TrayControl implements ClipboardOwner {
	private static Logger log = MailSignerClient.getLog();
	private String userLogin = null;
	private String computerName = null;
	private ArrayList<String> outlookDirs = MailSignerClient.getSettings().getOutlookDirsArray();
	private Computer currentComputer;
	private boolean isConnected = false;
	private User user;
	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static EntityTransaction entr;
	
	/**
	 * Controller for the tray icon menu actions
	 * @param serverAddress Address of the database server
	 */
	public TrayControl() {
		userLogin = System.getProperty("user.name");
		try {
			computerName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			log.error("Cannot retrieve user login from system");
		}
	}
	
	/**
	 * Connect to the database, given the server address
	 * @param serverAddress Server address
	 */
	public void connectDB(InetAddress serverAddress) {
		if(serverAddress == null) {
			return;			
		}
		
		Map<String,String> dbProps = createDbProperties("jdbc:hsqldb:hsql:/" + serverAddress.toString() + "/mailsignerdb");
		System.out.println("Connecting to: jdbc:hsqldb:hsql:/" + serverAddress.toString() + "/mailsignerdb");
		emf = Persistence.createEntityManagerFactory("MailSignerJPA", dbProps);
		em = emf.createEntityManager();
		entr = em.getTransaction();		
		user = loadUser();		
		if(user != null) {
			isConnected  = true;
		}
	}

	/**
	 * Redeploy signatures to the Outlook folders
	 */
	public void deploy() {
		if(!isConnected || user == null || isSynchronized()) {
			return;
		}
		
		HashMap<String,String> fieldMap = createFieldMap(user);
		Set<Usersignature> userSignatures = loadUsersSignatures();
		
		ArrayList<String> deletionList = new ArrayList<String>();
		HashMap<String,CompletedSignature> creationList = new HashMap<String, CompletedSignature>();
		
		for (Usersignature userSignature : userSignatures) {
			log.info("Preparing user signature [" + userSignature.getSignature().getTitle() + "]");
			if(userSignature.getEnabled() == 0) {
				String unusedSignatureTitle = userSignature.getSignature().getTitle();
				deletionList.add(unusedSignatureTitle);
				log.info("Signature [" +  unusedSignatureTitle + "] added to deletion list");
				continue;
			}
			
			CompletedSignature completedSignature = new CompletedSignature();
			completedSignature.setTitle(userSignature.getSignature().getTitle());
			
			if(userSignature.getSignature().getHtml() != null) {
				log.info("Replacing html fields");
				String htmlFromByte = new String(userSignature.getSignature().getHtml());
				String html = replaceFields(htmlFromByte, fieldMap, false);
				completedSignature.setHtml(html);
			}
			
			if(userSignature.getSignature().getRtf() != null) {
				log.info("Replacing rtf fields");
				String rtfFromByte = new String(userSignature.getSignature().getRtf());
				String rtf = replaceFields(rtfFromByte, fieldMap, true);
				completedSignature.setRtf(rtf);
			}
			
			if(userSignature.getSignature().getTxt() != null) {
				log.info("Replacing txt fields");
				String txtFromByte = new String(userSignature.getSignature().getTxt());
				String txt = replaceFields(txtFromByte, fieldMap, false);
				completedSignature.setTxt(txt);
			}

			creationList.put(completedSignature.getTitle(), completedSignature);
			log.info("Signature [" + completedSignature.getTitle() + "] added to creation list");
		}
		
		deleteSignatures(deletionList);
		boolean writeSuccess = writeSignatures(creationList);
		updateComputerState(writeSuccess);
	}
	
	/**
	 * Place the selected signature on the clipboard
	 * @param selectedFormat 
	 * @param selectedSignature 
	 */
	public void clipboard(Usersignature selectedSignature, Integer selectedFormat) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Signature signatureFrame = selectedSignature.getSignature();
		HashMap<String,String> fieldMap = createFieldMap(user);
		
		if(selectedFormat == 0 && signatureFrame.getHtml() != null) {
			String htmlFromByte = new String(selectedSignature.getSignature().getHtml());
			String html = replaceFields(htmlFromByte, fieldMap, false);
			Transferable htmlTransferable = new HtmlSelection(html);
			clipboard.setContents(htmlTransferable, this);
		} else if(selectedFormat == 1 && signatureFrame.getRtf() != null) {
			String rtfFromByte = new String(selectedSignature.getSignature().getRtf());
			String rtf = replaceFields(rtfFromByte, fieldMap, true);
			Transferable rtfTransferable = new RtfSelection(rtf);
			clipboard.setContents(rtfTransferable, this);
		} else if(selectedFormat == 2 && signatureFrame.getTxt() != null) {
			String txtFromByte = new String(selectedSignature.getSignature().getTxt());
			String txt = replaceFields(txtFromByte, fieldMap, false);
			Transferable txtTransferable = new StringSelection(txt);
			clipboard.setContents(txtTransferable, this);
		}
	}
	
	/**
	 * Load the persisted signatures for the current user
	 * @return Set containing Usersignature
	 */
	public Set<Usersignature> loadUsersSignatures() {
		if(!isConnected || user == null) {
			return null;
		}
		entr.begin();
		em.refresh(user);
		entr.commit();
		return user.getUsersignatures();
	}
	
	/**
	 * Create properties for connecting to the database
	 * @param dbURL Url of database to connect to
	 * @return Map containing properties for the database connection
	 */
	private Map<String,String> createDbProperties(String dbURL) {
		Map<String,String> dbProps = new HashMap<String,String>();
		dbProps.put("javax.persistence.jdbc.url", dbURL);
		dbProps.put("javax.persistence.jdbc.password", "");
		dbProps.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
		dbProps.put("javax.persistence.jdbc.user", "SA");
		return dbProps;
	}
	
	/**
	 * Loads details for the current user
	 * @return Persistent user type from MailSigner persistence library
	 */
	private User loadUser() {
		if(em == null) {
			return null;
		}
		TypedQuery<User> userQuery = em.createNamedQuery("User.findUser", User.class);
		userQuery.setParameter("value", userLogin);
		// TODO: throw exception on no users found!
		if(userQuery.getResultList().size() < 1) {
			return null;
		}
		return userQuery.getSingleResult();
	}
	
	/**
	 * Get deployment state from database
	 * @return True if up to date
	 */
	private boolean isSynchronized() {
		TypedQuery<Computer> computerQuery = em.createNamedQuery("Computer.selectComputer", Computer.class);
		computerQuery.setParameter("value", computerName);
		List<Computer> computerResult = computerQuery.getResultList();
		log.debug("Searching for computer entry, found: " + computerResult.size());
		if(computerResult.size() > 0) {
			currentComputer = computerResult.get(computerQuery.getFirstResult());
			if(currentComputer.getDeployed() == 1) {
				log.info("Database reports no changes");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Update the current computers status. If current compuer is not found
	 * one is created and entered into database.
	 * @param writeSuccess State after attempting to write signatures.
	 */
	private void updateComputerState(boolean writeSuccess) {
		byte deployed = (byte) (writeSuccess?1:0);
		if(currentComputer == null) {
			entr.begin();
			Computer computer = new Computer();
			ComputerPK computerPK = new ComputerPK();
			computerPK.setUserIduser(user.getIduser());
			computer.setId(computerPK);
			computer.setUser(user);
			computer.setLabel(computerName);
			computer.setDeployed(deployed);
			user.getComputers().add(computer);
			em.persist(computer);
			entr.commit();
			log.debug("Created computer [" + computerName + "] userId[" +  user.getIduser() + "] state [" + deployed + "]");
		} else {
			currentComputer.setDeployed(deployed);
			entr.begin();
			em.persist(currentComputer);
			entr.commit();
			log.debug("Deployment state for computer [" + computerName + "] set to " + deployed);
		}
	}
	
	/**
	 * Creates a hashmap consisting of code/value mappings, to be used in the string
	 * replacement method.
	 * @param user Persistent user type from MailSigner persistence library
	 * @return A hashmap contaning code/value bindings
	 */
	private HashMap<String,String> createFieldMap(User user) {
		Set<Userfield> userFields = user.getUserfields();
		HashMap<String, String> fieldMap = new HashMap<String,String>();
		for (Userfield userField : userFields) {
			log.debug("Field: " + userField.getField().getLabel() + " - Value: " + userField.getValue() + " - Code: " + userField.getField().getCode());
			fieldMap.put(userField.getField().getCode(), userField.getValue());
		}
		return fieldMap;
	}
	
	/**
	 * Replaces fields specified from the map in the text string
	 * @param text Input string to have fields replaced
	 * @param replacements Map containing key, value mappings
	 * @return String with fields replaced
	 */
	private String replaceFields(String text, Map<String, String> replacements, Boolean rtf) {
		Pattern pattern;
		if(rtf) {			
			pattern = Pattern.compile("\\\\\\{#(.+?)#\\\\\\}");
		} else {
			pattern = Pattern.compile("\\{#(.+?)#\\}");
		}
		Matcher matcher = pattern.matcher(text);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			// TODO: make this prettier....
			log.debug("Pattern match found at " + matcher.start());
			String replacement = replacements.get("{#" + matcher.group(1) + "#}");
			if (replacement != null) {
				log.debug("Replacing field [" + matcher.group(1) + "] with " + replacement);
				matcher.appendReplacement(buffer, "");
				buffer.append(replacement);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}
	
	/**
	 * Deletes unused signatures from the folder list
	 * @param unusedSignatures
	 */
	private void deleteSignatures(ArrayList<String> unusedSignatures) {
		Iterator<String> it = unusedSignatures.iterator();
		while (it.hasNext()) {
			String signatureTitle = it.next();
			
			for(String path : outlookDirs) {
				File dirTest = new File(path);
				if(!dirTest.exists()) {
					log.error("The path [" + path + "] does not exist");
					continue;
				}
				log.info("Deleting [" + signatureTitle + "] signature from " + path);
				
				File html = new File(path + "//" + signatureTitle + ".html");
				boolean htmlDelete = html.delete();
				log.debug("HTML deleted: " + htmlDelete);
				
				File htm = new File(path + "//" + signatureTitle + ".htm");
				boolean htmDelete = htm.delete();
				log.debug("HTM deleted: " + htmDelete);
				
				File rtf = new File(path + "//" + signatureTitle + ".rtf");
				boolean rtfDelete = rtf.delete();
				log.debug("RTF deleted: " + rtfDelete);
				
				File txt = new File(path + "//" + signatureTitle + ".txt");
				boolean txtDelete =	txt.delete();
				log.debug("TXT deleted: " + txtDelete);
			}
		}
	}
	
	/**
	 * Creates the file output of completed signatures.
	 * @param completedSignatures 
	 * @return If any of the writes fail, return value is false
	 */
	private boolean writeSignatures(HashMap<String,CompletedSignature> completedSignatures) {
		Iterator<Entry<String,CompletedSignature>> it = completedSignatures.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, CompletedSignature> entry = (Map.Entry<String, CompletedSignature>) it.next();
			String signatureTitle = entry.getKey();
			CompletedSignature completedSignature = entry.getValue();
			
			log.debug("Deploying signature [" + signatureTitle + "]");
			
			try {
				for (String path : outlookDirs) {
					log.info("Deploying to path: " + path);
					File dirTest = new File(path);
					if(!dirTest.exists()) {
						log.error("The path " + path + " does not exist");
						// if one of the paths doesn't exist return false, since user might want
						// a re-deployment and this will not happen if current computer is marked
						// as deployed.
						return false;
					}
					
					FileWriter htmlWriter = new FileWriter(path + "//" + signatureTitle + ".html");
					htmlWriter.write(completedSignature.getHtml());
					htmlWriter.close();
					log.debug("HTML written");
					
					FileWriter htmWriter = new FileWriter(path + "//" + signatureTitle + ".htm");
					htmWriter.write(completedSignature.getHtml());
					htmWriter.close();
					log.debug("HTM written");
					
					FileWriter rtfWriter = new FileWriter(path + "//" + signatureTitle + ".rtf");
					rtfWriter.write(completedSignature.getRtf());
					rtfWriter.close();
					log.debug("RTF written");
					
					FileWriter txtWriter = new FileWriter(path + "//" + signatureTitle + ".txt");
					txtWriter.write(completedSignature.getTxt());
					txtWriter.close();
					log.debug("TXT written");
				}
			} catch (IOException e) {
				log.error("Cannot write signature to path");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComputerName() {
		return computerName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUserLogin() {
		return userLogin;
	}
	
	public boolean isConnected() {
		user = loadUser();
		isConnected = user != null;
		return isConnected;
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub
		
	}

}