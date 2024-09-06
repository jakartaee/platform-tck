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

package ee.jakarta.tck.persistence.core.annotations.entity;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static Coffee cRef[] = new Coffee[5];

	public Client() {
	}

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Coffee" };
		return createDeploymentJar("jpa_core_annotations_entity.jar", pkgNameWithoutSuffix, classes);

	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			createDeployment();
			removeTestData();
			logTrace( "Create Test data");
			createTestData();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: annotationEntityTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:993; PERSISTENCE:SPEC:995;
	 * PERSISTENCE:JAVADOC:29; PERSISTENCE:SPEC:762; PERSISTENCE:SPEC:402;
	 * PERSISTENCE:SPEC:404;
	 * 
	 * @test_Strategy: The name annotation element defaults to the unqualified name
	 * of the entity class. This name is used to refer to the entities in queries.
	 * 
	 * Name the entity using a lower case name and ensure the query can be executed
	 * with the lower case entity name as the abstract schema name.
	 * 
	 */
	@Test
	public void annotationEntityTest1() throws Exception {

		logTrace( "Begin annotationEntityTest1");
		boolean pass = true;
		List c = null;

		try {
			getEntityTransaction().begin();
			final String[] expectedBrands = new String[] { "vanilla creme", "mocha", "hazelnut", "decaf",
					"breakfast blend" };

			logTrace( "find coffees by brand name");
			c = getEntityManager().createQuery("Select c.brandName from cof c ORDER BY c.brandName DESC")
					.setMaxResults(10).getResultList();

			final String[] result = (String[]) (c.toArray(new String[c.size()]));
			logTrace( "Compare results of Coffee Brand Names");
			pass = Arrays.equals(expectedBrands, result);

			if (!pass) {
				logErr( "Did not get expected results.  Expected 5 Coffees : "
						+ "vanilla creme, mocha, hazelnut, decaf, breakfast blend. " + " Received: " + c.size());
				Iterator it = c.iterator();
				while (it.hasNext()) {
					logTrace( " Coffee Brand Name: " + it.next());
				}
			} else {
				logTrace( "Expected results received");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("annotationEntityTest1 failed");
	}

	/*
	 * @testName: annotationEntityTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:993; PERSISTENCE:SPEC:995;
	 * PERSISTENCE:JAVADOC:29
	 * 
	 * @test_Strategy: The name annotation element defaults to the unqualified name
	 * of the entity class. This name is used to refer to the entities in queries.
	 * 
	 * Name the entity using a different name than the entity class name and ensure
	 * the query can be executed with the lower case entity name as the abstract
	 * schema name selecting teh
	 * 
	 */
	@Test
	public void annotationEntityTest2() throws Exception {

		logTrace( "Begin annotationEntityTest2");
		boolean pass1 = true;
		boolean pass2 = false;
		List c = null;

		try {
			getEntityTransaction().begin();
			final Integer[] expectedPKs = new Integer[] { 21, 22, 23, 24, 25 };

			logTrace( "find all coffees");
			c = getEntityManager().createQuery("Select c from cof c").setMaxResults(10).getResultList();

			if (c.size() != 5) {
				logErr(
						"Did not get expected results.  Expected 5 references, got: " + c.size());
				pass1 = false;
			} else if (pass1) {
				logTrace( "Expected size received, verify contents . . . ");
				Iterator i = c.iterator();
				int foundCof = 0;
				while (i.hasNext()) {
					logTrace( "Check List for expected coffees");
					Coffee o = (Coffee) i.next();
					for (int l = 0; l < 5; l++) {
						if (expectedPKs[l].equals(o.getId())) {
							logTrace( "Found coffee with PK: " + o.getId());
							foundCof++;
							break;
						}
					}
				}
				if (foundCof != 5) {
					logErr( "anotationEntityTest2: Did not get expected results");
					pass2 = false;
				} else {
					logTrace( "Expected results received");
					pass2 = true;
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass2 = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("annotationEntityTest1 failed");
	}

	/*
	 * 
	 * Business Methods to set up data for Test Cases
	 */

	private void createTestData() throws Exception {
		try {

			logTrace( "createTestData");

			getEntityTransaction().begin();
			logTrace( "Create 5 Coffees");
			cRef[0] = new Coffee(21, "hazelnut", 1.0F);
			cRef[1] = new Coffee(22, "vanilla creme", 2.0F);
			cRef[2] = new Coffee(23, "decaf", 3.0F);
			cRef[3] = new Coffee(24, "breakfast blend", 4.0F);
			cRef[4] = new Coffee(25, "mocha", 5.0F);

			logTrace( "Start to persist coffees ");
			for (Coffee coffee : cRef) {
				getEntityManager().persist(coffee);
				logTrace( "persisted coffee " + coffee);
			}
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected Exception creating test data:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
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

        }
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM COFFEE").executeUpdate();
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
