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

package com.sun.ts.tests.jpa.core.criteriaapi.metamodelquery;

import java.lang.System.Logger;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Address_;
import com.sun.ts.tests.jpa.common.schema30.Alias;
import com.sun.ts.tests.jpa.common.schema30.Alias_;
import com.sun.ts.tests.jpa.common.schema30.Country_;
import com.sun.ts.tests.jpa.common.schema30.CreditCard;
import com.sun.ts.tests.jpa.common.schema30.CreditCard_;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.LineItem_;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Order_;
import com.sun.ts.tests.jpa.common.schema30.Phone;
import com.sun.ts.tests.jpa.common.schema30.Phone_;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.Product_;
import com.sun.ts.tests.jpa.common.schema30.ShelfLife_;
import com.sun.ts.tests.jpa.common.schema30.Spouse;
import com.sun.ts.tests.jpa.common.schema30.Spouse_;
import com.sun.ts.tests.jpa.common.schema30.UtilCustomerData;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SetAttribute;

public class Client1IT extends UtilCustomerData {

	private static final Logger logger = (Logger) System.getLogger(Client1IT.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1IT.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_metamodelquery1.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */
	/*
	 * @testName: queryTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:317.1; PERSISTENCE:SPEC:750;
	 * PERSISTENCE:SPEC:764; PERSISTENCE:SPEC:746.1
	 * 
	 * @test_Strategy: Find All Customers. Verify the results were accurately
	 * returned.
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest2() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute findAllCustomers");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> c = cquery.from(Customer.class);
			cquery.select(c);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[customerRef.length];
			for (int i = 0; i < customerRef.length; i++) {
				expectedPKs[i] = Integer.toString(i + 1);
			}

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + customerRef.length
						+ " references, got: " + clist.size());

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest2 failed");
		}
	}

	/*
	 * @testName: queryTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:322; PERSISTENCE:SPEC:394;
	 * PERSISTENCE:SPEC:751; PERSISTENCE:SPEC:753; PERSISTENCE:SPEC:754;
	 * PERSISTENCE:SPEC:755
	 * 
	 * @test_Strategy: This query is defined on a one-one relationship and used
	 * conditional AND in query. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest4() throws Exception {
		boolean pass = false;
		Customer c;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			Customer expected = getEntityManager().find(Customer.class, "3");
			logger.log(Logger.Level.TRACE, "find Customer with Home Address in Swansea");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(
					cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
							cbuilder.parameter(String.class, "street")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.city),
							cbuilder.parameter(String.class, "city")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.state),
							cbuilder.parameter(String.class, "state")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.zip),
							cbuilder.parameter(String.class, "zip")))
					.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("street", "125 Moxy Lane").setParameter("city", "Swansea").setParameter("state", "MA")
					.setParameter("zip", "11345");
			c = tquery.getSingleResult();

			if (expected == c) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");

			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest4 failed");
		}
	}

	
	/*
	 * @testName: queryTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:338;
	 * 
	 * @test_Strategy: This query is defined on a one-one relationship using
	 * conditional OR in query. Verify the results were accurately returned.
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest6() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Customers with Home Address Information");

			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
							cbuilder.parameter(String.class, "street")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.city),
							cbuilder.parameter(String.class, "city")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.state),
							cbuilder.parameter(String.class, "state")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.zip),
							cbuilder.parameter(String.class, "zip"))));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("street", "47 Skyline Drive");
			tquery.setParameter("city", "Chelmsford");
			tquery.setParameter("state", "VT");
			tquery.setParameter("zip", "02155");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest6 failed");
		}
	}

	/*
	 * @testName: queryTest15
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:330;
	 * 
	 * @test_Strategy: Execute a query method with a string literal enclosed in
	 * single quotes (the string includes a single quote) in the conditional
	 * expression of the WHERE clause. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest15() throws Exception {
		boolean pass = false;
		Customer c;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			Customer expected = getEntityManager().find(Customer.class, "5");
			logger.log(Logger.Level.TRACE, "find customer with name: Stephen S. D'Milla");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get(Customer_.name), cbuilder.parameter(String.class, "cName")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("cName", "Stephen S. D'Milla");
			c = tquery.getSingleResult();

			if (expected == c) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest15 failed");
		}
	}

	/*
	 * @testName: queryTest16
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query method using comparison operator IN in a
	 * comparison expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest16() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers IN home city: Lexington");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.home).get(Address_.city).in("Lexington"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest16 failed");
		}
	}

	/*
	 * @testName: queryTest17
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:353;
	 * PERSISTENCE:JAVADOC:988
	 * 
	 * @test_Strategy: Execute a query using comparison operator NOT IN in a
	 * comparison expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest17() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers NOT IN home city: Swansea or Brookline");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> h = customer.join(Customer_.home, JoinType.LEFT);
			cquery.where(h.get(Address_.city).in("Swansea", "Brookline").not());
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";
			expectedPKs[4] = "7";
			expectedPKs[5] = "8";
			expectedPKs[6] = "10";
			expectedPKs[7] = "11";
			expectedPKs[8] = "12";
			expectedPKs[9] = "13";
			expectedPKs[10] = "14";
			expectedPKs[11] = "15";
			expectedPKs[12] = "16";
			expectedPKs[13] = "17";
			expectedPKs[14] = "18";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest17 failed");
		}
	}

	/*
	 * @testName: queryTest18
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause. The pattern-value includes a
	 * percent character. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest18() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers with home ZIP CODE that ends in 77");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.like(customer.get(Customer_.home).get(Address_.zip), "%77"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest18 failed");
		}
	}

	/*
	 * @testName: queryTest19
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query using the comparison operator NOT LIKE in a
	 * comparison expression within the WHERE clause. The pattern-value includes a
	 * percent character and an underscore. Verify the results were accurately
	 * returned.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest19() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with a home zip code that does not contain"
					+ " 44 in the third and fourth position");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.notLike(customer.get(Customer_.home).get(Address_.zip), "%44_"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "9";
			expectedPKs[6] = "10";
			expectedPKs[7] = "11";
			expectedPKs[8] = "12";
			expectedPKs[9] = "13";
			expectedPKs[10] = "14";
			expectedPKs[11] = "15";
			expectedPKs[12] = "16";
			expectedPKs[13] = "17";
			expectedPKs[14] = "18";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest19 failed");
		}
	}

	/*
	 * @testName: queryTest22
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359;
	 * 
	 * @test_Strategy: Execute a query using the IS NULL comparison operator in the
	 * WHERE clause. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest22() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who have a null work zip code");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.work).get(Address_.zip).isNull());
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "13";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest22 failed");
		}
	}

	/*
	 * @testName: queryTest23
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359
	 * 
	 * @test_Strategy: Execute a query using the IS NOT NULL comparison operator
	 * within the WHERE clause. Verify the results were accurately returned. (This
	 * query is executed against non-NULL data. For NULL data, see test queryTest47)
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest23() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who do not have null work zip code entry");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.work).get(Address_.zip).isNotNull());
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[17];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "12";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 17 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest23 failed");
		}
	}

	/*
	 * @testName: queryTest36
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352
	 * 
	 * @test_Strategy: Execute a query using comparison operator IN in a conditional
	 * expression within the WHERE clause where the value for the IN expression is
	 * an input parameter. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest36() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who lives in city Attleboro");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.home).get(Address_.city).in(cbuilder.parameter(String.class, "city")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("city", "Attleboro");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "13";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest36 failed");
		}
	}

	/*
	 * @testName: queryTest37
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:354; PERSISTENCE:JAVADOC:833
	 * 
	 * @test_Strategy: Execute two methods using the comparison operator IN in a
	 * comparison expression within the WHERE clause and verify the results of the
	 * two queries are equivalent regardless of the way the expression is composed.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest37() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		String expectedPKs2[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute two queries composed differently and verify results");
			CriteriaQuery<Customer> cquery1 = cbuilder.createQuery(Customer.class);
			Root<Customer> customer1 = cquery1.from(Customer.class);
			cquery1.where(customer1.get(Customer_.home).get(Address_.state).in("NH", "RI")).select(customer1);
			TypedQuery<Customer> tquery1 = getEntityManager().createQuery(cquery1);
			tquery1.setMaxResults(customerRef.length);
			List<Customer> clist1 = tquery1.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "5";
			expectedPKs[1] = "6";
			expectedPKs[2] = "12";
			expectedPKs[3] = "14";
			expectedPKs[4] = "16";

			CriteriaQuery<Customer> cquery2 = cbuilder.createQuery(Customer.class);
			Root<Customer> customer2 = cquery2.from(Customer.class);
			cquery2.where(cbuilder.or(cbuilder.equal(customer2.get(Customer_.home).get(Address_.state), "NH"),
					cbuilder.equal(customer2.get(Customer_.home).get(Address_.state), "RI"))).select(customer2);
			TypedQuery<Customer> tquery2 = getEntityManager().createQuery(cquery2);
			tquery2.setMaxResults(customerRef.length);
			List<Customer> clist2 = tquery2.getResultList();

			expectedPKs2 = new String[5];
			expectedPKs2[0] = "5";
			expectedPKs2[1] = "6";
			expectedPKs2[2] = "12";
			expectedPKs2[3] = "14";
			expectedPKs2[4] = "16";

			if (!checkEntityPK(clist1, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for first query.  Expected 5 reference, got: " + clist1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query");
				pass1 = true;
			}

			if (!checkEntityPK(clist2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for second query.  Expected 5 reference, got: " + clist2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest37 failed");
		}
	}

	/*
	 * @testName: queryTest47
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:376; PERSISTENCE:SPEC:401;
	 * PERSISTENCE:SPEC:399.3; PERSISTENCE:SPEC:422; PERSISTENCE:SPEC:752;
	 * PERSISTENCE:SPEC:753
	 * 
	 * @test_Strategy: The IS NOT NULL construct can be used to eliminate the null
	 * values from the result set of the query. Verify the results are accurately
	 * returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest47() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		final String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find work zip codes that are not null");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotNull(customer.get(Customer_.work).get(Address_.zip)))
					.select(customer.get(Customer_.work).get(Address_.zip))
					.orderBy(cbuilder.asc(customer.get(Customer_.work).get(Address_.zip)));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> clist = tquery.getResultList();

			String[] result = (String[]) (clist.toArray(new String[clist.size()]));
			logger.log(Logger.Level.TRACE, "Compare results of work zip codes");
			pass = Arrays.equals(expectedZips, result);
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest47 failed");
		}
	}

	/*
	 * @testName: queryTest52
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424; PERSISTENCE:SPEC:789
	 * 
	 * @test_Strategy: Define a query using Boolean operator AND in a conditional
	 * test ( True AND True = True) where the second condition is NULL. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest52() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.and(cbuilder.isNotNull(customer.get(Customer_.country)),
					cbuilder.equal(customer.get(Customer_.name), cbuilder.parameter(String.class, "cName"))));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("cName", "Shelly D. McGowan");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest52 failed");
		}
	}

	/*
	 * @testName: queryTest56
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:375; PERSISTENCE:SPEC:410;
	 * PERSISTENCE:SPEC:403; PERSISTENCE:SPEC:814; PERSISTENCE:SPEC:816
	 * 
	 * @test_Strategy: This query returns a null
	 * single_valued_association_path_expression. Verify the results were accurately
	 * returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest56() throws Exception {

		boolean pass1 = false;
		boolean pass2 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		final String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all work zip codes");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer.get(Customer_.work).get(Address_.zip));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> clist = tquery.getResultList();

			if (clist.size() != 18) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 18 references, got: " + clist.size());
			} else {
				pass1 = true;
				int numOfNull = 0;
				int foundZip = 0;
				for (String s : clist) {
					logger.log(Logger.Level.TRACE, "Check contents of List for null");
					Object o = s;
					if (o == null) {
						numOfNull++;
						continue;
					}

					logger.log(Logger.Level.TRACE, "Check List for expected zip codes");

					for (int l = 0; l < 17; l++) {
						if (expectedZips[l].equals(o)) {
							foundZip++;
							break;
						}
					}
				}
				if ((numOfNull != 1) || (foundZip != 17)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results");
					pass2 = false;
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest56 failed");
		}
	}

	/*
	 * @testName: queryTest58
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:410;
	 * 
	 * @test_Strategy: This query returns a null
	 * single_valued_association_path_expression. Verify the results are accurately
	 * returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest58() throws Exception {
		boolean pass = false;
		Object s;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find home zip codes");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get(Customer_.home).get(Address_.street), "212 Edgewood Drive"))
					.select(customer.get(Customer_.name));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			s = tquery.getSingleResult();

			if (s != null) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected null.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("queryTest58 failed");
		}
	}

	/*
	 * @testName: queryTest59
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:408
	 * 
	 * @test_Strategy: This tests a null single_valued_association_path_expression
	 * is returned using IS NULL. Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest59() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine which customers have an null name");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNull(customer.get(Customer_.name)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest59 failed");
		}
	}

	/*
	 * @testName: queryTest61
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:778;PERSISTENCE:SPEC:780;
	 * PERSISTENCE:SPEC:1714; PERSISTENCE:SPEC:1715;
	 * 
	 * @test_Strategy: Execute a query defining an identification variable for
	 * c.work in an OUTER JOIN clause. The JOIN operation will include customers
	 * without addresses. Verify the results are accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest61() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> w = customer.join(Customer_.work, JoinType.LEFT);
			cquery.where(cbuilder.isNull(w.get(Address_.zip)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "13";
			expectedPKs[1] = "19";
			expectedPKs[2] = "20";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest61 failed");
		}
	}

	/*
	 * @testName: queryTest64
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:372.5;PERSISTENCE:SPEC:817;
	 * PERSISTENCE:SPEC:395
	 * 
	 * @test_Strategy: A constructor may be used in the SELECT list to return a
	 * collection of Java instances. The specified class is not required to be an
	 * entity or mapped to the database. The constructor name must be fully
	 * qualified.
	 *
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest64() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get(Customer_.work).get(Address_.city),
					cbuilder.parameter(String.class, "workcity")));
			cquery.select(cbuilder.construct(Customer.class, customer.get(Customer_.id), customer.get(Customer_.name)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("workcity", "Burlington");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[18];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "12";
			expectedPKs[12] = "13";
			expectedPKs[13] = "14";
			expectedPKs[14] = "15";
			expectedPKs[15] = "16";
			expectedPKs[16] = "17";
			expectedPKs[17] = "18";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 18 references, got: " + clist.size());
				pass = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest64 failed");
		}

	}

	/*
	 * @testName: queryTest69
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:384; PERSISTENCE:SPEC:389;
	 * PERSISTENCE:SPEC:406; PERSISTENCE:SPEC:824; PERSISTENCE:SPEC:392;
	 * 
	 * @test_Strategy: This test verifies the same results of two queries using the
	 * keyword DISTINCT or not using DISTINCT in the query with the aggregate
	 * keyword COUNT to verity the NULL values are eliminated before the aggregate
	 * is applied.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest69() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		final Long expectedResult1 = Long.valueOf(17);
		final Long expectedResult2 = Long.valueOf(16);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaQuery<Long> cquery1 = cbuilder.createQuery(Long.class);
			Root<Customer> customer1 = cquery1.from(Customer.class);
			cquery1.select(cbuilder.count(customer1.get(Customer_.home).get(Address_.city)));
			TypedQuery<Long> tquery1 = getEntityManager().createQuery(cquery1);
			Long result1 = tquery1.getSingleResult();

			if (!(result1.equals(expectedResult1))) {
				logger.log(Logger.Level.ERROR,
						"Query1 in queryTest69 returned:" + result1 + " expected: " + expectedResult1);
			} else {
				logger.log(Logger.Level.TRACE, "query1 in queryTest69 returned expected results");
				pass1 = true;
			}
			CriteriaQuery<Long> cquery2 = cbuilder.createQuery(Long.class);
			Root<Customer> customer2 = cquery2.from(Customer.class);
			cquery2.select(cbuilder.countDistinct(customer2.get(Customer_.home).get(Address_.city)));
			TypedQuery<Long> tquery2 = getEntityManager().createQuery(cquery2);
			Long result2 = tquery2.getSingleResult();

			if (!(result2.equals(expectedResult2))) {
				logger.log(Logger.Level.ERROR,
						"Query 2 in queryTest69 returned:" + result2 + " expected: " + expectedResult2);
			} else {
				logger.log(Logger.Level.TRACE, "query 2 in queryTest69 returned expected results");
				pass2 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest69 failed");
		}

	}

	/*
	 * @testName: queryTest71
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:744;PERSISTENCE:JAVADOC:128
	 * 
	 * @test_Strategy: The NoResultException is thrown by the persistence provider
	 * when Query.getSingleResult is invoked and there are not results to return.
	 * Verify the results are accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void queryTest71() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Check if a spouse is related to a customer");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Spouse> spouse = cquery.from(Spouse.class);
			cquery.where(cbuilder.equal(spouse.get(Spouse_.id), "7")).select(spouse.get(Spouse_.customer));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.getSingleResult();

			getEntityTransaction().commit();
		} catch (NoResultException e) {
			logger.log(Logger.Level.TRACE, "queryTest71: NoResultException caught as expected : " + e);
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest71 failed");
		}
	}

	/*
	 * @testName: test_leftouterjoin_1xM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:780
	 * 
	 * @test_Strategy: LEFT OUTER JOIN for 1-M relationship. Retrieve credit card
	 * information for a customer with name like Caruso.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_leftouterjoin_1xM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join(Customer_.creditCards, JoinType.LEFT);
			cquery.where(cbuilder.like(customer.get(Customer_.name), "%Caruso")).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_leftouterjoin_1xM failed");
		}
	}

	/*
	 * @testName: test_groupBy
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:810; PERSISTENCE:SPEC:756;
	 * 
	 * @test_Strategy: Test for Only Group By in a simple select statement. Country
	 * is an Embeddable entity.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_groupBy() throws Exception {
		boolean pass = false;
		final String expectedCodes[] = new String[] { "CHA", "GBR", "IRE", "JPN", "USA" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer.get(Customer_.country).get(Country_.code));
			cquery.groupBy(customer.get(Customer_.country).get(Country_.code));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(country.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedCodes, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 4 Country Codes: "
						+ "CHA, GBR, JPN, USA. Received: " + result.size());
				for (String s : result) {
					logger.log(Logger.Level.TRACE, " Credit Card Type: " + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_groupBy failed");
		}
	}

	/*
	 * @testName: test_innerjoin_1x1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:372;
	 * PERSISTENCE:SPEC:372.2; PERSISTENCE:JAVADOC:987
	 * 
	 * @test_Strategy: Inner Join for 1-1 relationship. Select all customers with
	 * spouses.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_innerjoin_1x1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join(Customer_.spouse);
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "7";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";
			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_innerjoin_1x1 failed");
		}
	}

	/*
	 * @testName: fetchFetchSingularAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:965
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	public void fetchFetchSingularAttributeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

			Root<Customer> customer = cquery.from(Customer.class);
			Fetch f = customer.fetch(Customer_.spouse);

			Fetch f1 = f.fetch(Customer_.spouse);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals(Customer_.spouse.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {

				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.spouse.getName() + ", actual:" + attr.getName());
			}
			JoinType jt = f1.getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass2 = true;
			} else {

				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {

			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fetchFetchSingularAttributeTest failed");
		}
	}

	/*
	 * @testName: fetchFetchSingularAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:966
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	public void fetchFetchSingularAttributeJoinTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

			Root<Customer> customer = cquery.from(Customer.class);
			Fetch f = customer.fetch(Customer_.spouse);

			Fetch f1 = f.fetch(Customer_.spouse, JoinType.INNER);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals(Customer_.spouse.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.spouse.getName() + ", actual:" + attr.getName());
			}
			JoinType jt = f1.getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fetchFetchSingularAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: fetchTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:962; PERSISTENCE:JAVADOC:963;
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	public void fetchTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			JoinType jt = customer.fetch(Customer_.spouse).getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			jt = customer.fetch(Customer_.spouse, JoinType.INNER).getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass2 = true;

			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			jt = customer.fetch(Customer_.spouse, JoinType.LEFT).getJoinType();
			if (jt.equals(JoinType.LEFT)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass3 = true;

			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.LEFT.name() + ", actual:" + jt.name());
			}

			/*
			 * JoinType.RIGHT Not Required in JPA 2.0 jt =
			 * customer.fetch(Customer_.spouse,JoinType.RIGHT).getJoinType(); if
			 * (jt.equals(JoinType.RIGHT)) {
			 * logger.log(Logger.Level.TRACE,"Received expected JoinType"); pass=true; }
			 * else { logger.log(Logger.Level.ERROR,"Expected JoinType:" +
			 * JoinType.RIGHT.name() + ", actual:" + jt.name()); }
			 */

			Attribute attr = customer.fetch(Customer_.spouse).getAttribute();
			if (attr.getName().equals(Customer_.spouse.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass4 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.spouse.getName() + ", actual:" + attr.getName());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("fetchTest failed");
		}
	}

