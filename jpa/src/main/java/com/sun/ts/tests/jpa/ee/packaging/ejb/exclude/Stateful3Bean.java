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

package com.sun.ts.tests.jpa.ee.packaging.ejb.exclude;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.packaging.ejb.resource_local.Stateless3Bean;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Stateful3Bean implements Stateful3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateless3Bean.class.getName());

	@PersistenceContext(unitName = "CTS-EJB-EXCLUDE")
	private EntityManager entityManager;

	private static final B bRef[] = new B[5];

	public SessionContext sessionContext;

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	public void createTestData() {
		try {

			logger.log(Logger.Level.TRACE, "createTestData - create 5 B Instances");

			bRef[0] = new B("11", "myB", 1);
			bRef[1] = new B("12", "yourB", 2);
			bRef[2] = new B("13", "herB", 3);
			bRef[3] = new B("14", "hisB", 4);
			bRef[4] = new B("15", "ourB", 5);

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
		logger.log(Logger.Level.TRACE, "stateful3Bean removeTestData");

		try {
			entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
			entityManager.createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
			entityManager.flush();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught while cleaning up:", e);
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		entityManager.getEntityManagerFactory().getCache().evictAll();
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

	public boolean test1() {

		logger.log(Logger.Level.TRACE, "Begin test1");
		boolean pass = false;

		try {

			createTestData();

			logger.log(Logger.Level.TRACE, "try to find Entity B now that it has been create and persisted.");
			final B newB = entityManager.find(B.class, "15");

			if (newB != null) {
				logger.log(Logger.Level.TRACE, "found B entity as expected");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
			pass = false;
		}

		return pass;
	}

	public boolean test2() {

		logger.log(Logger.Level.TRACE, "Begin test2");
		boolean pass = false;

		try {

			logger.log(Logger.Level.TRACE, "test2:  newA");
			final A newA = new A("100", "nonexistent", 5);
			logger.log(Logger.Level.TRACE, "test2:  try to Persist Entity A");
			entityManager.persist(newA);
			logger.log(Logger.Level.TRACE, "test2:  Did not get expected Exception");

		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected, A is not an Entity");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught in test2:", e);
			pass = false;
		}
		return pass;
	}
}
