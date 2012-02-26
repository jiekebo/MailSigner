package com.mailsigner.model.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.mailsigner.model.ActiveDirectoryUserModel;

public class AdUserTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 9026487116152558086L;
	private static final String[] COLUMN_NAMES = {"Select", "Login", "First name", "Last name"};
	private List<ActiveDirectoryUserModel> adUsers;
	private List<Integer> selection;

	public AdUserTableModel() {}
	
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		if(adUsers != null)
			return adUsers.size();
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0: return selection.contains(rowIndex);
			case 1: return adUsers.get(rowIndex).getLogon();
			case 2: return adUsers.get(rowIndex).getFirstName();
			case 3: return adUsers.get(rowIndex).getLastName();
			default: return new String("");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Integer index = new Integer(rowIndex);
		if(selection.contains(index)){
			selection.remove(index);
			return;
		}
		selection.add(index);
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
		default: return String.class;
		}
	}
	
	public void setAdUsers(List<ActiveDirectoryUserModel> adUsers) {
		this.adUsers = adUsers;
		selection = new ArrayList<Integer>();
	}
	
	public List<Integer> getSelection() {
		return selection;
	}
}
