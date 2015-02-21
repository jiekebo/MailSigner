package com.mailsigner;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.naming.NamingException;


import org.junit.Test;

import com.mailsigner.model.ActiveDirectoryUserModel;
import com.mailsigner.util.AdInterface;


public class AdInterfaceTest {

	private AdInterface adInterface;
	
	public AdInterfaceTest() {
		adInterface = new AdInterface("192.168.56.101", "home.kronetorp.com", "jiekebo", "8Kyst7pony", 10000);
	}
	
	@Test
	public void testConnect() {
		try {
			assertTrue(adInterface.connect());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAdUsers() {
		try {
			adInterface.connect();
			List<ActiveDirectoryUserModel> adUsers = adInterface.getAdUsers();
			for (ActiveDirectoryUserModel adUser : adUsers) {
				System.out.println(adUser.getLogon());
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}