package com.mailsigner.control;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.mailsigner.MailSigner;
import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.User;

public class ComputerController {

	private EntityManager em = MailSigner.getEm();
	private EntityTransaction entr = MailSigner.getEntr();
	
	public ComputerController () {
		
	}
	
	public void setUndeployed(User user) {
		entr.begin();
		Set<Computer> usersComputers = user.getComputers();
		for (Computer computer : usersComputers) {
			computer.setDeployed(new Byte((byte) 0));
		}
		em.flush();
		entr.commit();
	}
}
