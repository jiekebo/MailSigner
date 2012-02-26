package com.mailsigner.model.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * The persistent class for the FIELD database table.
 * 
 */
@NamedQueries({ 
	@NamedQuery(name = "Field.allFields", query = "SELECT f FROM Field f"),
	@NamedQuery(name = "Field.allAdFields", query = "SELECT f FROM Field f WHERE f.protected_ LIKE :value")
})
@Entity
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int idfield;

	private String code;

	private String label;

	@Column(name = "PROTECTED")
	private byte protected_;

	// bi-directional many-to-one association to Userfield
	@OneToMany(mappedBy = "field")
	private Set<Userfield> userfields;

	public Field() {
	}

	public int getIdfield() {
		return this.idfield;
	}

	public void setIdfield(int idfield) {
		this.idfield = idfield;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public byte getProtected_() {
		return this.protected_;
	}

	public void setProtected_(byte protected_) {
		this.protected_ = protected_;
	}

	public Set<Userfield> getUserfields() {
		return this.userfields;
	}

	public void setUserfields(Set<Userfield> userfields) {
		this.userfields = userfields;
	}

}