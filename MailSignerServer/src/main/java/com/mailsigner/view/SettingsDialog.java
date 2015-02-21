package com.mailsigner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.mailsigner.MailSigner;
import com.mailsigner.util.Settings;

public class SettingsDialog extends JFrameCenter implements ActionListener {
	
	private static final long serialVersionUID = -635023389582769279L;
	private JPanel contentPane;
	private JTextField host;
	private JTextField domain;
	private JTextField userLogin;
	private JPasswordField userPassword;
	private JSpinner timeout;
	private JButton btnOk;
	private JButton btnCancel;
	private Settings settings;
	private JTextField dataLocation;

	/**
	 * Create the frame.
	 */
	public SettingsDialog() {
		setBounds(100, 100, 450, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel controlPanel = new JPanel();
		FlowLayout fl_controlPanel = (FlowLayout) controlPanel.getLayout();
		fl_controlPanel.setAlignment(FlowLayout.RIGHT);
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		controlPanel.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		controlPanel.add(btnCancel);
		
		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.CENTER);
		
		JPanel adPanel = new JPanel();
		adPanel.setBorder(new TitledBorder(null, "Active Directory settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel filePanel = new JPanel();
		filePanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Misc.", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		GroupLayout gl_inputPanel = new GroupLayout(inputPanel);
		gl_inputPanel.setHorizontalGroup(
			gl_inputPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_inputPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_inputPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(filePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
						.addComponent(adPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_inputPanel.setVerticalGroup(
			gl_inputPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_inputPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(adPanel, GroupLayout.PREFERRED_SIZE, 154, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(filePanel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addGap(109))
		);
		
		JLabel lblDataLocation = new JLabel("Data location");
		
		dataLocation = new JTextField();
		dataLocation.setColumns(10);
		GroupLayout gl_filePanel = new GroupLayout(filePanel);
		gl_filePanel.setHorizontalGroup(
			gl_filePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDataLocation)
					.addGap(50)
					.addComponent(dataLocation, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_filePanel.setVerticalGroup(
			gl_filePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filePanel.createSequentialGroup()
					.addGroup(gl_filePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDataLocation)
						.addComponent(dataLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(111, Short.MAX_VALUE))
		);
		filePanel.setLayout(gl_filePanel);
		
		JLabel lblAdHostAdress = new JLabel("Host adress");
		
		JLabel lblAdDomainName = new JLabel("Domain name");
		
		JLabel lblUserLogin = new JLabel("User login");
		
		JLabel lblUserPassword = new JLabel("User password");
		
		JLabel lblTimeout = new JLabel("Timeout");
		
		host = new JTextField();
		host.setColumns(10);
		
		domain = new JTextField();
		domain.setColumns(10);
		
		userLogin = new JTextField();
		userLogin.setColumns(10);
		
		userPassword = new JPasswordField();
		
		timeout = new JSpinner();
		timeout.setEditor(new NumberEditor(timeout, "#"));
		
		GroupLayout gl_adPanel = new GroupLayout(adPanel);
		gl_adPanel.setHorizontalGroup(
			gl_adPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_adPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_adPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAdHostAdress)
						.addComponent(lblAdDomainName)
						.addComponent(lblUserLogin)
						.addComponent(lblUserPassword)
						.addComponent(lblTimeout))
					.addGap(41)
					.addGroup(gl_adPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(userPassword, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
						.addComponent(userLogin, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
						.addComponent(domain, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
						.addComponent(host, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
						.addComponent(timeout, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_adPanel.setVerticalGroup(
			gl_adPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_adPanel.createSequentialGroup()
					.addGroup(gl_adPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAdHostAdress)
						.addComponent(host, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_adPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAdDomainName)
						.addComponent(domain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_adPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserLogin)
						.addComponent(userLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_adPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserPassword)
						.addComponent(userPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_adPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTimeout)
						.addComponent(timeout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(41, Short.MAX_VALUE))
		);
		adPanel.setLayout(gl_adPanel);
		inputPanel.setLayout(gl_inputPanel);
		loadSettings();
	}
	
	private void loadSettings() {
		settings = MailSigner.getSettings();
		host.setText(settings.getHost());
		domain.setText(settings.getDomain());
		userLogin.setText(settings.getUser());
		userPassword.setText(settings.getPass());
		timeout.setValue(settings.getTimeout());
		
		dataLocation.setText(settings.getDatapath());
	}
	
	private void saveSettings() {
		settings.setHost(host.getText());
		settings.setDomain(domain.getText());
		settings.setUser(userLogin.getText());
		settings.setPass(new String(userPassword.getPassword()));
		Integer timeoutValue = (Integer) timeout.getValue();
		settings.setTimeout(timeoutValue);
		
		settings.setDatapath(dataLocation.getText());
		
		MailSigner.writeProperties();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnOk) {
			saveSettings();
			dispose();
		}
		if(e.getSource() == btnCancel) {
			dispose();
		}
		
	}
}
