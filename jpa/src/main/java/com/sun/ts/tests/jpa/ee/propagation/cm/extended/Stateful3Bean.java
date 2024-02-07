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

package com.sun.ts.tests.jpa.ee.propagation.cm.extended;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.Account;
import com.sun.ts.tests.jpa.ee.common.B;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
public class Stateful3Bean implements Stateful3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateful3Bean.class.getName());

	/*
	 * If multiple persistence units exist the unitName element must be specified.
	 * In this archive, only one persistence unit exists; thus, unitName is omitted.
	 */

	@PersistenceContext(type = PersistenceContextType.EXTENDED, unitName = "CTS-EXT-UNIT")
	private EntityManager entityManager;

	public SessionContext sessionContext;

	private static final B bRef[] = new B[5];

	@EJB(beanName = "TellerBean", beanInterface = Teller.class)
	private Teller beanRef;

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
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

	public void createTestData() {
		try {

			logger.log(Logger.Level.TRACE, "createTestData");

			logger.log(Logger.Level.TRACE, "Create 5 Bees");
			bRef[0] = new B("1", "customerB1", 1);
			bRef[1] = new B("2", "customerB2", 2);
			bRef[2] = new B("3", "customerB3", 3);
			bRef[3] = new B("4", "customerB4", 4);
			bRef[4] = new B("5", "customerB5", 5);

			logger.log(Logger.Level.TRACE, "Start to persist Bees ");
			for (B b : bRef) {
				if (b != null) {
					entityManager.persist(b);
					logger.log(Logger.Level.TRACE, "persisted B " + b);
				}
			}
			entityManager.flush();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected while creating test data:" + e);
		}
	}

	public void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		try {
			entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
			entityManager.createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		entityManager.getEntityManagerFactory().getCache().evictAll();

	}

	public boolean test1() {

		logger.log(Logger.Level.TRACE, "Begin test1");
		boolean pass = false;

		try {
			removeTestData();

			createTestData();
			B anotherB = entityManager.find(B.class, "3");

			if (anotherB != null) {
				logger.log(Logger.Level.TRACE, "newB found" + anotherB.getName());
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
			pass = false;
		} finally {
			removeTestData();
		}
		return pass;
	}

	/*
	 * test_Strategy: getTransaction will throw an IllegalStateException if invoked
	 * on a Container-Managed JTA EM
	 *
	 */

	public boolean test2() {

		logger.log(Logger.Level.TRACE, "Begin test2");
		boolean pass = false;

		try {
			entityManager.getTransaction();
		} catch (IllegalStateException ise) {
			pass = true;
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected: " + ise);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}
		return pass;
	}

	public boolean test3() {
		String accounts;
		boolean pass = false;

		try {
			beanRef.removeTestData();
			logger.log(Logger.Level.TRACE, "DEBUG:  createAccountData");
			beanRef.createTestData();

			accounts = beanRef.getAllAccounts();

			if (accounts != null) {
				logger.log(Logger.Level.TRACE, accounts);
			}

			Account ACCOUNT = entityManager.find(Account.class, 1075);

			pass = beanRef.checkAccountStatus(ACCOUNT);

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			beanRef.removeTestData();
		}
		return pass;
	}

	public boolean test4() {
		Double EXPECTED_BALANCE = 10540.75D;
		Double balance;
		boolean pass = false;

		try {
			beanRef.removeTestData();
			System.out.println("DEBUG:  createAccountData");
			beanRef.createTestData();

			Account ACCOUNT = entityManager.find(Account.class, 1075);

			balance = beanRef.balance(ACCOUNT.id());
			balance = beanRef.deposit(ACCOUNT.id(), 100.0);
			balance = beanRef.withdraw(ACCOUNT.id(), 50.0);

			if (EXPECTED_BALANCE.equals(balance)) {
				logger.log(Logger.Level.TRACE, "Expected balance received.");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						" Did not get Expected balance, got:" + balance + "Expected: " + EXPECTED_BALANCE);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			beanRef.removeTestData();
		}
		return pass;
	}

	public boolean test5() {
		boolean pass = false;
		boolean pass1 = false;

		try {
			removeTestData();
			logger.log(Logger.Level.TRACE, "createTestData");
			createTestData();
			logger.log(Logger.Level.TRACE, "find customerB");
			B customerB = entityManager.find(B.class, "4");

			if (null != customerB) {
				logger.log(Logger.Level.TRACE, "check customer status");
				pass1 = beanRef.checkCustomerStatus(customerB);
			}

			entityManager.refresh(customerB);

			if ((pass1) && (customerB.getA().getName().equals("customerA9"))) {
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			removeTestData();
		}
		return pass;
	}

	public boolean test6() {
		boolean pass = false;

		try {

			logger.log(Logger.Level.TRACE, "find customerB");
			B customerB = entityManager.find(B.class, "3");

			if (null != customerB) {
				logger.log(Logger.Level.TRACE, "customer is not null, call rollbackStatus()");
				pass = beanRef.rollbackStatus(customerB);
			}

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		}
		return pass;
	}

	public boolean verifyTest6() {
		boolean pass = false;

		try {

			logger.log(Logger.Level.TRACE, "verifyTest6:  find customerB");
			B customerB = entityManager.find(B.class, "3");

			if ((customerB.getName().equals("customerB3"))) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						" did not get the expected result.  Expected" + " customerB3, got: " + customerB.getName());
			}

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception in verifyTest6:", e);
		} finally {
			removeTestData();

		}
		return pass;
	}

	public boolean test7() {
		boolean pass = false;
		boolean pass1 = false;

		try {
			removeTestData();
			logger.log(Logger.Level.TRACE, "createTestData");
			createTestData();
			B customerB = entityManager.find(B.class, "5");

			if (null != customerB) {
				logger.log(Logger.Level.TRACE, "customer is not null, call flushStatus()");
				pass1 = beanRef.flushStatus(customerB);

				logger.log(Logger.Level.TRACE, "refresh customerB entity to be sure to get actual state");
				entityManager.refresh(customerB);

				if ((pass1) && (customerB.getName().equals("flushB"))) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							" did not get the expected result.  Expected" + " flushB, got: " + customerB.getName());
				}
			}
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			removeTestData();

		}
		return pass;
	}

}
