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

package com.sun.ts.tests.jpa.ee.propagation.am;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.Account;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@Stateless(name = "Stateless3Bean")
@Remote({ Stateless3IF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Stateless3Bean implements Stateless3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateless3Bean.class.getName());

	@PersistenceUnit(unitName = "CTS-APPMANAGED-UNIT")
	private EntityManagerFactory entityManagerFactory;

	private EntityManager entityManager;

	public SessionContext sessionContext;

	private Map myMap = new HashMap();

	private Account accountRef;

	private static final int ACCOUNTS[] = { 1000, 1075, 40, 30564, 387 };

	private static final double BALANCES[] = { 50000.0, 10490.75, 200.50, 25000.0, 1000000.0 };

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	// Our business methods

	public void transfer(final int from, final int to, final double amt) {
		logger.log(Logger.Level.TRACE, "transfer()");
		withdraw(from, amt);
		deposit(to, amt);
	}

	public double balance(final int acct) {
		logger.log(Logger.Level.TRACE, "balance()");
		Account thisAccount = entityManager.find(Account.class, acct);
		double balance;
		try {
			balance = thisAccount.balance();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("Exception occurred in balance: " + e);
		}
		return balance;
	}

	public double deposit(final int acct, final double amt) {
		logger.log(Logger.Level.TRACE, "deposit()");
		double balance;
		Account thisAccount = entityManager.find(Account.class, acct);
		try {
			balance = thisAccount.deposit(amt);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("Exception occurred in deposit: " + e);
		}
		return balance;
	}

	public double withdraw(final int acct, final double amt) {
		logger.log(Logger.Level.TRACE, "withdraw()");
		double balance;
		Account thisAccount = entityManager.find(Account.class, acct);
		try {
			balance = thisAccount.withdraw(amt);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("Exception occurred in withdraw: " + e);
		}
		return balance;
	}

	public boolean checkAccountStatus(final Account acct) {
		logger.log(Logger.Level.TRACE, "checkAccountStatus()");
		Account thisAccount = entityManager.find(Account.class, acct.id());
		if (acct.equals(thisAccount)) {
			return true;
		} else {
			return false;
		}

	}

	public String getAllAccounts() {
		StringBuffer accounts = new StringBuffer();
		List result;
		try {
			result = entityManager.createQuery("select a from Account a").getResultList();

			Iterator i = result.iterator();
			while (i.hasNext()) {
				Account a1 = (Account) i.next();
				accounts.append("" + a1.id() + "  " + (double) a1.balance() + "\n");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("Exception occurred in getAllAccounts: " + e);
		}
		return accounts.toString();
	}

	// Helpers

	public void removeTestData() {
		logger.log(Logger.Level.TRACE, "entering removeTestData()");
		try {
			entityManager.createNativeQuery("DELETE FROM ACCOUNT").executeUpdate();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		entityManagerFactory.getCache().evictAll();
		logger.log(Logger.Level.TRACE, "leaving removeTestData()");
	}

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "entering createTestData()");

		try {

			logger.log(Logger.Level.TRACE, "Create 5 Account Entities");

			for (int i = 0; i < ACCOUNTS.length; i++) {
				logger.log(Logger.Level.TRACE, "Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
				System.out.println("Creating account=" + ACCOUNTS[i] + ", balance=" + BALANCES[i]);
				accountRef = new Account(ACCOUNTS[i], BALANCES[i]);
				entityManager.persist(accountRef);

			}
			entityManager.flush();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "createTestData: Unexpected Exception caught in createTestData", e);
		} finally {
			logger.log(Logger.Level.TRACE, "createTestData complete");
		}
		logger.log(Logger.Level.TRACE, "leaving createTestData()");

	}

	public void init(final Properties p) {
		logger.log(Logger.Level.TRACE, "entering init()");
		try {
			TestUtil.init(p);
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			throw new EJBException(e.getMessage());
		}
		logger.log(Logger.Level.TRACE, "leaving init()");
	}

	// Test Methods

	public boolean test1() {
		String accounts;
		boolean pass = false;

		try {

			createEntityManager();

			createTestData();

			accounts = getAllAccounts();

			if (accounts != null) {
				logger.log(Logger.Level.TRACE, accounts);
			}

			Account ACCOUNT = entityManager.find(Account.class, 1075);
			pass = checkAccountStatus(ACCOUNT);

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			closeEntityManager();
		}
		logger.log(Logger.Level.TRACE, "leaving test1()");
		return pass;
	}

	public boolean test2() {
		Double EXPECTED_BALANCE = 10540.75D;
		Double balance;
		boolean pass = false;

		try {
			createEntityManager();

			createTestData();

			Account ACCOUNT = entityManager.find(Account.class, 1075);

			balance = balance(ACCOUNT.id());
			balance = deposit(ACCOUNT.id(), 100.0);
			balance = withdraw(ACCOUNT.id(), 50.0);

			if (EXPECTED_BALANCE.compareTo(balance) == 0) {
				logger.log(Logger.Level.TRACE, "Expected balance received.");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						" Did not get Expected balance, got:" + balance + "Expected: " + EXPECTED_BALANCE);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			closeEntityManager();
		}
		logger.log(Logger.Level.TRACE, "leaving test2()");
		return pass;
	}

	public void createEntityManager() {
		logger.log(Logger.Level.TRACE, "entering createEntityManager()");

		if (entityManagerFactory != null) {
			logger.log(Logger.Level.TRACE, "EntityManagerFactory is not null");
			entityManager = entityManagerFactory.createEntityManager(myMap);
		} else {
			logger.log(Logger.Level.ERROR, "Unexpected: EntityManagerFactory is null");
		}
		logger.log(Logger.Level.TRACE, "leaving createEntityManager()");

	}

	public void closeEntityManager() {
		logger.log(Logger.Level.TRACE, "entering closeEntityManager()");
		try {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "closeEntityManager: Unexpected Exception caught while closing "
					+ "an Application-Managed EntityManager" + e);
		}
		logger.log(Logger.Level.TRACE, "leaving closeEntityManager()");
	}

	public void doCleanup() {
		logger.log(Logger.Level.TRACE, "entering doCleanup()");
		createEntityManager();
		removeTestData();
		closeEntityManager();
		logger.log(Logger.Level.TRACE, "leaving doCleanup()");
	}
}
