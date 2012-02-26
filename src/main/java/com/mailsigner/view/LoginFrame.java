package com.mailsigner.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.mailsigner.MailSigner;
import com.mailsigner.util.Login;


public class LoginFrame extends JFrameCenter implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7838149832465040315L;
	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField passwordField;
	private String login;
	private String loginPassword;
	private JCheckBox rememberMeCheckbox;
	private JButton cancelButton;
	private JButton loginButton;

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setTitle("Please login");
		login = MailSigner.getSettings().getLogin();
		loginPassword = MailSigner.getSettings().getLoginpassword();
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 280);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel logoLabel = new JLabel("");
		logoLabel.setIcon(new ImageIcon(LoginFrame.class.getResource("/com/mailsigner/assets/login.jpg")));
		contentPane.add(logoLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JLabel emailLabel = new JLabel("Enter your e-mail address:");
		
		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setText(login);
		
		JLabel passwordLabel = new JLabel("Enter your password:");
		
		passwordField = new JPasswordField();
		passwordField.setText(loginPassword);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		rememberMeCheckbox = new JCheckBox("Remember me");
		
		if(!login.equals("") && !loginPassword.equals("")){
			rememberMeCheckbox.doClick();
		}
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(emailLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 178, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(passwordLabel)
								.addPreferredGap(ComponentPlacement.RELATED, 213, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(rememberMeCheckbox)
									.addPreferredGap(ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
									.addComponent(cancelButton)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(loginButton))
								.addComponent(emailField, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
								.addComponent(passwordField, 344, 344, 344))))
					.addGap(28))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(23)
					.addComponent(emailLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(emailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(loginButton)
						.addComponent(cancelButton)
						.addComponent(rememberMeCheckbox))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			String[] registration = new String[4];
			Login rl = new Login();
			
			try {
				registration = rl.performLogin(emailField.getText(), new String(passwordField.getPassword()));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(contentPane, "Connection error");
			}
			
			MailSigner.getSettings().setCompanyName(registration[0]);
			MailSigner.getSettings().setFirstName(registration[1]);
			MailSigner.getSettings().setLastName(registration[2]);
			int userLimit = new Integer(registration[3]);
			MailSigner.getSettings().setUserLimit(userLimit);
			
			if (userLimit == 0) {
				JOptionPane.showMessageDialog(null, "Invalid login", "Login Error", JOptionPane.WARNING_MESSAGE);
			} else {
				login = emailField.getText();
				loginPassword = new String(passwordField.getPassword());
				setVisible(false);
				MainFrame mainFrame = new MainFrame();
				mainFrame.setVisible(true);
			}
		}
		if(e.getSource() == cancelButton) {
			System.exit(0);
		}
		if(e.getSource() == rememberMeCheckbox) {
			if(rememberMeCheckbox.isSelected()){	
				MailSigner.getSettings().setLogin(login);
				MailSigner.getSettings().setLoginpassword(loginPassword);
				MailSigner.writeProperties();
			} else {
				MailSigner.getSettings().setLogin("");
				MailSigner.getSettings().setLoginpassword("");
				MailSigner.writeProperties();
			}			
		}
	}
}
