package com.mailsigner.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mailsigner.MailSigner;
import com.mailsigner.control.UserController;
import com.mailsigner.control.exception.NoUserSelectedException;
import com.mailsigner.control.exception.UserExistsException;
import com.mailsigner.control.exception.UserlimitReachedException;
import com.mailsigner.control.exception.UsernameEmptyException;

public class UserTab extends JPanel implements ActionListener, ListSelectionListener, PropertyChangeListener, MouseListener {
	
	private static final long serialVersionUID = 848808761490438011L;
	private JButton getAdUsersButton;
	private JButton refreshAdUsersButton;
	private JButton addUserButton;
	private JButton removeUserButton;
	private JButton renameUserButton;
	private JList userList;
	private JTable userDetailsTable;
	private JTable userSignaturesTable;
	private JTable userStationsTable;
	private IconListRenderer iconListRenderer = new IconListRenderer();
	private UserController userController = MailSigner.getUserController();
	
	public UserTab() {		
		prepareLayout();
		userList.setModel(userController.updateModel());
		userList.setCellRenderer(iconListRenderer);
	}
	
	public void prepareLayout() {
		setLayout(new BorderLayout(0, 0));
		
		JToolBar usersToolBar = new JToolBar();
		usersToolBar.setFloatable(false);
		add(usersToolBar, BorderLayout.NORTH);
		
		getAdUsersButton = new JButton("");
		getAdUsersButton.setToolTipText("Check for new ad-users");
		getAdUsersButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/147.png")));
		getAdUsersButton.addActionListener(this);
		getAdUsersButton.addMouseListener(this);
		usersToolBar.add(getAdUsersButton);
		
		refreshAdUsersButton = new JButton("");
		refreshAdUsersButton.setToolTipText("Refresh selected user's details (only for ad-users)");
		refreshAdUsersButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/122.png")));
		refreshAdUsersButton.addActionListener(this);
		refreshAdUsersButton.addMouseListener(this);
		usersToolBar.add(refreshAdUsersButton);
		
		usersToolBar.addSeparator();
		
		addUserButton = new JButton("");
		addUserButton.setToolTipText("Add new user");
		addUserButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/user--plus.png")));
		addUserButton.addActionListener(this);
		addUserButton.addMouseListener(this);
		usersToolBar.add(addUserButton);
		
		removeUserButton = new JButton("");
		removeUserButton.setToolTipText("Remove selected user(s)");
		removeUserButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/user--minus.png")));
		removeUserButton.addActionListener(this);
		removeUserButton.addMouseListener(this);
		usersToolBar.add(removeUserButton);
		
		renameUserButton = new JButton("");
		renameUserButton.setToolTipText("Rename selected user");
		renameUserButton.setIcon(new ImageIcon(MainFrame.class.getResource("/com/mailsigner/assets/icons/ui-text-field-select.png")));
		renameUserButton.addActionListener(this);
		renameUserButton.addMouseListener(this);
		usersToolBar.add(renameUserButton);
		
		JSplitPane usersSplitPane = new JSplitPane();
		add(usersSplitPane, BorderLayout.CENTER);
		
		JSplitPane userSplitPane = new JSplitPane();
		userSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		usersSplitPane.setRightComponent(userSplitPane);
		
		JPanel userDetailsPanel = new JPanel();
		userSplitPane.setLeftComponent(userDetailsPanel);
		userDetailsPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane userDetailsTableScrollPane = new JScrollPane();
		userDetailsPanel.add(userDetailsTableScrollPane, BorderLayout.CENTER);
		
		userDetailsTable = new JTable();
		userDetailsTable.setModel(userController.getUserDetailsTableController());
		userDetailsTable.getColumnModel().getColumn(0).setMinWidth(120);
		userDetailsTable.getColumnModel().getColumn(0).setMaxWidth(120);
		ButtonTableCellRenderer buttonTableCellRenderer = new ButtonTableCellRenderer();
		userDetailsTable.getColumnModel().getColumn(0).setCellRenderer(buttonTableCellRenderer);
		userDetailsTableScrollPane.setViewportView(userDetailsTable);
		
		
		JPanel userSettingsPanel = new JPanel();
		userSplitPane.setRightComponent(userSettingsPanel);
		
		JScrollPane userSignaturesScrollPane = new JScrollPane();
		
		userSignaturesTable = new JTable();
		userSignaturesTable.setModel(userController.getUserSignaturesTableController());
		userSignaturesTable.getColumnModel().getColumn(0).setMaxWidth(30);
		userSignaturesScrollPane.setViewportView(userSignaturesTable);
		
		JScrollPane userStationsScrollPane = new JScrollPane();
		
		userStationsTable = new JTable();
		userStationsScrollPane.setViewportView(userStationsTable);
		userStationsTable.setModel(userController.getUserComputersTableController());
		GroupLayout gl_userSettingsPanel = new GroupLayout(userSettingsPanel);
		gl_userSettingsPanel.setHorizontalGroup(
			gl_userSettingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_userSettingsPanel.createSequentialGroup()
					.addComponent(userSignaturesScrollPane, GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userStationsScrollPane, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE))
		);
		gl_userSettingsPanel.setVerticalGroup(
			gl_userSettingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_userSettingsPanel.createSequentialGroup()
					.addGroup(gl_userSettingsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(userSignaturesScrollPane)
						.addComponent(userStationsScrollPane, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
					.addGap(0))
		);
		userSettingsPanel.setLayout(gl_userSettingsPanel);
		userSplitPane.setDividerLocation(300);
		
		JScrollPane userTableScrollPane = new JScrollPane();
		usersSplitPane.setLeftComponent(userTableScrollPane);
		
		userList = new JList();
		userList.addListSelectionListener(this);
		userTableScrollPane.setViewportView(userList);
		usersSplitPane.setDividerLocation(200);
	}
	
