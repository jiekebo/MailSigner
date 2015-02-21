package com.mailsigner.control.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface MailSignerService {
	enum SignatureFormat {HTML, RTF, TXT};
	
	@WebMethod
	String[] getSignatureCreationList(String userName);
	
	@WebMethod
	String[] getSignatureDeletionList(String userName);
	
	@WebMethod
	byte[] getSignature(String signatureTitle, SignatureFormat format);
	
	@WebMethod
	byte[][] getFullSignature(String signatureTitle);
	
	@WebMethod
	String[][] getUserFields(String userName);
	
	@WebMethod
	void updateUserComputer(String userName, String computerName);
}