package com.mailsigner.model.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * The persistent class for the USER database table.
 * 
 */
@NamedQueries({ 
	@NamedQuery(name = "User.allUsers", query = "SELECT u FROM User u ORDER BY UPPER(u.logon)"),
	@NamedQuery(name = "User.findUser", query = "SELECT u FROM User u WHERE u.logon LIKE :value")
})
@Entity
public class User implements Comparable<User>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int iduser;

	private byte activedirectory;

	private String logon;

	// bi-directional many-to-one association to Computer
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "user")
	private Set<Computer> computers;

	// bi-directional many-to-one association to Userfield
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "user")
	private Set<Userfield> userfields;

	// bi-directional many-to-one association to Usersignature
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "user")
	private Set<Usersignature> usersignatures;

	public User() {
	}

	public int getIduser() {
		return this.iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public byte getActivedirectory() {
		return this.activedirectory;
	}

	public void setActivedirectory(byte activedirectory) {
		this.activedirectory = activedirectory;
	}

	public String getLogon() {
		return this.logon;
	}

	public void setLogon(String logon) {
		this.logon = logon;
	}

	public Set<Computer> getComputers() {
		return this.computers;
	}

	public void setComputers(Set<Computer> computers) {
		this.computers = computers;
	}

	public Set<Userfield> getUserfields() {
		return this.userfields;
	}

	public void setUserfields(Set<Userfield> userfields) {
		this.userfields = userfields;
	}

	public Set<Usersignature> getUsersignatures() {
		return this.usersignatures;
	}

	public void setUsersignatures(Set<Usersignature> usersignatures) {
		this.usersignatures = usersignatures;
	}

	@Override
	public int compareTo(User arg0) {
		return this.getLogon().compareToIgnoreCase(arg0.getLogon());
	}

}