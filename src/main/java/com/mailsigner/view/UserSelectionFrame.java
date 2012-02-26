package com.mailsigner.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import com.mailsigner.model.ActiveDirectoryUserModel;
import com.mailsigner.model.table.AdUserTableModel;


public class UserSelectionFrame extends JDialogCenter implements ActionListener {
	private static final long serialVersionUID = -980678125312796284L;
	private JPanel contentPane;
	private JTable table;
	private JButton btnCancel;
	private JButton btnAdd;
	private AdUserTableModel adUserTableModel;
	private List<ActiveDirectoryUserModel> filteredList;
	private List<ActiveDirectoryUserModel> adUsers;
	

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					UserSelectionFrame frame = new UserSelectionFrame(null);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public UserSelectionFrame(JFrame parent, List<ActiveDirectoryUserModel> adUsers) {
		super(parent);
		this.adUsers = adUsers;
		setTitle("Select Active Directory users");
		setSize(450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		panel.add(btnAdd);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		panel.add(btnCancel);
		
		JScrollPane panel_1 = new JScrollPane();
		contentPane.add(panel_1, BorderLayout.CENTER);
		
		table = new JTable();
		panel_1.setViewportView(table);
		
		adUserTableModel = new AdUserTableModel();
		adUserTableModel.setAdUsers(adUsers);
		
		table.setModel(adUserTableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
	}
	
	public List<ActiveDirectoryUserModel> getFilteredList() {
		return filteredList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCancel){
			dispose();
		}
		if(e.getSource() == btnAdd) {
			List<Integer> selection = adUserTableModel.getSelection();
			filteredList = new ArrayList<ActiveDirectoryUserModel>();
			for (Integer integer : selection) {
				filteredList.add(adUsers.get(integer.intValue()));
			}
			dispose();
		}
		
	}

}
