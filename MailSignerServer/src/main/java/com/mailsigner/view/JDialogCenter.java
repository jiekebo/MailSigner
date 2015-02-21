package com.mailsigner.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class JDialogCenter extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4495673165927963222L;

	JDialogCenter(JFrame parent) {
		super(parent);
	}

	/**
	 * Method that when called places a window in the center of the screen
	 */
	public void centerWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		setLocation(x, y);
	}

	@Override
	public void setVisible(boolean b) {
		centerWindow();
		super.setVisible(b);
	}
}
