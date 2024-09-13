/*
 * Copyright (c) 2014, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeyenumerated;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.ts.lib.harness.Status;





import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	

	private static final long serialVersionUID = 20L;

	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	private static Employee empRef[] = new Employee[5];
	private static Employee2 empRef2[] = new Employee2[5];
	private static Employee3 empRef3[] = new Employee3[5];
	private static Employee4 empRef4[] = new Employee4[5];

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			
			removeTestData();
			createTestData();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * public void setup(String[] args, Properties p) throws Exception {
	 * logTrace("setup"); try { super.setup(args,p);
	 * removeCustTestData(); } catch (Exception e) {
	 * logErr("Exception: ", e); throw new
	 * Exception("Setup failed:", e);
	 * 
	 * } }
	 */

	/*
	 * @testName: mapKeyEnumeratedTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2051; PERSISTENCE:SPEC:2052;
	 * PERSISTENCE:SPEC:2052.2
	 *
	 * @test_Strategy:
	 *
	 */
		public void mapKeyEnumeratedTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Offices> expected = new HashSet<Offices>();
		expected.add(Offices.OFF000);
		expected.add(Offices.OFF002);
		expected.add(Offices.OFF004);

		try {
			getEntityTransaction().begin();
			Employee emp = getEntityManager().find(Employee.class, 1);
			logTrace( "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department dept = emp.getDepartment();
			logTrace( "Dept=" + dept.getName());
			Map<Offices, Employee> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Offices, Employee> entry : emps.entrySet()) {
					logTrace( "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<Offices> keys = emps.keySet();
			for (Offices key : keys) {
				logTrace( "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logTrace( "Received expected keys");
				pass1 = true;
			} else {
				logErr( "Did not received expected keys");
				logErr( "Expected:");
				for (Offices key : expected) {
					logTrace( "key:" + key);
				}
				logErr( "Actual:");
				for (Offices key : keys) {
					logTrace( "key:" + key);
				}
			}

			Set<Employee> sExpected = new HashSet<Employee>();
			sExpected.add(empRef[0]);
			sExpected.add(empRef[1]);
			sExpected.add(empRef[2]);

			Collection<Employee> employees = emps.values();
			for (Employee e : employees) {
				logTrace( "values:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logTrace( "Received expected values");
				pass2 = true;
			} else {
				logErr( "Did not received expected values");
				logErr( "Expected:");
				for (Employee e : sExpected) {
					logTrace(
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
				logErr( "Actual:");
				for (Employee e : employees) {
					logTrace(
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("mapKeyEnumeratedTest failed");
		}
	}

	/*
	 * @testName: mapKeyEnumeratedDefaultTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2054; PERSISTENCE:SPEC:2052;
	 * PERSISTENCE:SPEC:2052.2;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void mapKeyEnumeratedDefaultTypeTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Offices> expected = new HashSet<Offices>();
		expected.add(Offices.OFF001);
		expected.add(Offices.OFF003);

		try {
			getEntityTransaction().begin();
			Employee2 emp = getEntityManager().find(Employee2.class, 2);
			logTrace( "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department2 dept = emp.getDepartment();
			logTrace( "Dept=" + dept.getName());
			Map<Offices, Employee2> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Offices, Employee2> entry : emps.entrySet()) {
					logTrace( "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<Offices> keys = emps.keySet();
			for (Offices key : keys) {
				logTrace( "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logTrace( "Received expected keys");
				pass1 = true;
			} else {
				logErr( "Did not received expected keys");
				logErr( "Expected:");
				for (Offices key : expected) {
					logTrace( "key:" + key);
				}
				logErr( "Actual:");
				for (Offices key : keys) {
					logTrace( "key:" + key);
				}
			}

			Set<Employee2> sExpected = new HashSet<Employee2>();
			sExpected.add(empRef2[0]);
			sExpected.add(empRef2[1]);

			Collection<Employee2> employees = emps.values();
			for (Employee2 e : employees) {
				logTrace( "values:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logTrace( "Received expected values");
				pass2 = true;
			} else {
				logErr( "Did not received expected values");
				logErr( "Expected:");
				for (Employee2 e : sExpected) {
					logTrace(
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
				logErr( "Actual:");
				for (Employee2 e : employees) {
					logTrace(
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("mapKeyEnumeratedDefaultTypeTest failed");
		}
	}

	/*
	 * @testName: mapKeyEnumeratedWithMayKeyAnnotationTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2053; PERSISTENCE:SPEC:2052;
	 * PERSISTENCE:SPEC:2052.2;
	 * 
	 * @test_Strategy:
	 *
	 */
		public void mapKeyEnumeratedWithMayKeyAnnotationTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Offices> expected = new HashSet<Offices>();
		expected.add(Offices.OFF001);
		expected.add(Offices.OFF003);

		try {
			getEntityTransaction().begin();
			Employee3 emp = getEntityManager().find(Employee3.class, 6);
			logTrace( "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department3 dept = emp.getDepartment();
			logTrace( "Dept=" + dept.getName());
			@SuppressWarnings("unchecked")
			Map<Offices, Employee3> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Offices, Employee3> entry : emps.entrySet()) {
					logTrace( "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<Offices> keys = emps.keySet();
			for (Offices key : keys) {
				logTrace( "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logTrace( "Received expected keys");
				pass1 = true;
			} else {
				logErr( "Did not received expected keys");
				logErr( "Expected:");
				for (Offices key : expected) {
					logTrace( "key:" + key);
				}
				logErr( "Actual:");
				for (Offices key : keys) {
					logTrace( "key:" + key);
				}
			}

			Set<Employee3> sExpected = new HashSet<Employee3>();
			sExpected.add(empRef3[0]);
			sExpected.add(empRef3[1]);

			Collection<Employee3> employees = emps.values();
			for (Employee3 e : employees) {
				logTrace( "values:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logTrace( "Received expected values");
				pass2 = true;
			} else {
				logErr( "Did not received expected values");
				logErr( "Expected:");
				for (Employee3 e : sExpected) {
					logTrace(
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
				logErr( "Actual:");
				for (Employee3 e : employees) {
					logTrace(
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("mapKeyEnumeratedWithMayKeyAnnotationTest failed");
		}
	}

	/*
	 * @testName: elementCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2052; PERSISTENCE:SPEC:2052.1;
	 * 
	 * @test_Strategy:
	 */
		public void elementCollectionTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Numbers> expected = new HashSet<Numbers>();
		expected.add(Numbers.one);
		expected.add(Numbers.two);

		try {
			getEntityTransaction().begin();
			Employee4 emp = getEntityManager().find(Employee4.class, 8);
			logTrace( "Name:" + emp.getLastName());
			Department4 dept = emp.getDepartment();
			logTrace( "Dept=" + dept.getName());
			Map<Numbers, EmbeddedEmployee> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Numbers, EmbeddedEmployee> entry : emps.entrySet()) {
					logTrace( "map:" + entry.getKey() + ", " + entry.getValue().employeeId + " "
							+ entry.getValue().employeeName);
				}
			}
			Set<Numbers> keys = emps.keySet();
			for (Numbers key : keys) {
				logTrace( "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logTrace( "Received expected keys");
				pass1 = true;
			} else {
				logErr( "Did not received expected keys");
				logErr( "Expected:");
				for (Numbers key : expected) {
					logTrace( "key:" + key);
				}
				logErr( "Actual:");
				for (Numbers key : keys) {
					logTrace( "key:" + key);
				}
			}

			Set<Employee4> sExpected = new HashSet<Employee4>();
			sExpected.add(empRef4[0]);
			sExpected.add(empRef4[1]);

			Collection<Employee4> employees = emps.values().stream()
					.flatMap(a -> Stream.of(new Employee4(a.employeeId, a.employeeName))).collect(Collectors.toList());
			for (Employee4 e : employees) {
				logTrace( "values:" + e.getId() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logTrace( "Received expected values");
				pass2 = true;
			} else {
				logErr( "Did not received expected values");
				logErr( "Expected:");
				for (Employee4 e : sExpected) {
					logTrace( "Employee:" + e.getId() + " " + e.getLastName());
				}
				logErr( "Actual:");
				for (Employee4 e : employees) {
					logTrace( "Employee:" + e.getId() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("elementCollectionTest failed");
		}
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */
	private void createTestData() throws Exception {
		logTrace( "createTestData");
		try {
			getEntityTransaction().begin();

			logTrace( "Create 2 - Departments");
			Department deptRef[] = new Department[2];
			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");

			logTrace( "Persist departments ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logTrace( "persisted department " + dept.getName());
				}
			}

			logTrace( "Create 2 - Department2");
			Department2 deptRef2[] = new Department2[2];
			deptRef2[0] = new Department2(3, "Development");
			deptRef2[1] = new Department2(4, "Release");

			logTrace( "Persist Department2s ");
			for (Department2 dept : deptRef2) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logTrace( "persisted department2 " + dept.getName());
				}
			}

			logTrace( "Create 2 - Department3");
			Department3 deptRef3[] = new Department3[2];
			deptRef3[0] = new Department3(5, "Shipping");
			deptRef3[1] = new Department3(6, "Receiving");

			logTrace( "Persist Department2s ");
			for (Department3 dept : deptRef3) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logTrace( "persisted department3 " + dept.getName());
				}
			}

			logTrace( "Create 2 - Department4");
			Department4 deptRef4[] = new Department4[2];
			deptRef4[0] = new Department4(7, "Accounts_Receivable");
			deptRef4[1] = new Department4(8, "Accounts_Payable");

			logTrace( "Persist Department4 ");
			for (Department4 dept : deptRef4) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logTrace( "persisted department4 " + dept.getName());
				}
			}
			logTrace( "Create 5 - Employee");
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[0].setDepartment(deptRef[0]);

			empRef[1] = new Employee(3, "Shelly", "McGowan");
			empRef[1].setDepartment(deptRef[0]);

			empRef[2] = new Employee(5, "Stephen", "DMilla");
			empRef[2].setDepartment(deptRef[0]);

			empRef2[0] = new Employee2(2, "Arthur", "Frechette");
			empRef2[0].setDepartment(deptRef2[0]);

			empRef2[1] = new Employee2(4, "Robert", "Bissett");
			empRef2[1].setDepartment(deptRef2[0]);

			empRef3[0] = new Employee3(6, "Douglas", "Donahue");
			empRef3[0].setDepartment(deptRef3[0]);

			empRef3[1] = new Employee3(7, "Kellie", "Sanborn");
			empRef3[1].setDepartment(deptRef3[0]);

			empRef4[0] = new Employee4(8, "Grace");
			empRef4[0].setDepartment(deptRef4[0]);

			empRef4[1] = new Employee4(9, "Bender");
			empRef4[1].setDepartment(deptRef4[1]);

			Map<Offices, Employee> link = new HashMap<Offices, Employee>();

			link.put(Offices.OFF000, empRef[0]);
			link.put(Offices.OFF002, empRef[1]);
			link.put(Offices.OFF004, empRef[2]);
			deptRef[0].setLastNameEmployees(link);

			Map<Offices, Employee2> link2 = new HashMap<Offices, Employee2>();
			link2.put(Offices.OFF001, empRef2[0]);
			link2.put(Offices.OFF003, empRef2[1]);
			deptRef2[0].setLastNameEmployees(link2);

			Map<Offices, Employee3> link3 = new HashMap<Offices, Employee3>();
			link3.put(Offices.OFF001, empRef3[0]);
			link3.put(Offices.OFF003, empRef3[1]);
			deptRef3[0].setLastNameEmployees(link3);

			Map<Numbers, EmbeddedEmployee> link4 = new HashMap<Numbers, EmbeddedEmployee>();
			link4.put(Numbers.one, new EmbeddedEmployee(empRef4[0]));
			link4.put(Numbers.two, new EmbeddedEmployee(empRef4[1]));
			deptRef4[0].setLastNameEmployees(link4);

			logTrace( "Persist Employee ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logTrace( "persisted Employee " + emp.getId());
				}
			}
			logTrace( "Persist Employee2 ");
			for (Employee2 emp : empRef2) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logTrace( "persisted Employee2 " + emp.getId());
				}
			}

			logTrace( "Persist Employee3 ");
			for (Employee3 emp : empRef3) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logTrace( "persisted Employee3 " + emp.getId());
				}
			}

			logTrace( "Persist Employee4 ");
			for (Employee4 emp : empRef4) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logTrace( "persisted Employee4 " + emp.getId());
				}
			}
			// Merge Department
			logTrace( "Merge Department ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logTrace( "merged Department " + dept.getName());

				}
			}

			// Merge Department
			logTrace( "Merge Department2 ");
			for (Department2 dept : deptRef2) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logTrace( "merged Department2 " + dept.getName());

				}
			}

			// Merge Department
			logTrace( "Merge Department3 ");
			for (Department3 dept : deptRef3) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logTrace( "merged Department3 " + dept.getName());

				}
			}
			// Merge Department
			logTrace( "Merge Department4 ");
			for (Department4 dept : deptRef4) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logTrace( "merged Department4 " + dept.getName());

				}
			}

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

	
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	/*
	 * public void cleanupCust() throws Exception {
	 * logTrace("cleanup"); removeCustTestData();
	 * logTrace("cleanup complete, calling super.cleanup");
	 * super.cleanup(); }
	 */

	private void removeTestData() {
		logTrace( "removeTestData");

		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL").executeUpdate();
			getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL2").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT2").executeUpdate();
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

	/*
	 * private void removeCustTestData() {
	 * logTrace("removeCustTestData"); if
	 * (getEntityTransaction().isActive()) { getEntityTransaction().rollback(); }
	 * try { getEntityTransaction().begin();
	 * getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate(
	 * );
	 * getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
	 * getEntityTransaction().commit(); } catch (Exception e) {
	 * logErr("Exception encountered while removing entities:", e); } finally { try {
	 * if (getEntityTransaction().isActive()) { getEntityTransaction().rollback(); }
	 * } catch (Exception re) {
	 * logErr("Unexpected Exception in removeTestData:", re);
	 * } } }
	 */
}
