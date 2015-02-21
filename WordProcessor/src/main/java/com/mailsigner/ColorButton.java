package com.mailsigner;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class ColorButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Color color = Color.black;

	public ColorButton() {
		super();
		setContentAreaFilled(false);
	}
	
	public void setColor(Color color) {
		this.color = color;
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, getSize().width-1, getSize().height-1);
		super.paintComponent(g);
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
	    g.drawRect(0, 0, getSize().width-1,getSize().height-1);
	}

}
