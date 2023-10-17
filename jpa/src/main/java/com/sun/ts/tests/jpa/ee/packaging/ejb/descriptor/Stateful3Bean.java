/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jpa.ee.packaging.ejb.descriptor;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Stateful3Bean implements Stateful3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateful3Bean.class.getName());

	private EntityManager entityManager;

	private EntityManagerFactory emf;

	private Map myMap = new HashMap();

	public SessionContext sessionContext;

	private static final B bRef[] = new B[5];

	private static final C cRef[] = new C[5];

	private EntityManager getEntityManager() {
		logger.log(Logger.Level.TRACE, "Look up EntityManagerFactory,get EntityManager");
		try {

			emf = (EntityManagerFactory) sessionContext.lookup("persistence/MyPersistenceUnit");

			if (emf != null) {
				entityManager = emf.createEntityManager(myMap);
			} else {
				logger.log(Logger.Level.ERROR, "EntityManagerFactory is null");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught while setting EntityManager", e);
		}
		return entityManager;
	}

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	public void createTestData() {
		try {

			logger.log(Logger.Level.TRACE, "createTestData");

			logger.log(Logger.Level.TRACE, "Create 2 B Entities");
			bRef[0] = new B("1", "myB", 1);
			bRef[1] = new B("2", "yourB", 2);

			logger.log(Logger.Level.TRACE, "Start to persist Bs ");
			for (B b : bRef) {
				if (b != null) {
					entityManager.persist(b);
					logger.log(Logger.Level.TRACE, "persisted B " + b);
				}
			}

			logger.log(Logger.Level.TRACE, "Create 2 C Entities");
			cRef[0] = new C("5", "myC", 5);
			cRef[1] = new C("6", "yourC", 6);

			logger.log(Logger.Level.TRACE, "Start to persist Cs ");
			for (C c : cRef) {
				if (c != null) {
					entityManager.persist(c);
					logger.log(Logger.Level.TRACE, "persisted C " + c);
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected while creating test data:" + e);
		}
	}

	public void removeTestData() {
		logger.log(Logger.Level.TRACE, "stateful3Bean removeTestData");

		try {
			if ((entityManager == null) || (!entityManager.isOpen())) {
				entityManager = getEntityManager();
			}
			entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
			entityManager.createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught while cleaning up:", e);
		} finally {

			if (entityManager.isOpen()) {
				entityManager.close();
			}
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		emf.getCache().evictAll();
		logger.log(Logger.Level.TRACE, "cleanup complete");
	}

	public void init(Properties p) {
		logger.log(Logger.Level.TRACE, "init");
		try {
			TestUtil.init(p);
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			throw new EJBException(e.getMessage());
		}
	}

	public boolean test1() {

		logger.log(Logger.Level.TRACE, "Begin test1");
		boolean pass = false;
		EntityManager em = getEntityManager();

		try {
			createTestData();

			B anotherB = em.find(B.class, "1");

			if (anotherB != null) {
				logger.log(Logger.Level.TRACE, "anotherB found");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "test1: Unexpected Exception :", e);
		} finally {
			try {
				if (em.isOpen()) {
					em.close();
				}
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.ERROR, "Unexpected IllegalStateException caught closing EntityManager", ise);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception caught in while closing EntityManager", e);
			}
		}

		return pass;
	}

	public boolean test2() {
		logger.log(Logger.Level.TRACE, "Begin test2");
		boolean pass = false;
		EntityManager thisEM = getEntityManager();

		try {
			if (thisEM.isOpen()) {
				logger.log(Logger.Level.TRACE, "EntityManager is OPEN, try close");
				thisEM.close();
			}

			if (!thisEM.isOpen()) {
				logger.log(Logger.Level.TRACE, "EntityManager isOpen, returns false as expected");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "EntityManager isOpen, returns false - unexpected");
			}

		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.ERROR, "Unexpected IllegalStateException caught:", ise);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught:", e);
		}

		return pass;
	}

	public boolean test3() {
		logger.log(Logger.Level.TRACE, "Begin test3");
		boolean pass = false;
		EntityManager thatEM = getEntityManager();

		try {
			if (thatEM.isOpen()) {
				thatEM.close();
			}

			if (!thatEM.isOpen()) {
				thatEM.close();
			}
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException caught as expected");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught in test3", e);
		}

		return pass;
	}

	public boolean test4() {
		logger.log(Logger.Level.TRACE, "Begin test4");
		boolean pass = false;
		try {
			EntityManager entityManager = getEntityManager();
			entityManager.getTransaction();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "Caught Expected Exception :" + e);
			pass = true;
		} finally {
			try {
				if (entityManager.isOpen()) {
					entityManager.close();
				}
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.ERROR, "Unexpected IllegalStateException caught closing EntityManager", ise);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception caught while closing EntityManager", e);
			}
		}

		return pass;
	}

	public boolean test6() {

		logger.log(Logger.Level.TRACE, "Begin test6");
		boolean pass = false;
		EntityManager em = getEntityManager();

		try {
			createTestData();

			C c = em.find(C.class, "5");

			if (c != null) {
				logger.log(Logger.Level.TRACE, "c found");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "test1: Unexpected Exception :", e);
		} finally {
			try {
				if (em.isOpen()) {
					em.close();
				}
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.ERROR, "Unexpected IllegalStateException caught closing EntityManager", ise);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception caught in while closing EntityManager", e);
			}
		}

		return pass;
	}

}
