/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa.ee.packaging.jar;


import java.util.Properties;

import ee.jakarta.tck.persistence.common.PMClientBase;
import org.junit.jupiter.api.Test;

public class Client extends PMClientBase {



	public Client() {
	}

	/*
	 * @class.setup_props:
	 */
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args, p);
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: JarFileElementsTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:957
	 *
	 * @test_Strategy: With the above archive (jar-file elements), deploy, create
	 * entities persist, then find.
	 *
	 */
	@Test
	public void JarFileElementsTest() throws Exception {
		boolean pass = true;
		final int count = 2;
		getEntityTransaction().begin();
		for (int i = 1; i <= count; i++) {
			A a = new A(Integer.toString(i), "name_" + Integer.toString(i), i);
			getEntityManager().persist(a);
			logTrace( "persisted order " + a.toString());
		}

		for (int i = 1 + count; i <= count + count; i++) {
			C c = new C(Integer.toString(i), "name_" + Integer.toString(i), i);
			getEntityManager().persist(c);
			logTrace( "persisted order " + c.toString());
		}

		for (int i = 1; i <= count; i++) {
			B b = new B(Integer.toString(i), "name_" + Integer.toString(i), i);
			getEntityManager().persist(b);
			logTrace( "persisted order " + b.toString());
		}

		getEntityTransaction().commit();

		logTrace( "find the previously persisted entities");
		for (int i = 1; i <= count; i++) {
			A a = getEntityManager().find(A.class, Integer.toString(i));
			if (a != null) {
				logTrace( "Find returned non-null A entity:" + a.toString());
			} else {
				logErr( "persisted A[" + i + "] DOES NOT EXIST");
				pass = false;
			}
		}

		for (int i = 1 + count; i <= count + count; i++) {
			C c = getEntityManager().find(C.class, Integer.toString(i));
			if (c != null) {
				logTrace( "Find returned non-null C entity:" + c.toString());
			} else {
				logErr( "persisted C[" + i + "] DOES NOT EXIST");
				pass = false;
			}
		}

		for (int i = 1; i <= count; i++) {
			B b = getEntityManager().find(B.class, Integer.toString(i));
			if (b != null) {
				logTrace( "Find returned non-null B entity:" + b.toString());
			} else {
				logErr( "persisted B[" + i + "] DOES NOT EXIST");
				pass = false;
			}
		}
		if (!pass) {
			throw new Exception("JarFileElementsTest failed");
		}
	}

	@Override
	public void cleanup() throws Exception {
		logTrace( "cleanup");
		removeTestData();
		logTrace( "cleanup complete, calling super.cleanup");
		super.cleanup();
	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
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
