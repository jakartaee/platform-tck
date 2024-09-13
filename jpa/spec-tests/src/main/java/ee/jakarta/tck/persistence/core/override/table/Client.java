/*
 * Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.table;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;




import ee.jakarta.tck.persistence.common.PMClientBase;

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
			logErr( "Exception:test failed ", e);
		}
	}

	/*
	 * @testName: testNoTableAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1028; PERSISTENCE:SPEC:1028.1;
	 * 
	 * @test_Strategy: Table and Entity are defined in orm.xml without using its
	 * annotation. The following test reads the entity and table names from the
	 * orm.xml and persists the entity.
	 */
		public void testNoTableAnnotation() throws Exception {
		NoTableAnnotation entity = new NoTableAnnotation();
		entity.setId(ID);
		try {
			getEntityTransaction().begin();
			logTrace( "persisting entity" + entity);
			getEntityManager().persist(entity);
			logTrace( "flushing");
			getEntityManager().flush();
			logTrace( "Test Passed");
		} catch (Exception e) {
			logErr( "test failed");
			throw new Exception(e);
		}
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
