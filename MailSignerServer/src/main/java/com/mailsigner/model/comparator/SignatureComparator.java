package com.mailsigner.model.comparator;

import java.util.Comparator;

import com.mailsigner.model.persistence.Signature;

public class SignatureComparator implements Comparator<Signature> {

	@Override
	public int compare(Signature o1, Signature o2) {
		return o1.getTitle().compareToIgnoreCase(o2.getTitle());
	}
}