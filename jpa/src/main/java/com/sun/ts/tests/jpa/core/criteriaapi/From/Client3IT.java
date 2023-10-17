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

package com.sun.ts.tests.jpa.core.criteriaapi.From;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;

public class Client3IT extends Util {

	private static final Logger logger = (Logger) System.getLogger(Client3IT.class.getName());

	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3IT.class.getPackageName();
		String pkgName = Client3IT.class.getPackageName() + ".";
		String[] classes = {};
		return createDeploymentJar("jpa_core_criteriaapi_from3.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: joinCollectionAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:989;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.orders o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			CollectionJoin order = customer.join(Customer_.orders);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionAttributeTest failed");
		}
	}

	/*
	 * @testName: joinCollectionAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:993;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.orders o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			CollectionJoin<Customer, Order> order = customer.join(Customer_.orders, JoinType.INNER);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinCollectionStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1001;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.orders o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			CollectionJoin<Customer, Order> order = customer.joinCollection("orders");
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionStringTest failed");
		}
	}

	/*
	 * @testName: joinCollectionStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1003;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.orders o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			CollectionJoin<Customer, Order> order = customer.joinCollection("orders", JoinType.INNER);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinSetAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:990;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.orders2 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			SetJoin<Customer, Order> order = customer.join(Customer_.orders2);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetAttributeTest failed");
		}
	}

	/*
	 * @testName: joinSetAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:994
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.orders2 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			SetJoin<Customer, Order> order = customer.join(Customer_.orders2, JoinType.INNER);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinSetStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1013;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.orders2 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			SetJoin<Customer, Order> order = customer.joinSet("orders2");
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetStringTest failed");
		}
	}

	/*
	 * @testName: joinSetStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1015;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.orders o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			SetJoin<Customer, Order> order = customer.joinSet("orders2", JoinType.INNER);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinListAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:991;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.orders3 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			ListJoin<Customer, Order> order = customer.join(Customer_.orders3);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListAttributeTest failed");
		}
	}

	/*
	 * @testName: joinListAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:995;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.orders3 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			ListJoin<Customer, Order> order = customer.join(Customer_.orders3, JoinType.INNER);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinListStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1005;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.orders3 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			ListJoin<Customer, Order> order = customer.joinList("orders3");
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListStringTest failed");
		}
	}

	/*
	 * @testName: joinListStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1007;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.orders3 o WHERE (o.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			ListJoin<Customer, Order> order = customer.joinList("orders3", JoinType.INNER);
			cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: fromGetCorrelationParentTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:983
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fromGetCorrelationParentTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			Subquery<String> sq = cquery.subquery(String.class);
			From<Customer, Customer> sqc = sq.correlate(customer);
			boolean isCorr = sqc.isCorrelated();
			if (isCorr) {
				logger.log(Logger.Level.TRACE, "From.isCorrelated() return true");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected From.isCorrelated() to return true, actual:" + isCorr);
			}
			From f = sqc.getCorrelationParent();
			String name = f.getJavaType().getSimpleName();
			if (name.equals("Customer")) {
				logger.log(Logger.Level.TRACE, "Received expected parent:" + name);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected getCorrelationParent() to return Customer, actual:" + name);
			}
			Join<Customer, Address> w = sqc.join("work");
			sq.select(w.<String>get("state"));
			sq.where(cbuilder.equal(w.get("state"), cbuilder.parameter(String.class, "state")));
			cquery.where(customer.get("home").get("state").in(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("state", "MA");
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[11];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "7";
			expectedPKs[5] = "8";
			expectedPKs[6] = "9";
			expectedPKs[7] = "11";
			expectedPKs[8] = "13";
			expectedPKs[9] = "15";
			expectedPKs[10] = "18";
			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_in:  Did not get expected results. "
						+ " Expected 11 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("fromGetCorrelationParentTest failed");
		}
	}
}
