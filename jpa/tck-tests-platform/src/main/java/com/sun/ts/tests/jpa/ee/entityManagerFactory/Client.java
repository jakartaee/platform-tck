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

package com.sun.ts.tests.jpa.ee.entityManagerFactory;

import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.entityManagerFactory.Order;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties props = null;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_ee_entityManagerFactory.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setupNoData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupNoData");
		try {
			super.setup();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			removeTestData();
			createOrderTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupNoData() throws Exception {
		super.cleanup();
	}

	public void cleanup() throws Exception {
		removeTestData();
		logger.log(Logger.Level.TRACE, "done cleanup, calling super.cleanup");
		super.cleanup();
	}

	/*
	 * 
	 * /*
	 * 
	 * @testName: createEntityManagerFactoryStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:146;
	 * 
	 * @test_Strategy: Create an EntityManagerFactory via String
	 */
	@Test
	public void createEntityManagerFactoryStringTest() throws Exception {
		boolean pass = false;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName());
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

	private void createOrderTestData() {

		try {
			getEntityTransaction().begin();
			Order[] orders = new Order[5];
			orders[0] = new Order(1, 111);
			orders[1] = new Order(2, 222);
			orders[2] = new Order(3, 333);
			orders[3] = new Order(4, 444);
			orders[4] = new Order(5, 555);

			for (Order o : orders) {
				logger.log(Logger.Level.TRACE, "Persisting order:" + o.toString());
				getEntityManager().persist(o);
			}
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			clearCache();
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
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
