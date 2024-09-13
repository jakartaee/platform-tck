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

package ee.jakarta.tck.persistence.core.criteriaapi.From;


import java.util.ArrayList;
import java.util.List;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Department;
import ee.jakarta.tck.persistence.common.schema30.Department_;
import ee.jakarta.tck.persistence.common.schema30.Employee;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class Client4 extends Util {


	public static void main(String[] args) {
		Client4 theTests = new Client4();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	/*
	 * @testName: joinMapAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:992;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d FROM Department d JOIN d.lastNameEmployees e WHERE (e.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
		public void joinMapAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();

			CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
			Root<Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> employee = department.join(Department_.lastNameEmployees);
			cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
			TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
			List<Department> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logErr(
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapAttributeTest failed");
		}
	}

	/*
	 * @testName: joinMapAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:996;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d FROM Department d INNER JOIN d.lastNameEmployees e WHERE (e.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
		public void joinMapAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			getEntityTransaction().begin();

			CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
			Root<Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> employee = department.join(Department_.lastNameEmployees,
					JoinType.INNER);
			cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
			TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
			List<Department> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logErr(
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinMapStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1009;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d.id, d.name, e.lastname FROM DEPARTMENT d JOIN d.lastNameEmployees e
	 * WHERE (e.id = 1)
	 *
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
		public void joinMapStringTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();

		expected.add("1, Marketing, Frechette");

		List<String> actual = new ArrayList<String>();
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			From<Department, Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> mEmployee = department.joinMap("lastNameEmployees");

			cquery.where(cbuilder.equal(mEmployee.get("id"), "1"));
			cquery.multiselect(department.get(Department_.id), department.get(Department_.name),
					mEmployee.value().<String>get("lastName"));

			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			List<Tuple> clist = tquery.getResultList();

			for (Tuple t : clist) {
				logTrace( "result:" + t.get(0) + ", " + t.get(1) + ", " + t.get(2));
				actual.add(t.get(0) + ", " + t.get(1) + ", " + t.get(2));
			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logTrace( "Received expected results");
				pass = true;
			} else {
				logErr( "Did not get expected results:");
				for (String s : expected) {
					logErr( "expected:" + s);
				}
				for (String s : actual) {
					logErr( "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapStringTest failed");
		}
	}

	/*
	 * @testName: joinMapStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1011;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT d.id, d.name, e.lastname FROM DEPARTMENT d INNER JOIN
	 * d.lastNameEmployees e WHERE (e.id = 1)
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
		public void joinMapStringJoinTypeTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();

		expected.add("1, Marketing, Frechette");

		List<String> actual = new ArrayList<String>();
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			From<Department, Department> department = cquery.from(Department.class);
			MapJoin<Department, String, Employee> mEmployee = department.joinMap("lastNameEmployees", JoinType.INNER);

			cquery.where(cbuilder.equal(mEmployee.get("id"), "1"));
			cquery.multiselect(department.get(Department_.id), department.get(Department_.name),
					mEmployee.value().<String>get("lastName"));

			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			List<Tuple> clist = tquery.getResultList();

			for (Tuple t : clist) {
				logTrace( "result:" + t.get(0) + ", " + t.get(1) + ", " + t.get(2));
				actual.add(t.get(0) + ", " + t.get(1) + ", " + t.get(2));
			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logTrace( "Received expected results");
				pass = true;
			} else {
				logErr( "Did not get expected results:");
				for (String s : expected) {
					logErr( "expected:" + s);
				}
				for (String s : actual) {
					logErr( "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: fromGetMapAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1028;
	 * 
	 * @test_Strategy:
	 *
	 * SELECT d.lastNameEmployees FROM DEPARTMENT d WHERE d.ID = 1
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
		public void fromGetMapAttributeTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();
		expected.add("1, Alan, Frechette");
		expected.add("3, Shelly, McGowan");
		expected.add("5, Stephen, DMilla");

		List<String> actual = new ArrayList<String>();
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();

			CriteriaQuery cquery = cbuilder.createQuery(Employee.class);
			From<Department, Department> department = cquery.from(Department.class);
			cquery.where(cbuilder.equal(department.get("id"), 1));
			cquery.select(department.get(Department_.lastNameEmployees));
			TypedQuery tquery = getEntityManager().createQuery(cquery);
			List<Employee> list = tquery.getResultList();

			for (Employee e : list) {
				logTrace(
						" employee:" + e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());
				actual.add(e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logTrace( "Received expected results");
				pass = true;
			} else {
				logErr( "Did not get expected results:");
				for (String s : expected) {
					logErr( "expected:" + s);
				}
				for (String s : actual) {
					logErr( "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("fromGetMapAttributeTest failed");
		}
	}

	/*
	 * @testName: pathGetMapAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1098;
	 * 
	 * @test_Strategy:
	 *
	 * SELECT d.lastNameEmployees FROM DEPARTMENT d WHERE d.ID = 1
	 */
	@SetupMethod(name = "setupDepartmentEmployeeData")
		public void pathGetMapAttributeTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();
		expected.add("1, Alan, Frechette");
		expected.add("3, Shelly, McGowan");
		expected.add("5, Stephen, DMilla");

		List<String> actual = new ArrayList<String>();
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();

			CriteriaQuery cquery = cbuilder.createQuery(Employee.class);
			Path<Department> department = cquery.from(Department.class);
			cquery.where(cbuilder.equal(department.get("id"), 1));
			cquery.select(department.get(Department_.lastNameEmployees));
			TypedQuery tquery = getEntityManager().createQuery(cquery);
			List<Employee> list = tquery.getResultList();

			for (Employee e : list) {
				logTrace(
						" employee:" + e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());
				actual.add(e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logTrace( "Received expected results");
				pass = true;
			} else {
				logErr( "Did not get expected results:");
				for (String s : expected) {
					logErr( "expected:" + s);
				}
				for (String s : actual) {
					logErr( "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("pathGetMapAttributeTest failed");
		}
	}

}
