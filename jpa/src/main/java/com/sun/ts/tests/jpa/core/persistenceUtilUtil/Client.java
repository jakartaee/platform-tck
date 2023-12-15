/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.persistenceUtilUtil;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.PersistenceUnitUtil;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee" };
		return createDeploymentJar("jpa_core_persistenceUtilUtil.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getPersistenceUtilUtilTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:384;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void getPersistenceUtilUtilTest() throws Exception {
		boolean pass = false;
		PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
		if (puu != null) {
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "getPersistenceUtil() returned null");
		}

		if (!pass) {
			throw new Exception("getPersistenceUtilUtilTest failed");
		}
	}

	/*
	 * @testName: getIdentifierTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:385;
	 * 
	 * @test_Strategy: Call PersistenceUnitUtil.getIdentifierTest on an entity and
	 * verify the correct id is returned
	 */
	@Test
	public void getIdentifierTest() throws Exception {
		boolean pass = true;
		Employee emp = new Employee(1, "foo", "bar", getSQLDate("2000-02-14"), (float) 35000.0);

		logger.log(Logger.Level.INFO, "Test entity not yet persisted");
		try {
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) puu.getIdentifier(emp);
			if (id == null || id != 1) {
				logger.log(Logger.Level.ERROR, "expected a null or id: 1, actual id:" + id);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}
		logger.log(Logger.Level.INFO, "Test entity persisted");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(emp);

			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) puu.getIdentifier(emp);
			if (id != 1) {
				logger.log(Logger.Level.ERROR, "expected a null or id: 1, actual id:" + id);
				pass = false;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getIdentifierTest failed");
		}
	}

	/*
	 * @testName: getIdentifierIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:548;
	 * 
	 * @test_Strategy: Call PersistenceUnitUtil.getIdentifierTest of a non-entity
	 * and verify IllegalArgumentException is thrown
	 */
	@Test
	public void getIdentifierIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		try {
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			puu.getIdentifier(this);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			pass = true;
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getIdentifierIllegalArgumentExceptionTest failed");
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
