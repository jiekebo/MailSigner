package com.mailsigner.control;

import com.mailsigner.MailSigner;
import com.mailsigner.control.exception.NoUserSelectedException;
import com.mailsigner.control.exception.UserExistsException;
import com.mailsigner.control.exception.UserlimitReachedException;
import com.mailsigner.control.exception.UsernameEmptyException;
import com.mailsigner.control.table.UserComputersTableController;
import com.mailsigner.control.table.UserDetailsController;
import com.mailsigner.control.table.UserSignaturesTableController;
import com.mailsigner.model.UserModel;
import com.mailsigner.model.list.SortedListModel;
import com.mailsigner.model.persistence.User;
import com.mailsigner.util.Settings;

public class UserController {

	private UserModel userModel = new UserModel();
	private Settings settings = MailSigner.getSettings();
	private UserDetailsController userDetailsTableController = new UserDetailsController(userModel);
	private UserSignaturesTableController userSignaturesTableController = new UserSignaturesTableController();
	private UserComputersTableController userComputersTableController = new UserComputersTableController();
	private boolean userListUpdate;

	public UserController() {}

	public void addUser(String name) throws UserlimitReachedException, UserExistsException, UsernameEmptyException {
		if (userModel.getListModel().getSize() >= settings.getUserLimit()) {
			throw new UserlimitReachedException();
		}
		if (isExistingUser(name)) {
			throw new UserExistsException();
		}
		if (name == null || name.length() <= 0) {
			throw new UsernameEmptyException();
		}

		userModel.addUser(name);
	}

	public void clearTables() {
		userDetailsTableController.deleteData();
		userSignaturesTableController.deleteData();
		userComputersTableController.deleteData();
	}
	
	public SortedListModel updateModel() {
		userModel.updateListModel();
		return userModel.getListModel();
	}

	public UserComputersTableController getUserComputersTableController() {
		return userComputersTableController;
	}
	
	public UserDetailsController getUserDetailsTableController() {
		return userDetailsTableController;
	}

	public UserSignaturesTableController getUserSignaturesTableController() {
		return userSignaturesTableController;
	}

	public boolean isAdUser() {
		return userModel.isAdUser();
	}
	
	public boolean isExistingUser(String name) {
		return userModel.isExistingUser(name);
	}
	
	public boolean isUserListUpdate() {
		return userListUpdate;
	}
	
	public void removeUsers(int[] index) {
		userListUpdate = true;
		if(index == null || index.length <= 0) {
			return;
		}
		
		for (int i = index.length - 1; i >= 0; i--) {
			userModel.removeUser(index[i]);
		}
		userListUpdate = false;
	}
	
	public void renameUser(int index, String name) throws NoUserSelectedException, UsernameEmptyException, UserExistsException {
		if (index == -1) {
			throw new NoUserSelectedException();
		}
		if (name == null || name.length() <= 0) {
			throw new UsernameEmptyException();
		}
		if (isExistingUser(name)) {
			throw new UserExistsException();
		}
		userModel.renameUser(index, name);
	}
	
	public void setSelectedUser(int index) {
		User user = userModel.getUser(index);
		userDetailsTableController.setUser(user);
		userSignaturesTableController.setUser(user);
		userComputersTableController.setUser(user);
		userModel.setSelectedUser(index);
	}
}