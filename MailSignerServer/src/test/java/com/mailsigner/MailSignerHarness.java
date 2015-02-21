package com.mailsigner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mailsigner.model.persistence.Computer;
import com.mailsigner.model.persistence.ComputerPK;
import com.mailsigner.model.persistence.Field;
import com.mailsigner.model.persistence.Signature;
import com.mailsigner.model.persistence.User;
import com.mailsigner.model.persistence.Userfield;
import com.mailsigner.model.persistence.UserfieldPK;

public class MailSignerHarness {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("MailSignerJPA");
		EntityManager em = emf.createEntityManager();
		EntityTransaction entr = em.getTransaction();
		entr.begin();

		// create the user
		User user = new User();
		user.setLogon("Jiekebo3");
		byte ad = 0;
		user.setActivedirectory(ad);

		// get all fields in database
		Query fieldQuery = em.createQuery("SELECT field FROM Field field");
//		fieldQuery.setFirstResult(1); // jpa does not like a 0 as primary key...
		// remember all id's are generated from 1..n
		@SuppressWarnings("unchecked")
		List<Field> fieldList = fieldQuery.getResultList();
		@SuppressWarnings("unused")
		Iterator<Field> fieldIterator = fieldList.iterator();

		// add each field to a set
		Set<Userfield> userFieldsSet = new HashSet<Userfield>();
		for (Field field : fieldList) {
			UserfieldPK userFieldPK = new UserfieldPK();
			userFieldPK.setUserIduser(user.getIduser());
			// how to get the generated user id before it has been persisted?
			// above actually works!!! Incredible JPA!!!
			userFieldPK.setFieldIdfield(field.getIdfield());

			Userfield userField = new Userfield();
			userField.setId(userFieldPK);
			userField.setField(field);
			userField.setUser(user);
			userField.setValue("");
			userFieldsSet.add(userField);
			em.persist(userField);
		}
		user.setUserfields(userFieldsSet);

		// create a computer for the user
		ComputerPK computerPK = new ComputerPK();
		computerPK.setUserIduser(user.getIduser());

		Computer computer = new Computer();
		computer.setId(computerPK);
		computer.setDeployed(ad);
		computer.setUser(user);
		em.persist(computer);

		Set<Computer> usersComputers = new HashSet<Computer>();

		user.setComputers(usersComputers);

		// persist the final user
		em.persist(user);

		Signature signature = new Signature();
		signature.setTitle("Testing");
		em.persist(signature);

		Field field = new Field();
		field.setLabel("Facebook account");
		field.setCode("blabla");
		em.persist(field);

		entr.commit();
		em.close();

		try {
			em = emf.createEntityManager();
			Query query = em.createQuery("SELECT st FROM User st");
			query.setFirstResult(0);
			@SuppressWarnings("unchecked")
			List<User> stuList = query.getResultList();
			Iterator<User> stuIterator = stuList.iterator();
			System.out.println("Has students:" + stuIterator.hasNext());
			while (stuIterator.hasNext()) {
				User st = stuIterator.next();
				System.out.println("Logon:" + st.getLogon());
			}
		} finally {
			em.close();
			emf.close();
		}
	}
}