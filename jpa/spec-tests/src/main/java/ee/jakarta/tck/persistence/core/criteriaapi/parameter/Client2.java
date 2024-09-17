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

package ee.jakarta.tck.persistence.core.criteriaapi.parameter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;




import com.sun.ts.lib.harness.SetupMethod;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class Client2 extends Client {

	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			
			getEntityManager();
			removeTestData();
			createTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}


	/*
	 * @testName: parameterExpressionInObjectArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1086;
	 * 
	 * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 IN (1, 2))
	 */
	@SetupMethod(name = "setupEmployee")
		public void parameterExpressionInObjectArrayTest() throws Exception {
		boolean pass = false;
		try {
			List<Integer> expected = new ArrayList<Integer>();
			expected.add(empRef[0].getId());
			expected.add(empRef[1].getId());
			expected.add(empRef[2].getId());
			expected.add(empRef[3].getId());
			expected.add(empRef[4].getId());

			getEntityTransaction().begin();
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
			if (cquery != null) {
				Root<Employee> employee = cquery.from(Employee.class);

				cquery.select(employee);

				List<Predicate> criteria = new ArrayList<Predicate>();
				ParameterExpression<Integer> pe = qbuilder.parameter(Integer.class, "num");

				Object[] o = { Integer.valueOf(1), Integer.valueOf(2) };

				criteria.add(pe.in(o));

				cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

				Query q = getEntityManager().createQuery(cquery);

				q.setParameter("num", 1);
				List<Employee> result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : result) {
					actual.add(e.getId());
				}
				Collections.sort(actual);
				logTrace( "actual" + actual);

				if (expected.equals(actual)) {
					logTrace( "Successfully returned expected results");
					pass = true;
				} else {
					logErr( "expected: " + expected + ", actual: " + actual);
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
			getEntityTransaction().commit();
		} finally {
			removeTestData();
		}

		if (!pass) {
			throw new Exception("parameterExpressionInObjectArrayTest test failed");

		}
	}

	/*
	 * @testName: parameterExpressionInExpressionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1087;
	 * 
	 * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 IN (1, 2))
	 */
	@SetupMethod(name = "setupEmployee")
		public void parameterExpressionInExpressionArrayTest() throws Exception {
		boolean pass = false;
		try {
			List<Integer> expected = new ArrayList<Integer>();
			expected.add(empRef[0].getId());
			expected.add(empRef[1].getId());
			expected.add(empRef[2].getId());
			expected.add(empRef[3].getId());
			expected.add(empRef[4].getId());

			getEntityTransaction().begin();
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
			if (cquery != null) {
				Root<Employee> employee = cquery.from(Employee.class);

				cquery.select(employee);

				List<Predicate> criteria = new ArrayList<Predicate>();
				ParameterExpression<String> pe = qbuilder.parameter(String.class, "sid");

				Expression[] exp = new Expression[] { qbuilder.literal("1"), qbuilder.literal("2") };

				criteria.add(pe.in(exp));

				cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

				Query q = getEntityManager().createQuery(cquery);

				q.setParameter("sid", "1");
				List<Employee> result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : result) {
					actual.add(e.getId());
				}
				Collections.sort(actual);
				logTrace( "actual" + actual);

				if (expected.equals(actual)) {
					logTrace( "Successfully returned expected results");
					pass = true;
				} else {
					logErr( "expected: " + expected + ", actual: " + actual);
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
			getEntityTransaction().commit();
		} finally {
			removeTestData();
		}

		if (!pass) {
			throw new Exception("parameterExpressionInExpressionArrayTest test failed");

		}
	}

	/*
	 * @testName: parameterExpressionInCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1088;
	 * 
	 * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 IN (1, 2))
	 */
	@SetupMethod(name = "setupEmployee")
		public void parameterExpressionInCollectionTest() throws Exception {
		boolean pass = false;
		try {
			List<Integer> expected = new ArrayList<Integer>();
			expected.add(empRef[0].getId());
			expected.add(empRef[1].getId());
			expected.add(empRef[2].getId());
			expected.add(empRef[3].getId());
			expected.add(empRef[4].getId());

			getEntityTransaction().begin();
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
			if (cquery != null) {
				Root<Employee> employee = cquery.from(Employee.class);

				cquery.select(employee);

				List<Predicate> criteria = new ArrayList<Predicate>();
				ParameterExpression<String> pe = qbuilder.parameter(String.class, "sid");

				Collection col = new ArrayList();
				col.add("1");
				col.add("2");

				criteria.add(pe.in(col));

				cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

				Query q = getEntityManager().createQuery(cquery);

				q.setParameter("sid", "1");
				List<Employee> result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : result) {
					actual.add(e.getId());
				}
				Collections.sort(actual);
				logTrace( "actual" + actual);

				if (expected.equals(actual)) {
					logTrace( "Successfully returned expected results");
					pass = true;
				} else {
					logErr( "expected: " + expected + ", actual: " + actual);
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
			getEntityTransaction().commit();
		} finally {
			removeTestData();
		}

		if (!pass) {
			throw new Exception("parameterExpressionInCollectionTest test failed");

		}
	}

	/*
	 * @testName: parameterExpressionInExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1089;
	 * 
	 * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 = 1)
	 */
	@SetupMethod(name = "setupEmployee")
		public void parameterExpressionInExpressionTest() throws Exception {
		boolean pass = false;
		try {
			List<Integer> expected = new ArrayList<Integer>();
			expected.add(empRef[0].getId());
			expected.add(empRef[1].getId());
			expected.add(empRef[2].getId());
			expected.add(empRef[3].getId());
			expected.add(empRef[4].getId());

			getEntityTransaction().begin();
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
			if (cquery != null) {
				Root<Employee> employee = cquery.from(Employee.class);

				cquery.select(employee);

				List<Predicate> criteria = new ArrayList<Predicate>();
				ParameterExpression<String> pe = qbuilder.parameter(String.class, "sid");

				Expression exp = qbuilder.literal("1");

				criteria.add(pe.in(exp));

				cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

				Query q = getEntityManager().createQuery(cquery);

				q.setParameter("sid", "1");
				List<Employee> result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : result) {
					actual.add(e.getId());
				}
				Collections.sort(actual);
				logTrace( "actual" + actual);

				if (expected.equals(actual)) {
					logTrace( "Successfully returned expected results");
					pass = true;
				} else {
					logErr( "expected: " + expected + ", actual: " + actual);
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
			getEntityTransaction().commit();
		} finally {
			removeTestData();
		}

		if (!pass) {
			throw new Exception("parameterExpressionInExpressionTest test failed");

		}
	}

}
