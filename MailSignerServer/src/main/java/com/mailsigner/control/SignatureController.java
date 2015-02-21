package com.mailsigner.control;

import com.mailsigner.MailSigner;
import com.mailsigner.control.exception.NoSignatureSelectedException;
import com.mailsigner.control.exception.SignatureExistsException;
import com.mailsigner.control.exception.SignatureNotFoundException;
import com.mailsigner.control.exception.SignatureTitleEmptyException;
import com.mailsigner.model.SignatureModel;
import com.mailsigner.model.list.SortedListModel;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.Usersignature;

public class SignatureController {
	private SignatureModel signatureModel = new SignatureModel();	
	
	public SignatureController() {}
	
	public void addSignature(String title) throws SignatureTitleEmptyException, SignatureExistsException {
		if (isExistingSignature(title)) {
			throw new SignatureExistsException();
		}
		if (title == null || title.length() <= 0) {
			throw new SignatureTitleEmptyException();
		}
		signatureModel.addSignature(title);
	}
	
	public SortedListModel getListModel() {
		signatureModel.updateListModel();
		return signatureModel.getListModel();
	}
	
	/**
	 * Get a signature designated by it's index in the results array.
	 * @param index Index of the signature.
	 * @return Signature object.
	 * @throws NoSignatureSelectedException 
	 */
	public Signature getSignature(int index) throws NoSignatureSelectedException {
		if(index < 0) {
			throw new NoSignatureSelectedException();
		}
		return signatureModel.getSignature(index);
	}
	
	
	public boolean isExistingSignature(String title) {
		return signatureModel.isExistingSignature(title);
	}
	
	public Signature findSignature(String title) throws SignatureNotFoundException {
		return signatureModel.findSignature(title);
	}
	
	public void removeSignatures(int[] index) {
		if (index == null || index.length <= 0) {
			return;
		}
		for (int i = index.length - 1; i >= 0; i--) {
			signatureModel.removeSignature(index[i]);
		}
	}
	
	/**
	 * Renames a signature in the database
	 * @param signature Signature to be renamed
	 * @param newTitle New title of the signature
	 * @throws NoSignatureSelectedException 
	 * @throws SignatureTitleEmptyException 
	 * @throws SignatureExistsException 
	 */
	public void renameSignature(int index, String title) throws NoSignatureSelectedException, SignatureTitleEmptyException, SignatureExistsException {
		if(index == -1) {
			throw new NoSignatureSelectedException();
		}
		if(title == null || title.length() <= 0) {
			throw new SignatureTitleEmptyException();
		}
		if(isExistingSignature(title)) {
			throw new SignatureExistsException();
		}
		signatureModel.renameSignature(index, title);
		MailSigner.getUserController().clearTables();
	}
	
	/**
	 * Save a signature
	 * @param signature Signature to be saved
	 * @param html Byte array with html content
	 * @param rtf Byte array with rtf content
	 * @param txt Byte array with txt content
	 * @param desc Byte array with description
	 */
	public void saveSignature(int index, byte[] html, byte[] rtf, byte[] txt, byte[] desc) {
		signatureModel.saveSignature(index, html, rtf, txt, desc);
	}

	/**
	 * Toggle the signatures state.
	 * @param signature Signature to be toggled.
	 */
	public void toggleSignature(Usersignature signature) {
		byte setting;
		if(!signature.isEnabled()) {
			setting = 1;			
		} else {
			setting = 0;
		}
		signatureModel.toggleSignature(signature, setting);
	}
}
