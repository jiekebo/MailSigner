package com.mailsigner.control.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.User;


public class UserComputersTableController extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9186221960255730825L;
	private static final String[] COLUMN_NAMES = {"Computer", "Sync"};
	private List<Computer> list;

	public UserComputersTableController() {}
	
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
		Computer detailRow = list.get(rowIndex);
		byte disabled = 0;
		switch(columnIndex){
			case 0: return detailRow.getLabel();
			case 1: return (detailRow.getDeployed() == disabled)?false:true;
			default: return new String("");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		super.setValueAt(aValue, rowIndex, columnIndex);
	}
	
	public void setUser(User user) {
		Set<Computer> userComputers = user.getComputers();
		list = new ArrayList<Computer>();
		list.addAll(userComputers);
		//TODO: sorting
		this.fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case 0: return String.class;
		case 1: return Boolean.class;
		default: return null;
		}
		//return super.getColumnClass(columnIndex);
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
