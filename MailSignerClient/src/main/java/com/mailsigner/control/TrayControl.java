package com.mailsigner.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mailsigner.MailSignerClient;
import com.mailsigner.control.exceptions.UserNotFoundException;
import com.mailsigner.control.ws.Base64BinaryArray;
import com.mailsigner.control.ws.MailSignerService;
import com.mailsigner.control.ws.SignatureFormat;
import com.mailsigner.control.ws.StringArray;
import com.mailsigner.control.ws.StringArrayArray;
import com.mailsigner.model.CompletedSignature;
import com.mailsigner.model.HtmlSelection;
import com.mailsigner.model.RtfSelection;

public class TrayControl implements ClipboardOwner {
	private static Logger log = MailSignerClient.getLog();
	private String userName = null;
	private String computerName = null;
	private ArrayList<String> outlookDirs = MailSignerClient.getSettings().getOutlookDirsArray();
	private MailSignerService service;
	
	/**
	 * Controller for the tray icon menu actions
	 * @param serverAddress Address of the database server
	 */
	public TrayControl() {
		userName = System.getProperty("user.name");
		try {
			computerName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			log.error("Cannot retrieve user login from system");
		}
	}
	
	public void connectWebservice(URL serviceLocation, String serviceDomain, String serviceName) {
		MailSignerServiceClient serviceClient = new MailSignerServiceClient(serviceLocation, "http://ws.control.mailsigner.com/", "MailSignerServiceImplService");
		service = serviceClient.connect();
	}

	/**
	 * Redeploy signatures to the Outlook folders
	 */
	public void deploy() {
		try {
			HashMap<String, String> fieldMap = getUserFields();
			List<String> deletionList = service.getSignatureDeletionList(userName).getItem();
			List<String> creationList = getUserSignatures();

			HashMap<String,CompletedSignature> signatureCollection = new HashMap<String, CompletedSignature>();
			
			for (String signatureName : creationList) {
				log.info("Preparing user signature [" + signatureName + "]");
				Base64BinaryArray fullSignature = service.getFullSignature(signatureName);
				List<byte[]> fullSignatureBytes = fullSignature.getItem();
				CompletedSignature completedSignature = new CompletedSignature();
				
				log.info("Replacing html fields");
				String html = replaceFields(new String(fullSignatureBytes.get(0)), fieldMap, false);
				log.info("Replacing rtf fields");
				String rtf = replaceFields(new String(fullSignatureBytes.get(1)), fieldMap, true);
				log.info("Replacing txt fields");
				String txt = replaceFields(new String(fullSignatureBytes.get(2)), fieldMap, false);
				
				completedSignature.setHtml(html);
				completedSignature.setRtf(rtf);
				completedSignature.setTxt(txt);
				
				signatureCollection.put(signatureName, completedSignature);
			}
			
			deleteSignatures(deletionList);
			writeSignatures(signatureCollection);
			service.updateUserComputer(userName, computerName);
		} catch (UserNotFoundException e) {
			log.debug("User could not be found");
		} catch (IOException e) {
			log.debug("Writing signatures genereated error", e);
		}
	}
	
	/**
	 * Place the selected signature on the clipboard
	 * @param selectedFormat 
	 * @param selectedSignature 
	 */
	public void clipboard(String selectedSignature, int selectedFormat) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		byte[] signature = null;

		//TODO: remove magic numbers!!!
		switch (selectedFormat) {
			case 0:
				signature = service.getSignature(selectedSignature, SignatureFormat.HTML);
				break;
			case 1:
				signature = service.getSignature(selectedSignature, SignatureFormat.RTF);
				break;
			case 2:
				signature = service.getSignature(selectedSignature, SignatureFormat.TXT);
				break;
		}
		
		String signatureValue = null;
		try {
			signatureValue = new String(signature, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String, String> fieldMap = null;
		try {
			fieldMap = getUserFields();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(selectedFormat == 0 && signatureValue != null) {
			String html = replaceFields(signatureValue, fieldMap, false);
			Transferable htmlTransferable = new HtmlSelection(html);
			clipboard.setContents(htmlTransferable, this);
		} else if(selectedFormat == 1 && signature != null) {
			String rtf = replaceFields(signatureValue, fieldMap, true);
			Transferable rtfTransferable = new RtfSelection(rtf);
			clipboard.setContents(rtfTransferable, this);
		} else if(selectedFormat == 2 && signature != null) {
			String txt = replaceFields(signatureValue, fieldMap, false);
			Transferable txtTransferable = new StringSelection(txt);
			clipboard.setContents(txtTransferable, this);
		}
	}
	
	public List<String> getUserSignatures() throws UserNotFoundException {
		StringArray response = service.getSignatureCreationList(userName);
		List<String> userSignatures = response.getItem();
		if(userSignatures.size() == 0) {
			throw new UserNotFoundException();
		}
		return userSignatures;
	}
	
	private HashMap<String,String> getUserFields() throws UserNotFoundException {
		StringArrayArray response = service.getUserFields(userName);
		List<StringArray> entries = response.getItem();
		
		if(entries.size() == 0) {
			throw new UserNotFoundException();
		}
		
		HashMap<String, String> userFields = new HashMap<String, String>();
		
		List<String> fields = entries.get(0).getItem();
		List<String> values = entries.get(1).getItem();
		
		for(int i = 0; i < fields.size(); i++) {
			userFields.put(fields.get(i), values.get(i));
			log.debug("Adding Field: " + fields.get(i));
			log.debug("Adding Value: " + values.get(i));
		}
		return userFields;
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
	private void deleteSignatures(List<String> unusedSignatures) {
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
	 * @throws IOException 
	 */
	private void writeSignatures(HashMap<String,CompletedSignature> completedSignatures) throws IOException {
		Iterator<Entry<String,CompletedSignature>> it = completedSignatures.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, CompletedSignature> entry = it.next();
			String signatureTitle = entry.getKey();
			CompletedSignature completedSignature = entry.getValue();
			
			log.debug("Deploying signature [" + signatureTitle + "]");
			
			for (String path : outlookDirs) {
				log.info("Deploying to path: " + path);
				File dirTest = new File(path);
				if(!dirTest.exists()) {
					log.error("The path " + path + " does not exist");
					// if one of the paths doesn't exist return false, since user might want
					// a re-deployment and this will not happen if current computer is marked
					// as deployed.
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
		}
	}
	
	/**
	 * 
	 * @return asdf
	 */
	public String getComputerName() {
		return computerName;
	}
	
	/**
	 * 
	 * @return asdf
	 */
	public String getUserLogin() {
		return userName;
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub
		
	}

}