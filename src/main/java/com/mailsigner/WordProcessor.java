package com.mailsigner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.rtf.RTFEditorKit;

public class WordProcessor extends JPanel implements ActionListener, CaretListener, FocusListener {
	private static final long serialVersionUID = 1L;
	protected boolean skipUpdate;
	protected int xStart = -1;
	protected int xFinish = -1;
	private JTextPane monitor;
	private StyleContext context;
	private DefaultStyledDocument doc;
	private RTFEditorKit kit;
	private JToolBar toolBar;
	private JComboBox fontComboBox;
	private JComboBox fontSizeComboBox;
	private JToggleButton tglbtnBold;
	private JToggleButton tglbtnItalic;
	private JToggleButton tglbtnUnderline;
	private ColorButton btnTextColor;
	private ColorButton btnBackgroundColor;
	private JLabel lblTextColor;
	private JLabel lblTextBackground;
	
	// Attributes
	private int fontSize = 0;
	private String fontName = "";
	private Boolean bold = false;
	private Boolean italic = false;
	private Boolean underline = false;
	private Color foregroundColor;
	private Color backgroundColor;
	private Properties properties;

	public WordProcessor() {
		// Load properties file
		try {
			properties = new Properties();
			properties.load(getClass().getResourceAsStream("/wordprocessor.properties"));
		} catch (IOException e) {
			System.out.println("Properties file could not be loaded from path");
		}
		if(underline){}
		if(italic){}
		createMonitor();
		createMenuBar();
		showAttributes(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		WordProcessor wordProcessor = new WordProcessor();
		frame.add(wordProcessor);
		frame.setSize(new Dimension(800, 600));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createMonitor() {
		this.setLayout(new BorderLayout());
		monitor = new JTextPane();
		kit = new RTFEditorKit();
		monitor.setEditorKit(kit);
		context = new StyleContext();
		doc = new DefaultStyledDocument(context);
		monitor.setDocument(doc);
		monitor.addCaretListener(this);
		monitor.addFocusListener(this);
		JScrollPane ps = new JScrollPane(monitor);
		this.add(ps, BorderLayout.CENTER);
	}

	private void createMenuBar() {
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		this.add(toolBar, BorderLayout.NORTH);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();

		fontComboBox = new JComboBox(fontNames);
		fontComboBox.setMaximumSize(fontComboBox.getPreferredSize());
		fontComboBox.setEditable(true);
		fontComboBox.addActionListener(this);
		toolBar.add(fontComboBox);

		fontSizeComboBox = new JComboBox(new String[] { "8", "9", "10", "11",
				"12", "14", "16", "18", "20", "22", "24", "26", "28", "36",
				"48", "72" });
		fontSizeComboBox.setMaximumSize(fontSizeComboBox.getPreferredSize());
		fontSizeComboBox.setEditable(true);
		fontSizeComboBox.addActionListener(this);
		toolBar.add(fontSizeComboBox);

		tglbtnBold = new JToggleButton("");
		
		tglbtnBold.setIcon(getImageIcon("boldIcon"));
		tglbtnBold.addActionListener(this);
		toolBar.add(tglbtnBold);

		tglbtnItalic = new JToggleButton("");
		tglbtnItalic.setIcon(getImageIcon("italicIcon"));
		tglbtnItalic.addActionListener(this);
		toolBar.add(tglbtnItalic);

		tglbtnUnderline = new JToggleButton("");
		tglbtnUnderline.setIcon(getImageIcon("underlineIcon"));
		tglbtnUnderline.addActionListener(this);
		toolBar.add(tglbtnUnderline);
		
		btnTextColor = new ColorButton();
		btnTextColor.setMaximumSize(new Dimension(25, 25));
		btnTextColor.addActionListener(this);
		
		lblTextColor = new JLabel("Color: ");
		toolBar.add(lblTextColor);
		toolBar.add(btnTextColor);
		
		btnBackgroundColor = new ColorButton();
		btnBackgroundColor.setMaximumSize(new Dimension(25,25));
		btnBackgroundColor.addActionListener(this);
		
		lblTextBackground = new JLabel("Background: ");
		toolBar.add(lblTextBackground);
		toolBar.add(btnBackgroundColor);
	}

	/**
	 * Update controls with selections attributes
	 * @param p Caret position
	 */
	private void showAttributes(int p) {
		if(p != 0)
			p--;
		skipUpdate = true;
		AttributeSet a = doc.getCharacterElement(p).getAttributes();
		String name = StyleConstants.getFontFamily(a);
		if (!fontName.equals(name)) {
			fontName = name;
			fontComboBox.setSelectedItem(name);
		}
		int size = StyleConstants.getFontSize(a);
		if (fontSize != size) {
			fontSize = size;
			fontSizeComboBox.setSelectedItem(Integer.toString(fontSize));
		}
		boolean selectionBold = StyleConstants.isBold(a);
		if (selectionBold != tglbtnBold.isSelected())
			tglbtnBold.setSelected(selectionBold);
		if(bold) {
			tglbtnBold.setSelected(bold);
			bold = false;
		}
		boolean selectionItalic = StyleConstants.isItalic(a);
		if (selectionItalic != tglbtnItalic.isSelected())
			tglbtnItalic.setSelected(selectionItalic);
		boolean selectionUnderline = StyleConstants.isUnderline(a);
		if(selectionUnderline != tglbtnUnderline.isSelected())
			tglbtnUnderline.setSelected(selectionUnderline);
		skipUpdate = false;
		foregroundColor = StyleConstants.getForeground(a);
		btnTextColor.setColor(foregroundColor);
		backgroundColor = StyleConstants.getBackground(a);
		btnBackgroundColor.setColor(backgroundColor);
	}

	private void setAttributeSet(AttributeSet attr) {
		if (skipUpdate)
			return;
		int xStart = monitor.getSelectionStart();
		int xFinish = monitor.getSelectionEnd();
		if (!monitor.hasFocus()) {
			xStart = this.xStart;
			xFinish = this.xFinish;
		}
		if (xStart != xFinish) {
			doc.setCharacterAttributes(xStart, xFinish - xStart, attr, false);
		} else {
			MutableAttributeSet inputAttributes = kit.getInputAttributes();
			inputAttributes.addAttributes(attr);
		}
	}

	public void insertField(String field) {
		monitor.replaceSelection(field);
	}

	public void clear() {
		monitor.setText("");
	}
	
	public void read(byte[] input) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(input);
//			StringReader in = new StringReader(input);
//			m_doc = new DefaultStyledDocument(m_context);
			monitor.setText("");
			kit.read(in, doc, 0);
			monitor.setDocument(doc);
			in.close();
			monitor.setCaretPosition(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public byte[] write() {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			kit.write(out, doc, 0, doc.getLength());
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out.toByteArray();
	}

	public Document getDocument() {
		return monitor.getDocument();
	}

	public void setFont() {
		fontName = fontComboBox.getSelectedItem().toString();
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, fontName);
		setAttributeSet(attr);
		monitor.grabFocus();
	}

	public void setFontSize() {
		int selectedFontSize = 0;
		try {
			selectedFontSize = Integer.parseInt(fontSizeComboBox.getSelectedItem().toString());
		} catch (NumberFormatException ex) {
			return;
		}
		fontSize = selectedFontSize;
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontSize(attr, selectedFontSize);
		setAttributeSet(attr);
		monitor.grabFocus();
	}

	public void toggleBold() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBold(attr, tglbtnBold.isSelected());
		setAttributeSet(attr);
		bold = true;
		monitor.grabFocus();
	}

	public void toggleItalic() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setItalic(attr, tglbtnItalic.isSelected());
		setAttributeSet(attr);
		italic = true;
		monitor.grabFocus();
	}
	
