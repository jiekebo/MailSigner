package com.mailsigner.model.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the COMPUTER database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name = "Computer.findComputer", query = "SELECT c FROM Computer c WHERE c.label = :label AND c.user = :user")
})
@Entity
public class Computer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@GeneratedValue(strategy = IDENTITY)
	private ComputerPK id;
	private String label;
	private byte deployed;

	//bi-directional many-to-one association to User
    @ManyToOne
	private User user;

    public Computer() {
    }

	public ComputerPK getId() {
		return this.id;
	}

	public void setId(ComputerPK id) {
		this.id = id;
	}
	
	public byte getDeployed() {
		return this.deployed;
	}

	public void setDeployed(byte deployed) {
		this.deployed = deployed;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
}