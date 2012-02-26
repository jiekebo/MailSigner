package com.mailsigner.model.comparator;

import java.util.Comparator;

import com.mailsigner.model.persistence.Usersignature;

public class UsersignatureComparator implements Comparator<Usersignature> {

	@Override
	public int compare(Usersignature o1, Usersignature o2) {
		return o1.getSignature().getTitle().compareToIgnoreCase(o2.getSignature().getTitle());
	}
}