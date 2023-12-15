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

package com.sun.ts.tests.jpa.se.entityManagerFactory;

import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties props = null;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Member", pkgName + "Member_", pkgName + "Order", pkgName + "Order_" };
		return createDeploymentJar("jpa_se_entityManagerFactory.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setupNoData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupNoData");
		try {
			super.setup();
			createDeployment();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupNoData() throws Exception {
		try {
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: getMetamodelIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:536;
	 * 
	 * @test_Strategy: Close the EntityManagerFactory, then call emf.getMetaModel()
	 */
	@Test
	public void getMetamodelIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		try {
			EntityManagerFactory emf = getEntityManager().getEntityManagerFactory();
			emf.close();
			try {
				emf.getMetamodel();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getMetamodelIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: createEntityManagerFactoryNoBeanValidatorTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1291; PERSISTENCE:SPEC:1914;
	 * 
	 * @test_Strategy: Instantiate createEntityManagerFactory when there is no Bean
	 * Validation provider present in the environment
	 */
	@Test
	public void createEntityManagerFactoryNoBeanValidatorTest() throws Exception {
		boolean pass = false;
		myProps.put("jakarta.persistence.validation.mode", "callback");
		displayMap(myProps);
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), myProps);
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(new Order(1, 111));
			logger.log(Logger.Level.ERROR, "Did not receive expected PersistenceException");
		} catch (PersistenceException pe) {
			logger.log(Logger.Level.TRACE, "Received expected PersistenceException");
			pass = true;
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected Exception", ex);
		}
		if (!pass) {
			throw new Exception("createEntityManagerFactoryNoBeanValidatorTest failed");
		}
	}

	/*
	 * @testName: createEntityManagerFactoryStringMapTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1480;
	 * 
	 * @test_Strategy: Create an EntityManagerFactory via String,Map
	 */
	@Test
	public void createEntityManagerFactoryStringMapTest() throws Exception {
		boolean pass = false;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(),
					getPersistenceUnitProperties());
			if (emf != null) {
				logger.log(Logger.Level.TRACE, "Received non-null EntityManagerFactory");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received null EntityManagerFactory");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("createEntityManagerFactoryStringTest failed");
		}
	}

}
