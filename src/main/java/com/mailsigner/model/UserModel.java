package com.mailsigner.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.mailsigner.MailSigner;
import com.mailsigner.control.exception.UserNotFoundException;
import com.mailsigner.model.comparator.UserComparator;
import com.mailsigner.model.list.SortedListModel;
import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.Field;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Userfield;
import com.mailsigner.model.persistence.UserfieldPK;
import com.mailsigner.model.persistence.Usersignature;
import com.mailsigner.model.persistence.UsersignaturePK;

public class UserModel {

	private EntityManager em = MailSigner.getEm();
	private EntityTransaction entr = MailSigner.getEntr();
	private DefaultListModel userListModel = new DefaultListModel();
	private SortedListModel sortedListModel = 
			new SortedListModel(userListModel, SortedListModel.SortOrder.ASCENDING, new UserComparator());
	private Logger log = MailSigner.getLog();
	private User selectedUser;
	
	public UserModel() {}
	
	public void addUser(String name) {
		entr.begin();
		User user = new User();
		user.setLogon(name);

		// get all fields in database
		TypedQuery<Field> fieldQuery = em.createNamedQuery("Field.allFields", Field.class);
		List<Field> fieldList = fieldQuery.getResultList();
		TypedQuery<Signature> signatureQuery = em.createNamedQuery("Signature.allSignatures", Signature.class);
		List<Signature> signatureList = signatureQuery.getResultList();

		// add each field to a set
		Set<Userfield> userFieldsSet = new HashSet<Userfield>();
		for (Field field : fieldList) {
			UserfieldPK userFieldPK = new UserfieldPK();
			userFieldPK.setUserIduser(user.getIduser());
			// how to get the generated user id before it has been persisted?
			// above actually works!!! Incredible JPA!!!
			userFieldPK.setFieldIdfield(field.getIdfield());

			Userfield userField = new Userfield();
			userField.setId(userFieldPK);
			userField.setField(field);
			userField.setUser(user);
			userField.setValue("");
			userFieldsSet.add(userField);
			em.persist(userField);
		}
		user.setUserfields(userFieldsSet);
		
		
		Set<Usersignature> signatureSet = new HashSet<Usersignature>();
		for (Signature signature : signatureList) {
			UsersignaturePK userSignaturePK = new UsersignaturePK();
			userSignaturePK.setSignatureIdsignature(signature.getIdsignature());
			userSignaturePK.setUserIduser(user.getIduser());
			
			Usersignature userSignature = new Usersignature();
			userSignature.setId(userSignaturePK);
			userSignature.setSignature(signature);
			userSignature.setUser(user);
			signatureSet.add(userSignature);
			em.persist(userSignature);
		}
		user.setUsersignatures(signatureSet);
		
		
		Set<Computer> computerSet = new HashSet<Computer>();
		user.setComputers(computerSet);
		
		// persist the user
		em.persist(user);
		entr.commit();
		userListModel.addElement(user);
	}
	
	public SortedListModel getListModel() {
		return sortedListModel;
	}
	
	public User getUser(int index) {
		User user = (User) sortedListModel.getElementAt(index);
		em.refresh(user);
		log.debug("User " + user.getLogon() + " refreshed and selected");
		return user;
	}
	
	public User findUser(String name) throws UserNotFoundException {
		TypedQuery<User> userQuery = em.createNamedQuery("User.findUser", User.class);
		userQuery.setParameter("value", name);
		if(userQuery.getResultList().size() <= 0) {
			throw new UserNotFoundException();			
		}
		return userQuery.getSingleResult();
	}
	
	public boolean isAdUser() {
		if (selectedUser != null) {
			return selectedUser.getActivedirectory() == 0 ? false : true;
		}
		return false;
	}
	
	public boolean isExistingUser(String name) {
		TypedQuery<User> userQuery = em.createNamedQuery("User.findUser", User.class);
		userQuery.setParameter("value", name);
		if(userQuery.getResultList().size() > 0) {
			log.debug("User of the name " + name + " already exists");
			User user = userQuery.getResultList().get(userQuery.getFirstResult());
			log.debug("User input: " + name + " Database contains: " + user.getLogon());
			return name.equals(user.getLogon());
		}
		return false;
	}
	
	public void removeUser(int index) {
		User user = (User) userListModel.get(index);
		entr.begin();
		em.remove(user);
		entr.commit();
		userListModel.removeElement(user);
		log.debug("User " + user.getLogon() + " has been removed");
	}
	
	public void renameUser(int index, String name) {
		User user = (User) userListModel.get(index);
		userListModel.remove(index);
		entr.begin();
		user.setLogon(name);
		entr.commit();
		userListModel.addElement(user);
	}

	public void setSelectedUser(int index) {
		User user = (User) sortedListModel.getElementAt(index);
		selectedUser = user;
	}
	
	public void updateListModel() {
		TypedQuery<User> userQuery = em.createNamedQuery("User.allUsers", User.class);
		List<User> resultList = userQuery.getResultList();
		Iterator<User> it = resultList.iterator();
		while (it.hasNext()) {
			User user = it.next();
			userListModel.addElement(user);
		}
	}
	
	public void updateUserField(Userfield userField, String value) {
		entr.begin();
		userField.setValue(value);
		entr.commit();
	}

}
