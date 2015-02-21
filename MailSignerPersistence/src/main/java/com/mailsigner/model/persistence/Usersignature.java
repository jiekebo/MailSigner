package com.mailsigner.model.persistence;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the USERSIGNATURES database table.
 * 
 */
@Entity
@Table(name = "USERSIGNATURES")
public class Usersignature implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UsersignaturePK id;

	private byte enabled;

	// bi-directional many-to-one association to Signature
	@ManyToOne
	private Signature signature;

	// bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Usersignature() {
	}

	public UsersignaturePK getId() {
		return this.id;
	}

	public void setId(UsersignaturePK id) {
		this.id = id;
	}

	public byte getEnabled() {
		return this.enabled;
	}
	
	public boolean isEnabled() {
		if(this.enabled == 1) {
			return true;
		}
		return false;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}

	public Signature getSignature() {
		return this.signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}