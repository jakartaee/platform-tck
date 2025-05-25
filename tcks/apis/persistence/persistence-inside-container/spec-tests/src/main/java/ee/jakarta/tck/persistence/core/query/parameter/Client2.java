/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.query.parameter;


import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Query;

public class Client2 extends PMClientBase {



	protected final Employee empRef[] = new Employee[5];

	public Client2() {
	}
	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setupEmployee");
		try {
			super.setup(args,p);
			getEntityManager();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}
	public void setupEmployee(String[] args, Properties p) throws Fault {
		TestUtil.logTrace("setupEmployee");
		try {
			super.setup(args, p);
			createEmployeeData();
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Fault("Setup failed:", e);
		}
	}
	public void cleanup() throws Exception {
		TestUtil.logTrace("calling super.cleanup");
		super.cleanup();
	}

	/*
	 * @testName: parameterPositionalTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1636; PERSISTENCE:SPEC:1638;
	 * PERSISTENCE:SPEC:1640;
	 * 
	 * @test_Strategy: Create a query with a having clause with a positional
	 * parameter and retrieve information about the parameter.
	 */
	@SetupMethod(name = "setupEmployee")
	@CleanupMethod(name = "cleanupEmployee")
	public void parameterPositionalTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT e.id FROM Employee e WHERE e.id > ?2 GROUP BY e.id HAVING e.id <=?1")
					.setParameter(2, 2).setParameter(1, 4).getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "3";
			expectedPKs[1] = "4";

			if (!checkEntityPK(result, expectedPKs)) {
				logErr( "Expected 1 result, got: " + result.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterPositionalTest failed");
	}

	/*
	 * @testName: parameterUpdateTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1636; PERSISTENCE:SPEC:1638;
	 * PERSISTENCE:SPEC:1639;
	 * 
	 * @test_Strategy: Create an update query with a positional parameter and
	 * retrieve information about the parameter.
	 */
	@SetupMethod(name = "setupEmployee")
	@CleanupMethod(name = "cleanupEmployee")
	public void parameterUpdateTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			Query q = getEntityManager().createQuery("UPDATE Employee e SET e.firstName=?1, e.lastName=?1 WHERE e.id=1")
					.setParameter(1, "foo");

			int result_size = q.executeUpdate();
			if (result_size == 1) {
				logTrace( "Updated 1 rows");
			}

			doFlush();
			clearCache();
			Employee emp = getEntityManager().find(Employee.class, 1);

			if (emp.getFirstName().equals("foo") && emp.getLastName().equals("foo")) {
				logTrace( "Received expected result:" + emp.toString());
				pass = true;
			} else {
				logErr( "Expected: firstName=foo, lastName=foo, actual: firstName="
						+ emp.getFirstName() + ", lastName=" + emp.getLastName());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterUpdateTest failed");
	}

	/*
	 * @testName: parameterCaseTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1641;
	 * 
	 * @test_Strategy: Create a query with a name parameter using different cases
	 * and retrieve information about the parameter.
	 */
	@SetupMethod(name = "setupEmployee")
	@CleanupMethod(name = "cleanupEmployee")
	public void parameterCaseTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT e.id FROM Employee e WHERE e.id > :Id GROUP BY e.id HAVING e.id <=:iD")
					.setParameter("Id", 2).setParameter("iD", 4).getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "3";
			expectedPKs[1] = "4";

			if (!checkEntityPK(result, expectedPKs)) {
				logErr( "Expected 1 result, got: " + result.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterCaseTest failed");
	}

	/*
	 * @testName: parameterNamedParameterTwiceTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1642;
	 * 
	 * @test_Strategy: Create a query using a name parameter twice and retrieve
	 * information about the parameter.
	 */
	@SetupMethod(name = "setupEmployee")
	@CleanupMethod(name = "cleanupEmployee")
	public void parameterNamedParameterTwiceTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			Query q = getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.id >=:ID AND e.id <=:ID");
			result = q.setParameter("ID", 2).getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";

			if (!checkEntityPK(result, expectedPKs)) {
				logErr( "Expected 1 result, got: " + result.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterNamedParameterTwiceTest failed");
	}

	public void createEmployeeData() throws Exception {
		logTrace( "createDepartmentEmployeeData");
		getEntityTransaction().begin();

		try {
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[1] = new Employee(2, "Arthur", "Frechette");
			empRef[2] = new Employee(3, "Shelly", "McGowan");
			empRef[3] = new Employee(4, "Robert", "Bissett");
			empRef[4] = new Employee(5, "Stephen", "DMilla");
			logTrace( "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logTrace( "persisted employee " + emp.getId());
				}
			}
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("createDepartmentEmployeeData failed:", e);
		}
	}

	public void cleanupEmployee() throws Exception {
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
