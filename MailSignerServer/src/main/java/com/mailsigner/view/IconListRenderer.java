package com.mailsigner.view;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;

public class IconListRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ImageIcon adUserIcon = new ImageIcon(getClass().getResource(
			"/com/mailsigner/assets/icons/user-business.png"));
	private ImageIcon nonAdUserIcon = new ImageIcon(getClass().getResource(
			"/com/mailsigner/assets/icons/user.png"));
	private ImageIcon premSigIcon = new ImageIcon(getClass().getResource(
			"/com/mailsigner/assets/icons/154.png"));
	private ImageIcon nonPremSigIcon = new ImageIcon(getClass().getResource(
			"/com/mailsigner/assets/icons/160.png"));

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);

		if (value instanceof Signature) {
			Signature signature = (Signature) value;
			if(signature.getPremium() == 0) {
				setIcon(nonPremSigIcon);				
			} else {
				setIcon(premSigIcon);
			}
			setText(signature.getTitle());
		}

		if (value instanceof User) {
			User user = (User) value;
			if (user.getActivedirectory() == 0) {
				setIcon(nonAdUserIcon);
			} else {
				setIcon(adUserIcon);
			}
			setText(user.getLogon());
		}

		return this;
	}
}
