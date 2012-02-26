package com.mailsigner.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import com.hexidec.ekit.EkitCore;
import com.mailsigner.MailSigner;
import com.mailsigner.WordProcessor;
import com.mailsigner.control.FieldController;
import com.mailsigner.control.SignatureController;
import com.mailsigner.control.exception.NoSignatureSelectedException;
import com.mailsigner.control.exception.SignatureExistsException;
import com.mailsigner.control.exception.SignatureTitleEmptyException;
import com.mailsigner.model.combobox.FieldComboBoxModel;
import com.mailsigner.model.persistence.Field;
import com.mailsigner.model.persistence.Signature;

public class SignatureTab extends JPanel implements ActionListener,	ListSelectionListener, PropertyChangeListener, DocumentListener, MouseListener {
	
	private static final long serialVersionUID = -6654419643312041210L;
	private JButton addSignatureButton;
	private JButton removeSignatureButton;
	private JButton renameSignatureButton;
	private JButton saveSignatureButton;
	private JButton insertFieldButton;
	private JComboBox fieldComboBox;
	private JList signatureList;
	private JTabbedPane editorTabbedPane;
	private WordProcessor wordProcessor;
	private JEditorPane txtEditorPane;
	private JEditorPane descriptionEditorPane;
	private FieldComboBoxModel fieldComboBoxModel = new FieldComboBoxModel();
	private IconListRenderer iconListRenderer = new IconListRenderer();
	private EkitCore htmlEditor;
	private boolean documentsChanged;
	private boolean changeEditorContent;
	private int previousSelectedIndex;
	private Logger log = MailSigner.getLog();
	private SignatureController signatureController = MailSigner.getSignatureController();
	private FieldController fieldController = new FieldController();
	
	public static final String MAILSIGNER_TOOLBAR = "CT|CP|PS|SP|UN|RE|SP|FN|SP|UC|UM|SP|SR|*|IM|BL|IT|UD|SP|SP|AL|AC|AR|AJ|SP|UL|OL|SP|LK|SP|TI|TE|CE|RI|CI|RD|CD|*|ST|SP|FO";

	public SignatureTab() {
		prepareLayout();
		signatureList.setModel(signatureController.getListModel());
		signatureList.setCellRenderer(iconListRenderer);
		List<Field> fields = fieldController.loadFields();
		fieldComboBoxModel.setFields(fields);
		fieldComboBox.setModel(fieldComboBoxModel);
	}

