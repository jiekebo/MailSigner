package com.mailsigner.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.mailsigner.MailSigner;
import com.mailsigner.model.persistence.Field;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Userfield;
import com.mailsigner.model.persistence.UserfieldPK;

public class ActiveDirectoryUserModel {
	private String login;
	private String firstName;
	private String lastName;
	private String displayName;
	private String initials;
	private String description;
	private String office;
	private String telephone;
	private String email;
	private String website;
	private String street;
	private String poBox;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String home;
	private String pager;
	private String mobile;
	private String fax;
	private String ipphone;
	private String title;
	private String department;
	private String company;
	private String manager;
	private Set<Userfield> userFields;
	private EntityManager em = MailSigner.getEm();
	
	public String getLogon() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPoBox() {
		return poBox;
	}
	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String homephone) {
		this.home = homephone;
	}
	public String getPager() {
		return pager;
	}
	public void setPager(String pager) {
		this.pager = pager;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getIpphone() {
		return ipphone;
	}
	public void setIpphone(String ipphone) {
		this.ipphone = ipphone;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public Set<Userfield> getUserDetails(User user) {
		userFields = new HashSet<Userfield>();
		
		TypedQuery<Field> fieldQuery = MailSigner.getEm().createNamedQuery("Field.allAdFields", Field.class);
		fieldQuery.setParameter("value", "1");
		List<Field> fieldList = fieldQuery.getResultList();
		
		UserfieldPK firstNamePK = new UserfieldPK();
		firstNamePK.setUserIduser(user.getIduser());
		firstNamePK.setFieldIdfield(0);
		
		Userfield firstNameDetail = new Userfield();
		firstNameDetail.setId(firstNamePK);
		firstNameDetail.setValue(getFirstName());
		firstNameDetail.setField(fieldList.get(0));
		firstNameDetail.setUser(user);
		userFields.add(firstNameDetail);
		em.persist(firstNameDetail);
		
		
		UserfieldPK lastNamePK = new UserfieldPK();
		lastNamePK.setUserIduser(user.getIduser());
		lastNamePK.setFieldIdfield(1);
		
		Userfield lastNameDetail = new Userfield();
		lastNameDetail.setId(lastNamePK);
		lastNameDetail.setValue(getLastName());
		lastNameDetail.setField(fieldList.get(1));
		lastNameDetail.setUser(user);
		userFields.add(lastNameDetail);
		em.persist(lastNameDetail);
		
		
		UserfieldPK displayNamePK = new UserfieldPK();
		displayNamePK.setUserIduser(user.getIduser());
		displayNamePK.setFieldIdfield(2);
		
		Userfield displayNameDetail = new Userfield();
		displayNameDetail.setId(displayNamePK);
		displayNameDetail.setValue(getDisplayName());
		displayNameDetail.setField(fieldList.get(2));
		displayNameDetail.setUser(user);
		userFields.add(displayNameDetail);
		em.persist(displayNameDetail);
		
		
		UserfieldPK initialsPK = new UserfieldPK();
		initialsPK.setUserIduser(user.getIduser());
		initialsPK.setFieldIdfield(3);
		
		Userfield initialsDetail = new Userfield();
		initialsDetail.setId(initialsPK);
		initialsDetail.setValue(getInitials());
		initialsDetail.setField(fieldList.get(3));
		initialsDetail.setUser(user);
		userFields.add(initialsDetail);
		em.persist(initialsDetail);
		
		
		UserfieldPK descriptionPK = new UserfieldPK();
		descriptionPK.setUserIduser(user.getIduser());
		descriptionPK.setFieldIdfield(4);
		
		Userfield descriptionDetail = new Userfield();
		descriptionDetail.setId(descriptionPK);
		descriptionDetail.setValue(getDescription());
		descriptionDetail.setField(fieldList.get(4));
		descriptionDetail.setUser(user);
		userFields.add(descriptionDetail);
		em.persist(descriptionDetail);
		
		
		UserfieldPK officePK = new UserfieldPK();
		officePK.setUserIduser(user.getIduser());
		officePK.setFieldIdfield(5);
		
		Userfield officeDetail = new Userfield();
		officeDetail.setId(officePK);
		officeDetail.setValue(getOffice());
		officeDetail.setField(fieldList.get(5));
		officeDetail.setUser(user);
		userFields.add(officeDetail);
		em.persist(officeDetail);
		
		
		UserfieldPK telephonePK = new UserfieldPK();
		telephonePK.setUserIduser(user.getIduser());
		telephonePK.setFieldIdfield(6);
		
		Userfield telephoneDetail = new Userfield();
		telephoneDetail.setId(telephonePK);
		telephoneDetail.setValue(getTelephone());
		telephoneDetail.setField(fieldList.get(6));
		telephoneDetail.setUser(user);
		userFields.add(telephoneDetail);
		em.persist(telephoneDetail);
		
		
		UserfieldPK emailPK = new UserfieldPK();
		emailPK.setUserIduser(user.getIduser());
		emailPK.setFieldIdfield(7);
		
		Userfield emailDetail = new Userfield();
		emailDetail.setId(emailPK);
		emailDetail.setValue(getEmail());
		emailDetail.setField(fieldList.get(7));
		emailDetail.setUser(user);
		userFields.add(emailDetail);
		em.persist(emailDetail);
		
		
		UserfieldPK websitePK = new UserfieldPK();
		websitePK.setUserIduser(user.getIduser());
		websitePK.setFieldIdfield(8);
		
		Userfield websiteDetail = new Userfield();
		websiteDetail.setId(websitePK);
		websiteDetail.setValue(getWebsite());
		websiteDetail.setField(fieldList.get(8));
		websiteDetail.setUser(user);
		userFields.add(websiteDetail);
		em.persist(websiteDetail);
		
		
		UserfieldPK streetPK = new UserfieldPK();
		streetPK.setUserIduser(user.getIduser());
		streetPK.setFieldIdfield(9);
		
		Userfield streetDetail = new Userfield();
		streetDetail.setId(streetPK);
		streetDetail.setValue(getStreet());
		streetDetail.setField(fieldList.get(9));
		streetDetail.setUser(user);
		userFields.add(streetDetail);
		em.persist(streetDetail);
		
		
		UserfieldPK poBoxPK = new UserfieldPK();
		poBoxPK.setUserIduser(user.getIduser());
		poBoxPK.setFieldIdfield(10);
		
		Userfield poBoxDetail = new Userfield();
		poBoxDetail.setId(poBoxPK);
		poBoxDetail.setValue(getPoBox());
		poBoxDetail.setField(fieldList.get(10));
		poBoxDetail.setUser(user);
		userFields.add(poBoxDetail);
		em.persist(poBoxDetail);
		
		
		UserfieldPK cityPK = new UserfieldPK();
		cityPK.setUserIduser(user.getIduser());
		cityPK.setFieldIdfield(11);
		
		Userfield cityDetail = new Userfield();
		cityDetail.setId(cityPK);
		cityDetail.setValue(getCity());
		cityDetail.setField(fieldList.get(11));
		cityDetail.setUser(user);
		userFields.add(cityDetail);
		em.persist(cityDetail);
		
		
		UserfieldPK statePK = new UserfieldPK();
		statePK.setUserIduser(user.getIduser());
		statePK.setFieldIdfield(12);
		
		Userfield stateDetail = new Userfield();
		stateDetail.setId(statePK);
		stateDetail.setValue(getState());
		stateDetail.setField(fieldList.get(12));
		stateDetail.setUser(user);
		userFields.add(stateDetail);
		em.persist(stateDetail);
		
		
		UserfieldPK zipPK = new UserfieldPK();
		zipPK.setUserIduser(user.getIduser());
		zipPK.setFieldIdfield(13);
		
		Userfield zipDetail = new Userfield();
		zipDetail.setId(zipPK);
		zipDetail.setValue(getZip());
		zipDetail.setField(fieldList.get(13));
		zipDetail.setUser(user);
		userFields.add(zipDetail);
		em.persist(zipDetail);
		
		
		UserfieldPK countryPK = new UserfieldPK();
		countryPK.setUserIduser(user.getIduser());
		countryPK.setFieldIdfield(14);
		
		Userfield countryDetail = new Userfield();
		countryDetail.setId(countryPK);
		countryDetail.setValue(getCountry());
		countryDetail.setField(fieldList.get(14));
		countryDetail.setUser(user);
		userFields.add(countryDetail);
		em.persist(countryDetail);
		
		
		UserfieldPK homePK = new UserfieldPK();
		homePK.setUserIduser(user.getIduser());
		homePK.setFieldIdfield(15);
		
		Userfield homeDetail = new Userfield();
		homeDetail.setId(homePK);
		homeDetail.setValue(getHome());
		homeDetail.setField(fieldList.get(15));
		homeDetail.setUser(user);
		userFields.add(homeDetail);
		em.persist(homeDetail);
		
		
		UserfieldPK pagerPK = new UserfieldPK();
		pagerPK.setUserIduser(user.getIduser());
		pagerPK.setFieldIdfield(16);
		
		Userfield pagerDetail = new Userfield();
		pagerDetail.setId(pagerPK);
		pagerDetail.setValue(getPager());
		pagerDetail.setField(fieldList.get(16));
		pagerDetail.setUser(user);
		userFields.add(pagerDetail);
		em.persist(pagerDetail);
		
		
		UserfieldPK mobilePK = new UserfieldPK();
		mobilePK.setUserIduser(user.getIduser());
		mobilePK.setFieldIdfield(17);
		
		Userfield mobileDetail = new Userfield();
		mobileDetail.setId(mobilePK);
		mobileDetail.setValue(getMobile());
		mobileDetail.setField(fieldList.get(17));
		mobileDetail.setUser(user);
		userFields.add(mobileDetail);
		em.persist(mobileDetail);
		
		
		UserfieldPK faxPK = new UserfieldPK();
		faxPK.setUserIduser(user.getIduser());
		faxPK.setFieldIdfield(18);
		
		Userfield faxDetail = new Userfield();
		faxDetail.setId(faxPK);
		faxDetail.setValue(getFax());
		faxDetail.setField(fieldList.get(18));
		faxDetail.setUser(user);
		userFields.add(faxDetail);
		em.persist(faxDetail);
		
		
		UserfieldPK ipphonePK = new UserfieldPK();
		ipphonePK.setUserIduser(user.getIduser());
		ipphonePK.setFieldIdfield(19);
		
		Userfield ipphoneDetail = new Userfield();
		ipphoneDetail.setId(ipphonePK);
		ipphoneDetail.setValue(getIpphone());
		ipphoneDetail.setField(fieldList.get(19));
		ipphoneDetail.setUser(user);
		userFields.add(ipphoneDetail);
		em.persist(ipphoneDetail);
		
		
		UserfieldPK titlePK = new UserfieldPK();
		titlePK.setUserIduser(user.getIduser());
		titlePK.setFieldIdfield(20);
		
		Userfield titleDetail = new Userfield();
		titleDetail.setId(titlePK);
		titleDetail.setValue(getTitle());
		titleDetail.setField(fieldList.get(20));
		titleDetail.setUser(user);
		userFields.add(titleDetail);
		em.persist(titleDetail);
		
		
		UserfieldPK departmentPK = new UserfieldPK();
		departmentPK.setUserIduser(user.getIduser());
		departmentPK.setFieldIdfield(21);
		
		Userfield departmentDetail = new Userfield();
		departmentDetail.setId(departmentPK);
		departmentDetail.setValue(getDepartment());
		departmentDetail.setField(fieldList.get(21));
		departmentDetail.setUser(user);
		userFields.add(departmentDetail);
		em.persist(departmentDetail);
		
		
		UserfieldPK companyPK = new UserfieldPK();
		companyPK.setUserIduser(user.getIduser());
		companyPK.setFieldIdfield(22);
		
		Userfield companyDetail = new Userfield();
		companyDetail.setId(companyPK);
		companyDetail.setValue(getCompany());
		companyDetail.setField(fieldList.get(22));
		companyDetail.setUser(user);
		userFields.add(companyDetail);
		em.persist(companyDetail);
		
		
		UserfieldPK managerPK = new UserfieldPK();
		managerPK.setUserIduser(user.getIduser());
		managerPK.setFieldIdfield(23);
		
		Userfield managerDetail = new Userfield();
		managerDetail.setId(managerPK);
		managerDetail.setValue(getManager());
		managerDetail.setField(fieldList.get(23));
		managerDetail.setUser(user);
		userFields.add(managerDetail);
		em.persist(managerDetail);
		
		return userFields;
	}
	
	public void updateUser(User user) {
		TypedQuery<Userfield> usersFieldsQuery = em.createNamedQuery("Userfields.getUsersFields", Userfield.class);
		usersFieldsQuery.setParameter("value", new Integer(user.getIduser()).toString());
		List<Userfield> userFieldsList = usersFieldsQuery.getResultList();
		
		Userfield userField; 
		
		userField = userFieldsList.get(0);
		userField.setValue(getFirstName());
		
		userField = userFieldsList.get(1);
		userField.setValue(getLastName());
		
		userField = userFieldsList.get(2);
		userField.setValue(getDisplayName());
		
		userField = userFieldsList.get(3);
		userField.setValue(getInitials());
		
		userField = userFieldsList.get(4);
		userField.setValue(getDescription());
		
		userField = userFieldsList.get(5);
		userField.setValue(getOffice());
		
		userField = userFieldsList.get(6);
		userField.setValue(getTelephone());
		
		userField = userFieldsList.get(7);
		userField.setValue(getEmail());
		
		userField = userFieldsList.get(8);
		userField.setValue(getWebsite());
		
		userField = userFieldsList.get(9);
		userField.setValue(getStreet());
		
		userField = userFieldsList.get(10);
		userField.setValue(getPoBox());
		
		userField = userFieldsList.get(11);
		userField.setValue(getCity());
		
		userField = userFieldsList.get(12);
		userField.setValue(getState());
		
		userField = userFieldsList.get(13);
		userField.setValue(getZip());
		
		userField = userFieldsList.get(14);
		userField.setValue(getCountry());
		
		userField = userFieldsList.get(15);
		userField.setValue(getHome());
		
		userField = userFieldsList.get(16);
		userField.setValue(getPager());
		
		userField = userFieldsList.get(17);
		userField.setValue(getMobile());
		
		userField = userFieldsList.get(18);
		userField.setValue(getFax());
		
		userField = userFieldsList.get(19);
		userField.setValue(getIpphone());
		
		userField = userFieldsList.get(20);
		userField.setValue(getTitle());
		
		userField = userFieldsList.get(21);
		userField.setValue(getDepartment());
		
		userField = userFieldsList.get(22);
		userField.setValue(getCompany());
		
		userField = userFieldsList.get(23);
		userField.setValue(getManager());
	}
}