	public void toggleUnderline() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setUnderline(attr, tglbtnUnderline.isSelected());
		setAttributeSet(attr);
		underline = true;
		monitor.grabFocus();
	}
	
	public void setColor(Color color) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setForeground(attr, color);
		setAttributeSet(attr);
		monitor.grabFocus();
	}
	
	public void setTextBackground(Color color) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBackground(attr, color);
		setAttributeSet(attr);
		monitor.grabFocus();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fontComboBox) {
			setFont();
		}
		if (e.getSource() == fontSizeComboBox) {
			setFontSize();
		}
		if (e.getSource() == tglbtnBold) {
			toggleBold();
		}
		if (e.getSource() == tglbtnItalic) {
			toggleItalic();
		}
		if(e.getSource() == tglbtnUnderline) {
			toggleUnderline();
		}
		if(e.getSource() == btnTextColor) {
			Color newColor = JColorChooser.showDialog(this, "Choose color for text", foregroundColor);
			if(newColor != null)
				setColor(newColor);
		}
		if(e.getSource() == btnBackgroundColor) {
			Color newColor = JColorChooser.showDialog(this, "Choose text background", backgroundColor);
			if(newColor != null)
				setTextBackground(newColor);
		}
	}

	public void focusGained(FocusEvent e) {
		if (e.getSource() == monitor) {
			if (xStart >= 0 && xFinish >= 0)
				if (monitor.getCaretPosition() == xStart) {
					monitor.setCaretPosition(xFinish);
					monitor.moveCaretPosition(xStart);
				} else
					monitor.select(xStart, xFinish);
		}
	}

	public void focusLost(FocusEvent e) {
		if (e.getSource() == monitor) {
			xStart = monitor.getSelectionStart();
			xFinish = monitor.getSelectionEnd();
		}
	}

	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == monitor)
			showAttributes(e.getDot());
	}
	
	private ImageIcon getImageIcon(String property) {
		return new ImageIcon(
			Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(properties.getProperty(property))
			)
		);
	}
}