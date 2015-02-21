package com.mailsigner.control;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.mailsigner.control.ws.MailSignerService;

public class MailSignerServiceClient {
	
	URL serviceLocation;
	String serviceDomain;
	String serviceName;
	
	public MailSignerServiceClient(URL serviceLocation, String serviceDomain, String serviceName) {
		this.serviceLocation = serviceLocation;
		this.serviceDomain = serviceDomain;
		this.serviceName = serviceName;
		
	}
	
	public MailSignerService connect() {		
		QName qname = new QName(serviceDomain, serviceName);
		Service service = Service.create(serviceLocation, qname);
		MailSignerService mailSignerService = service.getPort(MailSignerService.class);
		return mailSignerService;		
	}
}
