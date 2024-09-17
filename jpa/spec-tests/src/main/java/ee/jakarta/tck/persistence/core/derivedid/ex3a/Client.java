/*
 * Copyright (c) 2009, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.derivedid.ex3a;


import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {



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
	 * @testName: DIDTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:1341; PERSISTENCE:SPEC:1182;
	 * PERSISTENCE:SPEC:1183; PERSISTENCE:SPEC:1184; PERSISTENCE:SPEC:1185;
	 *
	 * @test_Strategy: Derived Identifier The parent entity uses EmbeddedId Case
	 * (a): The dependent entity uses IdClass
	 */
		public void DIDTest() throws Exception {
		boolean pass = false;

		try {

			getEntityTransaction().begin();
			final DID3EmployeeId eId1 = new DID3EmployeeId("Java", "Duke");
			final DID3EmployeeId eId2 = new DID3EmployeeId("C", "foo");

			final DID3Employee employee1 = new DID3Employee(eId1);
			final DID3Employee employee2 = new DID3Employee(eId2);

			final DID3DependentId depId1 = new DID3DependentId("Obama", eId1);
			final DID3DependentId depId2 = new DID3DependentId("Michelle", eId1);
			final DID3DependentId depId3 = new DID3DependentId("John", eId2);

			final DID3Dependent dep1 = new DID3Dependent(depId1, employee1);
			final DID3Dependent dep2 = new DID3Dependent(depId2, employee1);
			final DID3Dependent dep3 = new DID3Dependent(depId3, employee2);

			getEntityManager().persist(employee1);
			getEntityManager().persist(employee2);
			getEntityManager().persist(dep1);
			getEntityManager().persist(dep2);
			getEntityManager().persist(dep3);

			getEntityManager().flush();
			logTrace( "persisted Employees and Dependents");

			// Refresh dependent
			DID3Dependent newDependent = getEntityManager().find(DID3Dependent.class, depId1);
			if (newDependent != null) {
				getEntityManager().refresh(newDependent);
			}

			List depList = getEntityManager()
					.createQuery("Select d from DID3Dependent d where d.name2='Obama' and d.emp.empId.firstName='Java'")
					.getResultList();
			newDependent = null;
			if (depList.size() > 0) {
				newDependent = (DID3Dependent) depList.get(0);
				if (newDependent == dep1) {
					pass = true;
					logTrace( "Received Expected Dependent");
				} else {
					logErr( "Searched Dependent not found");
				}
			} else {
				logErr( "getEntityManager().createQuery returned null entry");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			getEntityTransaction().rollback();
		}

		if (!pass) {
			throw new Exception("DTDTest failed");
		}
	}

	
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
			getEntityManager().createNativeQuery("DELETE FROM DID3DEPENDENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DID3EMPLOYEE").executeUpdate();
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
