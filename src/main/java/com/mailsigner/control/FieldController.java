package com.mailsigner.control;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.mailsigner.MailSigner;
import com.mailsigner.model.persistence.Field;

public class FieldController {
	private EntityManager em = MailSigner.getEm();
	
	public FieldController() {}
	
	/**
	 * Loads the fields from database and stores the result in this object.
	 * @return Also returns the result.
	 */
	public List<Field> loadFields() {
		TypedQuery<Field> fieldQuery = em.createNamedQuery("Field.allFields", Field.class);
		List <Field> fields = fieldQuery.getResultList();
		return fields;
	}
}
