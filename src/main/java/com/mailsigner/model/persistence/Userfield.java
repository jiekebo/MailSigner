package com.mailsigner.model.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the USERFIELDS database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name = "Userfields.getUsersFields", query = "SELECT f FROM Userfield f WHERE f.id.userIduser LIKE :value")
})
@Entity
@Table(name = "USERFIELDS")
public class Userfield implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserfieldPK id;

	private String value;

	// bi-directional many-to-one association to Field
	@ManyToOne
	private Field field;

	// bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Userfield() {
	}

	public UserfieldPK getId() {
		return this.id;
	}

	public void setId(UserfieldPK id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}