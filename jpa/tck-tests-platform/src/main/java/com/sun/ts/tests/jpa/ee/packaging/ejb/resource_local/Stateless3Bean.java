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
 * @(#)Stateless3Bean.java	1.2 06/02/28
 */

package com.sun.ts.tests.jpa.ee.packaging.ejb.resource_local;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.A;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.RollbackException;

@Stateless(name = "Stateless3Bean")
@Remote({ Stateless3IF.class })
public class Stateless3Bean implements Stateless3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateless3Bean.class.getName());

	public Stateless3Bean() {
	}

	public SessionContext sessionContext;

	private EntityManagerFactory emf;

	private EntityManager entityManager;

	private EntityTransaction entityTransaction;

	private Map thisMap = new HashMap();

	private static final String thisEMF = "java:comp/env/persistence/ThisPersistenceUnit";

	/*
	 * pu is defined in ejb.xml and looked up from JNDI persistence-unit-ref-name is
	 * optional if only one PU, description is optional
	 */

	// ================== business methods ====================================

	public void cleanupEM() {
		try {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException caught during entityManager.close()" + ise);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught during cleanupEM method" + e);
		}
	}

	public void removeTestData() {
		try {
			getEntityTransaction().begin();
			entityManager.createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught while cleaning up:", e);
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} finally {
			cleanupEM();
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		emf.getCache().evictAll();
		logger.log(Logger.Level.TRACE, "cleanup complete");
	}

	public void init(final Properties p) {
		logger.log(Logger.Level.TRACE, "init");
		try {
			TestUtil.init(p);
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			throw new EJBException(e.getMessage());
		}
	}

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	@PostConstruct
	public void prepareEnvironment() {
		try {
			logger.log(Logger.Level.TRACE, "In PostContruct");
			logger.log(Logger.Level.TRACE, "Obtain naming context");
			TSNamingContext nctx = new TSNamingContext();
			if (emf == null) {
				emf = (EntityManagerFactory) nctx.lookup(thisEMF);
			}
			if (emf != null) {
				logger.log(Logger.Level.TRACE, "EMF is not null, create Entity Manager");
				entityManager = emf.createEntityManager(thisMap);
				if (entityManager == null) {
					logger.log(Logger.Level.ERROR, "EntityManager is null!");
				}
			} else {
				logger.log(Logger.Level.ERROR, "EntityManagerFactory is null!");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "In PostConstruct: Exception caught during init", e);
		}
	}

	protected EntityTransaction getEntityTransaction() {
		if (!entityManager.isOpen()) {
			entityManager = emf.createEntityManager(thisMap);
		}
		return entityManager.getTransaction();
	}

	// Test Methods

	public boolean test1() {
		boolean pass = false;

		try {
			entityTransaction = getEntityTransaction();
			entityTransaction.begin();

			pass = entityTransaction.isActive();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception", e);
			pass = false;
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test2() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();
			entityTransaction.begin();

			if (entityTransaction.isActive()) {
				entityTransaction.begin();
			}

		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
			pass = true;
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught:", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test3() {
		boolean pass = false;

		try {
			A newA = new A("3", "test3", 3);
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();
			entityManager.persist(newA);
			entityTransaction.commit();

			entityTransaction.begin();
			A modifiedA = entityManager.find(A.class, "3");

			if ((null != modifiedA) && (modifiedA.getName().equals("test3"))) {
				modifiedA.setName("test3Modified");
			}
			entityManager.merge(modifiedA);
			entityTransaction.commit();

			A committedA = entityManager.find(A.class, "3");

			if (committedA.getName().equals("test3Modified")) {
				pass = true;
			}

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test4() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();

			if (!entityTransaction.isActive()) {
				entityTransaction.commit();
			}

		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected in test4");
			pass = true;
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test5() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();
			A newA = new A("5", "test5", 5);

			entityTransaction.begin();
			entityManager.persist(newA);
			entityTransaction.setRollbackOnly();
			entityTransaction.commit();

		} catch (RollbackException re) {
			logger.log(Logger.Level.TRACE, "RollbackException Caught as Expected");
			pass = true;
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test6() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();
			A newA = new A("6", "test6", 6);

			entityTransaction.begin();
			entityManager.persist(newA);
			entityTransaction.commit();

			entityTransaction.begin();
			A modifiedA = entityManager.find(A.class, "6");

			if ((null != modifiedA) && (modifiedA.getName().equals("test6"))) {
				modifiedA.setName("test6Modified");
			}
			entityManager.merge(modifiedA);
			entityTransaction.rollback();

			A rolledBackA = entityManager.find(A.class, "6");

			if (rolledBackA.getName().equals("test6")) {
				pass = true;
			}

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}

		return pass;
	}

	public boolean test7() {
		boolean pass = false;
		try {
			A newA = new A("7", "test7", 7);
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();
			entityManager.persist(newA);

			entityTransaction.setRollbackOnly();

			if ((entityTransaction.isActive()) && (entityTransaction.getRollbackOnly())) {
				pass = true;
			}

			entityTransaction.rollback();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test8() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();

			if (!entityTransaction.isActive()) {
				entityTransaction.setRollbackOnly();
				pass = true;
			}

		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
			pass = true;
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test9() {
		boolean pass = false;

		try {
			A newA = new A("9", "test9", 9);
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();
			entityManager.persist(newA);
			entityTransaction.setRollbackOnly();

			if ((entityTransaction.isActive()) && (entityTransaction.getRollbackOnly())) {
				pass = true;
			}

			entityTransaction.rollback();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test10() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();

			if (!entityTransaction.isActive()) {
				entityTransaction.getRollbackOnly();
			}

		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
			pass = true;

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught in test10", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test11() {
		boolean pass = false;
		try {
			A newA = new A("11", "test11", 11);
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();
			entityManager.persist(newA);

			if ((entityTransaction.isActive()) && (!entityTransaction.getRollbackOnly())) {
				pass = true;
			}

			entityTransaction.rollback();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test12() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();

			pass = entityTransaction.isActive();

			entityTransaction.rollback();

		} catch (PersistenceException e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected PersistenceException Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected PersistenceException rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test13() {
		boolean pass = false;
		try {
			entityTransaction = getEntityTransaction();

			pass = !entityTransaction.isActive();

		} catch (PersistenceException e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected PersistenceException Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected PersistenceException rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test14() {
		boolean pass = false;
		try {
			A newA = new A("14", "test14", 14);
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();
			entityManager.persist(newA);
			entityTransaction.commit();

			A newA2 = new A("14", "test14_2", 14);
			entityTransaction = getEntityTransaction();

			entityTransaction.begin();
			try {
				entityManager.persist(newA2);
				entityManager.flush();
			} catch (Exception ex) {
				logger.log(Logger.Level.TRACE, "newA2 has the same PK as newA");
				pass = entityTransaction.getRollbackOnly();
			}
			entityTransaction.rollback();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", e);
		} finally {
			try {
				if (entityTransaction.isActive()) {
					entityTransaction.rollback();
				}
			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected PersistenceException rolling back TX", e);
			}
		}
		return pass;
	}

	public boolean test15() {
		boolean pass = false;
		EntityTransaction et = null;

		if (!entityManager.isOpen()) {
			entityManager = emf.createEntityManager(thisMap);
		}
		try {
			entityManager.close();

			if (!entityManager.isOpen()) {
				et = entityManager.getTransaction();
			}
			if (et != null) {
				pass = true;
			}

		} catch (IllegalStateException ise) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception Caught", ise);
		} finally {
			try {
				if (et != null) {
					if (et.isActive()) {
						et.rollback();
					}
				} else {
					logger.log(Logger.Level.TRACE, "EntityTransaction never got assigned and is null");
					pass = false;
				}

			} catch (PersistenceException e) {
				logger.log(Logger.Level.ERROR, "Unexpected PersistenceException rolling back TX", e);
			}
		}
		return pass;
	}

}
