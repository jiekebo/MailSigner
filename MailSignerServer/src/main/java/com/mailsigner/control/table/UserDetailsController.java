package com.mailsigner.control.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.mailsigner.model.UserModel;
import com.mailsigner.model.comparator.FieldComparator;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Userfield;


public class UserDetailsController extends AbstractTableModel {

	private static final long serialVersionUID = 2949567480865957841L;
	private static final String[] COLUMN_NAMES = {"", "Value (double click to change)"};
	private FieldComparator fieldComparator = new FieldComparator();
	private List<Userfield> list;
	private UserModel userModel;

	public UserDetailsController(UserModel userModel) {
		this.userModel = userModel;
	}

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
		Userfield detailType = list.get(rowIndex);
		switch(columnIndex){
		case 0: return detailType.getField().getLabel();
		case 1: return detailType.getValue();
		default: return new String("");
		}
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Userfield userField = list.get(rowIndex);
		userModel.updateUserField(userField, (String) value); 
		this.fireTableDataChanged();
	}
	
	public void setUser(User user) {
		Set<Userfield> userFields = user.getUserfields(); 
		list = new ArrayList<Userfield>();
		list.addAll(userFields);
		Collections.sort(list, fieldComparator);
		this.fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 1);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
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
