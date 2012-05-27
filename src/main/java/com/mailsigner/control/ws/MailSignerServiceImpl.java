package com.mailsigner.control.ws;

import java.util.ArrayList;
import java.util.Set;

import javax.jws.WebService;

import com.mailsigner.MailSigner;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Usersignature;

@WebService(endpointInterface = "com.mailsigner.control.ws.MailSignerService")
public class MailSignerServiceImpl implements MailSignerService {
	@Override
	public ArrayList<Usersignature> getUserSignatures(String name) {
		User user = MailSigner.getUserController().getUser(name);
		Set<Usersignature> userSignaturesSet = user.getUsersignatures();
		
		ArrayList<Usersignature> userSignatureArray = new ArrayList<Usersignature>();
		
		for (Usersignature usersignature : userSignaturesSet) {
			if(usersignature.getEnabled()==1) {
				userSignatureArray.add(usersignature);
			}
		}
		return userSignatureArray;
	}

	@Override
	public String getUserSignaturesList(String name) {
		User user = MailSigner.getUserController().getUser(name);
		Set<Usersignature> userSignaturesSet = user.getUsersignatures();
		
		StringBuilder users = new StringBuilder();
		
		for (Usersignature usersignature : userSignaturesSet) {
			users.append(usersignature.getSignature().getTitle());
		}
		return users.toString();
	}
}