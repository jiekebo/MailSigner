package com.mailsigner.model.combobox;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import com.mailsigner.model.persistence.Field;

public class FieldComboBoxModel implements ComboBoxModel {

	private Object[] fieldsValueArray;
	private int selectedItem;

	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getElementAt(int i) {
		Field fieldElement = (Field) fieldsValueArray[i];
		return fieldElement.getLabel();
	}

	@Override
	public int getSize() {
		return fieldsValueArray.length;
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getSelectedItem() {
		Field selectedField = (Field) fieldsValueArray[selectedItem];
		return selectedField.getLabel();
	}

	@Override
	public void setSelectedItem(Object arg0) {
		String selectedFieldString = (String) arg0;
		for (int i = 0; i < fieldsValueArray.length; i++) {
			Field currentField = (Field) fieldsValueArray[i];
			if (currentField.getLabel().contentEquals(selectedFieldString))
				selectedItem = i;
		}
	}

	public void setFields(List<Field> fields) {
		fieldsValueArray = fields.toArray();
	}

	public Field getSelectedField() {
		return (Field) fieldsValueArray[selectedItem];
	}

}
