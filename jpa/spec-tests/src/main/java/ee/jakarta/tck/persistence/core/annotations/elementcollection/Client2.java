/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.elementcollection;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.annotations.assocoverride.Client;

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
	 * @testName: elementCollectionBasicType
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2007;
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
	
	public void elementCollectionBasicType() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Customer expected = new Customer("1");
			List<String> expectedphones = new ArrayList<String>();
			expectedphones.add("781-442-2010");
			expectedphones.add("781-442-2011");
			expectedphones.add("781-442-2012");

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
					for (String s : cust.getPhones()) {
						logTrace( "phone:" + s);
					}
					pass = true;
				} else {
					logErr( "Did not get expected results.");
					for (String s : expectedphones) {
						logErr( "expected:" + s);
					}
					logErr( "actual:");
					for (String s : cust.getPhones()) {
						logErr( "actual:" + s);
					}
				}
			} else {
				logErr( "Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("elementCollectionBasicType failed");
		}
	}

	/*
	 * @testName: elementCollectionBasicTypeXMLTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2008;
	 * 
	 * @test_Strategy: ElementCollection of a basic type using mapping file to
	 * define annotation
	 */
	
	public void elementCollectionBasicTypeXMLTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			CustomerXML expected = new CustomerXML("1");
			List<String> expectedphones = new ArrayList<String>();
			expectedphones.add("781-442-2010");
			expectedphones.add("781-442-2011");
			expectedphones.add("781-442-2012");

			expected.setPhones(expectedphones);
			logTrace( "Persisting Customer:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace( "find the previously persisted Customer and Country and verify them");
			CustomerXML cust = getEntityManager().find(CustomerXML.class, expected.getId());
			if (cust != null) {
				logTrace( "Found CustomerXML: " + cust.toString());
				if (cust.getPhones().containsAll(expectedphones) && expectedphones.containsAll(cust.getPhones())
						&& cust.getPhones().size() == expectedphones.size()) {
					logTrace( "Received expected Phones:");
					for (String s : cust.getPhones()) {
						logTrace( "phone:" + s);
					}
					pass = true;
				} else {
					logErr( "Did not get expected results.");
					for (String s : expectedphones) {
						logErr( "expected:" + s);
					}
					logErr( "actual:");
					for (String s : cust.getPhones()) {
						logErr( "actual:" + s);
					}
				}
			} else {
				logErr( "Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("elementCollectionBasicTypeXMLTest failed");
		}
	}
	/*
	 *
	 * Business Methods to set up data for Test Cases
	 *
	 */

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
