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

package ee.jakarta.tck.persistence.core.override.callbacklistener;



import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.override.util.CallBackCounts;

public class Client extends PMClientBase {

	

	public Client() {
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
	 * @testName: postLoad
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:702;
	 * PERSISTENCE:SPEC:703; PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707;
	 * PERSISTENCE:SPEC:708; PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710;
	 * PERSISTENCE:SPEC:711; PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713;
	 * PERSISTENCE:SPEC:714; PERSISTENCE:SPEC:715; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:717; PERSISTENCE:SPEC:718; PERSISTENCE:SPEC:719;
	 * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:721;
	 * 
	 * @test_Strategy: CallBack methods are tested using callback listeners.
	 */
	@Test
	public void postLoad() throws Exception {
		boolean pass3 = false;
		final Long ID = 1L;

		CallBackCounts.clearCountsMap();
		OverridenCallBack entity = new OverridenCallBack();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		getEntityManager().refresh(entity);
		getEntityManager().remove(entity);
		getEntityTransaction().commit();
		logTrace( "persisted entity" + entity);
		try {
			pass3 = checkLoadCallBacks();

			if (pass3) {
				logTrace( "testOverrideCallBackMethods Passed");
			} else {
				throw new Exception("Test failed while testing postLoad method");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing postLoad" + e);
		}
	}

	/*
	 * @testName: preAndPostPersist
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:702;
	 * PERSISTENCE:SPEC:703; PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707;
	 * PERSISTENCE:SPEC:708; PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710;
	 * PERSISTENCE:SPEC:711; PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713;
	 * PERSISTENCE:SPEC:714; PERSISTENCE:SPEC:715; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:717; PERSISTENCE:SPEC:718; PERSISTENCE:SPEC:719;
	 * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:721;
	 * 
	 * @test_Strategy: CallBack methods are tested using callback listeners.
	 */
	@Test
	public void preAndPostPersist() throws Exception {
		boolean pass1 = false;
		final Long ID = 1L;

		CallBackCounts.clearCountsMap();
		OverridenCallBack entity = new OverridenCallBack();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		getEntityManager().remove(entity);
		getEntityTransaction().commit();
		logTrace( "persisted entity" + entity);
		try {
			pass1 = checkPersistCallBacks();

			if (pass1) {
				logTrace( "testOverrideCallBackMethods Passed");
			} else {
				throw new Exception("Test failed while testing prepersist and " + "postpersist methods");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing preAndPostPersist" + e);
		}
	}

	/*
	 * @testName: preAndPostRemove
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:702;
	 * PERSISTENCE:SPEC:703; PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707;
	 * PERSISTENCE:SPEC:708; PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710;
	 * PERSISTENCE:SPEC:711; PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713;
	 * PERSISTENCE:SPEC:714; PERSISTENCE:SPEC:715; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:717; PERSISTENCE:SPEC:718; PERSISTENCE:SPEC:719;
	 * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:721;
	 * 
	 * @test_Strategy: CallBack methods are tested using callback listeners.
	 */
	@Test
	public void preAndPostRemove() throws Exception {
		boolean pass2 = false;
		final Long ID = 1L;

		CallBackCounts.clearCountsMap();
		OverridenCallBack entity = new OverridenCallBack();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		getEntityManager().remove(entity);
		getEntityManager().flush();
		getEntityTransaction().commit();
		logTrace( "persisted entity" + entity);
		try {
			pass2 = checkRemoveCallBacks();

			if (pass2) {
				logTrace( "testOverrideCallBackMethods Passed");
			} else {
				throw new Exception("Test failed while testing preremove and " + "postremove methods");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing preAndPostRemove" + e);
		}
	}

	private boolean checkPersistCallBacks() throws Exception {
		boolean result = false;
		if (test("prePersistFromXML", 1) && test("postPersistFromXML", 1)) {
			result = true;
		}
		return result;

	}

	private boolean checkRemoveCallBacks() throws Exception {
		boolean result = false;
		if (test("preRemoveFromXML", 1) && test("postRemoveFromXML", 1)) {
			result = true;
		}
		return result;
	}

	private boolean checkLoadCallBacks() throws Exception {
		boolean result = false;
		if (test("postLoadFromXML", 1)) {
			result = true;
		}
		return result;
	}

	private boolean test(final String callBackName, final int expectedCount) throws Exception {

		String thisTestId = callBackName;
		boolean pass = false;

		int count = CallBackCounts.getCount(callBackName);
		if (count == expectedCount) {
			logTrace( "test passed in test method" + thisTestId);
			pass = true;

		} else {
			logErr( "test failed in test method" + thisTestId);
			logTrace( "in CallBackName =" + callBackName);
			logTrace( "in expectedCount =" + expectedCount);
			logTrace( "in ActualCount =" + count);
		}

		return pass;

	}

	@AfterEach
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
