package com.mailsigner.control;

public class ActiveDirectoryController {

	public ActiveDirectoryController() {
		// TODO Auto-generated constructor stub
	}
	
	public void addAdUsers() {
//		String host = MailSigner.getSettings().getHost();
//		String domain = MailSigner.getSettings().getDomain();
//		String user = MailSigner.getSettings().getUser();
//		String pass = MailSigner.getSettings().getPass();
//		int timeout = MailSigner.getSettings().getTimeout();
//		AdInterface ad = new AdInterface(host,domain,user,pass,timeout);
//		
//		boolean connected = false;
//		
//		try {
//			connected = ad.connect();
//		} catch (AuthenticationException e) {
//			JOptionPane.showMessageDialog(MailSigner.getMainFrame(), "Password error.");
//		} catch (CommunicationException e) {
//			JOptionPane.showMessageDialog(MailSigner.getMainFrame(), "Connection error. Check configuration.");
//		} catch (NamingException e) {
//			System.out.println("Catch all");
//		}
//		
//		if(!connected) {
//			return;
//		}
//		
//		List<AdUser> adUsers = null;
//		try {
//			adUsers = ad.getAdUsers();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		UserSelectionFrame userSelectionFrame = new UserSelectionFrame(MailSigner.getMainFrame(), adUsers);
//		userSelectionFrame.setModal(true);
//		userSelectionFrame.setVisible(true); 
//		List<AdUser> filteredList = userSelectionFrame.getFilteredList();
//		if(filteredList == null) {
//			return;
//		}
//		byte activeDirectory = 1;
//		for (AdUser adUser : filteredList) {
//			String login = adUser.getLogon();
//			
//			TypedQuery<User> userQuery = em.createNamedQuery("User.findUser", User.class);
//			userQuery.setParameter("value", login);
//			TypedQuery<Signature> signatureQuery = em.createNamedQuery("Signature.allSignatures", Signature.class);
//			List<Signature> signatureList = signatureQuery.getResultList();
//			
//			entr.begin();
//			User newUser = new User();
//			newUser.setLogon(adUser.getLogon());
//			newUser.setActivedirectory(activeDirectory);
//
//			// if user login exist let user choose whether to override
//			if(userQuery.getResultList().size() > 0) {
//				User foundUser = userQuery.getSingleResult();
//				// if already ad-user, skip
//				if(foundUser.getActivedirectory() == 1) {
//					entr.rollback();
//					continue;
//				}
//				int choice = JOptionPane.showConfirmDialog(this,
//						"User \"" + login + "\" already exists. Replace with AD-user?",
//						"User exists",
//						JOptionPane.YES_NO_OPTION);
//				if(choice == JOptionPane.YES_OPTION) {
//					// TODO: copy the settings of the overridden users signatures
//					// This doesn't work, purpose: to copy the settings of the signatures...
////						Set<Usersignature> foundUsersSignatures = foundUser.getUsersignatures();
////						Set<Usersignature> newUsersSignatures = new HashSet<Usersignature>();
////						for (Usersignature foundUsersSignature : foundUsersSignatures) {
////							UsersignaturePK userSignaturePK = new UsersignaturePK();
////							userSignaturePK.setSignatureIdsignature(foundUsersSignature.getId().getSignatureIdsignature());
////							userSignaturePK.setUserIduser(newUser.getIduser());
////							
////							Usersignature userSignature = new Usersignature();
////							userSignature.setId(userSignaturePK);
////							userSignature.setEnabled(foundUsersSignature.getEnabled());
////							userSignature.setSignature(foundUsersSignature.getSignature());
////							userSignature.setUser(newUser);
////							newUsersSignatures.add(userSignature);
////							em.persist(userSignature);
////						}
////						newUser.setUsersignatures(newUsersSignatures);
////						newUser.setComputers(foundUser.getComputers());
////						Set<Userfield> userFields = adUser.getUserDetails(newUser);
////						newUser.setUserfields(userFields);
//					
//					Set<Userfield> userFields = adUser.getUserDetails(newUser);
//					newUser.setUserfields(userFields);
//					
//					Set<Usersignature> signatureSet = new HashSet<Usersignature>();
//					for (Signature signature : signatureList) {
//						UsersignaturePK userSignaturePK = new UsersignaturePK();
//						userSignaturePK.setSignatureIdsignature(signature.getIdsignature());
//						userSignaturePK.setUserIduser(newUser.getIduser());
//						
//						Usersignature userSignature = new Usersignature();
//						userSignature.setId(userSignaturePK);
//						userSignature.setSignature(signature);
//						userSignature.setUser(newUser);
//						signatureSet.add(userSignature);
//						em.persist(userSignature);
//					}
//					newUser.setUsersignatures(signatureSet);
//					
//					em.persist(newUser);
//					em.remove(foundUser);
//					entr.commit();
//				}
//				continue;
//			}
//			
//			// If user logon doesn't exists, create a new ad-user
//			Set<Userfield> userFields = adUser.getUserDetails(newUser);
//			newUser.setUserfields(userFields);
//			
//			Set<Usersignature> signatureSet = new HashSet<Usersignature>();
//			for (Signature signature : signatureList) {
//				UsersignaturePK userSignaturePK = new UsersignaturePK();
//				userSignaturePK.setSignatureIdsignature(signature.getIdsignature());
//				userSignaturePK.setUserIduser(newUser.getIduser());
//				
//				Usersignature userSignature = new Usersignature();
//				userSignature.setId(userSignaturePK);
//				userSignature.setSignature(signature);
//				userSignature.setUser(newUser);
//				signatureSet.add(userSignature);
//				em.persist(userSignature);
//			}
//			newUser.setUsersignatures(signatureSet);
//			
//			em.persist(newUser);
//			entr.commit();
//		}
//		loadUsersAction();
	}
	
	public void refreshAdUser() {
//		if(userList.getSelectedIndex() < 0) {
//			return;
//		}
//		
//		String host = MailSigner.getSettings().getHost();
//		String domain = MailSigner.getSettings().getDomain();
//		String user = MailSigner.getSettings().getUser();
//		String pass = MailSigner.getSettings().getPass();
//		int timeout = MailSigner.getSettings().getTimeout();
//		AdInterface ad = new AdInterface(host,domain,user,pass,timeout);
//		
//		boolean connected = false;
//		
//		try {
//			connected = ad.connect();
//		} catch (AuthenticationException e) {
//			JOptionPane.showMessageDialog(MailSigner.getMainFrame(), "Password error.");
//		} catch (CommunicationException e) {
//			JOptionPane.showMessageDialog(MailSigner.getMainFrame(), "Connection error. Check configuration.");
//		} catch (NamingException e) {
//			System.out.println("Catch all");
//		}
//		
//		if(!connected) {
//			return;
//		}
//		
//		List<AdUser> adUsers = null;
//		try {
//			adUsers = ad.getAdUsers();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		
//		User selectedUser = (User) userListModel.get(userList.getSelectedIndex());
//
//		Iterator<AdUser> it = adUsers.iterator();
//		AdUser adUser = null;
//		while (it.hasNext()) {
//			adUser = (AdUser) it.next();
//			if(adUser.getLogon().equals(selectedUser.getLogon())) {
//				break;
//			}
//		}
//		
//		adUser.updateUser(selectedUser);
//		entr.begin();
//		em.persist(selectedUser);
//		entr.commit();
//		
//		updateUserSettingsTables();
	}
}
