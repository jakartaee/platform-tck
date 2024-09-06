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
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;




import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class Client1 extends Client {

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			
			getEntityManager();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}



	/*
	 * @testName: parameterTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:1093; PERSISTENCE:JAVADOC:1092;
	 * 
	 * @test_Strategy: Create a query with 2 named parameters and retrieve
	 * information about the parameters.
	 */
		public void parameterTest1() throws Exception {
		logTrace( "Starting parameterTest1");
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		boolean pass6 = false;
		boolean pass7 = false;
		boolean pass8 = false;
		boolean pass9 = false;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);

			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();
			ParameterExpression<String> pe = qbuilder.parameter(String.class, "first");
			Class<?> c = pe.getParameterType();
			if (c.isAssignableFrom(java.lang.String.class)) {
				logTrace( "Received expected type from getParameterType()");
				pass1 = true;
			} else {
				logErr( "Expected type String from getParameterType(), instead got:" + c);
			}
			String name = pe.getName();
			if (name != null) {
				if (!name.equals("first")) {
					logErr( "getName() returned wrong name, expected: first, actual:" + name);
				} else {
					pass2 = true;
				}
			} else {
				logErr( "getName() returned null");
			}
			Integer position = pe.getPosition();
			if (position != null) {
				logErr( "getPosition() returned:" + position + ", instead of null");
			} else {
				pass3 = true;
			}

			ParameterExpression<String> pe2 = qbuilder.parameter(String.class, "last");
			criteria.add(qbuilder.equal(employee.get("firstName"), pe));
			criteria.add(qbuilder.equal(employee.get("lastName"), pe2));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);

			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logTrace( "parameter name = " + p.getName());
				logTrace( "parameter position = " + p.getPosition());
				logTrace( "parameter type =" + p.getParameterType());
			}

			String sExpected = "first";
			Parameter p1 = q.getParameter(sExpected);
			String sActual = p1.getName();
			if (!sActual.equals(sExpected)) {
				logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass4 = true;
			}

			sExpected = null;
			Integer iActual = p1.getPosition();
			if (iActual != null) {
				logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass5 = true;
			}
			sExpected = "java.lang.String";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass6 = true;
			}

			sExpected = "last";
			Parameter p2 = q.getParameter(sExpected);
			sActual = p2.getName();
			if (!sActual.equals(sExpected)) {
				logErr( "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass7 = true;
			}
			sExpected = null;
			iActual = p2.getPosition();
			if (iActual != null) {
				logErr( "p2.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass8 = true;
			}

			sExpected = "java.lang.String";
			sActual = p2.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p2.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass9 = true;
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7 || !pass8 || !pass9) {
			throw new Exception("parameterTest1 test failed");

		}

	}

	/*
	 * @testName: parameterTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a named parameter that is a float and
	 * retrieve information about the parameter.
	 */
		public void parameterTest2() throws Exception {
		logTrace( "Starting parameterTest2");
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);
			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();
			ParameterExpression<Float> pe = qbuilder.parameter(Float.class, "salary");
			criteria.add(qbuilder.equal(employee.get("salary"), pe));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);

			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logTrace( "parameter name = " + p.getName());
				logTrace( "parameter position = " + p.getPosition());
				logTrace( "parameter type =" + p.getParameterType());
			}

			String sExpected = "salary";
			Parameter p1 = q.getParameter(sExpected);
			String sActual = p1.getName();
			if (!sActual.equals(sExpected)) {
				logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass1 = true;
			}

			sExpected = null;
			Integer iActual = p1.getPosition();
			if (iActual != null) {
				logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass2 = true;
			}
			sExpected = "java.lang.Float";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass3 = true;
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("parameterTest2 test failed");

		}
	}

	/*
	 * @testName: parameterTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
	 * PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a named parameter that is a date and and
	 * retrieve information about the parameter.
	 */
		public void parameterTest3() throws Exception {
		logTrace( "Starting parameterTest3");
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);
			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();
			ParameterExpression<java.sql.Date> pe = qbuilder.parameter(java.sql.Date.class, "hdate");
			criteria.add(qbuilder.equal(employee.get("hireDate"), pe));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);

			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logTrace( "parameter name = " + p.getName());
				logTrace( "parameter position = " + p.getPosition());
				logTrace( "parameter type =" + p.getParameterType());
			}

			String sExpected = "hdate";
			Parameter p1 = q.getParameter(sExpected);
			String sActual = p1.getName();
			if (!sActual.equals(sExpected)) {
				logErr( "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass1 = true;
			}

			sExpected = null;
			Integer iActual = p1.getPosition();
			if (iActual != null) {
				logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
			} else {
				pass2 = true;
			}
			sExpected = "java.sql.Date";
			sActual = p1.getParameterType().getName();
			if (!sActual.equals(sExpected)) {
				logErr(
						"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
			} else {
				pass3 = true;
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("parameterTest3 test failed");

		}
	}

	/*
	 * @testName: parameterTest4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:382; PERSISTENCE:JAVADOC:383;
	 * 
	 * @test_Strategy: Create a query with a parameter where the name is not
	 * specified and retrieve information about the parameter.
	 */
		public void parameterTest4() throws Exception {
		logTrace( "Starting parameterTest4");
		boolean pass1 = false;
		boolean pass2 = true;
		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
		if (cquery != null) {
			Root<Employee> employee = cquery.from(Employee.class);
			cquery.select(employee);

			List<Predicate> criteria = new ArrayList<Predicate>();

			// No name is being assigned to the parameter
			ParameterExpression<String> pe = qbuilder.parameter(String.class);

			criteria.add(qbuilder.equal(employee.get("firstName"), pe));

			cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

			Query q = getEntityManager().createQuery(cquery);
			List<Object> list = new ArrayList<Object>(q.getParameters());
			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				logTrace( "parameter position = " + p.getPosition());
				logTrace( "parameter type =" + p.getParameterType());
			}

			for (int i = 0; i < list.size(); i++) {
				Parameter p = (Parameter) list.get(i);
				pass1 = true;
				// the value returned by getName() in this instance where no name has
				// been assigned to the parameter is undefined in the spec therefore
				// we will not test for it.

				String sExpected = null;
				Integer iActual = p.getPosition();
				if (iActual != null) {
					logErr( "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
				}
				sExpected = "java.lang.String";
				String sActual = p.getParameterType().getName();
				if (!sActual.equals(sExpected)) {
					logErr(
							"p1.getParameterType() - Expected: " + sExpected + ", actual:" + sActual);
				}
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2) {
			throw new Exception("parameterTest4 test failed");

		}

	}

}
