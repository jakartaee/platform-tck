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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TransactionRequiredException;

public class ClientIT extends PMClientBase {

	Properties props = null;

	Map map = new HashMap<String, Object>();

	public ClientIT() {
	}

	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_ee_entityManager.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeAll
	public void setup() throws Exception {
		TestUtil.logTrace("setup");
		try {
			super.setup();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterAll
	public void cleanup() throws Exception {
		TestUtil.logTrace("cleanup complete, calling super.cleanup");
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
			TestUtil.logMsg("Test UNSYNCHRONIZED");
			EntityManager em1 = getEntityManagerFactory().createEntityManager(SynchronizationType.UNSYNCHRONIZED, map);
			if (em1 != null) {
				TestUtil.logTrace("Received non-null EntityManager");
				pass1 = true;
			} else {
				TestUtil.logErr("Received null EntityManager");
			}
		} catch (Exception e) {
			TestUtil.logErr("Received unexpected exception", e);
		}
		try {
			TestUtil.logMsg("Test SYNCHRONIZED");
			EntityManager em2 = getEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED, map);
			if (em2 != null) {
				TestUtil.logTrace("Received non-null EntityManager");
				pass2 = true;
			} else {
				TestUtil.logErr("Received null EntityManager");
			}
		} catch (Exception e) {
			TestUtil.logErr("Received unexpected exception", e);
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
			TestUtil.logMsg("Test UNSYNCHRONIZED");
			EntityManager em1 = getEntityManagerFactory().createEntityManager(SynchronizationType.UNSYNCHRONIZED);
			if (em1 != null) {
				TestUtil.logTrace("Received non-null EntityManager");
				pass1 = true;
			} else {
				TestUtil.logErr("Received null EntityManager");
			}
		} catch (Exception e) {
			TestUtil.logErr("Received unexpected exception", e);
		}
		try {
			TestUtil.logMsg("Test SYNCHRONIZED");
			EntityManager em2 = getEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED);
			if (em2 != null) {
				TestUtil.logTrace("Received non-null EntityManager");
				pass2 = true;
			} else {
				TestUtil.logErr("Received null EntityManager");
			}
		} catch (Exception e) {
			TestUtil.logErr("Received unexpected exception", e);
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
			TestUtil.logErr("TransactionRequiredException not thrown");
		} catch (TransactionRequiredException e) {
			TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("joinTransactionTransactionRequiredExceptionTest failed");
		}
	}
}
