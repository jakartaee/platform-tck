/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.entityManager;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TransactionRequiredException;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties props = null;

	Map map = new HashMap<String, Object>();

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_ee_entityManager.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
		super.cleanup();
	}

	/*
	 * @testName: createEntityManagerSynchronizationTypeMapTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3318; PERSISTENCE:SPEC:1801;
	 * PERSISTENCE:SPEC:1804; PERSISTENCE:SPEC:1883.2;
	 * 
	 * @test_Strategy: Create an EntityManagerFactory via SynchronizationType,Map
	 */
	@Test
	public void createEntityManagerSynchronizationTypeMapTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Test UNSYNCHRONIZED");
			EntityManager em1 = getEntityManagerFactory().createEntityManager(SynchronizationType.UNSYNCHRONIZED, map);
			if (em1 != null) {
				logger.log(Logger.Level.TRACE, "Received non-null EntityManager");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received null EntityManager");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		try {
			logger.log(Logger.Level.INFO, "Test SYNCHRONIZED");
			EntityManager em2 = getEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED, map);
			if (em2 != null) {
				logger.log(Logger.Level.TRACE, "Received non-null EntityManager");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received null EntityManager");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("createEntityManagerSynchronizationTypeMapTest failed");
		}
	}

	/*
	 * @testName: createEntityManagerSynchronizationTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3322;
	 * 
	 * @test_Strategy: Create an EntityManagerFactory via SynchronizationType
	 */
	@Test
	public void createEntityManagerSynchronizationTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Test UNSYNCHRONIZED");
			EntityManager em1 = getEntityManagerFactory().createEntityManager(SynchronizationType.UNSYNCHRONIZED);
			if (em1 != null) {
				logger.log(Logger.Level.TRACE, "Received non-null EntityManager");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received null EntityManager");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		try {
			logger.log(Logger.Level.INFO, "Test SYNCHRONIZED");
			EntityManager em2 = getEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED);
			if (em2 != null) {
				logger.log(Logger.Level.TRACE, "Received non-null EntityManager");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received null EntityManager");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("createEntityManagerSynchronizationTypeTest failed");
		}
	}

	/*
	 * @testName: joinTransactionTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:489
	 * 
	 * @test_Strategy: Call EntityManager.joinTransaction() method when no
	 * transaction exists
	 */
	@Test
	public void joinTransactionTransactionRequiredExceptionTest() throws Exception {
		boolean pass = false;
		try {

			getEntityManager().joinTransaction();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException e) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("joinTransactionTransactionRequiredExceptionTest failed");
		}
	}
}
