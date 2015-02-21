package com.mailsigner.control.runnable;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;

import com.mailsigner.MailSigner;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.view.MainFrame;

public class RemoveSignaturesProcess extends SwingWorker<Void, Void> {
	
	private int[] index;
	private EntityManager em = MailSigner.getEm();
	private EntityTransaction entr = MailSigner.getEntr();
	private DefaultListModel signatureListModel;

	public RemoveSignaturesProcess(int index[], DefaultListModel signatureListModel) {
		this.index = index;
		this.signatureListModel = signatureListModel;
	}

	@Override
	protected Void doInBackground() {
		MainFrame.setToolTip("Deleting signature(s) from users");
		int deleteLength = index.length;
		for (int i = index.length - 1; i >= 0; i--) {
			Signature signature = (Signature) signatureListModel.get(index[i]);
			entr.begin();
			em.remove(signature);
			entr.commit();
			setProgress((deleteLength - i)  * 100 / deleteLength);
		}
		return null;
	}

	@Override
	protected void done() {
		MainFrame.clearToolTip();
//		MainFrame.getSignatureTab().loadSignaturesAction();
		MailSigner.getUserController().clearTables();
		MainFrame.setProgressBar(0);
		MainFrame.hideProgressBar();
		return;
	}

}