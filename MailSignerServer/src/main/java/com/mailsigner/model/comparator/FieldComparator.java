package com.mailsigner.model.comparator;

import java.util.Comparator;
import com.mailsigner.model.persistence.Userfield;

public class FieldComparator implements Comparator<Userfield> {

	@Override
	public int compare(Userfield o1, Userfield o2) {
		return ((Integer) o1.getField().getIdfield())
				.compareTo(o2.getField().getIdfield());
	}
}