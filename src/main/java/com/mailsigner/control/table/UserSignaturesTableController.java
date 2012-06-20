package com.mailsigner.control.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.mailsigner.MailSigner;
import com.mailsigner.control.ComputerController;
import com.mailsigner.control.SignatureController;
import com.mailsigner.model.comparator.UsersignatureComparator;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Usersignature;


public class UserSignaturesTableController extends AbstractTableModel {
	
	private static final long serialVersionUID = 9186221960255730825L;
	private static final String[] COLUMN_NAMES = {"Use", "Signature"};
	private UsersignatureComparator signatureComparator = new UsersignatureComparator();
	private ComputerController computerController = MailSigner.getComputerController();
	private SignatureController signatureController = MailSigner.getSignatureController();
	private List<Usersignature> list;
	private User user;

	public UserSignaturesTableController() {}
	
	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		if(list != null)
			return list.size();
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Usersignature signature = list.get(rowIndex);
		switch(columnIndex) {
			case 0: return (!signature.isEnabled())?false:true;
			case 1: return signature.getSignature().getTitle();
			default: return new String("");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		signatureController.toggleSignature(list.get(rowIndex));
		computerController.setUndeployed(user);
		this.fireTableDataChanged();
	}
	
	public void setUser(User user) {
		this.user = user;
		Set<Usersignature> userSignatures = user.getUsersignatures(); 
		list = new ArrayList<Usersignature>();
		list.addAll(userSignatures);
		Collections.sort(list, signatureComparator);
		this.fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 0);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
			case 0: return Boolean.class;
			case 1: return String.class;
			default: return null;
		}
	}
	
	public void deleteData() {
		int rows = getRowCount();
		if (rows == 0) {
			return;
		}
		list.clear();
		fireTableRowsDeleted(0, rows - 1);
	}
}
