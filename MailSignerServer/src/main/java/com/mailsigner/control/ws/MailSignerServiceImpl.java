package com.mailsigner.control.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.mailsigner.MailSigner;
import com.mailsigner.control.exception.ComputerNotFoundException;
import com.mailsigner.control.exception.SignatureNotFoundException;
import com.mailsigner.control.exception.UserNotFoundException;
import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Userfield;
import com.mailsigner.model.persistence.Usersignature;

@WebService(endpointInterface = "com.mailsigner.control.ws.MailSignerService")
public class MailSignerServiceImpl implements MailSignerService {
	
	Logger log = MailSigner.getLog();
	
	@Override
	public String[] getSignatureCreationList(String userName) {
		try {
			User user = MailSigner.getUserController().findUser(userName);
			Set<Usersignature> userSignaturesSet = user.getUsersignatures();
			ArrayList<String> userSignatureTitles = new ArrayList<String>();
			Map<Boolean, String> signatureTest = new HashMap<Boolean, String>();
			signatureTest.entrySet().toArray(new String[0]);
			for (Usersignature usersignature : userSignaturesSet) {
				if(usersignature.isEnabled()) {
					userSignatureTitles.add(usersignature.getSignature().getTitle());
					log.debug("WS - Signature [" + usersignature.getSignature().getTitle() + "] added to signature creation list");
				}
			}
			log.debug("WS - Signature creation list returned, containing " + userSignatureTitles.size() + " for creation");
			return userSignatureTitles.toArray(new String[0]);
		} catch (UserNotFoundException e) {
			return new String[0];
		}
	}
	
	@Override
	public String[] getSignatureDeletionList(String userName) {
		try {
			User user = MailSigner.getUserController().findUser(userName);
			Set<Usersignature> userSignaturesSet = user.getUsersignatures();
			ArrayList<String> userSignatureTitles = new ArrayList<String>();
			for (Usersignature usersignature : userSignaturesSet) {
				if(!usersignature.isEnabled()) {
					userSignatureTitles.add(usersignature.getSignature().getTitle());
					log.debug("WS - Signature [" + usersignature.getSignature().getTitle() + "] not enabled for user [" + userName + "], adding to deletion list.");
				}
			}
			log.debug("WS - Deletion list returned on request, containing " + userSignatureTitles.size() + " signatures for deletion");
			return userSignatureTitles.toArray(new String[0]);
		} catch (UserNotFoundException e) {
			return new String[0];
		}
	}
	
	@Override
	public byte[] getSignature(String signatureTitle, SignatureFormat format) {
		try {
			Signature signature = MailSigner.getSignatureController().findSignature(signatureTitle);
			switch (format) {
				case HTML:
					log.debug("WS - Signature [" + signatureTitle + "] returned in HTML format");
					return signature.getHtml();
				case RTF:
					log.debug("WS - Signature [" + signatureTitle + "] returned in RTF format");
					return signature.getRtf();
				case TXT:
					log.debug("WS - Signature [" + signatureTitle + "] returned in TXT format");
					return signature.getTxt();
				default: 
					log.debug("WS - Invalid signature format chosen");
					return new byte[0];
			}
		} catch (SignatureNotFoundException e) {
			log.debug("WS - Signature [" + signatureTitle + "] not found, returning empty string array");
			return new byte[0];
		}
	}
	
	@Override
	public byte[][] getFullSignature(String signatureTitle) {
		try {
			Signature signature = MailSigner.getSignatureController().findSignature(signatureTitle);
			byte[][] preparedSignature = new byte[3][];
			preparedSignature[0] = signature.getHtml();
			preparedSignature[1] = signature.getRtf();
			preparedSignature[2] = signature.getTxt();
			log.debug("WS - Full signature [" + signatureTitle + "] requested");
			return preparedSignature;
		} catch (SignatureNotFoundException e) {
			return new byte[0][0];
		}
	}

	@Override
	public String[][] getUserFields(String userName) {
		try {
			User user = MailSigner.getUserController().findUser(userName);
			Set<Userfield> userFields = user.getUserfields();
			String[][] preparedFields = new String[2][userFields.size()];
			int field = 0;
			for (Userfield userfield : userFields) {
				preparedFields[0][field] = userfield.getField().getCode();
				preparedFields[1][field] = userfield.getValue();
				field++;
			}
			log.debug("WS - User fields for user [" + userName + "] requested");
			return preparedFields;
		} catch (UserNotFoundException e) {
			log.debug("WS - User [" + userName + "] not found, returning empty string array");
			return new String[0][0];
		}
	}
	
	@Override
	public void updateUserComputer(String userName, String computerName) {
		User user = null;
		try {
			user = MailSigner.getUserController().findUser(userName);
			Computer computer = MailSigner.getComputerController().findComputer(user, computerName);
			log.debug("WS - Updating deployment state to 1 for user [" + userName + "] with computer [" + computerName + "]");
			computer.setDeployed(new Byte((byte) 1));
		} catch (ComputerNotFoundException e) {
			MailSigner.getComputerController().createUserComputer(user, computerName);
			log.debug("WS - Computer [" + computerName + "] for user [" + userName + "] has been created");
		} catch (UserNotFoundException e) {
			log.debug("WS - User [" + userName + "] not found");
		}
	}
}