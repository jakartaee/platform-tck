/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.orderby;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client2 extends PMClientBase {



	List<Address> addrRef;

	Address addr1 = null;

	Address addr2 = null;

	Address addr3 = null;

	List<Address2> addrRef2;

	Address2 addr11 = null;

	Address2 addr12 = null;

	Address2 addr13 = null;

	public Client2() {
	}

	public void setupAddress(String[] args, Properties p) throws Exception {
		logTrace( "setupAddress");
		try {
			super.setup(args,p);
			removeAddressData();
			createAddressData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: propertyDotNotationTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2089; PERSISTENCE:SPEC:2092;
	 * PERSISTENCE:SPEC:2091;
	 * 
	 * @test_Strategy: identifier is the name of the respective property
	 *
	 */

	public void propertyDotNotationTest() throws Exception {
		boolean pass = false;

		try {
			addrRef = new ArrayList<Address>();
			List<Address> expected = new ArrayList<Address>();
			expected.add(addr2);
			expected.add(addr3);
			expected.add(addr1);

			clearCache();
			A a = getEntityManager().find(A.class, "1");

			List<Address> actual = a.getAddressList();

			if (actual.size() == expected.size()) {
				int count = 0;
				for (int i = 0; i < expected.size(); i++) {
					logTrace(
							"Testing - expected[" + expected.get(i) + "], actual[" + actual.get(i) + "]");

					if (expected.get(i).equals(actual.get(i))) {
						count++;
					}
				}
				if (count == expected.size()) {
					pass = true;
				} else {
					logErr( "count=" + count + ", expected size:" + expected.size());
					for (Address aa : expected) {
						logErr( "expected:" + aa);
					}
					logErr( "------------");
					for (Address aa : actual) {
						logErr( "actual:" + aa);
					}
				}
			} else {
				logErr(
						"Expected list size:" + expected.size() + ", actual size:" + actual.size());
				for (Address aa : expected) {
					logErr( "expected:" + aa);
				}
				logErr( "------------");
				for (Address aa : actual) {
					logErr( "actual:" + aa);
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("propertyDotNotationTest failed");
		}
	}

	/*
	 * @testName: fieldDotNotationTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2089; PERSISTENCE:SPEC:2092;
	 * PERSISTENCE:SPEC:2091;
	 * 
	 * @test_Strategy: identifier is the name of the respective field
	 *
	 */
	public void fieldDotNotationTest() throws Exception {
		boolean pass = false;

		try {
			addrRef = new ArrayList<Address>();
			List<Address2> expected = new ArrayList<Address2>();
			expected.add(addr12);
			expected.add(addr13);
			expected.add(addr11);

			logTrace( "Clearing the cache");
			clearCache();
			A2 a = getEntityManager().find(A2.class, "2");

			List<Address2> actual = a.getAddressList();

			if (actual.size() == expected.size()) {
				int count = 0;
				for (int i = 0; i < expected.size(); i++) {
					logTrace(
							"Testing - expected[" + expected.get(i) + "], actual[" + actual.get(i) + "]");

					if (expected.get(i).equals(actual.get(i))) {
						count++;
					}
				}

				if (count == expected.size()) {
					pass = true;
				} else {
					logTrace( "count=" + count + ", expected size:" + expected.size());
					for (Address2 aa : expected) {
						logErr( "expected:" + aa);
					}
					logErr( "------------");
					for (Address2 aa : actual) {
						logErr( "actual:" + aa);
					}
				}
			} else {
				logErr(
						"Expected list size:" + expected.size() + ", actual size:" + actual.size());
				for (Address2 aa : expected) {
					logErr( "expected:" + aa);
				}
				logErr( "------------");
				for (Address2 aa : actual) {
					logErr( "actual:" + aa);
				}
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("propertyDotNotationTest failed");
		}
	}

	private void createAddressData() throws Exception {
		try {
			logTrace( "createAddressData");
			getEntityTransaction().begin();

			addr1 = new Address("1 Network Drive", "Burlington", "MA", new ZipCode("01801"));
			addr2 = new Address("634 Goldstar Road", "Peabody", "MA", new ZipCode("88444"));
			addr3 = new Address("3212 Boston Road", "Chelmsford", "MA", new ZipCode("01824"));
			addrRef = new ArrayList<Address>();
			addrRef.add(addr1);
			addrRef.add(addr2);
			addrRef.add(addr3);
			A a1 = new A("1", "b1", addrRef);

			addr11 = new Address2("1 Network Drive", "Burlington", "MA", new ZipCode2("01801"));
			addr12 = new Address2("634 Goldstar Road", "Peabody", "MA", new ZipCode2("88444"));
			addr13 = new Address2("3212 Boston Road", "Chelmsford", "MA", new ZipCode2("01824"));
			addrRef2 = new ArrayList<Address2>();
			addrRef2.add(addr11);
			addrRef2.add(addr12);
			addrRef2.add(addr13);
			A2 a2 = new A2("2", "b2", addrRef2);

			getEntityManager().persist(a1);
			getEntityManager().persist(a2);

			getEntityManager().flush();
			getEntityManager().refresh(a1);
			getEntityManager().refresh(a2);

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

	public void cleanupAddress() throws Exception {
		try {
			logTrace( "cleanup");
			removeAddressData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	private void removeAddressData() {
		logTrace( "removeAddressData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from COLTAB_ADDRESS").executeUpdate();
			getEntityManager().createNativeQuery("Delete from COLTAB").executeUpdate();
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
