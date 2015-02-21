package com.mailsigner.control;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.mailsigner.MailSigner;
import com.mailsigner.control.exception.ComputerNotFoundException;
import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.ComputerPK;
import com.mailsigner.model.persistence.User;

public class ComputerController {

	private EntityManager em = MailSigner.getEm();
	private EntityTransaction entr = MailSigner.getEntr();
	
	public ComputerController() {}
	
	public void setUndeployed(User user) {
		entr.begin();
		Set<Computer> usersComputers = user.getComputers();
		for (Computer computer : usersComputers) {
			computer.setDeployed(new Byte((byte) 0));
		}
		em.flush();
		entr.commit();
	}
	
	public void createUserComputer(User user, String computerName) {
		entr.begin();
		
		Computer computer = new Computer();
		computer.setLabel(computerName);
		computer.setUser(user);
		computer.setDeployed(new Byte((byte) 1));
		
		ComputerPK cpk = new ComputerPK();
		cpk.setUserIduser(user.getIduser());
		computer.setId(cpk);
				
		em.persist(computer);
		
		entr.commit();
	}
	
	public Computer findComputer(User user, String computerName) throws ComputerNotFoundException {
		TypedQuery<Computer> computerQuery = em.createNamedQuery("Computer.findComputer", Computer.class);
		computerQuery.setParameter("label", computerName);
		computerQuery.setParameter("user", user);
		if(computerQuery.getResultList().size() <= 0) {
			throw new ComputerNotFoundException();
		}
		return computerQuery.getSingleResult();
	}
}
