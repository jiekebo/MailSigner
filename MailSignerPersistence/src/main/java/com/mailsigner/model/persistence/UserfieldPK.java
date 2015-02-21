package com.mailsigner.model.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the USERFIELDS database table.
 * 
 */
@Embeddable
public class UserfieldPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "USER_IDUSER", insertable = false, updatable = false)
	private int userIduser;

	@Column(name = "FIELD_IDFIELD", insertable = false, updatable = false)
	private int fieldIdfield;

	public UserfieldPK() {
	}

	public int getUserIduser() {
		return this.userIduser;
	}

	public void setUserIduser(int userIduser) {
		this.userIduser = userIduser;
	}

	public int getFieldIdfield() {
		return this.fieldIdfield;
	}

	public void setFieldIdfield(int fieldIdfield) {
		this.fieldIdfield = fieldIdfield;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserfieldPK)) {
			return false;
		}
		UserfieldPK castOther = (UserfieldPK) other;
		return (this.userIduser == castOther.userIduser)
				&& (this.fieldIdfield == castOther.fieldIdfield);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userIduser;
		hash = hash * prime + this.fieldIdfield;

		return hash;
	}
}