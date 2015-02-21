package com.mailsigner.model.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

/**
 * The primary key class for the COMPUTER database table.
 * 
 */
@Embeddable
public class ComputerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = IDENTITY)
	private int idcomputer;

	@Column(name="USER_IDUSER", insertable=false, updatable=false)
	private int userIduser;

    public ComputerPK() {
    }
	public int getIdcomputer() {
		return this.idcomputer;
	}
	public void setIdcomputer(int idcomputer) {
		this.idcomputer = idcomputer;
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
		if (!(other instanceof ComputerPK)) {
			return false;
		}
		ComputerPK castOther = (ComputerPK)other;
		return 
			(this.idcomputer == castOther.idcomputer)
			&& (this.userIduser == castOther.userIduser);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idcomputer;
		hash = hash * prime + this.userIduser;
		
		return hash;
    }
}