package com.mailsigner.model.comparator;

import java.util.Comparator;

import com.mailsigner.model.persistence.User;

public class UserComparator implements Comparator<User> {

	@Override
	public int compare(User arg0, User arg1) {
		return arg0.getLogon().compareToIgnoreCase(arg1.getLogon());
	}
}
