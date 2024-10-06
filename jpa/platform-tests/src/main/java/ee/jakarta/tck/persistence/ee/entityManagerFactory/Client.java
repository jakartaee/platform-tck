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

package ee.jakarta.tck.persistence.ee.entityManagerFactory;


import java.util.Properties;


import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.entityManagerFactory.Order;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Client extends PMClientBase {



	Properties props = null;

	public Client() {
	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
			createOrderTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	public void cleanup() throws Exception {
		removeTestData();
		logTrace( "done cleanup, calling super.cleanup");
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

	public void createEntityManagerFactoryStringTest() throws Exception {
		boolean pass = false;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName());
			if (emf != null) {
				logTrace( "Received non-null EntityManagerFactory");
				pass = true;
			} else {
				logErr( "Received null EntityManagerFactory");
			}
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
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
				logTrace( "Persisting order:" + o.toString());
				getEntityManager().persist(o);
			}
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			clearCache();
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
