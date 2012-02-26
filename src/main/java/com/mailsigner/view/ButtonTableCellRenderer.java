package com.mailsigner.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class ButtonTableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null)
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		JButton label = new JButton(value.toString());
		// label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		label.setBorderPainted(false);
		label.setBackground(new Color(238, 238, 238));
		label.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
	}
}
