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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.basic;



import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		return createDeploymentJar("jpa_core_basic.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logTrace( "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: updateOrderTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
	 * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
	 * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
	 * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
	 * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
	 * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
	 * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
	 * PERSISTENCE:SPEC:846
	 * 
	 * @test_Strategy: With basic entity requirements, persist/remove an entity.
	 */
	@Test
	public void updateOrderTest() throws Exception {
		boolean pass = true;
		final int count = 6;
		Order order = null;
		getEntityTransaction().begin();
		for (int i = 1; i < count; i++) {
			order = new Order(i, 100 * i);
			getEntityManager().persist(order);
			logTrace( "persisted order " + order);
		}
		getEntityTransaction().commit();

		logTrace( "find and removing the previously persisted orders");
		getEntityTransaction().begin();
		for (int i = 1; i < count; i++) {
			order = getEntityManager().find(Order.class, i);
			if (order != null) {
				getEntityManager().remove(order);
				logTrace( "Found and removed order " + order);
			} else {
				logErr( "persisted order[" + i + "] DOES NOT EXIST");
				pass = false;
			}
		}
		getEntityTransaction().commit();
		if (!pass) {

			logTrace( "clearing the persistence context");
			clearCache();

			logTrace( "verify the previously removed orders were removed");
			for (int i = 1; i < count; i++) {
				order = getEntityManager().find(Order.class, i);
				if (order == null) {
					logTrace( "persisted order[" + i + "] was removed successfully");
				} else {
					logErr( "order[" + i + "] was NOT removed");
					pass = false;
				}
			}
		}
		if (!pass) {
			throw new Exception("updateOrderTest failed");
		}
	}

	/*
	 * @testName: newEntityTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1375;
	 * 
	 * @test_Strategy: Instantiate entity and verify it didn't get persisted
	 */
	@Test
	public void newEntityTest() throws Exception {
		boolean pass = false;
		logTrace( "Instantiate an order ");
		Order order = new Order(1, 101);
		logTrace( "Try to find it");
		Order order2 = getEntityManager().find(Order.class, 1);
		if (order2 == null) {
			logTrace( "Did not find order as expected");
			pass = true;
		} else {
			logErr( "Found order when it should not exist" + order2.toString());
		}

		if (!pass) {
			throw new Exception("newEntityTest failed");
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
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
