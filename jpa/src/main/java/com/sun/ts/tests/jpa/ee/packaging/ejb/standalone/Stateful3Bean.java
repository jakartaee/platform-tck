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

package com.sun.ts.tests.jpa.ee.packaging.ejb.standalone;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.B;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
public class Stateful3Bean implements Stateful3IF {

	private static final Logger logger = (Logger) System.getLogger(Stateful3Bean.class.getName());

	private EntityManager entityManager;

	@PostConstruct
	public void prepareEnvironment() {
		try {
			logger.log(Logger.Level.TRACE, "In PostContruct");
			entityManager = (EntityManager) sessionContext.lookup("persistence/MyPersistenceContext");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, " In PostConstruct: Exception caught while looking up EntityManager", e);
		}
	}

	public SessionContext sessionContext;

	private static final B bRef[] = new B[5];

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	public void createTestData() {
		logger.log(Logger.Level.TRACE, "createTestData");

		try {
			logger.log(Logger.Level.TRACE, "Create 5 Bees");
			bRef[0] = new B("1", "b1", 1);
			bRef[1] = new B("2", "b2", 2);
			bRef[2] = new B("3", "b3", 3);
			bRef[3] = new B("4", "b4", 4);
			bRef[4] = new B("5", "b5", 5);

			logger.log(Logger.Level.TRACE, "Start to persist Bees ");
			for (B b : bRef) {
				if (b != null) {
					entityManager.persist(b);
					logger.log(Logger.Level.TRACE, "persisted B " + b);
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected while creating test data:" + e);
		}
	}

	public void removeTestData() {
		try {
			entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught while cleaning up:", e);
		}
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logger.log(Logger.Level.TRACE, "Clearing cache");
		entityManager.getEntityManagerFactory().getCache().evictAll();
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

		try {

			createTestData();
			B anotherB = entityManager.find(B.class, "3");

			if (anotherB != null) {
				logger.log(Logger.Level.TRACE, "newB found" + anotherB.getName());
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}
		return pass;
	}

}
