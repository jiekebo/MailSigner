package com.mailsigner.model.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the USERSIGNATURES database table.
 * 
 */
@Embeddable
public class UsersignaturePK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "SIGNATURE_IDSIGNATURE", insertable = false, updatable = false)
	private int signatureIdsignature;

	@Column(name = "USER_IDUSER", insertable = false, updatable = false)
	private int userIduser;

	public UsersignaturePK() {
	}

	public int getSignatureIdsignature() {
		return this.signatureIdsignature;
	}

	public void setSignatureIdsignature(int signatureIdsignature) {
		this.signatureIdsignature = signatureIdsignature;
	}

	public int getUserIduser() {
		return this.userIduser;
	}

	public void setUserIduser(int userIduser) {
		this.userIduser = userIduser;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UsersignaturePK)) {
			return false;
		}
		UsersignaturePK castOther = (UsersignaturePK) other;
		return (this.signatureIdsignature == castOther.signatureIdsignature)
				&& (this.userIduser == castOther.userIduser);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.signatureIdsignature;
		hash = hash * prime + this.userIduser;

		return hash;
	}
}