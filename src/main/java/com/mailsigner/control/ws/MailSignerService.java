package com.mailsigner.control.ws;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.mailsigner.model.persistence.Usersignature;

@WebService
@SOAPBinding(style = Style.RPC)
public interface MailSignerService {
	@WebMethod
	ArrayList<Usersignature> getUserSignatures(String name);
	
	@WebMethod
	String getUserSignaturesList(String name);
}
