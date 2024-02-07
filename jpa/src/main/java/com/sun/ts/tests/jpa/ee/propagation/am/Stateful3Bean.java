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
import java.math.BigInteger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceUnit;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
public class Stateful3Bean implements Stateful3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateful3Bean.class.getName());

	@PersistenceUnit(unitName = "CTS-APPMANAGED-UNIT")
	private EntityManagerFactory entityManagerFactory;

	public SessionContext sessionContext;

	private EntityManager entityManager;

	private static final Member mRef[] = new Member[5];

	public Properties p;

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	@PostConstruct
	public void prepareEnvironment() {
		try {

			logger.log(Logger.Level.TRACE, "In PostContruct");
			if (entityManagerFactory != null) {
				entityManager = entityManagerFactory.createEntityManager();
			} else {
				logger.log(Logger.Level.ERROR, "EntityManagerFactory is null");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, " In PostConstruct: Exception caught while setting EntityManager", e);
		}
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

	public boolean test3() {
		boolean pass = false;
		int memId = 9;
		try {

			logger.log(Logger.Level.TRACE, "createTestData");
			entityManager.joinTransaction();
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "find member");
			Member member = getMember(memId);
			entityManager.clear();

			Member memberClone = getMember(memId);

			if (null != member) {
				checkMemberStatus(memberClone);
				entityManager.merge(member);
				entityManager.flush();
			} else {
				logger.log(Logger.Level.ERROR, " member is null, Unexpected - cannot proceed with test.");
			}
			pass = false;

		} catch (OptimisticLockException e) {
			pass = true;
			logger.log(Logger.Level.TRACE, "Caught expected OptimisticLockException");
		} finally {
			logger.log(Logger.Level.TRACE, "Finally, removeTestData");
			removeTestData();
		}
		return pass;
	}

	public boolean test4() {
		boolean pass = false;
		int memId = 8;
		try {

			logger.log(Logger.Level.TRACE, "test4: createTestData");
			entityManager.joinTransaction();
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "find member");
			Member member = getMember(memId);

			if (null != member) {
				logger.log(Logger.Level.TRACE, "check member status");
				checkMemberStatus(member);
				// When an EntityManager with an extended persistence context
				// is used, the persist, remove merge, and refresh operations
				// may be called regardless of whether a TX is active. The
				// effects of these operations will be committed to the database
				// when the extended persistence context is enlisted in a transactin
				// and the transaction commits.
				// The scope of an AM entity manager is extended.
				entityManager.refresh(member);

				if (member.getDonation().toString().equals("10000")) {
					pass = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, " member is null, Unexpected - cannot proceed with test.");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception:", e);
		} finally {
			removeTestData();
		}
		return pass;
	}

	public void checkMemberStatus(final Member m) {
		System.out.println("checkMemberStatus - joinTransaction");
		int newDonation = 10000;

		if (null == m) {
			logger.log(Logger.Level.TRACE, "checkMemberStatus: member is NULL");
		} else {
			if (m.isDuesPaid()) {
				logger.log(Logger.Level.TRACE, "checkCustomerStatus: thisMember is not null, setDonation");
				m.setDonation(new BigInteger("10000"));
			} else {
				BigInteger currentDonation = m.getDonation();
				int convertedDonation = 0;
				if (currentDonation != null) {
					convertedDonation = currentDonation.intValue();
				}
				m.setDonation(new BigInteger(String.valueOf(convertedDonation + newDonation)));
				m.setDuesPaid(true);
			}

			logger.log(Logger.Level.TRACE, "merge thisMember");
			entityManager.flush();
		}
	}

	public Member getMember(final int memberId) {
		logger.log(Logger.Level.TRACE, "getMember");
		return entityManager.find(Member.class, memberId);
	}

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");
		try {

			logger.log(Logger.Level.TRACE, "Create Member Entities");
			mRef[0] = new Member(7, "Jane Lam", false);
			mRef[1] = new Member(8, "Vinny Testa", false);
			mRef[2] = new Member(9, "Mario Luigi", true, new BigInteger("25000"));
			mRef[3] = new Member(10, "Sky Blue", false);
			mRef[4] = new Member(11, "Leonardi DaVinci", true, new BigInteger("100000"));

			logger.log(Logger.Level.TRACE, "Start to persist Members ");
			System.out.println("Persist Member Entities");
			for (Member m : mRef) {
				if (m != null) {
					entityManager.persist(m);
					logger.log(Logger.Level.TRACE, "persisted Member " + m);
				}
			}

			entityManager.flush();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected while creating member data:" + e);
		}
	}

	public void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		try {
			entityManager.createNativeQuery("DELETE FROM MEMBER").executeUpdate();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		entityManagerFactory.getCache().evictAll();
	}
}