	/*
	 * @testName: fetchGetParentTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:964
	 * 
	 * @test_Strategy: Get FetchParent then get the fetches that it contains and
	 * verify we got the correct one.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void fetchGetParentTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			FetchParent fp = customer.fetch(Customer_.spouse).getParent();
			Set<Fetch<?, ?>> f = fp.getFetches();
			if (f.size() == 1) {
				for (Fetch tmpFetch : f) {
					String name = tmpFetch.getAttribute().getName();
					if (name.equals("spouse")) {
						logger.log(Logger.Level.TRACE, "Received expected Fetch:" + name
								+ ", which means the correct FetchParent was returned");
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: spouse, actual:" + name);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected 1 fetch to be returned from FetchParent.getFetches(), got:" + f.size());
				for (Fetch tmpFetch : f) {
					String name = tmpFetch.getAttribute().getName();
					logger.log(Logger.Level.ERROR, "Fetch returned was:" + name);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchGetParentTest failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_1x1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:774;
	 * PERSISTENCE:SPEC:776; PERSISTENCE:JAVADOC:974; PERSISTENCE:JAVADOC:982;
	 * PERSISTENCE:JAVADOC:974; PERSISTENCE:JAVADOC:1017;
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for all
	 * Customers.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_fetchjoin_1x1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			Collection cFetches = customer.getFetches();
			if (cFetches.size() != 0) {
				logger.log(Logger.Level.ERROR,
						"Did not get correct number of fetches, expected:0, actual:" + cFetches.size());
				for (Object o : cFetches) {
					logger.log(Logger.Level.ERROR, "Fetch:" + o.toString());
				}
			}

			customer.fetch(Customer_.spouse);
			cFetches = customer.getFetches();
			if (cFetches.size() == 1) {
				logger.log(Logger.Level.TRACE, "Received expected number of fetches" + cFetches.size());
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get correct number of fetches, expected:1, actual:" + cFetches.size());
				for (Object o : cFetches) {
					logger.log(Logger.Level.ERROR, "Fetch:" + o.toString());
				}

			}
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "7";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_fetchjoin_1x1 failed");
		}
	}

	/*
	 * @testName: fetchSingularAttributeJoinType1X1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:975; PERSISTENCE:JAVADOC:1018;
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for all
	 * Customers.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void fetchSingularAttributeJoinType1X1Test() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.spouse, JoinType.INNER);
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "7";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchSingularAttributeJoinType1X1Test failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_1xM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:782; PERSISTENCE:SPEC:374;
	 * PERSISTENCE:SPEC:777; PERSISTENCE:SPEC:783; PERSISTENCE:JAVADOC:977;
	 * PERSISTENCE:SPEC:1715;
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_fetchjoin_1xM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.orders, JoinType.LEFT);
			cquery.where(customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "14";
			expectedPKs[1] = "17";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "test_fetchjoin_1xM failed", e);
		}

		if (!pass) {
			throw new Exception("test_fetchjoin_1xM failed");
		}
	}

	/*
	 * @testName: fetchPluralAttribute1xMTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:976; PERSISTENCE:JAVADOC:1019;
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	public void fetchPluralAttribute1xMTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.orders);
			cquery.where(customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "14";
			expectedPKs[1] = "17";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchPluralAttribute1xMTest failed");
		}
	}

	/*
	 * @testName: fetchPluralAttributeJoinType1xMTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1020;
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	public void fetchPluralAttributeJoinType1xMTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.orders, JoinType.INNER);
			cquery.where(customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "14";
			expectedPKs[1] = "17";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchPluralAttributeJoinType1xMTest failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_Mx1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:654
	 * 
	 * @test_Strategy: Retrieve customer information from Order.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_fetchjoin_Mx1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			order.fetch(Order_.customer, JoinType.LEFT);
			cquery.where(cbuilder.equal(order.get(Order_.customer).get(Customer_.home).get(Address_.city), "Lawrence"))
					.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("test_fetchjoin_Mx1 failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_Mx1_1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781
	 * 
	 * @test_Strategy: Retrieve customer information from Order.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_fetchjoin_Mx1_1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			order.fetch(Order_.customer, JoinType.LEFT);
			cquery.where(cbuilder.like(order.get(Order_.customer).get(Customer_.name), "%Caruso")).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_fetchjoin_Mx1_1 failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_MxM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781
	 * 
	 * @test_Strategy: Left Join Fetch for M-M relationship. Retrieve customers with
	 * orders that live in NH.
	 */
	@SetupMethod(name = "setupAliasData")
	public void test_fetchjoin_MxM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FETCHJOIN-MXM Executing Query");
			CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
			Root<Alias> alias = cquery.from(Alias.class);
			alias.fetch(Alias_.customers, JoinType.LEFT);
			cquery.where(cbuilder.like(alias.get(Alias_.alias), "a%")).select(alias);
			TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(aliasRef.length);
			List<Alias> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_fetchjoin_MxM failed");
		}
	}

	/*
	 * @testName: test_betweenDates
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:JAVADOC:15; PERSISTENCE:JAVADOC:166; PERSISTENCE:JAVADOC:189;
	 * PERSISTENCE:SPEC:1049; PERSISTENCE:SPEC:1059; PERSISTENCE:SPEC:1060
	 * 
	 * @test_Strategy: Execute a query containing using the operator BETWEEN with
	 * datetime_expression. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	public void test_betweenDates() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			final Date date1 = getSQLDate(2000, 2, 14);
			final Date date6 = getSQLDate(2005, 2, 18);
			logger.log(Logger.Level.TRACE, "The dates used in test_betweenDates is : " + date1 + " and " + date6);
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.between(product.get(Product_.shelfLife).get(ShelfLife_.soldDate),
					cbuilder.parameter(java.sql.Date.class, "date1"),
					cbuilder.parameter(java.sql.Date.class, "date6")));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("date1", date1);
			tquery.setParameter("date6", date6);
			tquery.setMaxResults(productRef.length);
			List<Product> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "31";
			expectedPKs[1] = "32";
			expectedPKs[2] = "33";
			expectedPKs[3] = "37";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + result.size());
			} else {

				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass)
			throw new Exception("test_betweenDates failed");
	}

	/*
	 * @testName: test_notBetweenArithmetic
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349
	 * 
	 * @test_Strategy: Execute a query containing using the operator BETWEEN and NOT
	 * BETWEEN. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_notBetweenArithmetic() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.not(cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "2";
			expectedPKs[1] = "4";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";
			expectedPKs[4] = "9";
			expectedPKs[5] = "10";
			expectedPKs[6] = "11";
			expectedPKs[7] = "12";
			expectedPKs[8] = "13";
			expectedPKs[9] = "15";
			expectedPKs[10] = "16";
			expectedPKs[11] = "17";
			expectedPKs[12] = "18";
			expectedPKs[13] = "19";
			expectedPKs[14] = "20";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_notBetweenArithmetic failed");
		}
	}

	/*
	 * @testName: test_notBetweenDates
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:600
	 * 
	 * @test_Strategy: Execute a query containing using the operator NOT BETWEEN.
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupProductData")
	public void test_notBetweenDates() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		final Date date1 = getSQLDate("2000-02-14");
		final Date newdate = getSQLDate("2005-02-17");
		logger.log(Logger.Level.TRACE, "The dates used in test_betweenDates is : " + date1 + " and " + newdate);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.not(cbuilder.between(product.get(Product_.shelfLife).get(ShelfLife_.soldDate),
					cbuilder.parameter(java.sql.Date.class, "date1"),
					cbuilder.parameter(java.sql.Date.class, "newdate"))));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("date1", date1);
			tquery.setParameter("newdate", newdate);
			tquery.setMaxResults(productRef.length);
			List<Product> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "31";
			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass)
			throw new Exception("test_notBetweenDates failed");
	}

	/*
	 * @testName: test_ANDconditionTT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424;
	 * 
	 * @test_Strategy: Both the conditions in the WHERE Clause are True and hence
	 * the result is also TRUE Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ANDconditionTT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.and(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 500)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "6";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ANDconditionTT failed");
		}
	}

	/*
	 * @testName: test_ANDconditionTF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424
	 * 
	 * @test_Strategy: First condition is True and Second is False and hence the
	 * result is also False
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ANDconditionTF() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.and(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ANDconditionTF failed");
		}
	}

	/*
	 * @testName: test_ANDconditionFT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424
	 * 
	 * @test_Strategy: First condition is FALSE and Second is TRUE and hence the
	 * result is also False
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ANDconditionFT() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.lt(order.get(Order_.totalPrice), 1000)).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ANDconditionFT failed");
		}
	}

	/*
	 * @testName: test_ANDconditionFF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424
	 * 
	 * @test_Strategy: First condition is FALSE and Second is FALSE and hence the
	 * result is also False
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ANDconditionFF() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.and(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ANDconditionFF failed");
		}
	}

	/*
	 * @testName: test_ORconditionTT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is TRUE OR Second is TRUE and hence the
	 * result is also TRUE
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ORconditionTT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 5000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "6";
			expectedPKs[1] = "11";
			expectedPKs[2] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ORconditionTT failed");
		}
	}

	/*
	 * @testName: test_ORconditionTF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is TRUE OR Second is FALSE and hence the
	 * result is also TRUE
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ORconditionTF() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "6";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ORconditionTF failed");
		}
	}

	/*
	 * @testName: test_ORconditionFT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is FALSE OR Second is TRUE and hence the
	 * result is also TRUE
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ORconditionFT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.lt(order.get(Order_.totalPrice), 1000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "9";
			expectedPKs[1] = "10";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			expectedPKs[4] = "15";
			expectedPKs[5] = "19";
			expectedPKs[6] = "20";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ORconditionFT failed");
		}
	}

	/*
	 * @testName: test_ORconditionFF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is FALSE OR Second is FALSE and hence the
	 * result is also FALSE
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ORconditionFF() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ORconditionFF failed");
		}
	}

	/*
	 * @testName: test_groupByWhereClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:808
	 * 
	 * @test_Strategy: Test for Group By within a WHERE clause
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_groupByWhereClause() throws Exception {
		boolean pass = false;
		final String[] expectedCusts = new String[] { "Jonathan K. Smith", "Kellie A. Sanborn", "Robert E. Bissett" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			cquery.where(cbuilder.between(o.get(Order_.totalPrice), 90D, 160D)).groupBy(customer.get(Customer_.name))
					.select(customer.get(Customer_.name));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));

			Arrays.sort(output);
			pass = Arrays.equals(expectedCusts, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 3 Customers : "
						+ "Jonathan K. Smith, Kellie A. Sanborn and Robert E. Bissett. Received: " + result.size());
				for (String s : result) {
					logger.log(Logger.Level.TRACE, " Customer: " + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_groupByWhereClause failed");
		}
	}

	/*
	 * @testName: test_groupByHaving
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:808; PERSISTENCE:SPEC:353;
	 * PERSISTENCE:SPEC:757; PERSISTENCE:SPEC:391
	 * 
	 * @test_Strategy: Test for Group By and Having in a select statement Select the
	 * count of customers in each country where Country is China, England
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_groupByHaving() throws Exception {
		boolean pass = false;
		final Long expectedGBR = Long.valueOf(2);
		final Long expectedCHA = Long.valueOf(4);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.groupBy(customer.get(Customer_.country).get(Country_.code))
					.having(customer.get(Customer_.country).get(Country_.code).in("GBR", "CHA"))
					.select(cbuilder.count(customer));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(country.length);
			List<Long> result = tquery.getResultList();

			int numOfExpected = 0;
			for (Long l : result) {
				logger.log(Logger.Level.TRACE, "Check result received . . . ");
				if ((l.equals(expectedGBR)) || (l.equals(expectedCHA))) {
					numOfExpected++;
				}
			}

			if (numOfExpected != 2) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 2 Values returned : "
						+ "2 with Country Code GBR and 4 with Country Code CHA. " + "Received: " + result.size());
				for (Long l : result) {
					logger.log(Logger.Level.TRACE, "count of Codes Returned: " + l);
				}
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_groupByHaving failed");
		}
	}

	/*
	 * @testName: substringHavingExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:JAVADOC:922;
	 * 
	 * @test_Strategy:Test for Functional Expression: substring in Having Clause
	 * Select all customers with alias = fish
	 */
	@SetupMethod(name = "setupAliasData")
	public void substringHavingExpressionTest() throws Exception {
		boolean pass = false;
		Object result;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Executing Query");
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			Expression exp = cbuilder.equal(a.get(Alias_.alias),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int1"), cbuilder.parameter(Integer.class, "int2")));
			cquery.select(cbuilder.count(customer)).groupBy(a.get(Alias_.alias)).having(exp);
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("string1", "fish");
			tquery.setParameter("int1", Integer.valueOf(1));
			tquery.setParameter("int2", Integer.valueOf(4));
			result = tquery.getSingleResult();

			logger.log(Logger.Level.TRACE, "Check results received .  .  .");
			if (expectedCount.equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected Count of 2, got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" substringHavingExpressionTest failed");
		}
	}

	/*
	 * @testName: substringHavingExpressionPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:923; PERSISTENCE:JAVADOC:946;
	 * 
	 * @test_Strategy:Test for Functional Expression: substring in Having Clause
	 * Select all customers with alias = fish
	 */
	@SetupMethod(name = "setupAliasData")
	public void substringHavingExpressionPredicateArrayTest() throws Exception {
		boolean pass = false;
		Object result;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Executing Query");
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			Predicate[] pred = {
					cbuilder.equal(a.get(Alias_.alias), cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int1"), cbuilder.parameter(Integer.class, "int2"))),
					cbuilder.equal(a.get(Alias_.alias), cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int1"), cbuilder.parameter(Integer.class, "int2"))) };

			cquery.select(cbuilder.count(customer)).groupBy(a.get(Alias_.alias)).having(pred);
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("string1", "fish");
			tquery.setParameter("int1", Integer.valueOf(1));
			tquery.setParameter("int2", Integer.valueOf(4));
			result = tquery.getSingleResult();

			logger.log(Logger.Level.TRACE, "Check results received .  .  .");
			if (expectedCount.equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected Count of 2, got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("substringHavingExpressionPredicateArrayTest failed");
		}
	}

	/*
	 * @testName: test_concatHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:803;
	 * PERSISTENCE:SPEC:804; PERSISTENCE:SPEC:805; PERSISTENCE:SPEC:806;
	 * PERSISTENCE:SPEC:734
	 * 
	 * @test_Strategy:Test for Functional Expression: concat in Having Clause Find
	 * customer Margaret Mills by firstname-lastname concatenation.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_concatHavingClause() throws Exception {
		boolean pass = false;
		String result;
		final String expectedCustomer = "Margaret Mills";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer.get(Customer_.name)).groupBy(customer.get(Customer_.name))
					.having(cbuilder.equal(customer.get(Customer_.name),
							cbuilder.concat("Margaret ", cbuilder.parameter(String.class, "lname"))));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("lname", "Mills");
			result = tquery.getSingleResult();

			if (result.equals(expectedCustomer)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {

				logger.log(Logger.Level.TRACE, "test_concatHavingClause:  Did not get expected results. " + "Expected: "
						+ expectedCustomer + ", got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_concatHavingClause failed");
		}
	}

	/*
	 * @testName: test_lowerHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.10
	 * 
	 * @test_Strategy:Test for Functional Expression: lower in Having Clause Select
	 * all customers in country with code GBR
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_lowerHavingClause() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(cbuilder.count(customer.get(Customer_.country).get(Country_.code)))
					.groupBy(customer.get(Customer_.country).get(Country_.code))
					.having(cbuilder.equal(cbuilder.lower(customer.get(Customer_.country).get(Country_.code)), "gbr"));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			List<Long> result = tquery.getResultList();

			if (result.size() == 1) {
				pass1 = true;
				Long l = (Long) result.get(0);
				if (l.equals(expectedCount)) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected result:" + expectedCount + ", actual:" + l);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected 1, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception(" test_lowerHavingClause failed");
		}
	}

	/*
	 * @testName: test_upperHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.11
	 * 
	 * @test_Strategy:Test for Functional Expression: upper in Having Clause Select
	 * all customers in country ENGLAND
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_upperHavingClause() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(cbuilder.count(customer.get(Customer_.country).get(Country_.country)))
					.groupBy(customer.get(Customer_.country).get(Country_.country)).having(cbuilder
							.equal(cbuilder.upper(customer.get(Customer_.country).get(Country_.country)), "ENGLAND"));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			List<Long> result = tquery.getResultList();

			if (result.size() == 1) {
				pass1 = true;
				Long l = (Long) result.get(0);
				if (l.equals(expectedCount)) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected result:" + expectedCount + ", actual:" + l);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected 1, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("test_upperHavingClause failed");
		}
	}

	/*
	 * @testName: test_lengthHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.4
	 * 
	 * @test_Strategy:Test for Functional Expression: length in Having Clause Select
	 * all customer names having the length of the city of the home address = 10
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_lengthHavingClause() throws Exception {
		boolean pass = false;
		final String[] expectedCities = new String[] { "Burlington", "Chelmsford", "Roslindale" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> a = customer.join(Customer_.home);
			cquery.groupBy(a.get(Address_.city)).having(cbuilder.equal(cbuilder.length(a.get(Address_.city)), 10))
					.select(a.get(Address_.city));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedCities, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 Cities, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_lengthHavingClause failed");
		}
	}

	/*
	 * @testName: test_locateHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.3
	 * 
	 * @test_Strategy: Test for LOCATE expression in the Having Clause Select
	 * customer names if there the string "Frechette" is located in the customer
	 * name.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_locateHavingClause() throws Exception {
		boolean pass = false;
		final String[] expectedCusts = new String[] { "Alan E. Frechette", "Arthur D. Frechette" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.groupBy(customer.get(Customer_.name))
					.having(cbuilder.gt(cbuilder.locate(customer.get(Customer_.name), "Frechette"), 0))
					.select(customer.get(Customer_.name));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedCusts, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 Customers, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_locateHavingClause failed");
		}
	}

	/*
	 * testName: test_trimHavingClause_01 assertion_ids: PERSISTENCE:SPEC:369.9
	 * test_Strategy: Test for TRIM BOTH characters (blank) in the Having Clause
	 *
	 * DISABLE THIS TEST FOR NOW
	 * 
	 * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_01()
	 * throws Exception { boolean pass = false; String result; final String expected
	 * = " David R. Vincent             "; final String expected2 =
	 * "'David R. Vincent'";
	 * 
	 * CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
	 * 
	 * 
	 * try { getEntityTransaction().begin();
	 * 
	 * Trim tTrim = getEntityManager().find(Trim.class, "19");
	 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
	 * (!tTrim.getName().equals(expected)) {
	 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
	 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
	 * tTrim.getName() + "|"); }
	 * 
	 * 
	 * CriteriaQuery<String> cquery = cbuilder.createQuery(String.class); Root<Trim>
	 * trim = cquery.from(Trim.class);
	 * cquery.select(trim.get(Trim_.name)).groupBy(trim.get(Trim_.name)).having(
	 * cbuilder.equal(cbuilder.trim(CriteriaBuilder.Trimspec.BOTH,
	 * trim.get(Trim_.name)), expected2)); TypedQuery<String> tquery =
	 * getEntityManager().createQuery(cquery); result = tquery.getSingleResult();
	 * 
	 * if (expected.equals(result)) { pass = true;
	 * logger.log(Logger.Level.TRACE,"Expected results received"); } else {
	 * logger.log(Logger.Level.ERROR,"Did not get expected results," + "Expected:|"
	 * + expected + "|, got: |" + result + "|"); }
	 * 
	 * getEntityTransaction().commit(); } catch (Exception e) {
	 * logger.log(Logger.Level.ERROR,"Caught unexpected exception:", e);
	 * 
	 * }
	 * 
	 * if (!pass) { throw new Exception(" test_trimHavingClause_01 failed"); } }
	 */

	/*
	 * testName: test_trimHavingClause_02 assertion_ids: PERSISTENCE:SPEC:369.9
	 * test_Strategy: Test for TRIM LEADING characters (blank) in the Having Clause
	 * 
	 * DISABLE THIS TEST FOR NOW
	 * 
	 * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_02()
	 * throws Exception { boolean pass = false; String result; final String expected
	 * = " David R. Vincent             "; final String expected2 =
	 * "'David R. Vincent             '";
	 * 
	 * CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
	 * 
	 * 
	 * try { getEntityTransaction().begin(); Trim tTrim =
	 * getEntityManager().find(Trim.class, "19");
	 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
	 * (!tTrim.getName().equals(expected)) {
	 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
	 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
	 * tTrim.getName() + "|"); }
	 * 
	 * CriteriaQuery<String> cquery = cbuilder.createQuery(String.class); Root<Trim>
	 * trim = cquery.from(Trim.class);
	 * cquery.select(trim.get(Trim_.name)).groupBy(trim.get(Trim_.name)).having(
	 * cbuilder.equal(cbuilder.trim(CriteriaBuilder.Trimspec.LEADING,
	 * trim.get(Trim_.name)), expected2)); TypedQuery<String> tquery =
	 * getEntityManager().createQuery(cquery); result = tquery.getSingleResult();
	 * 
	 * if (expected.equals(result)) { pass = true;
	 * logger.log(Logger.Level.TRACE,"Expected results received"); } else {
	 * logger.log(Logger.Level.ERROR,"Did not get expected results," + "Expected:|"
	 * + expected + "|, got: |" + result + "|"); }
	 * 
	 * getEntityTransaction().commit(); } catch (Exception e) {
	 * logger.log(Logger.Level.ERROR,"Caught unexpected exception:", e);
	 * 
	 * }
	 * 
	 * if (!pass) { throw new Exception("test_trimHavingClause_02 failed"); } }
	 */

	/*
	 * testName: test_trimHavingClause_03 assertion_ids: PERSISTENCE:SPEC:369.9
	 * test_Strategy: Test for TRIM TRAILING characters (blank) in the Having Clause
	 * 
	 * DISABLE THIS TEST FOR NOW
	 * 
	 * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_03()
	 * throws Exception {
	 * 
	 * boolean pass = false; String result; final String expected =
	 * " David R. Vincent             "; final String expected2 =
	 * "' David R. Vincent'";
	 * 
	 * CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
	 * 
	 * 
	 * try { getEntityTransaction().begin(); Trim tTrim =
	 * getEntityManager().find(Trim.class, "19");
	 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
	 * (!tTrim.getName().equals(expected)) {
	 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
	 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
	 * tTrim.getName() + "|"); }
	 * 
	 * CriteriaQuery<String> cquery = cbuilder.createQuery(String.class); Root<Trim>
	 * trim = cquery.from(Trim.class);
	 * cquery.select(trim.get(Trim_.name)).groupBy(trim.get(Trim_.name)).having(
	 * cbuilder.equal(cbuilder.trim(CriteriaBuilder.Trimspec.TRAILING,
	 * trim.get(Trim_.name)), expected2)); TypedQuery<String> tquery =
	 * getEntityManager().createQuery(cquery); result = tquery.getSingleResult();
	 * 
	 * if (expected.equals(result)) { pass = true;
	 * logger.log(Logger.Level.TRACE,"Expected results received"); } else {
	 * logger.log(Logger.Level.ERROR,"Did not get expected results," + "Expected:|"
	 * + expected + "|, got: |" + result + "|"); }
	 * 
	 * getEntityTransaction().commit(); } catch (Exception e) {
	 * logger.log(Logger.Level.ERROR,"Caught unexpected exception:", e);
	 * 
	 * }
	 * 
	 * if (!pass) { throw new Exception("test_trimHavingClause_03 failed"); } }
	 */

	/*
	 * @testName: test_ABSHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.5
	 * 
	 * @test_Strategy: Test for ABS expression in the Having Clause
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_ABSHavingClause() throws Exception {
		boolean pass = false;
		Object result;
		final Double expectedPrice = 10191.90D;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(cbuilder.sum(order.get(Order_.totalPrice))).groupBy(order.get(Order_.totalPrice))
					.having(cbuilder.equal(cbuilder.abs(order.get(Order_.totalPrice)),
							cbuilder.parameter(Double.class, "doubleValue")));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("doubleValue", 5095.95D);
			result = tquery.getSingleResult();

			if (expectedPrice.equals(result)) {
				pass = true;
				logger.log(Logger.Level.TRACE, "Expected results received");
			} else {
				logger.log(Logger.Level.ERROR, "test_ABSHavingClause:  Did not get expected results."
						+ "Expected 10190, got: " + (Double) result);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_ABSHavingClause failed");
		}
	}

	/*
	 * @testName: test_SQRTWhereClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.6
	 * 
	 * @test_Strategy: Test for SQRT expression in the WHERE Clause
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_SQRTWhereClause() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "SQRT: Executing Query");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.gt(cbuilder.sqrt(order.get(Order_.totalPrice)),
					cbuilder.parameter(Double.class, "doubleValue"))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("doubleValue", 70D);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "11";
			expectedPKs[1] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_SQRTWhereClause:  Did not get expected results."
						+ "  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_SQRTWhereClause failed");
		}

	}

	/*
	 * @testName: subQueryGetSelectionGetParentTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1180; PERSISTENCE:JAVADOC:1181
	 * 
	 * @test_Strategy: Test SubQuery with a Having statement.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQueryGetSelectionGetParentTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			AbstractQuery<?> parent = sq.getParent();
			if (parent != null) {
				logger.log(Logger.Level.TRACE, "Received non-null parent query");
				if (parent instanceof CriteriaQuery || parent instanceof Subquery) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"getParent() did not return a query of type CriteriaQuery or Subquery:" + parent);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getParent() returned null");

			}

			Expression selExpression1 = order2.get(Order_.id);
			sq.select(selExpression1);
			Expression selExpression2 = sq.getSelection();
			if (selExpression1.getJavaType().equals(selExpression2.getJavaType())) {
				logger.log(Logger.Level.TRACE, "Received expected selection expression" + selExpression1.getJavaType());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Receive unexpected expression - Expected:"
						+ selExpression1.getJavaType() + ", actual" + selExpression2.getJavaType());

			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("subQueryGetSelectionGetParentTest failed");
		}
	}

	/*
	 * @testName: subQueryHavingExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1184; PERSISTENCE:JAVADOC:1180;
	 * PERSISTENCE:JAVADOC:1200; PERSISTENCE:JAVADOC:1193;
	 * 
	 * @test_Strategy: Test SubQuery with a Having statement.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQueryHavingExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o.ID FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 GROUP BY o2.ID HAVING (o2.ID < '2' ) ) "
			 * ).getResultList(); for (Object obj : q) { String o = (String) obj;
			 * logger.log(Logger.Level.TRACE,"id:" + o); }
			 */

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			AbstractQuery<?> parent = sq.getParent();
			if (parent != null) {
				logger.log(Logger.Level.TRACE, "Received non-null parent query");
				if (parent instanceof CriteriaQuery || parent instanceof Subquery) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"getParent() did not return a query of type CriteriaQuery or Subquery:" + parent);
				}
			} else {
				logger.log(Logger.Level.ERROR, "SubQuery.getParent returned null");
			}

			Expression selExpression1 = order2.get(Order_.id);
			sq.select(selExpression1);
			Expression selExpression2 = sq.getSelection();
			if (selExpression1.getJavaType().equals(selExpression2.getJavaType())) {
				logger.log(Logger.Level.TRACE, "Received expected selection expression");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "SubQuery.getParent returned null");

			}

			sq.groupBy(order2.get(Order_.id));
			Predicate pred = sq.getGroupRestriction();
			if (pred == null) {
				logger.log(Logger.Level.TRACE,
						"Received null result from subquery.getGroupRestriction() when no restrictions exist");
				Expression exp = cbuilder.lessThan(order2.get(Order_.id), "2");
				sq.having(exp);

				// this having overrides the previous
				Expression exp2 = cbuilder.lessThanOrEqualTo(order2.get(Order_.id), "1");
				sq.having(exp2);
				pred = sq.getGroupRestriction();
				if (pred != null) {
					logger.log(Logger.Level.TRACE,
							"Received non-null result from subquery.getGroupRestriction() when restrictions do exist");

					cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
					TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
					List<Order> result = tquery.getResultList();
					if (TestUtil.traceflag) {
						for (Order o : result) {
							logger.log(Logger.Level.TRACE, "id:" + o.getId());
						}
					}

					String[] expectedPKs = new String[1];
					expectedPKs[0] = "1";

					if (!checkEntityPK(result, expectedPKs)) {
						logger.log(Logger.Level.ERROR, "Did not get expected results.");

					} else {
						logger.log(Logger.Level.TRACE, "Expected results received");
						pass3 = true;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Received null result from subquery.getGroupRestriction() when restrictions do exist");

				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected null result from subquery.getGroupRestriction() when no restrictions exist:"
								+ pred.toString());

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2 | !pass3) {
			throw new Exception("subQueryHavingExpressionTest failed");
		}
	}

	/*
	 * @testName: subQueryHavingPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1185; PERSISTENCE:JAVADOC:1201;
	 * 
	 * @test_Strategy: Test SubQuery with a Having statement.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQueryHavingPredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 GROUP BY o2.ID HAVING (o2.ID > '1' AND o2.ID < '2' ) ) "
			 * ).getResultList(); for (Object obj : q) { String o = (String) obj;
			 * logger.log(Logger.Level.TRACE,"id:" + o); }
			 */

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			sq.select(order2.get(Order_.id));
			sq.groupBy(order2.get(Order_.id));
			Predicate[] predArray1 = { cbuilder.greaterThan(order2.get(Order_.id), "3"),
					cbuilder.lessThan(order2.get(Order_.id), "4") };
			sq.having(predArray1);

			// this having overrides the previous
			Predicate[] predArray2 = { cbuilder.greaterThan(order2.get(Order_.id), "1"),
					cbuilder.lessThan(order2.get(Order_.id), "2") };
			sq.having(predArray2);

			cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();
			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId() + ", totalprice:" + o.getTotalPrice());
				}
			}
			String[] expectedPKs = new String[10];
			expectedPKs[0] = "10";
			expectedPKs[1] = "11";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			expectedPKs[4] = "14";
			expectedPKs[5] = "15";
			expectedPKs[6] = "16";
			expectedPKs[7] = "17";
			expectedPKs[8] = "18";
			expectedPKs[9] = "19";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQueryHavingPredicateArrayTest failed");
		}
	}

	/*
	 * @testName: subQuerySelectExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1186
	 * 
	 * @test_Strategy: Test SubQuery with a Select (Expression) statement.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQuerySelectExpressionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.TOTALPRICE IN (SELECT MAX(o2.TOTALPRICE) FROM ORDER_TABLE o2) "
			 * ).getResultList(); for (Order o : result) {
			 * logger.log(Logger.Level.TRACE,"id:"+o.getId()+", totalprice:"+o.getTotalPrice
			 * ()); }
			 */
			String[] expectedPKs = new String[2];
			expectedPKs[0] = "11";
			expectedPKs[1] = "16";

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> order2 = sq.from(Order.class);
			sq.select(cbuilder.min(order2.get(Order_.totalPrice)));
			// this select overrides the previous
			sq.select(cbuilder.max(order2.get(Order_.totalPrice)));
			cquery.where(cbuilder.in(order.get(Order_.totalPrice)).value(sq));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId() + ", totalprice:" + o.getTotalPrice());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQuerySelectExpressionTest failed");
		}
	}

	/*
	 * @testName: subQueryWhereExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1187; PERSISTENCE:JAVADOC:1204
	 * 
	 * @test_Strategy: Test SubQuery with a Where Expression statement. SELECT o
	 * FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 WHERE
	 * (o2.ID <= 1 )
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQueryWhereExpressionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);

			logger.log(Logger.Level.TRACE, "Received null from subquery.getRestriction() when no restrictions exist");

			Root<Order> order2 = sq.from(Order.class);

			sq.select(order2.get(Order_.id));

			Expression exp1 = cbuilder.lessThan(order2.get(Order_.id), "2");
			sq.where(exp1);
			Expression exp2 = cbuilder.lessThanOrEqualTo(order2.get(Order_.id), "1");
			sq.where(exp2);

			cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			String[] expectedPKs = new String[1];
			expectedPKs[0] = "1";

			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQueryWhereExpressionTest failed");
		}
	}

	/*
	 * @testName: subQueryWherePredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1188; PERSISTENCE:JAVADOC:1205;
	 * 
	 * @test_Strategy: Test SubQuery with a Where Predicate[] statement.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQueryWherePredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 WHERE ((o2.ID > 1) AND (o2.ID < 2)) "
			 * ).getResultList(); for (Object obj : q) { String o = (String) obj;
			 * logger.log(Logger.Level.TRACE,"id:" + o); }
			 */

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			sq.select(order2.get(Order_.id));

			Predicate[] predArray1 = { cbuilder.greaterThan(order2.get(Order_.id), "3"),
					cbuilder.lessThan(order2.get(Order_.id), "4") };
			sq.where(predArray1);

			// this having overrides the previous
			Predicate[] predArray2 = { cbuilder.greaterThan(order2.get(Order_.id), "1"),
					cbuilder.lessThan(order2.get(Order_.id), "2") };
			sq.where(predArray2);

			cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			String[] expectedPKs = new String[10];
			expectedPKs[0] = "10";
			expectedPKs[1] = "11";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			expectedPKs[4] = "14";
			expectedPKs[5] = "15";
			expectedPKs[6] = "16";
			expectedPKs[7] = "17";
			expectedPKs[8] = "18";
			expectedPKs[9] = "19";

			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQueryWherePredicateArrayTest failed");
		}
	}

	/*
	 * @testName: subQueryDistinctTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1178; PERSISTENCE:JAVADOC:1195;
	 * PERSISTENCE:JAVADOC:1196; PERSISTENCE:JAVADOC:1202;
	 * 
	 * @test_Strategy: Test SubQuery with a Where Predicate[] statement.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subQueryDistinctTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass4 = false;
		boolean pass5 = true;
		boolean pass6 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			/*
			 * List<Object[]> q = getEntityManager().
			 * createQuery("SELECT l.id, l.product.id, l.product.price FROM LineItem l WHERE l.product.id = (SELECT DISTINCT l.product.id FROM LineItem l WHERE l.product.price > 5000 )"
			 * ).getResultList(); for (Object[] o : q) {
			 * logger.log(Logger.Level.TRACE,"lineitem id:" + o[0] + ", prod id:" + o[1]+
			 * ", prod price:" + o[2]); }
			 */
			CriteriaQuery<LineItem> cquery = cbuilder.createQuery(LineItem.class);
			Root<LineItem> lineItem = cquery.from(LineItem.class);
			cquery.select(lineItem);

			Subquery<String> sq = cquery.subquery(String.class);
			String name = sq.getResultType().getName();
			if (name.equals("java.lang.String")) {
				logger.log(Logger.Level.TRACE, "Received expected type:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type: java.lang.String, actual: " + name);

			}

			Root<LineItem> lineItem2 = sq.from(LineItem.class);
			sq.where(cbuilder.greaterThan(lineItem2.get(LineItem_.product).get(Product_.price), 5000D))
					.select(lineItem2.get(LineItem_.product).get(Product_.id));
			sq.distinct(true);
			boolean b = sq.isDistinct();
			if (b) {
				logger.log(Logger.Level.TRACE, "Received expected isDistinct result:" + b);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected isDistinct: true, actual:" + b);

			}

			cquery.where(cbuilder.equal(lineItem.get(LineItem_.product).get(Product_.id), sq));

			Set<Root<?>> s = sq.getRoots();
			if (s.size() == 1) {
				logger.log(Logger.Level.TRACE, "Received expected size:" + s.size());
				for (Root r : s) {
					pass4 = true;
					name = r.getJavaType().getName();
					if (name.equals("com.sun.ts.tests.jpa.common.schema30.LineItem")) {
						logger.log(Logger.Level.TRACE, "Received expected root:" + name);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected Root: com.sun.ts.tests.jpa.common.schema30.LineItem, actual:" + name);
						pass5 = false;
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected size: 1, actual:" + s.size());

			}

			TypedQuery<LineItem> tquery = getEntityManager().createQuery(cquery);
			List<LineItem> result = tquery.getResultList();

			String[] expectedPKs = new String[2];
			expectedPKs[0] = "33";
			expectedPKs[1] = "44";

			if (TestUtil.traceflag) {
				for (LineItem l : result) {
					logger.log(Logger.Level.TRACE, " lineitem id:" + l.getId() + ", prod id:" + l.getProduct().getId()
							+ ", prod price:" + l.getProduct().getPrice());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass6 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2 || !pass4 || !pass5 || !pass6) {
			throw new Exception("subQueryDistinctTest failed");
		}
	}

	/*
	 * @testName: getCorrelatedJoinsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1179;
	 * 
	 * @test_Strategy: Test getting correlated joins from subquery.
	 */
	@CleanupMethod(name = "cleanupNoData")
	public void getCorrelatedJoinsTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Set cJoins = sq.getCorrelatedJoins();
			if (cJoins != null) {
				if (cJoins.size() == 0) {
					logger.log(Logger.Level.TRACE,
							"Received expected 0 correlated joins from subquery.getCorrelatedJoins() when none exist");

					// correlate subquery
					Join<Customer, Order> sqo = sq.correlate(customer.join(Customer_.orders));
					sq.select(sqo);
					cJoins = sq.getCorrelatedJoins();
					if (cJoins != null) {
						if (cJoins.size() == 1) {
							logger.log(Logger.Level.TRACE,
									"Received expected 1 correlated join from subquery.getCorrelatedJoins()");
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received " + cJoins.size()
									+ " correlated joins from subquery.getCorrelatedJoins() when 1 exist");

						}
					} else {
						logger.log(Logger.Level.ERROR,
								"Received null from subquery.getCorrelatedJoins() when 1 correlated join exists");

					}
				} else {
					logger.log(Logger.Level.ERROR, "Received " + cJoins.size()
							+ " unexpected correlated joins from subquery.getCorrelatedJoins() when non exist");

				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Received null from subquery.getCorrelatedJoins() instead of empty set when non exist");

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" getCorrelatedJoinsTest failed");
		}
	}

	/*
	 * @testName: test_subquery_exists_02
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;
	 * 
	 * @test_Strategy: Test for EXISTS in the Where Clause for a correlated query.
	 * Select the customers with orders where total order > 1500.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_exists_02() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			// create correlated subquery
			Subquery<Order> sq = cquery.subquery(Order.class);
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Order> sqo = sqc.join(Customer_.orders);
			sq.where(cbuilder.gt(sqo.get(Order_.totalPrice), 1500D));
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "5";
			expectedPKs[1] = "10";
			expectedPKs[2] = "14";
			expectedPKs[3] = "15";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_exists_02:  Did not get expected results. "
						+ " Expected 4 references, got: " + result.size());

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_subquery_exists_02 failed");
		}
	}

	/*
	 * @testName: test_subquery_like
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792;
	 * PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801; PERSISTENCE:SPEC:802
	 * 
	 * @test_Strategy: Use LIKE expression in a sub query. Select the customers with
	 * name like Caruso. The name Caruso is derived in the subquery.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_like() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);
			// create correlated subquery
			Subquery<Customer> sq = cquery.subquery(Customer.class);
			Root<Order> sqo = sq.correlate(order);
			Join<Order, Customer> sqc = sqo.join(Order_.customer);
			sq.where(cbuilder.like(sqc.get(Customer_.name), "%Caruso")).select(sqc);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_like:  Did not get expected "
						+ " results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_subquery_like failed");
		}
	}

	/*
	 * @testName: test_subquery_in
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:352.2
	 * 
	 * @test_Strategy: Use IN expression in a sub query.
	 */
	@SetupMethod(name = "setupCustomerData")
	public void test_subquery_in() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Address> w = sqc.join(Customer_.work);
			sq.select(w.get(Address_.state));
			sq.where(cbuilder.equal(w.get(Address_.state), cbuilder.parameter(String.class, "state")));
			cquery.where(customer.get(Customer_.home).get(Address_.state).in(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("state", "MA");
			tquery.setMaxResults(customerRef.length);
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
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_in failed");
		}
	}

	/*
	 * @testName: test_subquery_between
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802
	 * 
	 * @test_Strategy: Use BETWEEN expression in a sub query. Select the customers
	 * whose orders total price is between 1000 and 2000.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_between() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute query for test_subquery_between");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Order> sqo = sqc.join(Customer_.orders);
			sq.where(cbuilder.between(sqo.get(Order_.totalPrice), 1000D, 1200D));
			cquery.where(cbuilder.exists(sq));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "7";
			expectedPKs[3] = "8";
			expectedPKs[4] = "13";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_between:  Did not get expected "
						+ " results.  Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_subquery_between failed");
		}

	}

	/*
	 * @testName: test_subquery_join
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:765; PERSISTENCE:JAVADOC:1173;
	 * PERSISTENCE:JAVADOC:1038;
	 * 
	 * @test_Strategy: Use JOIN in a sub query. Select the customers whose orders
	 * have line items of quantity > 2.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_join() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			From f = sqo.getCorrelationParent();
			String name = f.getModel().getBindableJavaType().getSimpleName();
			if (name.equals("Order")) {
				logger.log(Logger.Level.TRACE, "Received expected CorrelationParent:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected CorrelationParent: Order, actual:" + name);

			}
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_join:  Did not get expected results."
						+ "  Expected 5 references, got: " + result.size());

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception(" test_subquery_join failed");
		}
	}

	/*
	 * @testName: getCorrelationParentIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1039; PERSISTENCE:JAVADOC:1041;
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	public void getCorrelationParentIllegalStateExceptionTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Root<Order> order = sq.from(Order.class);
			Join<Order, LineItem> sql = order.join(Order_.lineItemsCollection);
			boolean b = sql.isCorrelated();
			if (!b) {
				logger.log(Logger.Level.TRACE, "Received expected result from isCorrelated(): false");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected false from isCorrelated(), actual:" + b);

			}
			try {
				From f = sql.getCorrelationParent();

				logger.log(Logger.Level.ERROR, "Did not throw IllegalStateException");
				logger.log(Logger.Level.ERROR, "getCorrelationParent() returned:" + f);
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass2 = true;
			} catch (Exception ex) {

				logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("getCorrelationParentIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: subquerySetJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1170; PERSISTENCE:JAVADOC:1175;
	 * PERSISTENCE:JAVADOC:1137; PERSISTENCE:JAVADOC:1171;
	 * 
	 * @test_Strategy: Use JOIN in a sub query.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subquerySetJoinTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
			PluralAttribute pa = o.getModel();
			String paName = pa.getCollectionType().name();
			if (paName.equals("SET")) {
				logger.log(Logger.Level.TRACE, "Received expected PluralAttribute:" + paName);
				pass1 = true;
			} else {
				logger.log(Logger.Level.TRACE, "Expected PluralAttribute: SET, actual: " + paName);

			}

			SetAttribute sa = o.getModel();
			String saName = sa.getName();
			if (saName.equals("orders2")) {
				logger.log(Logger.Level.TRACE, "Received expected SetAttribute:" + saName);
			} else {
				logger.log(Logger.Level.TRACE, "Expected SetAttribute: orders2, actual: " + saName);

			}
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_join:  Did not get expected results."
						+ "  Expected 5 references, got: " + result.size());

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("subquerySetJoinTest failed");
		}
	}

	/*
	 * @testName: subqueryListJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1176; PERSISTENCE:JAVADOC:1138;
	 * 
	 * @test_Strategy: Use JOIN in a sub query.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subqueryListJoinTest() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			ListJoin<Customer, Order> o = customer.join(Customer_.orders3);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_join:  Did not get expected results."
						+ "  Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subqueryListJoinTest failed");
		}
	}

	/*
	 * @testName: subqueryCollectionJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1174
	 * 
	 * @test_Strategy: Use JOIN in a sub query.
	 */
	@SetupMethod(name = "setupOrderData")
	public void subqueryCollectionJoinTest() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_join:  Did not get expected results."
						+ "  Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subqueryCollectionJoinTest failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_GT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:766; PERSISTENCE:SPEC:793; PERSISTENCE:SPEC:799
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator ">".
	 * Select all customers where total price of orders is greater than ALL the
	 * values in the result set.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ALL_GT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> l = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(l.get(LineItem_.quantity), 3));
			sq.select(sqo.get(Order_.totalPrice));
			cquery.select(customer);
			cquery.where(cbuilder.gt(o.get(Order_.totalPrice), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "5";
			expectedPKs[1] = "10";
			expectedPKs[2] = "14";
			expectedPKs[3] = "15";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_GT:  Did not get expected results. "
						+ " Expected 4 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {

			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_subquery_ALL_GT failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_LT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "<".
	 * Select all customers where total price of orders is less than ALL the values
	 * in the result set.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ALL_LT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			cquery.select(customer);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3));
			sq.select(sqo.get(Order_.totalPrice));
			cquery.where(cbuilder.lt(o.get(Order_.totalPrice), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_LT:  Did not get expected results."
						+ "  Expected 1 reference, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_LT failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_EQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "=".
	 * Select all customers where total price of orders is = ALL the values in the
	 * result set. The result set contains the min of total price of orders.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ALL_EQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			cquery.select(customer);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.min(sqo.get(Order_.totalPrice)));
			cquery.where(cbuilder.equal(o.get("totalPrice"), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_EQ:  Did not get expected results. "
						+ " Expected 1 reference, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_EQ failed");
		}

	}

	/*
	 * @testName: test_subquery_ALL_LTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "<=".
	 * Select all customers where total price of orders is <= ALL the values in the
	 * result set. The result set contains the total price of orders where count of
	 * lineItems > 3.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ALL_LTEQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.le(o.get(Order_.totalPrice), cbuilder.all(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "9";
			expectedPKs[1] = "12";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_LTEQ:  Did not get expected results.  "
						+ "Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_LTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_GTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator ">=".
	 * Select all customers where total price of orders is >= ALL the values in the
	 * result set.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ALL_GTEQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.ge(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.ge(o.get(Order_.totalPrice), cbuilder.all(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "14";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_GTEQ:  Did not get expected results. "
						+ " Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_GTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_NOTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "<>".
	 * Select all customers where total price of orders is <> ALL the values in the
	 * result set.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ALL_NOTEQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.min(sqo.get(Order_.totalPrice)));
			cquery.where(cbuilder.notEqual(o.get(Order_.totalPrice), cbuilder.all(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[17];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_NOTEQ:  Did not get expected results."
						+ "  Expected 17 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_NOTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_ANY_GT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ANY in a subquery with the relational operator ">".
	 * Select all customers where total price of orders is greater than ANY of the
	 * values in the result. The result set contains the total price of orders where
	 * count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ANY_GT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.gt(o.get(Order_.totalPrice), cbuilder.any(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[16];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "10";
			expectedPKs[9] = "11";
			expectedPKs[10] = "13";
			expectedPKs[11] = "14";
			expectedPKs[12] = "15";
			expectedPKs[13] = "16";
			expectedPKs[14] = "17";
			expectedPKs[15] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ANY_GT:  Did not get expected results. "
						+ "  Expected 16 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("test_subquery_ANY_GT failed");
		}
	}

	/*
	 * @testName: test_subquery_ANY_LT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ANY in a subquery with the relational operator "<".
	 * Select all customers where total price of orders is less than ANY of the
	 * values in the result set. The result set contains the total price of orders
	 * where count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ANY_LT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.lt(o.get(Order_.totalPrice), cbuilder.any(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[17];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "11";
			expectedPKs[10] = "12";
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ANY_LT:  Did not get expected results.  "
						+ "Expected 17 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ANY_LT failed");
		}
	}

	/*
	 * @testName: test_subquery_ANY_EQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ANY in a subquery with the relational operator "=".
	 * Select all customers where total price of orders is = ANY the values in the
	 * result set. The result set contains the min and avg of total price of orders.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_ANY_EQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.max(sqo.get(Order_.totalPrice)));
			cquery.where(cbuilder.equal(o.get(Order_.totalPrice), cbuilder.any(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "14";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ANY_EQ:  Did not get expected results.  "
						+ "Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_ANY_EQ failed");
		}
	}

	/*
	 * @testName: test_subquery_SOME_LTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:795;
	 * PERSISTENCE:SPEC:797; PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: SOME with less than or equal to The result set contains the
	 * total price of orders where count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_SOME_LTEQ() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.le(o.get(Order_.totalPrice), cbuilder.some(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[18];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "12";
			expectedPKs[12] = "13";
			expectedPKs[13] = "14";
			expectedPKs[14] = "15";
			expectedPKs[15] = "16";
			expectedPKs[16] = "17";
			expectedPKs[17] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_SOME_LTEQ:  Did not get expected results. "
						+ " Expected 18 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_subquery_SOME_LTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_SOME_GTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:795;
	 * PERSISTENCE:SPEC:797; PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for SOME in a subquery with the relational operator
	 * ">=". Select all customers where total price of orders is >= SOME the values
	 * in the result set. The result set contains the total price of orders where
	 * count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	public void test_subquery_SOME_GTEQ() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.ge(o.get(Order_.totalPrice), cbuilder.some(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[17];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_SOME_GTEQ:  Did not get expected results. "
						+ " Expected 17 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_SOME_GTEQ failed");
		}
	}

}
