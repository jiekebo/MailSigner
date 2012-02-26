package com.mailsigner.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.mailsigner.model.ActiveDirectoryUserModel;


public class AdInterface {
	private String host;
	private String pass;
	private String principal;
	private String context;
	private Integer timeout;
	private DirContext dirContext;

	/**
	 * Constructs an Active Directory connection object
	 * @param host The name of the Active Directory server
	 * @param context Root address of the the Active Directory forest
	 * @param user The username of the profile used to establish connection
	 * @param pass The password of the profile used to establish connection
	 * @param timeout
	 */
	public AdInterface(String servername, String domain, String user, String pass, int timeout) {
		this.host = servername;
		this.pass = pass;
		this.principal = user + "@" + domain;
		this.timeout = timeout;
		
		// build the context, DC=something,DC=something,DC=something
		String[] contextMatches = domain.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < contextMatches.length; i++) {
			sb.append("DC=");
			sb.append(contextMatches[i]);
			if(i+1 < contextMatches.length)
				sb.append(",");
		}
		this.context = sb.toString();
	}
	
	/**
	 * Establishes a connection to an Active Directory server 
	 * @throws NamingException
	 */
	public boolean connect() throws NamingException {
		Hashtable<String, String> environment = new Hashtable<String, String>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL, "ldap://" + host + ":389");
		environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		environment.put(Context.SECURITY_PRINCIPAL, principal);
		environment.put(Context.SECURITY_CREDENTIALS, pass);
//		environment.put(Context.REFERRAL, "follow");
		environment.put("com.sun.jndi.ldap.connect.timeout", timeout.toString());
		dirContext = new InitialDirContext(environment);
		return true;
	}

	private String getAttribString(Attribute attrib) {
		if(attrib == null)
			return null;
		String attribString = attrib.toString();
		String subString = attribString.substring(attribString.indexOf(':') + 2);
		return subString;
	}
	
	public List<ActiveDirectoryUserModel> getAdUsers() throws NamingException {
		List<ActiveDirectoryUserModel> adUsers = new ArrayList<ActiveDirectoryUserModel>();
		// A SearchControls object holds information about the scope of a search.
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		// Searching for person which is user and has given name and surname
		String searchString = "(&(objectClass=user)(givenName=*)(sn=*))";
		// String searchString = "(&(objectClass=user)(sAMAccountName=*))";

		// NamingEnumeration is an interface for lists returned by methods
		// in the javax.naming and javax.naming.directory packages.
		// In our case, it's going to hold the results of our search.
		NamingEnumeration<?> enumer = dirContext.search(context, searchString, controls);

		// cycle through result set
		while (enumer.hasMoreElements()) {
			ActiveDirectoryUserModel adUser = new ActiveDirectoryUserModel();
			SearchResult sr = (SearchResult) enumer.next();
			Attributes attrs = sr.getAttributes();

			// Login Name
			String login = getAttribString(attrs.get("sAMAccountName"));

			// General Tab
			String firstName = getAttribString(attrs.get("givenName"));
			String lastName = getAttribString(attrs.get("sn"));
			String displayName = getAttribString(attrs.get("name"));
			String initials = getAttribString(attrs.get("initials"));
			String description = getAttribString(attrs.get("description"));
			String office = getAttribString(attrs.get("physicalDeliveryOfficeName"));
			String telephone = getAttribString(attrs.get("telephoneNumber"));
			String email = getAttribString(attrs.get("mail"));
			String website = getAttribString(attrs.get("wWWHomePage"));

			// Address Tab
			String street = getAttribString(attrs.get("streetAddress"));
			String poBox = getAttribString(attrs.get("postOfficeBox"));
			String city = getAttribString(attrs.get("l"));
			String state = getAttribString(attrs.get("st"));
			String zip = getAttribString(attrs.get("postalCode"));
			String country = getAttribString(attrs.get("co"));

			// Telephones Tab
			String home = getAttribString(attrs.get("homephone"));
			String pager = getAttribString(attrs.get("pager"));
			String mobile = getAttribString(attrs.get("mobile"));
			String fax = getAttribString(attrs.get("facsimileTelephoneNumber"));
			String ipphone = getAttribString(attrs.get("ipPhone"));

			// Organization Tab
			String title = getAttribString(attrs.get("title"));
			String department = getAttribString(attrs.get("department"));
			String company = getAttribString(attrs.get("company"));
			String manager = getAttribString(attrs.get("manager"));

			// Adding Login Name
			adUser.setLogin(login);

			// Adding General Tab
			adUser.setFirstName(firstName);
			adUser.setLastName(lastName);
			adUser.setDisplayName(displayName);
			adUser.setInitials(initials);
			adUser.setDescription(description);
			adUser.setOffice(office);
			adUser.setOffice(office);
			adUser.setTelephone(telephone);
			adUser.setEmail(email);
			adUser.setWebsite(website);

			// Adding Address Tab
			adUser.setStreet(street);
			adUser.setPoBox(poBox);
			adUser.setCity(city);
			adUser.setState(state);;
			adUser.setZip(zip);
			adUser.setCountry(country);

			// Adding Telephones Tab
			adUser.setHome(home);
			adUser.setPager(pager);
			adUser.setMobile(mobile);
			adUser.setFax(fax);
			adUser.setIpphone(ipphone);

			// Adding Organization Tab			
			adUser.setTitle(title);
			adUser.setDepartment(department);
			adUser.setCompany(company);
			if (manager != null)
				manager = manager.substring(manager.indexOf('=') + 1, manager.indexOf(','));
			adUser.setManager(manager);
			adUsers.add(adUser);
		}
		return adUsers;
	}
}