	private void prepareLayout() {
		setLayout(new BorderLayout(0, 0));

		JToolBar signaturesToolbar = new JToolBar();
		add(signaturesToolbar, BorderLayout.NORTH);
		signaturesToolbar.setFloatable(false);

		addSignatureButton = new JButton("");
		addSignatureButton.setToolTipText("Add a new signature");
		addSignatureButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/blue-document--plus.png")));
		addSignatureButton.addActionListener(this);
		addSignatureButton.addMouseListener(this);
		signaturesToolbar.add(addSignatureButton);

		removeSignatureButton = new JButton("");
		removeSignatureButton.setToolTipText("Remove selected signature(s)");
		removeSignatureButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/blue-document--minus.png")));
		removeSignatureButton.addActionListener(this);
		removeSignatureButton.addMouseListener(this);
		signaturesToolbar.add(removeSignatureButton);

		renameSignatureButton = new JButton("");
		renameSignatureButton.setToolTipText("Rename selected signature");
		renameSignatureButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/ui-text-field-select.png")));
		renameSignatureButton.addActionListener(this);
		renameSignatureButton.addMouseListener(this);
		signaturesToolbar.add(renameSignatureButton);

		saveSignatureButton = new JButton("");
		saveSignatureButton.setToolTipText("Save selected signature");
		saveSignatureButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/096.png")));
		saveSignatureButton.addActionListener(this);
		saveSignatureButton.addMouseListener(this);
		signaturesToolbar.add(saveSignatureButton);

		signaturesToolbar.addSeparator();

		JLabel lblNewLabel = new JLabel("User detail field:");
		signaturesToolbar.add(lblNewLabel);

		fieldComboBox = new JComboBox();
		fieldComboBox.addMouseListener(this);
		signaturesToolbar.add(fieldComboBox);

		insertFieldButton = new JButton("");
		insertFieldButton.setToolTipText("Insert selected field");
		insertFieldButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/103.png")));
		insertFieldButton.addActionListener(this);
		insertFieldButton.addMouseListener(this);
		signaturesToolbar.add(insertFieldButton);

		JSplitPane signaturesSplitPane = new JSplitPane();
		add(signaturesSplitPane);

		editorTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		signaturesSplitPane.setRightComponent(editorTabbedPane);

		JPanel htmlPanel = new JPanel();
		editorTabbedPane.addTab("HTML Version",	new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/089.png")),	htmlPanel, null);
		
		htmlEditor = new EkitCore(false, null, null, true, false, true, true, null, null, false, false, false, MAILSIGNER_TOOLBAR, true);
		htmlPanel.setLayout(new BorderLayout(0, 0));
		JToolBar htmlEditorToolbar = htmlEditor.getToolBar(true);
		htmlEditorToolbar.setLayout(new ModifiedFlowLayout());
		htmlPanel.add(htmlEditorToolbar, BorderLayout.NORTH);
//		JToolBar toolbars = new JToolBar();
//		toolbars.setOrientation(SwingConstants.VERTICAL);
//		toolbars.setFloatable(false);
//		FlowLayout fl_toolbars = new FlowLayout();
//		fl_toolbars.setAlignment(FlowLayout.LEFT);
//		toolbars.setLayout(fl_toolbars);		
//		htmlPanel.add(htmlEditor.getMenuBar(), BorderLayout.NORTH);
//		htmlPanel.add(htmlEditor.getToolBarFormat(true), BorderLayout.NORTH);
//		toolbars.add(htmlEditor.getToolBarMain(true));
//		toolbars.add(htmlEditor.getToolBarStyles(true));
//		htmlPanel.add(toolbars, BorderLayout.NORTH);
		htmlPanel.add(htmlEditor);

		JPanel rtfPanel = new JPanel();
		editorTabbedPane.addTab("RTF Version", new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/022.png")), rtfPanel, null);
		rtfPanel.setLayout(new BorderLayout(0, 0));

		wordProcessor = new WordProcessor();
		rtfPanel.add(wordProcessor, BorderLayout.CENTER);

		JPanel txtPanel = new JPanel();
		editorTabbedPane.addTab("Text Version",	new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/168.png")),	txtPanel, null);
		txtPanel.setLayout(new BorderLayout(0, 0));

		txtEditorPane = new JEditorPane();
		txtPanel.add(txtEditorPane, BorderLayout.CENTER);

		JPanel descriptionPanel = new JPanel();
		editorTabbedPane.addTab("Description", new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/059.png")), descriptionPanel, null);
		descriptionPanel.setLayout(new BorderLayout(0, 0));

		descriptionEditorPane = new JEditorPane();
		descriptionPanel.add(descriptionEditorPane, BorderLayout.CENTER);

		JScrollPane signatureListScrollPane = new JScrollPane();
		signaturesSplitPane.setLeftComponent(signatureListScrollPane);

		signatureList = new JList();
		signatureList.addListSelectionListener(this);
		signatureListScrollPane.setViewportView(signatureList);
		signaturesSplitPane.setDividerLocation(200);
		
		// Add document listeners
		htmlEditor.getDocument().addDocumentListener(this);
		wordProcessor.getDocument().addDocumentListener(this);
		txtEditorPane.getDocument().addDocumentListener(this);
		descriptionEditorPane.getDocument().addDocumentListener(this);
	}

	/**
	 * Clears the contents of all editors
	 */
	public void clearEditors() {
		htmlEditor.setDocumentText("");
		wordProcessor.clear();
		txtEditorPane.setText("");
		descriptionEditorPane.setText("");
	}

	/**
	 * Launches a thread which adds a signature.
	 */
	public void addSignatureAction() {
		String title = JOptionPane.showInputDialog(this, "Write a unique title for the signature", "Add new signature",	JOptionPane.QUESTION_MESSAGE);
		try {
			signatureController.addSignature(title);
		} catch (SignatureTitleEmptyException e) {
			JOptionPane.showMessageDialog(this, "Signature name cannot be empty");
		} catch (SignatureExistsException e) {
			JOptionPane.showMessageDialog(this, "Signature \"" + title + "\" exists", "Signature exists", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Launches a thread to remove selected signatures.
	 */
	public void removeSignatureAction() {
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to remove selected signature(s)?", "Remove signature", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.DEFAULT_OPTION) {
			return;
		}
		
		int[] index = signatureList.getSelectedIndices();
		signatureController.removeSignatures(index);
	}

	/**
	 * Saves the currently selected signature, or the previously selected signature.
	 * @param savePrevious Determines whether to save previously selected signature.
	 */
	public void saveSignatureAction(boolean savePrevious) {
		int index = -1;
		if(savePrevious) {
			index = previousSelectedIndex;
		} else {
			index = signatureList.getSelectedIndex();
		}
		if (index == -1) {
			return;
		}
		byte[] html = htmlEditor.getDocumentText().getBytes();
		byte[] rtf = wordProcessor.write();
		byte[] txt = txtEditorPane.getText().getBytes();
		byte[] desc = descriptionEditorPane.getText().getBytes();
		signatureController.saveSignature(index, html, rtf, txt, desc);
		documentsChanged = false;
	}
	
	/**
	 * Action to rename the signature from the dialog.
	 */
	public void renameSignatureAction() {
		int index = signatureList.getSelectedIndex();
		String title = null;
		try {
			Signature signature = signatureController.getSignature(index);
			title = (String) JOptionPane.showInputDialog(this, "Enter a new title", "Rename signature", JOptionPane.QUESTION_MESSAGE, null, null, new String(signature.getTitle()));
		} catch (NoSignatureSelectedException e1) {
			JOptionPane.showMessageDialog(this, "Select a signature to rename", "No signature selected", JOptionPane.WARNING_MESSAGE);
		}

		try {
			signatureController.renameSignature(index, title);
		} catch (NoSignatureSelectedException e) {
			JOptionPane.showMessageDialog(this, "Select a signature to rename", "No signature selected", JOptionPane.WARNING_MESSAGE);
		} catch (SignatureTitleEmptyException e) {
			JOptionPane.showMessageDialog(this, "Signature title cannot be empty", "No title", JOptionPane.WARNING_MESSAGE);
		} catch (SignatureExistsException e) {
			JOptionPane.showMessageDialog(this, "Signature \"" + title + "\" exists", "Signature exists", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Action to insert a field in the editor panes.
	 */
	public void insertFieldAction() {
		Field selectedField = fieldComboBoxModel.getSelectedField();
		if (signatureList.getSelectedIndex() < 0) {
			return;
		}
			
		switch (editorTabbedPane.getSelectedIndex()) {
			case 0:
				try {
					htmlEditor.insertUnicodeChar(selectedField.getCode());
					htmlEditor.setCaretPosition(htmlEditor.getCaretPosition() + selectedField.getCode().length() - 1);
				} catch (IOException e) {
					log.error("IO Exception", e);
				} catch (BadLocationException e) {
					log.error("Bad caret location", e);
				} catch (RuntimeException e) {
					log.error("Runtime exception", e);
				}
				break;
			case 1:
				wordProcessor.insertField(selectedField.getCode());
				break;
			case 2:
				txtEditorPane.replaceSelection(selectedField.getCode());
				break;
			case 3:
				descriptionEditorPane.replaceSelection(selectedField.getCode());
				break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addSignatureButton) {
			addSignatureAction();
		}

		if (e.getSource() == removeSignatureButton) {
			removeSignatureAction();
		}

		if (e.getSource() == renameSignatureButton) {
			renameSignatureAction();
		}

		if (e.getSource() == saveSignatureButton) {
			saveSignatureAction(false);
		}

		if (e.getSource() == insertFieldButton) {
			insertFieldAction();
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(documentsChanged) {
			int result = JOptionPane.showConfirmDialog(MailSigner.getMainFrame(), "Do you wish to save your changes?");
			if(result == JOptionPane.YES_OPTION) {
				saveSignatureAction(true);
			}
		}
		changeEditorContent = true;
		if (e.getSource() == signatureList) {
			clearEditors();

			int selectedIndex = -1;
			if ((selectedIndex = signatureList.getSelectedIndex()) != -1) {
				Signature signature = null;
				try {
					signature = signatureController.getSignature(selectedIndex);
				} catch (NoSignatureSelectedException e1) {
					log.debug("No signature on value changed event");
				}
				
				if(signature.getHtml() != null) {
					htmlEditor.setDocumentText(new String(signature.getHtml()));
				}
				
				if(signature.getRtf() != null) {
					wordProcessor.read(signature.getRtf());					
				}
				
				if(signature.getTxt() != null) {
					txtEditorPane.setText(new String(signature.getTxt()));
				}
				
				if(signature.getDescription() != null) {
					descriptionEditorPane.setText(new String(signature.getDescription()));
				}
				previousSelectedIndex = selectedIndex;
			} else {
				clearEditors();
			}
			changeEditorContent = false;
			documentsChanged = false;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if ("progress" == e.getPropertyName()) {
			MainFrame.showProgressBar();
			int progress = (Integer) e.getNewValue();
			MainFrame.setProgressBar(progress);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		if(signatureList.getSelectedIndex() < 0 || changeEditorContent) {
			return;
		}
		documentsChanged = true;
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == addSignatureButton) {
			MainFrame.setToolTip("Add a new signature");
		}
		if(e.getSource() == removeSignatureButton) {
			MainFrame.setToolTip("Remove selected signature(s)");
		}
		if(e.getSource() == renameSignatureButton) {
			MainFrame.setToolTip("Rename selected signature");
		}
		if(e.getSource() == saveSignatureButton) {
			MainFrame.setToolTip("Save selected signature");
		}
		if(e.getSource() == insertFieldButton) {
			MainFrame.setToolTip("Insert selected field into active editor");
		}
		if(e.getSource() == fieldComboBox) {
			MainFrame.setToolTip("Choose field to be inserted, which will be populated with user details");
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		MainFrame.clearToolTip();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Auto-generated method stub
	}
	
	public SignatureController getSignatureController() {
		return signatureController;
	}

}
