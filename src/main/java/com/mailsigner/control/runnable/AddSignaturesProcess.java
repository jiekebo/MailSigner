package com.mailsigner.control.runnable;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;

import com.mailsigner.MailSigner;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Usersignature;
import com.mailsigner.model.persistence.UsersignaturePK;
import com.mailsigner.view.MainFrame;

public class AddSignaturesProcess extends SwingWorker<Void, Void> {
	
	private EntityManager em = MailSigner.getEm();
	private EntityTransaction entr = MailSigner.getEntr();
	private String signatureTitle;
	private DefaultListModel signatureListModel;
	private Signature signature = new Signature();
	
	public AddSignaturesProcess(String signatureTitle, DefaultListModel signatureListModel) {
		this.signatureTitle = signatureTitle;
		this.signatureListModel = signatureListModel;
		
	}

	@Override
	protected Void doInBackground() {
		MainFrame.setToolTip("Adding signature to users");
		
		entr.begin();

		MailSigner.getLog().debug("Creating signature with title " + signatureTitle);
		signature.setTitle(signatureTitle);
		em.persist(signature);

		TypedQuery<User> userQuery = em.createNamedQuery("User.allUsers", User.class);
		List<User> users = userQuery.getResultList();
		int userCount = users.size();
		int i = 0;		
		for (User user : users) {
			UsersignaturePK userSignaturePK = new UsersignaturePK();
			userSignaturePK.setUserIduser(user.getIduser());
			userSignaturePK.setSignatureIdsignature(signature.getIdsignature());

			Usersignature userSignature = new Usersignature();
			userSignature.setId(userSignaturePK);
			userSignature.setSignature(signature);
			userSignature.setUser(user);

			em.persist(userSignature);
			i++;
			setProgress(i * 100 / userCount);
		}		
		entr.commit();
		return null;
	}

	@Override
	protected void done() {
		signatureListModel.addElement(signature);
		//TODO: Make this two triggered events instead of a static call to functions
		MainFrame.clearToolTip();
		MailSigner.getUserController().clearTables();
		MainFrame.setProgressBar(0);
		MainFrame.hideProgressBar();
		super.done();
	}
}
