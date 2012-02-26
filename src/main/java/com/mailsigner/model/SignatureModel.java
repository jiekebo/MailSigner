package com.mailsigner.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.mailsigner.MailSigner;
import com.mailsigner.control.runnable.AddSignaturesProcess;
import com.mailsigner.model.comparator.SignatureComparator;
import com.mailsigner.model.list.SortedListModel;
import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Usersignature;
import com.mailsigner.view.MainFrame;

public class SignatureModel {
	
	private EntityManager em = MailSigner.getEm();
	private EntityTransaction entr = MailSigner.getEntr();
	private DefaultListModel signatureListModel = new DefaultListModel();
	private SortedListModel sortedListModel = 
			new SortedListModel(signatureListModel, SortedListModel.SortOrder.ASCENDING, new SignatureComparator());
	private Logger log = MailSigner.getLog();
	
	public SignatureModel() {}
	
	public void addSignature(String title) {
		AddSignaturesProcess add = new AddSignaturesProcess(title, signatureListModel);
		add.addPropertyChangeListener(MainFrame.getSignatureTab());
		Thread addThread = new Thread(add);
		addThread.start();
	}
	
	public SortedListModel getListModel() {
		return sortedListModel;
	}
	
	public Signature getSignature(int index) {
		Signature signature = (Signature) sortedListModel.getElementAt(index);
		em.refresh(signature);
		log.debug("User " + signature.getTitle() + " refreshed and selected");
		return signature;
	}
	
	/**
	 * Checks if a signature with the given name already exists
	 * 
	 * @param title Name of the signature to check
	 * @return True if the signature already exists
	 */
	public boolean isExistingSignature(String title) {
		TypedQuery<Signature> signatureQuery = em.createNamedQuery("Signature.findSignature", Signature.class);
		signatureQuery.setParameter("value", title);
		if (signatureQuery.getResultList().size() > 0) {
			log.debug("Signature of the title " + title + " already exists");
			Signature signature = signatureQuery.getResultList().get(signatureQuery.getFirstResult());
			log.debug("User input: " + title + " Database contains: " + signature.getTitle());
			return title.equals(signature.getTitle());
		}
		return false;
	}

	public void removeSignature(int index) {
		Signature signature = (Signature) signatureListModel.get(index);
		entr.begin();
		em.remove(signature);
		entr.commit();
		signatureListModel.removeElement(signature);
		log.debug("Signature " + signature.getTitle() + " has been removed");
	}
	
	public void renameSignature(int index, String title) {
		Signature signature = (Signature) signatureListModel.get(index);
		signatureListModel.remove(index);
		entr.begin();
		signature.setTitle(title);
		entr.commit();
		signatureListModel.addElement(signature);
	}

	public void saveSignature(int index, byte[] html, byte[] rtf, byte[] txt, byte[] desc) {
		Signature signature = (Signature) signatureListModel.get(index);
		entr.begin();
		signature.setHtml(html);
		signature.setRtf(rtf);
		signature.setTxt(txt);
		signature.setDescription(desc);
		em.persist(signature);
		entr.commit();

		Set<Usersignature> signatures = signature.getUsersignatures();

		for (Usersignature usersignature : signatures) {
			if (usersignature.getEnabled() == 0) {
				return;
			}
			entr.begin();
			User user = usersignature.getUser();
			Set<Computer> usersComputers = user.getComputers();
			for (Computer computer : usersComputers) {
				computer.setDeployed(new Byte((byte) 0));
			}
			em.flush();
			entr.commit();
		}
	}
	
	public void toggleSignature(Usersignature signature, byte setting) {
		entr.begin();
		signature.setEnabled(setting);
		em.flush();
		entr.commit();
	}
	
	public void updateListModel() {
		TypedQuery<Signature> signatureQuery = em.createNamedQuery("Signature.allSignatures", Signature.class);
		List<Signature> resultList = signatureQuery.getResultList();
		Iterator<Signature> it = resultList.iterator();
		while (it.hasNext()) {
			Signature signature = it.next();
			signatureListModel.addElement(signature);
		}
	}
}
