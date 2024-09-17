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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaQuery;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Address_;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.Order_;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client1 extends Util {

	

	

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: fromClass
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:935; PERSISTENCE:SPEC:1509;
	 * PERSISTENCE:SPEC:1513; PERSISTENCE:SPEC:1792; PERSISTENCE:SPEC:1700;
	 *
	 * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
		public void fromClass() throws Exception {
		boolean pass = false;
		final Double expectedTotalPrice = 4500.0D;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(getEntityManager().getMetamodel().entity(Order.class));

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.lt(order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					expectedTotalPrice)));

			Query q = getEntityManager().createQuery(cquery);

			List result = q.getResultList();
			int expectedResultSize = 3;

			if (result != null) {
				if (result.size() == expectedResultSize) {
					logTrace( "Successfully returned expected results");
					pass = true;
				} else {
					logErr( "Mismatch in received results - expected = " + expectedResultSize
							+ " received = " + result.size());
				}
			} else {
				logErr( "Missing expected result");
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("fromClass failed");

		}
	}

	/*
	 * @testName: fromEntityType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1096; PERSISTENCE:JAVADOC:1104;
	 * PERSISTENCE:JAVADOC:936;
	 *
	 * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
		public void fromEntityType() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		final Double expectedTotalPrice = 4500.0D;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(getEntityManager().getMetamodel().entity(Order.class));

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();

			Path<Order> p = (Path) order;
			Expression exp = (Expression) p;

			Expression type = p.type();
			if (type != null) {
				logTrace( "Path.type() returned non-null result");
				pass1 = true;
			} else {
				logErr( "Expected non-null Path.type()");
			}
			cquery.select(order);
			cquery.where(cbuilder.not(
					cbuilder.lt(p.get(Order_.getSingularAttribute("totalPrice", Double.class)), expectedTotalPrice)));

			Query q = getEntityManager().createQuery(cquery);

			List result = q.getResultList();
			int expectedResultSize = 3;

			if (result != null) {
				if (result.size() == expectedResultSize) {
					logTrace( "Successfully returned expected results");
					pass2 = true;
				} else {
					logErr( "Mismatch in received results - expected = " + expectedResultSize
							+ " received = " + result.size());
				}
			} else {
				logErr( "Missing expected result");
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("fromEntityType failed");

		}
	}

	/*
	 * @testName: select
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:930; PERSISTENCE:SPEC:1751;
	 * PERSISTENCE:SPEC:1752; PERSISTENCE:SPEC:1753;
	 *
	 * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
		public void select() throws Exception {
		boolean pass = false;
		final Double expectedTotalPrice = 4500.0D;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.lt(order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					expectedTotalPrice)));

			Query q = getEntityManager().createQuery(cquery);

			List result = q.getResultList();
			int expectedResultSize = 3;

			if (result != null) {
				if (result.size() == expectedResultSize) {
					logTrace( "Successfully returned expected results");
					pass = true;
				} else {
					logErr( "Mismatch in received results - expected = " + expectedResultSize
							+ " received = " + result.size());
				}
			} else {
				logErr( "Missing expected result");
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("select test failed");

		}
	}

	/*
	 * @testName: wherePredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:933; PERSISTENCE:SPEC:1726;
	 * PERSISTENCE:SPEC:1728; PERSISTENCE:SPEC:1728.1; PERSISTENCE:SPEC:1795;
	 * 
	 * @test_Strategy: Pass a predicate array to the where clause and verify results
	 */
	@SetupMethod(name = "setupOrderData")
		public void wherePredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(Integer.valueOf(customerRef[3].getId()));
		Collections.sort(expected);
		List<Integer> actual = new ArrayList<Integer>();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logMsg( "Testing initial query");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get("name"), "Robert E. Bissett"));
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			logMsg( "Modify query but this change should not yet take effect");
			cquery.where(cbuilder.like(customer.get(Customer_.home).get(Address_.zip), "%77"));

			Predicate[] predArray = { cbuilder.like(customer.get(Customer_.name), "Karen%"),
					cbuilder.like(customer.get(Customer_.name), "%Tegan") };
			cquery.where(predArray);
			List<Customer> result = tquery.getResultList();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}

			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logErr( "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logTrace( "Expected results received");
				pass1 = true;
			}
			logMsg( "Now verify query change does take effect");
			actual.clear();
			expected.clear();
			result.clear();
			expected.add(Integer.valueOf(customerRef[5].getId()));
			tquery = getEntityManager().createQuery(cquery);

			result = tquery.getResultList();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
				logTrace( "Found id:" + c.getId());
			}

			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logErr( "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logTrace( "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception: ", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("wherePredicateArrayTest failed");
		}
	}

	/*
	 * @testName: fromGetParentPathTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1033;
	 * 
	 * @test_Strategy: Verify getParentPath() returns correct result.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
		public void fromGetParentPathTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		String expected = "ee.jakarta.tck.persistence.common.schema30.Order";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			From<Order, Order> from = (From) order;

			Path path = from.getParentPath();
			if (path == null) {
				logTrace( "Received expected null");
				pass1 = true;
			} else {
				logErr( "Expected:null, actual:" + path);
			}

			Path p = from.join(Order_.creditCard).getParentPath();
			if (p != null) {
				String name = p.getModel().getBindableJavaType().getName();
				if (name != null) {
					if (name.equals(expected)) {
						logTrace( "Received expected name:" + name);
						pass2 = true;
					} else {
						logErr( "Expected:" + expected + ", actual:" + name);
					}
				} else {
					logErr(
							"Null was returned for p.getModel().getBindableJavaType().getName()");
					logErr( "p.getModel:" + p.getModel());
					logErr(
							"p.getModel().getBindableJavaType():" + p.getModel().getBindableJavaType());
					logErr( "p.getModel().getBindableJavaType().getName():"
							+ p.getModel().getBindableJavaType().getName());
				}
			} else {
				logErr( "getParentPath() returned null");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception: ", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fromGetParentPathTest failed");
		}
	}

	/*
	 * @testName: getParameters
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:919
	 *
	 * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
		public void getParameters() throws Exception {
		boolean pass = false;
		final Double expectedTotalPrice = 4500.0D;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			cquery.select(order);

			cquery.where(cbuilder.not(cbuilder.lt(order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					expectedTotalPrice)));

			Set<ParameterExpression<?>> paramSet = cquery.getParameters();

			if (paramSet != null) {
				if (paramSet.isEmpty()) {
					pass = true;
					logTrace( "Received expected results");
				} else {
					logErr( "Received Incorrect results");
				}
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("getParameters test failed");

		}
	}

	/*
	 * @testName: distinctNotSpecifiedTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1664;
	 * 
	 * @test_Strategy: Verify duplicates are returned when distinct is not specified
	 * SELECT o.CUSTOMER.ID FROM ORDER_TABLE o
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
		public void distinctNotSpecifiedTest() throws Exception {
		boolean pass = false;
		Integer expectedPKs[];
		List<String> o;

		try {
			logTrace( "find All customer ids from Orders");

			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Order> order = cquery.from(Order.class);
				EntityType<Order> Order_ = order.getModel();

				Metamodel mm = getEntityManager().getMetamodel();
				EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);

				cquery.select(order.get(Order_.getSingularAttribute("customer", Customer.class))
						.get(Customer_.getSingularAttribute("id", String.class)));

				TypedQuery<String> tq = getEntityManager().createQuery(cquery);
				o = tq.getResultList();

				expectedPKs = new Integer[20];
				expectedPKs[0] = Integer.parseInt("1");
				expectedPKs[1] = Integer.parseInt("2");
				expectedPKs[2] = Integer.parseInt("3");
				expectedPKs[3] = Integer.parseInt("4");
				expectedPKs[4] = Integer.parseInt("4");
				expectedPKs[5] = Integer.parseInt("5");
				expectedPKs[6] = Integer.parseInt("6");
				expectedPKs[7] = Integer.parseInt("7");
				expectedPKs[8] = Integer.parseInt("8");
				expectedPKs[9] = Integer.parseInt("9");
				expectedPKs[10] = Integer.parseInt("10");
				expectedPKs[11] = Integer.parseInt("11");
				expectedPKs[12] = Integer.parseInt("12");
				expectedPKs[13] = Integer.parseInt("13");
				expectedPKs[14] = Integer.parseInt("14");
				expectedPKs[15] = Integer.parseInt("14");
				expectedPKs[16] = Integer.parseInt("15");
				expectedPKs[17] = Integer.parseInt("16");
				expectedPKs[18] = Integer.parseInt("17");
				expectedPKs[19] = Integer.parseInt("18");

				if (!checkEntityPK(o, expectedPKs, true, true)) {
					logErr( "Did not get expected results.  Expected " + expectedPKs.length
							+ " references, got: " + o.size());
				} else {
					logTrace( "Expected results received");
					pass = true;
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("distinctNotSpecifiedTest failed");
	}

}
