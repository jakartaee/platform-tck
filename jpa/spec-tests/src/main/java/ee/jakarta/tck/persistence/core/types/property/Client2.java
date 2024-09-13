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

package ee.jakarta.tck.persistence.core.types.property;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.types.common.Grade;

public class Client2 extends PMClientBase {



	public Client2() {
	}
	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeCustTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
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
			logTrace( "Persisting Customer:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace( "find the previously persisted Customer and Country and verify them");
			Customer cust = getEntityManager().find(Customer.class, expected.getId());
			if (cust != null) {
				logTrace( "Found Customer: " + cust.toString());
				if (cust.getPhones().containsAll(expectedphones) && expectedphones.containsAll(cust.getPhones())
						&& cust.getPhones().size() == expectedphones.size()) {
					logTrace( "Received expected Phones:");
					for (Grade g : cust.getPhones()) {
						logTrace( "phone:" + g);
					}
					pass = true;
				} else {
					logErr( "Did not get expected results.");
					for (Grade g : expectedphones) {
						logErr( "expected:" + g);
					}
					logErr( "actual:");
					for (Grade g : cust.getPhones()) {
						logErr( "actual:" + g);
					}
				}
			} else {
				logErr( "Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("elementCollectionTest failed");
		}
	}

	// Methods used for Tests

	public void cleanupCust() throws Exception {
		try {
			logTrace( "cleanup");
			removeCustTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	private void removeCustTestData() {
		logTrace( "removeCustTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
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