	public void updateButtons() {
		if(!userList.isSelectionEmpty()) {
			if(userController.isAdUser()) {
				refreshAdUsersButton.setEnabled(false);
				renameUserButton.setEnabled(true);
			} else {
				refreshAdUsersButton.setEnabled(true);
				renameUserButton.setEnabled(false);
			}
		}
	}
	
	public void addAdUsers() {
		//TODO: get ad controller
	}
	
	public void refreshAdUser() {
		//TODO: get ad controller
	}
	
	/**
	 * 
	 */
	public void addUser() {
		String newUserName = JOptionPane.showInputDialog(this, "Write a unique name, corresponding with user login", "Add a new user", JOptionPane.QUESTION_MESSAGE);
		try {
			userController.addUser(newUserName);
		} catch (UserlimitReachedException e) {
			JOptionPane.showMessageDialog(this, "User limit reached", "User limit reached", JOptionPane.WARNING_MESSAGE);
		} catch (UserExistsException e) {
			JOptionPane.showMessageDialog(this, "User \"" + newUserName + "\" exists", "User exists", JOptionPane.WARNING_MESSAGE);
		} catch (UsernameEmptyException e) {
			JOptionPane.showMessageDialog(this, "Username cannot be empty");
		}
	}
	
	/**
	 * 
	 */
	public void removeUser() {
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to remove selected user(s)?", "Remove user", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.NO_OPTION || choice == JOptionPane.DEFAULT_OPTION) {
			return;
		}
		
		int[] index = userList.getSelectedIndices();
		userController.removeUsers(index);
	}
	
	/**
	 * 
	 */
	public void renameUser() {
		int index = userList.getSelectedIndex();
		String name = JOptionPane.showInputDialog(this, "Write a new name", "Rename user", JOptionPane.QUESTION_MESSAGE);
		try {
			userController.renameUser(index, name);
		} catch (NoUserSelectedException e) {
			JOptionPane.showMessageDialog(this, "Select a user to rename", "No user selected", JOptionPane.WARNING_MESSAGE);
		} catch (UsernameEmptyException e) {
			JOptionPane.showMessageDialog(this, "Username cannot be empty", "No name", JOptionPane.WARNING_MESSAGE);
		} catch (UserExistsException e) {
			JOptionPane.showMessageDialog(this, "User \"" + name + "\" exists", "User exists", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getAdUsersButton) {
			addAdUsers();
		}

		if (e.getSource() == refreshAdUsersButton) {
			refreshAdUser();
		}

		if (e.getSource() == addUserButton) {
			addUser();
		}

		if (e.getSource() == removeUserButton) {
			removeUser();
		}

		if (e.getSource() == renameUserButton) {
			renameUser();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == userList) {
			if (userList.getSelectedIndex() == -1) {
				userController.clearTables();
				return;
			}
			if (userController.isUserListUpdate()) {
				return;
			}
			userController.setSelectedUser(userList.getSelectedIndex());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == getAdUsersButton) {
			MainFrame.setToolTip("Fetch users and user details from an Active Directory (see options for configuration).");
		}
		if(e.getSource() == refreshAdUsersButton) {
			MainFrame.setToolTip("Refresh selected Active Directory user's details");			
		}
		if(e.getSource() == addUserButton) {
			MainFrame.setToolTip("Add user for manual detail entry");
		}
		if(e.getSource() == removeUserButton) {
			MainFrame.setToolTip("Remove selected user(s)");
		}
		if(e.getSource() == renameUserButton) {
			MainFrame.setToolTip("Rename selected non-Active Directory user");
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

}
