/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.Department;
import com.sun.ts.tests.jpa.common.schema30.Department_;
import com.sun.ts.tests.jpa.common.schema30.Employee;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;

public class Client extends Util {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	/* Test setup */
	@BeforeAll
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "Entering Setup");
		try {
			super.setup();
			getEntityManager();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: joinStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:997;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void joinStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> address = customer.join("work");
			Expression e = cbuilder.literal("4");
			cquery.where(address.get("id").in(e)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";

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
			throw new Exception("joinStringTest failed");
		}
	}

	/*
	 * @testName: joinStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:999;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void joinStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> address = customer.join("work", JoinType.INNER);
			Expression e = cbuilder.literal("4");
			cquery.where(address.get("id").in(e)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";

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
			throw new Exception("joinStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:998; PERSISTENCE:JAVADOC:1000;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void joinStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "String Test");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			try {
				customer.join("doesnotexist");
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected exception");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		logger.log(Logger.Level.INFO, "String, JoinType Test");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			try {
				customer.join("doesnotexist", JoinType.INNER);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected exception");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: joinSingularAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:987;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void joinSingularAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> address = customer.join(Customer_.work);
			Expression e = cbuilder.literal("4");
			cquery.where(address.get("id").in(e)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";

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
			throw new Exception("joinSingularAttributeTest failed");
		}
	}

	/*
	 * @testName: joinSingularAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:988;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void joinSingularAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> address = customer.join(Customer_.work, JoinType.INNER);
			Expression e = cbuilder.literal("4");
			cquery.where(address.get("id").in(e)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";

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
			throw new Exception("joinSingularAttributeJoinTypeTest failed");
		}
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
	 * @testName: joinCollectionIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1002; PERSISTENCE:JAVADOC:1004;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void joinCollectionIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "String Test");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			try {
				customer.joinCollection("doesnotexist");
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected exception");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		logger.log(Logger.Level.INFO, "String, JoinType Test");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			try {
				customer.joinCollection("doesnotexist", JoinType.INNER);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected exception");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinCollectionIllegalArgumentExceptionTest failed");
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
	 * @testName: joinSetIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1014; PERSISTENCE:JAVADOC:1016;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void joinSetIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "String Test");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			try {
				customer.joinSet("doesnotexist");
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected exception");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		logger.log(Logger.Level.INFO, "String, JoinType Test");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			try {
				customer.joinSet("doesnotexist", JoinType.INNER);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected exception");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinSetIllegalArgumentExceptionTest failed");
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
	 * @testName: joinListIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1006; PERSISTENCE:JAVADOC:1008;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void joinListIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing String");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> root = cquery.from(Customer.class);
			try {
				root.joinList("doesnotexist");
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		logger.log(Logger.Level.INFO, "Testing String, JoinType");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> root = cquery.from(Customer.class);
			try {
				root.joinList("doesnotexist", JoinType.INNER);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinListIllegalArgumentExceptionTest failed");
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
	@Test
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
	@Test
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
	@Test
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
				logger.log(Logger.Level.TRACE, "result:" + t.get(0) + ", " + t.get(1) + ", " + t.get(2));
				actual.add(t.get(0) + ", " + t.get(1) + ", " + t.get(2));
			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results:");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

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
	@Test
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
				logger.log(Logger.Level.TRACE, "result:" + t.get(0) + ", " + t.get(1) + ", " + t.get(2));
				actual.add(t.get(0) + ", " + t.get(1) + ", " + t.get(2));
			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results:");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinMapStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinMapIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1010; PERSISTENCE:JAVADOC:1012
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void joinMapIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing String");

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> root = cquery.from(Customer.class);
			try {
				root.joinMap("doesnotexist");
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		logger.log(Logger.Level.INFO, "Testing String, JoinType");
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> root = cquery.from(Customer.class);
			try {
				root.joinMap("doesnotexist", JoinType.INNER);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinMapIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: fromGetCorrelationParentIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:984;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void fromGetCorrelationParentIllegalStateExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			boolean isCorr = customer.isCorrelated();
			if (!isCorr) {
				logger.log(Logger.Level.TRACE, "isCorrelated() return false");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected isCorrelated() to return false, actual:" + isCorr);
			}
			try {
				customer.getCorrelationParent();
				logger.log(Logger.Level.ERROR, "Did not throw IllegalStateException");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fromGetCorrelationParentIllegalStateExceptionTest failed");
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
	@Test
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

			CriteriaQuery cquery = cbuilder.createQuery(Expression.class);
			From<Department, Department> department = cquery.from(Department.class);
			cquery.where(cbuilder.equal(department.get("id"), 1));
			cquery.select(department.get(Department_.lastNameEmployees));
			TypedQuery tquery = getEntityManager().createQuery(cquery);
			List<Employee> list = tquery.getResultList();

			for (Employee e : list) {
				logger.log(Logger.Level.TRACE,
						" employee:" + e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());
				actual.add(e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results:");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
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
	@Test
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

			CriteriaQuery cquery = cbuilder.createQuery(Expression.class);
			Path<Department> department = cquery.from(Department.class);
			cquery.where(cbuilder.equal(department.get("id"), 1));
			cquery.select(department.get(Department_.lastNameEmployees));
			TypedQuery tquery = getEntityManager().createQuery(cquery);
			List<Employee> list = tquery.getResultList();

			for (Employee e : list) {
				logger.log(Logger.Level.TRACE,
						" employee:" + e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());
				actual.add(e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results:");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("pathGetMapAttributeTest failed");
		}
	}

}
