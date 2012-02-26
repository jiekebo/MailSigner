package com.mailsigner.model.persistence;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * The persistent class for the SIGNATURE database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name = "Signature.allSignatures", query = "SELECT s FROM Signature s ORDER BY UPPER(s.title)"),
	@NamedQuery(name = "Signature.findSignature", query = "SELECT s FROM Signature s WHERE s.title LIKE :value")
})
@Entity
public class Signature implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int idsignature;
	
	private byte premium;

	private String title;

	@Lob @Basic(fetch=LAZY)
	private byte[] description;

	@Lob
	private byte[] html;
	
	@Lob
	private byte[] rtf;
	
	@Lob
	private byte[] txt;

	// bi-directional many-to-one association to Usersignature
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "signature")
	private Set<Usersignature> usersignatures;

	public Signature() {
	}

	public int getIdsignature() {
		return this.idsignature;
	}

	public void setIdsignature(int idsignature) {
		this.idsignature = idsignature;
	}
	
	public byte getPremium() {
		return premium;
	}

	public void setPremium(byte premium) {
		this.premium = premium;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte[] getDescription() {
		return description;
	}

	public void setDescription(byte[] description) {
		this.description = description;
	}

	public byte[] getHtml() {
		return html;
	}

	public void setHtml(byte[] html) {
		this.html = html;
	}

	public byte[] getRtf() {
		return rtf;
	}

	public void setRtf(byte[] rtf) {
		this.rtf = rtf;
	}

	public byte[] getTxt() {
		return txt;
	}

	public void setTxt(byte[] txt) {
		this.txt = txt;
	}

	public Set<Usersignature> getUsersignatures() {
		return this.usersignatures;
	}

	public void setUsersignatures(Set<Usersignature> usersignatures) {
		this.usersignatures = usersignatures;
	}

}