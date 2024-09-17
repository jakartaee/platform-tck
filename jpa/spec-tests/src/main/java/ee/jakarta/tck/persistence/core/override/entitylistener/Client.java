/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.entitylistener;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;




import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.override.util.CallBackCounts;

public class Client extends PMClientBase {



	private static final Long ID = 1L;

	public Client() {
	}

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: testOverrideEntityListener
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested by overriding entity listener in
	 * XML file.
	 */
		public void testOverrideEntityListener() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		CallBackCounts.clearCountsMap();
		OverridenListener entity = new OverridenListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		logTrace( "persisted entity" + entity);
		getEntityManager().remove(entity);
		getEntityManager().flush();
		logTrace( "Removed entity" + entity);
		getEntityTransaction().commit();
		try {
			pass1 = checkPersistCallBacks();
			pass2 = checkRemoveCallBacks();
			if ((pass1 && pass2) == true) {
				logTrace( "testOverrideEntityListener Passed");
			} else if (pass1 == true) {
				throw new Exception("Test failed while testing preremove and "
						+ "postremove methods in testOverrideEntityListener ");
			} else if (pass2 == true) {
				throw new Exception("Test failed while testing prepersist and "
						+ "postpersist methods in testOverrideEntityListener ");
			}

		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideEntityListener" + e);
		}

	}

	/*
	 * @testName: testEntityListenerXML
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested by using entitylistener with
	 * empty xml tag.
	 */
		public void testEntityListenerXML() throws Exception {

		boolean pass = false;
		CallBackCounts.clearCountsMap();
		NoEntityListener entity = new NoEntityListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		logTrace( "persisted entity" + entity);
		try {
			pass = checkPersistCallBacks();
			if (pass == true) {
				logTrace( "Test Passed");
			} else {
				throw new Exception("TestEntityListenerXML method failed");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testEntityListenerXML" + e);
		} finally {
			getEntityManager().remove(entity);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testNoEntityListener
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested without using entitylistener.
	 */
		public void testNoEntityListener() throws Exception {

		boolean pass = false;
		CallBackCounts.clearCountsMap();
		NoListener entity = new NoListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		logTrace( "persisted entity" + entity);
		try {
			pass = checkPersistCallBacks();
			if (pass == true) {
				logTrace( "Test Passed");
			} else {
				throw new Exception("TestNoEntityListener method failed");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoEntityListener" + e);
		} finally {
			getEntityManager().remove(entity);
			getEntityTransaction().commit();
		}
	}

	private boolean checkPersistCallBacks() throws Exception {
		boolean result = false;
		if (test("prePersist", 1) && (test("postPersist", 1))) {
			result = true;
		}
		return result;
	}

	private boolean checkRemoveCallBacks() throws Exception {
		boolean result = false;
		if (test("preRemove", 1) && (test("preRemove", 1))) {
			result = true;
		}
		return result;

	}

	private boolean test(final String callBackName, final int expectedCount) throws Exception {
		int count = CallBackCounts.getCount(callBackName);
		boolean result = false;
		if (count == expectedCount) {
			logTrace( "test passed" + callBackName);
			result = true;
		} else {
			logErr( "test failed" + callBackName);
		}
		return result;
	}

	
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM NOENTITYLISTENER_TABLE").executeUpdate();
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
