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

package com.sun.ts.tests.jpa.core.types.property;

import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.types.common.Grade;


public class Client2IT extends PMClientBase {

	public Client2IT() {
	}

	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2IT.class.getPackageName();
		String pkgName = Client2IT.class.getPackageName() + ".";
		String[] classes = { Grade.class.getCanonicalName(), pkgName + "Customer" };
		return createDeploymentJar("jpa_core_types_property2.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeAll
	public void setupCust() throws Exception {
		TestUtil.logTrace("setup");
		try {
			super.setup();
			createDeployment();
			removeCustTestData();
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * @testName: elementCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2017; PERSISTENCE:SPEC:2018;
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
	@Test
	public void elementCollectionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Customer expected = new Customer("1");
			// Since this column in db can take Strings just use
			// that for the test
			List<Grade> expectedphones = new ArrayList<Grade>();
			expectedphones.add(Grade.A);
			expectedphones.add(Grade.B);
			expectedphones.add(Grade.C);

			expected.setPhones(expectedphones);
			TestUtil.logTrace("Persisting Customer:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			TestUtil.logTrace("find the previously persisted Customer and Country and verify them");
			Customer cust = getEntityManager().find(Customer.class, expected.getId());
			if (cust != null) {
				TestUtil.logTrace("Found Customer: " + cust.toString());
				if (cust.getPhones().containsAll(expectedphones) && expectedphones.containsAll(cust.getPhones())
						&& cust.getPhones().size() == expectedphones.size()) {
					TestUtil.logTrace("Received expected Phones:");
					for (Grade g : cust.getPhones()) {
						TestUtil.logTrace("phone:" + g);
					}
					pass = true;
				} else {
					TestUtil.logErr("Did not get expected results.");
					for (Grade g : expectedphones) {
						TestUtil.logErr("expected:" + g);
					}
					TestUtil.logErr("actual:");
					for (Grade g : cust.getPhones()) {
						TestUtil.logErr("actual:" + g);
					}
				}
			} else {
				TestUtil.logErr("Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("elementCollectionTest failed");
		}
	}

	// Methods used for Tests

	@AfterAll
	public void cleanupCust() throws Exception {
		TestUtil.logTrace("cleanup");
		removeCustTestData();
		TestUtil.logTrace("cleanup complete, calling super.cleanup");
		super.cleanup();
		removeDeploymentJar();
	}

	private void removeCustTestData() {
		TestUtil.logTrace("removeCustTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
