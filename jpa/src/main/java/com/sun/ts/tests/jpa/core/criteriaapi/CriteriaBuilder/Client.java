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

package com.sun.ts.tests.jpa.core.criteriaapi.CriteriaBuilder;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Alias;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.HardwareProduct;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Order_;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.ShelfLife;
import com.sun.ts.tests.jpa.common.schema30.SoftwareProduct;
import com.sun.ts.tests.jpa.common.schema30.SoftwareProduct_;
import com.sun.ts.tests.jpa.common.schema30.Spouse;
import com.sun.ts.tests.jpa.common.schema30.Trim;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.Trimspec;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client extends Util {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	@BeforeAll
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			getEntityManager();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: createQuery
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:756; PERSISTENCE:SPEC:1701;
	 * PERSISTENCE:SPEC:1703; PERSISTENCE:SPEC:1704;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createQuery() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createQuery Test  failed");
		}
	}

	/*
	 * @testName: createQuery2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:757; PERSISTENCE:SPEC:1703;
	 * PERSISTENCE:SPEC:1704;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createQuery2() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createQuery2 Test  failed");
		}
	}

	/*
	 * @testName: createTuple
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:758; PERSISTENCE:SPEC:1703;
	 * PERSISTENCE:SPEC:1704;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createTuple() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query Tuple");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query Tuple");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createTuple Test  failed");
		}
	}

	/*
	 * @testName: construct
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:752; PERSISTENCE:JAVADOC:1470;
	 * PERSISTENCE:JAVADOC:1026; PERSISTENCE:SPEC:1705; PERSISTENCE:SPEC:1752;
	 * PERSISTENCE:SPEC:1754; PERSISTENCE:SPEC:1669;
	 *
	 * @test_Strategy: convert the following jpql to CriteriaQuery
	 *
	 * SELECT NEW com.sun.ts.tests.jpa.core.query.language.schema30.Customer (c.id,
	 * c.name) FROM Customer c where c.home.city = 'Roslindale'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void construct() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];

		expected[0] = customerRef[16].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(cbuilder.construct(com.sun.ts.tests.jpa.common.schema30.Customer.class,
					customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class))))
					.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), "Roslindale"));
			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> actual = tq.getResultList();
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("construct Test  failed");
		}
	}

	/*
	 * @testName: tupleIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:433
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleIntTest() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();

			for (Tuple t : result) {
				Integer id = Integer.valueOf((String) t.get(0));
				String name = (String) t.get(1);
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass1 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass1 = false;
					}
				}

			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleIntTest test failed");
		}
	}

	/*
	 * @testName: tupleToArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:435; PERSISTENCE:SPEC:1727;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c where (id = 3 or id = 4)
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleToArrayTest() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		expected.add(customerRef[2].getId());
		expected.add(customerRef[2].getName());
		expected.add(customerRef[3].getId());
		expected.add(customerRef[3].getName());

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			cquery.where(
					cbuilder.or(cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "3"),
							cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "4"))

			);

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			List<String> actual = new ArrayList<String>();
			for (Tuple t : result) {
				for (Object o : t.toArray()) {
					logger.log(Logger.Level.TRACE, "Object:" + o);
					actual.add((String) o);
				}
			}
			if (TestUtil.traceflag) {
				logger.log(Logger.Level.TRACE, "actual" + actual);
			}

			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected + ", actual: " + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("tupleToArrayTest test failed");
		}
	}

	/*
	 * @testName: tupleIntClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:432; PERSISTENCE:SPEC:1706
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleIntClassTest() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();

			for (Tuple t : result) {
				Integer id = Integer.valueOf((String) t.get(0));
				String name = (String) t.get(1);
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass1 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass1 = false;
					}
				}

			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleIntClassTest test failed");
		}
	}

	/*
	 * @testName: tupleGetIntIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:603; PERSISTENCE:JAVADOC:1164;
	 * PERSISTENCE:SPEC:1303;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c Call Tuple.get() using a tuple element that does not
	 * exist
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleGetIntIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(
					new Selection[] { customer.get(Customer_.getSingularAttribute("id", String.class)).alias("ID"),
							customer.get(Customer_.getSingularAttribute("name", String.class)).alias("NAME") });

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			for (Tuple t : result) {
				logger.log(Logger.Level.INFO, "Testing invalid index");
				try {
					t.get(99);
					logger.log(Logger.Level.ERROR,
							"Did not get expected IllegalArgumentException for invalid index:" + t.get(99));
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
					if (getEntityTransaction().getRollbackOnly() != true) {
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
					}
				} catch (Exception ex) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
				}
				break;
			}

		} else {

			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("tupleGetIntIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: tupleGetIntClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:602; PERSISTENCE:SPEC:1303;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c Call Tuple.get() using a tuple element that does not
	 * exist
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleGetIntClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Use Tuple Query");
			cquery.multiselect(customer.get("id"), customer.get("name"), customer.get("home"));
			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			Tuple t = result.get(0);
			logger.log(Logger.Level.INFO, "Testing valid index");
			logger.log(Logger.Level.TRACE, "value:" + t.get(1, String.class));

			logger.log(Logger.Level.INFO, "Testing invalid index");
			try {
				t.get(99, String.class);

				logger.log(Logger.Level.ERROR,
						"Did not get expected IllegalArgumentException for invalid index:" + t.get(99, String.class));
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				if (getEntityTransaction().getRollbackOnly() != true) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			logger.log(Logger.Level.INFO, "Testing invalid type");

			try {
				t.get(2, Date.class);

				logger.log(Logger.Level.ERROR,
						"Did not get expected IllegalArgumentException for invalid type:" + t.get(2, Date.class));
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleGetIntClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: tupleGetElementsGetTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:429; PERSISTENCE:JAVADOC:434;
	 * PERSISTENCE:JAVADOC:1168; PERSISTENCE:JAVADOC:1169; PERSISTENCE:SPEC:1321;
	 * PERSISTENCE:SPEC:1763;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c where c.name is not nulls
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleGetElementsGetTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)).alias("ID"),
					customer.get(Customer_.getSingularAttribute("name", String.class)).alias("NAME"));

			cquery.where(cbuilder.isNotNull(customer.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);
			List<Tuple> result = tq.getResultList();

			for (Tuple t : result) {
				String expId = (String) t.get("ID");
				String expName = (String) t.get("NAME");
				logger.log(Logger.Level.TRACE, "Received:" + expId + ", " + expName);

				List<TupleElement<?>> lte = t.getElements();
				for (TupleElement<?> te : lte) {
					String alias = te.getAlias();
					String type = te.getJavaType().getName();
					if (alias.equals("ID")) {
						String actId = (String) t.get(te);
						if (actId.equals(expId)) {
							logger.log(Logger.Level.TRACE, "Received expected id:" + actId);
							pass1 = true;

						} else {
							logger.log(Logger.Level.ERROR, "Expected id:" + expId + ", actual:|" + actId + "|");
						}
						if (type.equals("java.lang.String")) {
							pass2 = true;

							logger.log(Logger.Level.TRACE, "Received expected Java Type for ID:" + type);
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected java type of ID: java.lang.String, actual:|" + type + "|");
						}

					} else if (alias.equals("NAME")) {
						String actName = (String) t.get(te);
						if (actName.equals(expName)) {
							logger.log(Logger.Level.TRACE, "Received expected name:" + actName);
							pass3 = true;

						} else {
							logger.log(Logger.Level.ERROR, "Expected name:|" + expName + "|, actual:|" + actName + "|");
						}

						if (type.equals("java.lang.String")) {
							logger.log(Logger.Level.TRACE, "Received expected Java Type for NAME:" + type);
							pass4 = true;

						} else {
							logger.log(Logger.Level.ERROR,
									"Expected java type of NAME: java.lang.String, actual:|" + type + "|");
						}

					} else {
						logger.log(Logger.Level.ERROR, "Received unexpected TupleElement:" + alias);
					}
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("tupleGetElementsGetTest failed");
		}
	}

	/*
	 * @testName: tupleGetTupleElementIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:599; PERSISTENCE:SPEC:1303;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery SELECT ID, NAME
	 * FROM CUSTOMER_TABLE WHERE (ID = 1) SELECT ID, QUANTITY FROM PRODUCT_TABLE
	 * WHERE (ID = 1)
	 */
	@SetupMethod(name = "setupCustAliasProductData")
	@Test
	public void tupleGetTupleElementIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Execute first Tuple Query");

			cquery.multiselect(customer.get("id").alias("ID"), customer.get("name").alias("NAME"));

			cquery.where(cbuilder.equal(customer.get("id"), "3"));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);
			List<Tuple> result = tq.getResultList();

			logger.log(Logger.Level.TRACE, "Number of Tuples from first query:" + result.size());
			Tuple t1 = result.get(0);

			logger.log(Logger.Level.TRACE, "Tuples Received:" + t1.get(0) + ", " + t1.get(1));

			// get second Tuple and second TupleElement inorder to trigger
			// IllegalArgumentException
			CriteriaQuery<Tuple> cquery1 = cbuilder.createTupleQuery();
			Root<Product> product = cquery1.from(Product.class);

			logger.log(Logger.Level.TRACE, "Execute second Tuple Query");

			cquery1.multiselect(product.get("id").alias("ID"), product.get("quantity").alias("QUANTITY"));

			cquery1.where(cbuilder.equal(product.get("id"), "1"));

			TypedQuery<Tuple> tq2 = getEntityManager().createQuery(cquery1);
			List<Tuple> result2 = tq2.getResultList();
			Tuple t2 = null;
			logger.log(Logger.Level.TRACE, "Number of Tuples received from second query:" + result2.size());
			try {
				t2 = result2.get(0);
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			if (t2 != null) {

				logger.log(Logger.Level.TRACE, "Tuple Received:" + t2.get(0) + ", " + t2.get(1));

				List<TupleElement<?>> lte2 = t2.getElements();
				TupleElement<?> te2 = lte2.get(1);

				logger.log(Logger.Level.TRACE,
						"TupleElement from second query that will be looked up in the Tuple result returned from first query:"
								+ te2.getAlias());
				try {

					// Using a tuple element returned in the second query, try to get a
					// tuple from the first query using
					// that tuple element
					t1.get(te2);
					logger.log(Logger.Level.ERROR,
							"Did not throw IllegalArgumentException when calling Tuple.get with a TupleElement that doesn't exist");

				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
					if (getEntityTransaction().getRollbackOnly() != true) {
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
					}
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "result2.get(0) returned null");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleGetTupleElementIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: tupleGetStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:431
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleGetStringTest() throws Exception {
		boolean pass1 = true;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)).alias("ID"),
					customer.get(Customer_.getSingularAttribute("name", String.class)).alias("NAME"));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();

			for (Tuple t : result) {
				Integer id = Integer.valueOf((String) t.get(0));
				String name = (String) t.get(1);
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass1 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass1 = false;
					}
				}

			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleGetStringTest failed");
		}
	}

	/*
	 * @testName: tupleGetStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:601; PERSISTENCE:SPEC:1303;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c Call Tuple.get() using a tuple element that does not
	 * exist
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleGetStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)).alias("ID"),
					customer.get(Customer_.getSingularAttribute("name", String.class)).alias("NAME"));

			cquery.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"));
			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			Tuple t = result.get(0);
			logger.log(Logger.Level.INFO, "Testing valid alias");
			logger.log(Logger.Level.TRACE, "value:" + t.get("NAME"));

			logger.log(Logger.Level.INFO, "Testing invalid alias");
			try {
				t.get("doesnotexist");
				logger.log(Logger.Level.ERROR,
						"Did not get expected IllegalArgumentException for TupleElement:t.get(\"doesnotexist\")");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				if (getEntityTransaction().getRollbackOnly() != true) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception:" + e);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("tupleGetStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: tupleGetStringClassIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:600; PERSISTENCE:SPEC:1303;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c Call Tuple.get() using a tuple type that does not
	 * match
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleGetStringClassIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)).alias("ID"),
					customer.get(Customer_.getSingularAttribute("name", String.class)).alias("NAME"));

			cquery.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"));
			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			Tuple t = result.get(0);
			logger.log(Logger.Level.INFO, "Testing valid index");
			logger.log(Logger.Level.TRACE, "value:" + t.get("NAME"));
			logger.log(Logger.Level.INFO, "Testing a name that does not exist");

			try {
				t.get("doesnotexist", String.class);
				logger.log(Logger.Level.ERROR,
						"Did not throw IllegalArgumentException for TupleElement: t.get(\"doesnotexist\", String.class)");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				if (getEntityTransaction().getRollbackOnly() != true) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Transaction was marked for rollback and should not have been");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception:" + e);
			}
			logger.log(Logger.Level.INFO, "Testing invalid type");

			try {
				t.get("ID", Date.class);
				logger.log(Logger.Level.ERROR,
						"Did not throw IllegalArgumentException for TupleElement:t.get(\"ID\", Date.class)");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Got expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception:" + e);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleGetStringClassIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: tupleGetStringClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:430
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleGetStringClassTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)).alias("ID"),
					customer.get(Customer_.getSingularAttribute("name", String.class)).alias("NAME"));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

			List<Tuple> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();

			pass1 = true;
			for (Tuple t : result) {
				Integer id = Integer.valueOf((String) t.get(0));
				String name = (String) t.get(1);
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass2 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass2 = false;
					}
				}

			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("tupleGetStringClassTest failed");
		}
	}

	/*
	 * @testName: tupleElementGetAliasTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:436; PERSISTENCE:JAVADOC:1102;
	 * PERSISTENCE:JAVADOC:1103
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void tupleElementGetAliasTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			String id = idPath.alias("IDID").getAlias();
			if (id.equals("IDID")) {
				logger.log(Logger.Level.TRACE, "id=" + id);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected id value:IDID, actual value:" + id);
			}
			Bindable b = idPath.getModel();
			Bindable.BindableType bbt = b.getBindableType();
			if (bbt.equals(Bindable.BindableType.SINGULAR_ATTRIBUTE)) {
				logger.log(Logger.Level.TRACE, "Received expected model:" + bbt.name());
				pass2 = true;

			} else {
				logger.log(Logger.Level.ERROR,
						"Expected model:" + Bindable.BindableType.SINGULAR_ATTRIBUTE.name() + ", actual:" + bbt.name());
			}

			Path p = idPath.getParentPath();
			Class parent = p.getJavaType();
			if (parent.getName().equals(Customer.class.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected parent:" + parent.getName());
				pass3 = true;

			} else {
				logger.log(Logger.Level.ERROR,
						"Expected parent class:" + Customer.class.getName() + ", actual:" + parent.getName());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("tupleElementGetAliasTest failed");
		}
	}

	/*
	 * @testName: tupleElementGetJavaTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:437
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleElementGetJavaTypeTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			Class type = idPath.getJavaType();
			if (type.getSimpleName().equals("String")) {
				logger.log(Logger.Level.TRACE, "type=" + type.getSimpleName());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected element type of String, actual value:" + type.getSimpleName());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass) {
			throw new Exception("tupleElementGetJavaTypeTest failed");
		}
	}

	/*
	 * @testName: tupleSelectionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:872; PERSISTENCE:SPEC:1752;
	 * PERSISTENCE:SPEC:1754;
	 *
	 * @test_Strategy: Call tuple(Selection[]) and verify result Select c.id, c.name
	 * from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void tupleSelectionArrayTest() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
			if (cquery != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
				Root<Customer> cust = cquery.from(Customer.class);

				// Get Metamodel from Root
				EntityType<Customer> Customer_ = cust.getModel();
				logger.log(Logger.Level.TRACE, "Use Tuple Query");

				cquery.where(qbuilder.equal(cust.get(Customer_.getSingularAttribute("id", String.class)), "4"));

				Selection[] s = { cust.get("id"), cust.get("name") };
				cquery.select(qbuilder.tuple(s));

				Query q = getEntityManager().createQuery(cquery);

				List result = q.getResultList();
				if (result.size() == 1) {
					Tuple t = (Tuple) result.get(0);
					String id = (String) t.get(0);

					if (id.equals(customerRef[3].getId())) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + id);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id:" + customerRef[3].getId() + ", actual:" + id);
					}
					String name = (String) t.get(1);

					if (name.equals(customerRef[3].getName())) {
						logger.log(Logger.Level.TRACE, "Received expected name:" + name);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[3].getName() + ", actual:" + name);
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Received incorrect result size - Expected: 1, Actual:" + result.size());
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {

			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("tupleSelectionArrayTest failed");
		}
	}

	/*
	 * @testName: tupleSelectionArrayIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:873
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void tupleSelectionArrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
			if (cquery != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
				Root<Customer> cust = cquery.from(Customer.class);

				Selection[] s = { cust.get("id"), cust.get("name") };

				logger.log(Logger.Level.INFO, "Testing tuple");
				try {
					qbuilder.tuple(qbuilder.tuple(s));
					logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass1 = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

				logger.log(Logger.Level.INFO, "Testing array");
				try {
					qbuilder.tuple(qbuilder.array(s));
					logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
					pass2 = true;
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("tupleSelectionArrayIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: array
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:739; PERSISTENCE:SPEC:1752;
	 * PERSISTENCE:SPEC:1754;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery and return the
	 * result as an object array
	 *
	 * Select c.id, c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void array() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		boolean pass3 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.select(cbuilder.array(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class))));

			Query q = getEntityManager().createQuery(cquery);

			List<Object[]> result = q.getResultList();

			List<Integer> actual = new ArrayList<Integer>();

			pass1 = true;
			for (Object[] row : result) {
				Integer id = Integer.valueOf((String) row[0]);
				String name = (String) row[1];
				if (name != null) {
					if (customerRef[id - 1].getName().equals(name)) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						pass2 = false;
					}
				} else {
					if (customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
						pass2 = false;
					}
				}
			}
			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("array test failed");
		}
	}

	/*
	 * @testName: arrayIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:740
	 *
	 * @test_Strategy: Create a CriteriaBuilder array and pass that to the
	 * CriteriaBuilder.array() method
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void arrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			Selection[] s = { customer.get("id"), customer.get("name") };

			logger.log(Logger.Level.INFO, "Testing tuple");
			try {
				qbuilder.array(qbuilder.tuple(s));
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

			logger.log(Logger.Level.INFO, "Testing array");
			try {
				qbuilder.array(qbuilder.array(s));
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2) {
			throw new Exception("arrayIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: constructIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:753
	 *
	 * @test_Strategy: Create a CriteriaBuilder array and pass that to the
	 * CriteriaBuilder.construct(...) method
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void constructIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			Selection[] s = { customer.get("id"), customer.get("name") };

			logger.log(Logger.Level.INFO, "Testing tuple");
			try {
				qbuilder.construct(Customer.class, qbuilder.tuple(s));
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

			logger.log(Logger.Level.INFO, "Testing array");
			try {
				qbuilder.construct(Customer.class, qbuilder.array(s));
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2) {
			throw new Exception("constructIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: primaryKeyJoinColumnTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1120;
	 * PERSISTENCE:SPEC:1121;PERSISTENCE:SPEC:1121.1; PERSISTENCE:SPEC:2109;
	 * 
	 * @test_Strategy: Select p from Product p where p.whouse = "WH5"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void primaryKeyJoinColumnTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			cquery.select(product);
			cquery.where(cbuilder.equal(product.get("wareHouse"), "WH5"));
			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			if (result.size() == 1 && result.get(0).getWareHouse().equals("WH5")) {
				logger.log(Logger.Level.TRACE, "Expected results received:" + result.get(0).getWareHouse());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned: " + result.get(0).getWareHouse() + ", expected: WH5");
				for (Product p : result) {
					logger.log(Logger.Level.ERROR, "**id=" + p.getId() + ", model=" + p.getWareHouse());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("primaryKeyJoinColumnTest  failed");
		}
	}

	/*
	 * @testName: asc
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:741; PERSISTENCE:SPEC:1690;
	 * PERSISTENCE:SPEC:1774;
	 *
	 * @test_Strategy: Select hardwareProduct from HardwareProduct ORDER BY
	 * hardware.Product.modeNumber
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void asc() throws Exception {
		final int expectedModelNumber = 40;
		final int expectedResultSize = 10;
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<HardwareProduct> cquery = cbuilder.createQuery(HardwareProduct.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<HardwareProduct> hardProd = cquery.from(HardwareProduct.class);
			EntityType<HardwareProduct> HardwareProduct_ = hardProd.getModel();
			cquery.select(hardProd);
			cquery.orderBy(cbuilder.asc(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber"))));
			TypedQuery<HardwareProduct> tq = getEntityManager().createQuery(cquery);
			List<HardwareProduct> result = tq.getResultList();

			if (result.size() == expectedResultSize && result.get(0).getModelNumber() == expectedModelNumber) {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned: " + result.get(0).getModelNumber() + ", expected: " + expectedModelNumber);
				for (HardwareProduct o : result) {
					logger.log(Logger.Level.ERROR, "**id=" + o.getId() + ", model=" + o.getModelNumber());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("asc Test  failed");
		}
	}

	/*
	 * @testName: desc
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:762; PERSISTENCE:SPEC:1774;
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void desc() throws Exception {
		final int expectedModelNumber = 104;
		final int expectedResultSize = 10;
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<HardwareProduct> cquery = cbuilder.createQuery(HardwareProduct.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<HardwareProduct> hardProd = cquery.from(HardwareProduct.class);
			EntityType<HardwareProduct> HardwareProduct_ = hardProd.getModel();
			cquery.select(hardProd);
			cquery.orderBy(cbuilder.desc(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber"))));
			TypedQuery<HardwareProduct> tq = getEntityManager().createQuery(cquery);
			List<HardwareProduct> result = tq.getResultList();

			if (result.size() == expectedResultSize && result.get(7).getModelNumber() == expectedModelNumber) {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned: " + result.get(7).getModelNumber() + "expected: " + expectedModelNumber);
				for (HardwareProduct o : result) {
					logger.log(Logger.Level.ERROR, "**id=" + o.getId() + ", model=" + o.getModelNumber());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("desc Test  failed");
		}
	}

	/*
	 * @testName: avg
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:742; PERSISTENCE:SPEC:1510;
	 * PERSISTENCE:SPEC:1740;
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * AVG(o.totalPrice) FROM Order o
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void avg() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> order_ = order.getModel();
			cquery.select(cbuilder.avg(order.get(order_.getSingularAttribute("totalPrice", Double.class))));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double d1 = 1487.29;
			Double d2 = 1487.30;

			Double d3 = tq.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "Avg test returned expected results: " + d1);
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("avg test  failed");
		}
	}

	/*
	 * @testName: sumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:853; PERSISTENCE:SPEC:1740;
	 * PERSISTENCE:SPEC:1746; PERSISTENCE:SPEC:1746.1;
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * Sum(p.price) FROM Product p
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumExpTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cbuilder.createQuery(Number.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("price", Double.class));
			cquery.select(cbuilder.sum(e));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			double d1 = 33387.14D;
			double d2 = 33387.15D;

			Number d3 = tq.getSingleResult();
			if (d3 instanceof Double) {
				logger.log(Logger.Level.TRACE, "Received expected type of Double");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type Double, actual:" + d3);
			}
			double d4 = d3.doubleValue();
			if (((d4 >= d1) && (d4 < d2))) {
				logger.log(Logger.Level.TRACE, "sum returned expected results: " + d1);
				pass2 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("sumExpTest failed");
		}
	}

	/*
	 * @testName: sumExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:855
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * sum(p.price,100) FROM Product p WHERE p.id = 1
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumExpNumTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("quantity", Integer.class));
			cquery.select(cbuilder.sum(e, 100));
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id")), "1"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer actual = tq.getSingleResult();
			Integer expected = productRef[0].getQuantity() + 100;
			if (actual.equals(expected)) {
				logger.log(Logger.Level.TRACE, "sum returned expected results: " + actual);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + expected + ", actual value:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumExpNumTest failed");
		}
	}

	/*
	 * @testName: sumNumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:856
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * sum(100,p.price) FROM Product p WHERE p.id = 1
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumNumExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("quantity", Integer.class));
			cquery.select(cbuilder.sum(100, e));
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id")), "1"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer actual = tq.getSingleResult();
			Integer expected = productRef[0].getQuantity() + 100;
			if (actual.equals(expected)) {
				logger.log(Logger.Level.TRACE, "sum test returned expected results: " + actual);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + expected + ", actual value:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumNumExpTest failed");
		}
	}

	/*
	 * @testName: sumExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:854
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * sum(p.quantity,p.quantity) FROM Product p WHERE p.id = 1
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();
			Expression e = product.get(Product_.getSingularAttribute("quantity", Integer.class));
			cquery.select(cbuilder.sum(e, e));
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id")), "1"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer actual = tq.getSingleResult();
			Integer expected = productRef[0].getQuantity() + productRef[0].getQuantity();
			if (actual.equals(expected)) {
				logger.log(Logger.Level.TRACE, "sum test returned expected results: " + actual);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected value:" + expected + ", actual value:" + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumExpExpTest failed");
		}
	}

	/*
	 * @testName: max
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:815; PERSISTENCE:SPEC:1740;
	 * PERSISTENCE:SPEC:1674;
	 *
	 * @test_Strategy: Convert following JPQL to CriteriaQuery SELECT DISTINCT
	 * MAX(l.quantity) FROM LineItem l
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void max() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		final Integer i1 = 8;
		List<Integer> i2, i3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery1 = cbuilder.createQuery(Integer.class);
		CriteriaQuery<Integer> cquery2 = cbuilder.createQuery(Integer.class);
		if (cquery1 != null && cquery2 != null) {
			logger.log(Logger.Level.TRACE,
					"select DISTINCT MAXIMUM number of lineItem quantities available an order may have");
			Root<LineItem> lineItem = cquery1.from(LineItem.class);
			// Get Metamodel from Root
			EntityType<LineItem> lineItem_ = lineItem.getModel();
			cquery1.select(cbuilder.max(lineItem.get(lineItem_.getSingularAttribute("quantity", Integer.class))));
			cquery1.distinct(true);
			TypedQuery<Integer> tq1 = getEntityManager().createQuery(cquery1);
			i2 = tq1.getResultList();

			logger.log(Logger.Level.TRACE, "select MAXIMUM number of lineItem quantities available an order may have");
			Root<LineItem> lineItem2 = cquery2.from(LineItem.class);

			// Get Metamodel from Root
			EntityType<LineItem> lineItem2_ = lineItem2.getModel();
			cquery2.select(cbuilder.max(lineItem2.get(lineItem2_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Integer> tq2 = getEntityManager().createQuery(cquery2);

			i3 = tq2.getResultList();

			logger.log(Logger.Level.INFO, "Verify select WITH DISTINCT keyword");
			if (i2.size() == 1) {
				Integer result = i2.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass1 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i2) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}
			logger.log(Logger.Level.INFO, "Verify select WITHOUT DISTINCT keyword");
			if (i3.size() == 1) {
				Integer result = i3.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass2 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i3) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("max test failed");
		}
	}

	/*
	 * @testName: min
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:816; PERSISTENCE:SPEC:1740;
	 * PERSISTENCE:SPEC:1674;
	 *
	 * @test_Strategy: convert the forllowing JPQL into CriteriaQuery SELECT
	 * DISTINCT MIN(l.quantity) FROM LineItem l
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void min() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		final Integer i1 = 1;
		List<Integer> i2, i3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery1 = cbuilder.createQuery(Integer.class);
		CriteriaQuery<Integer> cquery2 = cbuilder.createQuery(Integer.class);
		if (cquery1 != null && cquery2 != null) {
			logger.log(Logger.Level.TRACE,
					"select DISTINCT MIN number of lineItem quantities available an order may have");
			Root<LineItem> lineItem = cquery1.from(LineItem.class);
			// Get Metamodel from Root
			EntityType<LineItem> lineItem_ = lineItem.getModel();
			cquery1.select(cbuilder.min(lineItem.get(lineItem_.getSingularAttribute("quantity", Integer.class))));
			cquery1.distinct(true);
			TypedQuery<Integer> tq1 = getEntityManager().createQuery(cquery1);
			i2 = tq1.getResultList();

			logger.log(Logger.Level.TRACE, "select MIN number of lineItem quantities available an order may have");
			Root<LineItem> lineItem2 = cquery2.from(LineItem.class);

			// Get Metamodel from Root
			EntityType<LineItem> lineItem2_ = lineItem2.getModel();
			cquery2.select(cbuilder.min(lineItem2.get(lineItem2_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Integer> tq2 = getEntityManager().createQuery(cquery2);

			i3 = tq2.getResultList();

			logger.log(Logger.Level.INFO, "Verify select WITH DISTINCT keyword");
			if (i2.size() == 1) {
				Integer result = i2.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass1 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i2) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}
			logger.log(Logger.Level.INFO, "Verify select WITHOUT DISTINCT keyword");
			if (i3.size() == 1) {
				Integer result = i3.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass2 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i3) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("max test failed");
		}
	}

	/*
	 * @testName: greatest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:777
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void greatest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			logger.log(Logger.Level.TRACE, "find Greatest Order id Using lexicographic comparision");

			cquery.select(cbuilder.greatest(order.get(Order_.getSingularAttribute("id", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			// lexicographic comparision should return 9 as the greatest id
			// from the following list of ids
			// { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 }
			String expectedResult = "9";
			String queryOutput = tq.getSingleResult();

			if (queryOutput.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Received expected result : " + expectedResult);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Query returned : " + queryOutput + " Expected result : " + expectedResult);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("greatest test failed");
		}
	}

	/*
	 * @testName: least
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:794
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void least() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			logger.log(Logger.Level.TRACE, "find least Order id Using lexicographic comparision");

			cquery.select(cbuilder.least(order.get(Order_.getSingularAttribute("id", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			// lexicographic comparision should return 1 as the least id
			// from the following list of ids
			// { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 }
			String expectedResult = "1";
			String queryOutput = tq.getSingleResult();

			if (queryOutput.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Received expected result : " + expectedResult);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Query returned : " + queryOutput + " Expected result : " + expectedResult);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("least test failed");
		}
	}

	/*
	 * @testName: count
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:754; PERSISTENCE:SPEC:1740;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select COUNT
	 * (o.id) From Order o
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void count() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);
			EntityType<Order> Order_ = order.getModel();
			cquery.select(cbuilder.count(order.get(Order_.getSingularAttribute("id", String.class))));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);

			Long countResult = tq.getSingleResult();
			Long expectedCount = 20L;

			if (countResult.equals(expectedCount)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "count test returned:" + countResult + "expected: " + expectedCount);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("count test failed");
		}
	}

	/*
	 * @testName: countDistinct
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:755
	 *
	 * @test_Strategy: Select DISTINCT Count(c.home.city) from Customer c
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void countDistinct() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);
			logger.log(Logger.Level.TRACE, "count number of orders by customer");

			cquery.select(cbuilder.countDistinct(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("city", String.class))));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);

			Long countResult = tq.getSingleResult();
			Long expectedCount = 16L;

			if (countResult.equals(expectedCount)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "count test returned:" + countResult + "expected: " + expectedCount);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("countDistinct test failed");
		}
	}

	/*
	 * @testName: exists
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:769; PERSISTENCE:JAVADOC:1190;
	 * PERSISTENCE:SPEC:1767;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery
	 *
	 * SELECT product FROM PRODUCT product WHERE EXISTS (Select hardProd From
	 * PRODUCT hardprod where hardprod.id = '19').
	 *
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void exists() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = "19";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cquery.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product.class);
			if (hardProd.getModel().getName().equals(Product.class.getSimpleName())) {
				logger.log(Logger.Level.TRACE, "Received expected subquery root");
			} else {
				logger.log(Logger.Level.ERROR, "Expected subquery root:" + Product.class.getSimpleName() + ", actual:"
						+ hardProd.getModel().getName());
			}

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "19"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cquery.where(cbuilder.exists(sq));

			cquery.select(product);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("exists test failed");
		}
	}

	/*
	 * @testName: subqueryFromEntityTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1191; PERSISTENCE:SPEC:1765;
	 *
	 * @test_Strategy:
	 *
	 * SELECT product FROM PRODUCT product WHERE EXISTS (Select hardProd From
	 * PRODUCT hardprod where hardprod.id = '19').
	 *
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void subqueryFromEntityTypeTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = "19";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Product> product = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = product.getModel();

			// create Subquery instance, with root Customer
			Subquery<Product> sq = cquery.subquery(Product.class);
			Root<Product> hardProd = sq.from(Product_);
			if (hardProd.getModel().getName().equals(Product_.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected subquery root");
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected subquery root:" + Product_.getName() + ", actual:" + hardProd.getModel().getName());
			}

			// the subquery references the root of the containing query
			sq.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "19"))
					.select(hardProd);

			// an exists condition is applied to the subquery result
			cquery.where(cbuilder.exists(sq));

			cquery.select(product);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryFromEntityTypeTest failed");
		}
	}

	/*
	 * @testName: all
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:735; PERSISTENCE:SPEC:1766;
	 * PERSISTENCE:SPEC:1766.1;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery
	 *
	 * Select hardProd.modelNumber From HardwareProduct hardProd where
	 * hardProd.modelNumber > ALL ( Select subHardProd.modelNumber From
	 * HardwareProduct subHardProd where subHardProd.modelNumber < 10050 )
	 *
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void all() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(10050);
		expected.add(2578);
		expected.add(3000);
		expected.add(10000);
		expected.add(2368);
		Collections.sort(expected);

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<HardwareProduct> hardProd = cquery.from(HardwareProduct.class);

			// Get Metamodel from Root
			EntityType<HardwareProduct> HardwareProduct_ = hardProd.getModel();

			// create Subquery instance
			Subquery<Integer> sq = cquery.subquery(Integer.class);
			Root<HardwareProduct> subHardProd = sq.from(HardwareProduct.class);

			sq.select(subHardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)));
			sq.where(cbuilder.lt(subHardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)),
					1050));

			cquery.select(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)));
			cquery.where(cbuilder.gt(hardProd.get(HardwareProduct_.getSingularAttribute("modelNumber", Integer.class)),
					cbuilder.all(sq)));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);
			List<Integer> actual = tq.getResultList();
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + i);
				}
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + i);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("all test failed");
		}
	}

	/*
	 * @testName: some
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:847; PERSISTENCE:JAVADOC:1136;
	 * PERSISTENCE:SPEC:1766; PERSISTENCE:SPEC:1766.3;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery SELECT DISTINCT
	 * c FROM Customer c, IN(c.orders) co WHERE co.totalPrice <= SOME(Select
	 * o.totalPrice FROM Order o, IN(o.lineItems) l WHERE l.quantity = 3 )
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void some() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			// Get Root Customer
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodels
			EntityType<Order> Order_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Order.class);

			EntityType<LineItem> LineItem_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.LineItem.class);

			EntityType<Customer> Customer_ = customer.getModel();

			// Join Customer-Order
			Join<Customer, Order> orders = customer.join(Customer_.getCollection("orders", Order.class));

			// create Subquery instance
			Subquery<Double> sq = cquery.subquery(Double.class);

			// Create Roots
			Root<Order> order = sq.from(Order.class);

			// Join Order-LineItem
			Join<Order, LineItem> lineItems = order.join(Order_.getCollection("lineItemsCollection", LineItem.class));

			// Create SubQuery
			sq.select(order.get(Order_.getSingularAttribute("totalPrice", Double.class)))
					.where(cbuilder.equal(lineItems.get(LineItem_.getSingularAttribute("quantity", Integer.class)), 3));

			cquery.select(customer);

			// Create Main Query with SubQuery
			cquery.where(cbuilder.le(orders.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					cbuilder.some(sq)));
			cquery.distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("some test failed");
		}
	}

	/*
	 * @testName: any
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:738; PERSISTENCE:SPEC:1766;
	 * PERSISTENCE:SPEC:1766.2;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery SELECT DISTINCT
	 * object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice < ANY (Select
	 * o.totalPrice FROM Order o, IN(o.lineItems) l WHERE l.quantity = 3 )
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void any() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 18; i++) {
			if (i != 9) {
				expected[j++] = customerRef[i].getId();
			}
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			// Get Root Customer
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodels
			EntityType<Order> Order_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Order.class);

			EntityType<LineItem> LineItem_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.LineItem.class);

			EntityType<Customer> Customer_ = customer.getModel();

			// Join Customer-Order
			Join<Customer, Order> orders = customer.join(Customer_.getCollection("orders", Order.class));

			// create Subquery instance
			Subquery<Double> sq = cquery.subquery(Double.class);

			// Create Roots
			Root<Order> order = sq.from(Order.class);

			// Join Order-LineItem
			Join<Order, LineItem> lineItems = order.join(Order_.getCollection("lineItemsCollection", LineItem.class));

			// Create SubQuery
			sq.select(order.get(Order_.getSingularAttribute("totalPrice", Double.class)))
					.where(cbuilder.equal(lineItems.get(LineItem_.getSingularAttribute("quantity")), 3));

			cquery.select(customer);

			// Create Main Query with SubQuery
			cquery.where(
					cbuilder.lt(orders.get(Order_.getSingularAttribute("totalPrice", Double.class)), cbuilder.any(sq)));
			cquery.distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("any test failed");
		}
	}

	/*
	 * @testName: andPredicates
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:737; PERSISTENCE:SPEC:1729;
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.street = '125 Moxy
	 * Lane' AND c.home.city = 'Swansea' AND c.home.state = 'MA and c.home.zip =
	 * '11345'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void andPredicates() throws Exception {
		boolean pass = false;

		Customer expectedCustomer = customerRef[2];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.and(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)), "125 Moxy Lane"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), "Swansea"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), "MA"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)), "11345")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			Customer result = tq.getSingleResult();

			if (result.equals(expectedCustomer)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expectedCustomer + ", actual:" + result);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("andPredicates test failed");

		}
	}

	/*
	 * @testName: orPredicates
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:834; PERSISTENCE:SPEC:1729;
	 *
	 * @test_Strategy: SELECT Distinct c from Customer c WHERE c.home.street = '47
	 * Skyline Drive' OR c.home.city = 'Chelmsford' OR c.home.state = 'VT' OR
	 * c.home.zip = '02155'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void orPredicates() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[9].getId();
		expected[2] = customerRef[10].getId();
		expected[3] = customerRef[12].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)), "47 Skyline Drive"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), "Chelmsford"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), "VT"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)), "02155")));
			cquery.distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("orPredicates test failed");

		}
	}

	/*
	 * @testName: notPredicate
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:821; PERSISTENCE:SPEC:1729;
	 *
	 * @test_Strategy: Select Distinct o FROM Order o WHERE NOT o.totalPrice < 4500
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void notPredicate() throws Exception {
		boolean pass = false;

		final Double expectedTotalPrice = 4500.0D;

		String[] expected = new String[3];
		expected[0] = orderRef[4].getId();
		expected[1] = orderRef[10].getId();
		expected[2] = orderRef[15].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.lt(order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					expectedTotalPrice)));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notPredicate test failed");

		}
	}

	/*
	 * @testName: conjunction
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:751
	 *
	 * @test_Strategy: Use Conjunction Select Distinct o FROM Order o where
	 * o.customer.name = 'Robert E. Bissett'
	 *
	 * Note: cbuilder.conjunction() always returns true
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void conjunction() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isTrue(cbuilder.conjunction())));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("conjunction test failed");

		}
	}

	/*
	 * @testName: disjunction
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:766
	 *
	 * @test_Strategy: Use Disjunction Select Distinct o FROM Order o where
	 * o.customer.name = 'Robert E. Bissett'
	 *
	 * Note: cbuilder.disjunction() always returns false
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void disjunction() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isFalse(cbuilder.disjunction())));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("disjunction test failed");

		}
	}

	/*
	 * @testName: isTrue
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:790
	 *
	 * @test_Strategy: Use Conjunction Select Distinct o FROM Order o where
	 * o.customer.name = 'Robert E. Bissett'
	 *
	 * Note: cbuilder.conjunction() always returns true
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void isTrue() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isTrue(cbuilder.conjunction())));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isTrue test failed");

		}
	}

	/*
	 * @testName: isFalse
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:782
	 *
	 * @test_Strategy: Use Disjunction Select o FROM Order o where o.customer.name =
	 * 'Robert E. Bissett'
	 *
	 * Note: cbuilder.disjunction() always returns false
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void isFalse() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isFalse(cbuilder.disjunction())));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isFalse test failed");

		}
	}

	/*
	 * @testName: isNull
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:789; PERSISTENCE:JAVADOC:957;
	 * PERSISTENCE:SPEC:1683; PERSISTENCE:SPEC:1728.2;
	 * 
	 * @test_Strategy: Select c FROM Customer c where c.name is null
	 *
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void isNull() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[11].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(customer);

			cquery.where(cbuilder.isNull(customer.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isNull test failed");

		}
	}

	/*
	 * @testName: isNotNull
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:788; PERSISTENCE:JAVADOC:956;
	 * PERSISTENCE:SPEC:1728.3;
	 *
	 * @test_Strategy: Select c FROM Customer c where c.work.zip IS NOT NULL
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void isNotNull() throws Exception {
		boolean pass = false;

		String[] expected = new String[17];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if (i != 12) {

				expected[j++] = customerRef[i].getId();
			}
		}

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cb.createQuery(Customer.class);

		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = mm.entity(Customer.class);
			EntityType<Address> Address_ = mm.entity(Address.class);
			cquery.where(cb.isNotNull(customer.get(Customer_.getSingularAttribute("work", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class))));

			cquery.select(customer);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isNotNull test failed");

		}

	}

	/*
	 * @testName: equalExpObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:768
	 *
	 * @test_Strategy: Use equal and not Select o FROM Order o where o.customer.name
	 * <> 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void equalExpObjectTest() throws Exception {
		boolean pass = false;
		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett")));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("equalExpObjectTest failed");

		}
	}

	/*
	 * @testName: equalExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:767; PERSISTENCE:SPEC:1748;
	 *
	 * @test_Strategy: Use equal and not Select o FROM Order o where o.customer.name
	 * <> 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void equalExpExpTest() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.equal(
					order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)),
					cbuilder.literal("Robert E. Bissett"))));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("equalExpExpTest failed");

		}
	}

	/*
	 * @testName: notEqualExpObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:823
	 *
	 * @test_Strategy: Use notEqual Select o FROM Order o where o.customer.name <>
	 * 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void notEqualExpObjectTest() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.notEqual(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notEqualExpObjectTest test failed");

		}
	}

	/*
	 * @testName: notEqualExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:822
	 *
	 * @test_Strategy: Use notEqual Select o FROM Order o where o.customer.name <>
	 * 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void notEqualExpExpTest() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.notEqual(
					order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)),
					cbuilder.literal("Robert E. Bissett")));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notEqualExpExpTest test failed");

		}
	}

	/*
	 * @testName: sumAsDoubleTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:857
	 *
	 * @test_Strategy:
	 * 
	 * Select sumAsDouble(p.quantity*0.5) From Product p WHERE p.quantity > 50 and
	 * p.quantity < 100
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumAsDoubleTest() throws Exception {
		boolean pass = false;
		double expected = 217.5d;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Double> cquery = cb.createQuery(Double.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.gt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50),
					cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 100));

			cquery.select(cb.sumAsDouble(
					cb.toFloat(cb.prod(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 0.5f))));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double actual = tq.getSingleResult();

			logger.log(Logger.Level.TRACE, "actual" + actual);

			if (expected == actual) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected + ", actual: " + actual);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumAsDoubleTest failed");

		}
	}

	/*
	 * @testName: sumAsLongTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:858
	 *
	 * @test_Strategy:
	 * 
	 * Select sumAsLong(p.quantity) From Product p WHERE p.quantity > 50 and
	 * p.quantity < 100
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sumAsLongTest() throws Exception {
		boolean pass = false;
		long expected = 435;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Long> cquery = cb.createQuery(Long.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.gt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50),
					cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 100));
			cquery.select(cb.sumAsLong(prod.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);
			Long actual = tq.getSingleResult();

			logger.log(Logger.Level.TRACE, "actual" + actual);

			if (expected == actual) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected + ", actual: " + actual);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sumAsLongTest failed");

		}
	}

	/*
	 * @testName: greaterThanExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:774
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder
					.greaterThan(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("greaterThanExpNumTest failed");

		}
	}

	/*
	 * @testName: greaterThanExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:773
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		;
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(
					cbuilder.greaterThan(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
							cbuilder.literal(4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("greaterThanExpExpTest failed");

		}
	}

	/*
	 * @testName: greaterThanOrEqualToExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:795; PERSISTENCE:JAVADOC:776
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanOrEqualToExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];
		;

		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.greaterThanOrEqualTo(
					cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("greaterThanOrEqualToExpNumTest failed");

		}
	}

	/*
	 * @testName: greaterThanOrEqualToExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:775
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanOrEqualToExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];

		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.greaterThanOrEqualTo(
					cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
					cbuilder.literal(4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("greaterThanOrEqualToExpExpTest failed");

		}
	}

	/*
	 * @testName: lessThanExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:797
	 *
	 * @test_Strategy:
	 * 
	 * Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		;
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lessThan(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanExpNumTest failed");

		}
	}

	/*
	 * @testName: lessThanExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:796
	 *
	 * @test_Strategy:
	 * 
	 * Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		;
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(
					cb.lessThan(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), cb.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanExpExpTest failed");

		}
	}

	/*
	 * @testName: lessThanOrEqualToExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:799
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanOrEqualToExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		;
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);

		if (cquery != null) {
			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lessThanOrEqualTo(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanOrEqualToExpNumTest failed");

		}
	}

	/*
	 * @testName: lessThanOrEqualToExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:798
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void lessThanOrEqualToExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		;
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);

		if (cquery != null) {
			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lessThanOrEqualTo(prod.get(Product_.getSingularAttribute("quantity", Integer.class)),
					cb.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lessThanOrEqualToExpExpTest failed");

		}
	}

	/*
	 * @testName: between
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:743; PERSISTENCE:JAVADOC:835
	 *
	 * @test_Strategy: SELECT p From Product p where p.shelfLife.soldDate BETWEEN
	 * :date1 AND :date6
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void between() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = "31";
		expected[1] = "32";
		expected[2] = "33";
		expected[3] = "37";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		Date date1 = getSQLDate("2000-02-14");
		Date date6 = getSQLDate("2005-02-18");

		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			EmbeddableType<ShelfLife> ShelfLife_ = mm.embeddable(ShelfLife.class);

			cquery.select(product);

			cquery.where(cbuilder.between(
					product.get(Product_.getSingularAttribute("shelfLife", ShelfLife.class))
							.get(ShelfLife_.getSingularAttribute("soldDate", Date.class)),
					cbuilder.parameter(Date.class, "date1"), cbuilder.parameter(Date.class, "date6")));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("date1", date1);
			tq.setParameter("date6", date6);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("between test failed");
		}
	}

	/*
	 * @testName: gtExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:779
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void gtExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(
					cbuilder.gt(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("gtExpNumTest failed");

		}
	}

	/*
	 * @testName: gtExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:778
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void gtExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		;
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.gt(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
					cbuilder.literal(4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("gtExpExpTest failed");

		}
	}

	/*
	 * @testName: geExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:772
	 *
	 * @test_Strategy: Select Distinct a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void geExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];
		;
		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(
					cbuilder.ge(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));
			cquery.distinct(true);

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("geExpNumTest failed");

		}
	}

	/*
	 * @testName: geExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:771
	 *
	 * @test_Strategy: Select Distinct a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void geExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];
		;
		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.ge(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
					cbuilder.literal(4)));
			cquery.distinct(true);

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("geExpExpTest failed");

		}
	}

	/*
	 * @testName: ltExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:814
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void ltExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("ltExpNumTest failed");

		}
	}

	/*
	 * @testName: ltExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:813
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity < 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void ltExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[20];
		expected[0] = "4";
		expected[1] = "6";
		expected[2] = "7";
		expected[3] = "8";
		expected[4] = "9";
		expected[5] = "12";
		expected[6] = "15";
		expected[7] = "16";
		expected[8] = "17";
		expected[9] = "19";
		expected[10] = "21";
		expected[11] = "22";
		expected[12] = "24";
		expected[13] = "27";
		expected[14] = "28";
		expected[15] = "30";
		expected[16] = "31";
		expected[17] = "35";
		expected[18] = "36";
		expected[19] = "37";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cb.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cb.lt(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), cb.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("ltExpExpTest failed");

		}
	}

	/*
	 * @testName: leExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:793
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void leExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cbuilder.le(prod.get(Product_.getSingularAttribute("quantity", Integer.class)), 50));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("leExpNumTest failed");

		}
	}

	/*
	 * @testName: leExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:792
	 *
	 * @test_Strategy: Select p From Product p WHERE p.quantity <= 50
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void leExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[22];
		expected[0] = "4";
		expected[1] = "5";
		expected[2] = "6";
		expected[3] = "7";
		expected[4] = "8";
		expected[5] = "9";
		expected[6] = "12";
		expected[7] = "15";
		expected[8] = "16";
		expected[9] = "17";
		expected[10] = "19";
		expected[11] = "20";
		expected[12] = "21";
		expected[13] = "22";
		expected[14] = "24";
		expected[15] = "27";
		expected[16] = "28";
		expected[17] = "30";
		expected[18] = "31";
		expected[19] = "35";
		expected[20] = "36";
		expected[21] = "37";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> prod = cquery.from(Product.class);

			// Get Metamodel from Root
			EntityType<Product> Product_ = prod.getModel();
			cquery.where(cbuilder.le(prod.get(Product_.getSingularAttribute("quantity", Integer.class)),
					cbuilder.literal(50)));
			cquery.select(prod);

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);
			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("leExpExpTest failed");

		}
	}

	/*
	 * @testName: neg
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:820
	 *
	 * @test_Strategy: SELECT NEG(p.quantity) From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void neg() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.neg(product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = Integer.valueOf(-5);

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("neg test failed");
		}

	}

	/*
	 * @testName: abs
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:734
	 *
	 * @test_Strategy: Select o From Order o WHERE :dbl < ABS(- o.totalPrice)
	 *
	 * Note :dbl = 1180D
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void abs() throws Exception {
		boolean pass = false;

		String[] expected = new String[9];
		expected[0] = orderRef[0].getId();
		expected[1] = orderRef[1].getId();
		expected[2] = orderRef[3].getId();
		expected[3] = orderRef[4].getId();
		expected[4] = orderRef[5].getId();
		expected[5] = orderRef[10].getId();
		expected[6] = orderRef[15].getId();
		expected[7] = orderRef[16].getId();
		expected[8] = orderRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			cquery.select(order);
			cquery.where(cbuilder.lt(cbuilder.parameter(Double.class, "dbl"),
					cbuilder.abs(cbuilder.neg(order.get(Order_.getSingularAttribute("totalPrice", Double.class))))));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("dbl", 1180D);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("abs test failed");

		}
	}

	/*
	 * @testName: prodExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:838; PERSISTENCE:SPEC:1746;
	 * PERSISTENCE:SPEC:1746.2;
	 *
	 * @test_Strategy: SELECT p.quantity *10F From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void prodExpNumTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cbuilder.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 10F));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);

			Number result = tq.getSingleResult();
			Float expectedResult = 5F * 10F;

			if (result instanceof Float) {
				logger.log(Logger.Level.TRACE, "Received expected type of Float");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type Double, actual:" + result);
			}
			Float f = result.floatValue();
			if (f == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + f);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("prodExpNumTest failed");
		}
	}

	/*
	 * @testName: prodNumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:839
	 *
	 * @test_Strategy: SELECT 10 * p.quantity From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void prodNumExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.prod(10, product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = 10 * 5;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + result);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("prodNumExpTest failed");
		}
	}

	/*
	 * @testName: prodExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:837
	 *
	 * @test_Strategy: SELECT p.quantity * p.quantity From Product p where
	 * p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void prodExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = 5 * 5;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + result);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"expected: " + expectedResult.intValue() + ", actual result:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("prodExpExpTest failed");
		}
	}

	/*
	 * @testName: diffExpNumberTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:764
	 *
	 * @test_Strategy: SELECT DIFF(p.quantity, 2) From Product p where p.quantity=5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void diffExpNumberTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.diff(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 2));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expected = Integer.valueOf(3);

			if (result.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("diffExpNumberTest failed");
		}
	}

	/*
	 * @testName: diffNumberExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:765
	 *
	 * @test_Strategy: SELECT DIFF(8, p.quantity) From Product p where p.quantity=5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void diffNumberExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.diff(8, product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expected = Integer.valueOf(3);

			if (result.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("diffNumberExpTest failed");
		}
	}

	/*
	 * @testName: diffExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:763
	 *
	 * @test_Strategy: SELECT DIFF(p.quantity, p.quantity) From Product p where
	 * p.quantity=5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void diffExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.diff(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expected = Integer.valueOf(0);

			if (result.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: " + expected.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("diffExpExpTest failed");
		}
	}

	/*
	 * @testName: quotExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:841
	 *
	 * @test_Strategy: SELECT QUOT(p.quantity, 2) From Product p where p.quantity =
	 * 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void quotExpNumTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cb.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(Product.class);

			cquery.select(cb.quot(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 2));
			cquery.where(cb.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			Number actual = tq.getSingleResult();

			Integer expected = Integer.valueOf(2);

			if (actual.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected.intValue() + ", actual:" + actual.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("quotExpNumTest failed");
		}
	}

	/*
	 * @testName: quotNumExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:842
	 *
	 * @test_Strategy: SELECT QUOT(10, p.quantity) From Product p where p.quantity =
	 * 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void quotNumExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cb.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(Product.class);

			cquery.select(cb.quot(10, product.get(Product_.getSingularAttribute("quantity", Integer.class))));
			cquery.where(cb.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			Number actual = tq.getSingleResult();

			Integer expected = Integer.valueOf(2);

			if (actual.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected.intValue() + ", actual:" + actual.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("quotNumExpTest failed");
		}
	}

	/*
	 * @testName: quotExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:840
	 *
	 * @test_Strategy: SELECT QUOT(2, p.quantity) From Product p where p.quantity =
	 * 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void quotExpExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Number> cquery = cb.createQuery(Number.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(Product.class);

			cquery.select(cb.quot(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));
			cquery.where(cb.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Number> tq = getEntityManager().createQuery(cquery);
			Number actual = tq.getSingleResult();

			Integer expected = Integer.valueOf(1);

			if (actual.intValue() == expected.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "expected: " + expected.intValue() + ", actual:" + actual.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("quotExpExpTest failed");
		}
	}

	/*
	 * @testName: modExpIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:818
	 *
	 * @test_Strategy: Select Object(p) From Product p where MOD(550, 100) =
	 * p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void modExpIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = "5";
		expected[1] = "20";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), 100),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("modExpIntTest failed");
		}
	}

	/*
	 * @testName: modExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:817
	 *
	 * @test_Strategy: Select Object(p) From Product p where MOD(550, 100) =
	 * p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void modExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = "5";
		expected[1] = "20";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), cbuilder.literal(100)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("modExpExpTest failed");
		}
	}

	/*
	 * @testName: modIntExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:819
	 *
	 * @test_Strategy: Select Object(p) From Product p where MOD(550, 100) =
	 * p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void modIntExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = "5";
		expected[1] = "20";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.where(cbuilder.equal(cbuilder.mod(550, cbuilder.literal(100)),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Product> tq = getEntityManager().createQuery(cquery);

			List<Product> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("modIntExpTest failed");
		}
	}

	/*
	 * @testName: sqrt
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:848
	 *
	 * @test_Strategy: SELECT SQRT(p.quantity) From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void sqrt() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.sqrt(product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double result = tq.getSingleResult();
			Double expectedResult = 2.1D;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				// logger.log(Logger.Level.ERROR,"test returned:" + result.doubleValue() +
				// "expected:
				// " + expectedResult.doubleValue());
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sqrt test failed");
		}
	}

	/*
	 * @testName: toLong
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:864
	 *
	 * @test_Strategy: SELECT toLong(p.quantity * 5L) From Product p where
	 * p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toLong() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder
					.toLong(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5L)));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);

			Long result = tq.getSingleResult();
			Long expectedResult = Long.valueOf(25);

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toLong test failed");
		}
	}

	/*
	 * @testName: toInteger
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:863
	 *
	 * @test_Strategy: SELECT toInteger(p.quantity) From Product p where
	 * p.partNumber = 373767373
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toInteger() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.toInteger(product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			cquery.where(
					cbuilder.equal(product.get(Product_.getSingularAttribute("partNumber", Long.class)), 373767373));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = Integer.valueOf(5);

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toInteger test failed");
		}
	}

	/*
	 * @testName: toFloat
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:862
	 *
	 * @test_Strategy: SELECT p.quantity *1/2 From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toFloat() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Float> cquery = cbuilder.createQuery(Float.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.toFloat(
					cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 0.5f)));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Float> tq = getEntityManager().createQuery(cquery);

			Float result = tq.getSingleResult();
			Float expectedResult = 2.5F;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toFloat test failed");
		}

	}

	/*
	 * @testName: toDouble
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:861
	 *
	 * @test_Strategy: SELECT toDouble(SQRT(p.quantity)) From Product p where
	 * p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toDouble() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder
					.toDouble(cbuilder.sqrt(product.get(Product_.getSingularAttribute("quantity", Integer.class)))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double result = tq.getSingleResult();
			Double expectedResult = 2.1D;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toDouble test failed");
		}
	}

	/*
	 * @testName: toBigDecimal
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:859
	 *
	 * @test_Strategy: SELECT ToBigDecimal(p.quantity * BIGDECIMAL) From Product p
	 * where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toBigDecimal() throws Exception {
		final BigDecimal expectedResult = new BigDecimal("50.5");
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<BigDecimal> cquery = cbuilder.createQuery(BigDecimal.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.toBigDecimal(cbuilder.prod(
					product.get(Product_.getSingularAttribute("quantity", Integer.class)), new BigDecimal("10.1"))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<BigDecimal> tq = getEntityManager().createQuery(cquery);

			BigDecimal result = tq.getSingleResult();

			if (result.compareTo(expectedResult) == 0) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned:" + result + "expected: " + expectedResult);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toBigDecimal test failed");
		}
	}

	/*
	 * @testName: toBigInteger
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:860
	 *
	 * @test_Strategy: SELECT toBigInteger(p.quantity * BigInteger("10000000000"))
	 * From Product p where p.quantity = 5
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toBigInteger() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<BigInteger> cquery = cbuilder.createQuery(BigInteger.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder
					.toBigInteger(cbuilder.prod(product.get(Product_.getSingularAttribute("quantity", Integer.class)),
							new BigInteger("10000000000"))));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("quantity", Integer.class)), 5));

			TypedQuery<BigInteger> tq = getEntityManager().createQuery(cquery);

			BigInteger result = tq.getSingleResult();
			BigInteger expectedResult = new BigInteger("50000000000");

			if (result.compareTo(expectedResult) == 0) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned:" + result + "expected: " + expectedResult);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toBigInteger test failed");
		}
	}

	/*
	 * @testName: toStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:865
	 *
	 * @test_Strategy: SELECT ToString(p.id) From Product p where p.id ='1'
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void toStringTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(cbuilder.toString(cbuilder.literal('a')));

			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();
			String expectedResult = "a";

			if (result.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned:" + result + "expected: " + expectedResult);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("toStringTest test failed");
		}
	}

	/*
	 * @testName: literal
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:806
	 *
	 * @test_Strategy: SELECT p.quantity From Product p where 5 = p.quantity
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void literal() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(product.get(Product_.getSingularAttribute("quantity", Integer.class)));

			cquery.where(cbuilder.equal(cbuilder.literal(5),
					product.get(Product_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);

			Integer result = tq.getSingleResult();
			Integer expectedResult = 5;

			if (result.intValue() == expectedResult.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedResult.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("literal test failed");
		}
	}

	/*
	 * @testName: literalIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:807
	 *
	 * @test_Strategy:
	 */
	@Test
	public void literalIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			cbuilder.literal(null);
			logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("literalIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: parameter
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:836; PERSISTENCE:SPEC:1750;
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.street = :street OR
	 * c.home.city = :city OR c.home.state = :state OR c.home.zip = :zip
	 *
	 * where :street = '47 Skyline Drive' :city ='Chelmsford' :state ='VT' :zip =
	 * '02155'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameter() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[9].getId();
		expected[2] = customerRef[10].getId();
		expected[3] = customerRef[12].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			ParameterExpression<String> param1 = cbuilder.parameter(String.class, "streetParam");
			ParameterExpression<String> param2 = cbuilder.parameter(String.class, "cityParam");
			ParameterExpression<String> param3 = cbuilder.parameter(String.class, "stateParam");
			ParameterExpression<String> param4 = cbuilder.parameter(String.class, "zipParam");

			cquery.select(customer);
			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)), param1),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("city", String.class)), param2),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), param3),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)), param4)));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			tq.setParameter("streetParam", "47 Skyline Drive");
			tq.setParameter("cityParam", "Chelmsford");
			tq.setParameter("stateParam", "VT");
			tq.setParameter("zipParam", "02155");

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("parameter test failed");

		}
	}

	/*
	 * @testName: parameterCaseSensitiveTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1306; PERSISTENCE:SPEC:1307
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.state = :state OR
	 * c.home.state = :STATE
	 *
	 * where :state ='RI' :STATE ='VT'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameterCaseSensitiveTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = customerRef[9].getId();
		expected[1] = customerRef[13].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			ParameterExpression<String> param1 = cbuilder.parameter(String.class, "stateParam");
			ParameterExpression<String> param2 = cbuilder.parameter(String.class, "STATEPARAM");

			cquery.select(customer);
			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), param1),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("state", String.class)), param2)));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			tq.setParameter("stateParam", "RI");
			tq.setParameter("STATEPARAM", "VT");

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("parameterCaseSensitiveTest test failed");

		}
	}

	/*
	 * @testName: criteriaBuilderValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:898
	 *
	 * @test_Strategy: SELECT c.id from Customer c WHERE c.home.state IN (?1, ?2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderValuesTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[12];

		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[6].getId();
		expected[5] = customerRef[7].getId();
		expected[6] = customerRef[8].getId();
		expected[7] = customerRef[9].getId();
		expected[8] = customerRef[10].getId();
		expected[9] = customerRef[12].getId();
		expected[10] = customerRef[14].getId();
		expected[11] = customerRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			CriteriaBuilder.In inExp = cbuilder.in(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("state", String.class)));
			inExp.value(cbuilder.parameter(String.class, "state1"));
			inExp.value(cbuilder.parameter(String.class, "state2"));
			cquery.where(inExp);

			List<Customer> result = getEntityManager().createQuery(cquery).setParameter("state1", "MA")
					.setParameter("state2", "VT").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + c.getId() + ", state:" + c.getHome().getState());
				actual.add(Integer.parseInt(c.getId()));
			}

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("criteriaBuilderValuesTest failed");

		}
	}

	/*
	 * @testName: criteriaBuilderIn1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:780; PERSISTENCE:JAVADOC:898;
	 * PERSISTENCE:JAVADOC:899; PERSISTENCE:SPEC:1728.4;
	 *
	 * @test_Strategy: SELECT c.id from Customer c WHERE c.home.state IN (?1, ?2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderIn1Test() throws Exception {
		boolean pass = false;

		String[] expected = new String[12];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[6].getId();
		expected[5] = customerRef[7].getId();
		expected[6] = customerRef[8].getId();
		expected[7] = customerRef[9].getId();
		expected[8] = customerRef[10].getId();
		expected[9] = customerRef[12].getId();
		expected[10] = customerRef[14].getId();
		expected[11] = customerRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			CriteriaBuilder.In inExp = cbuilder.in(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("state", String.class)));
			inExp.value(cbuilder.parameter(String.class, "state1"));
			inExp.value(cbuilder.parameter(String.class, "state2"));

			cquery.where(inExp);

			List<Customer> result = getEntityManager().createQuery(cquery).setParameter("state1", "MA")
					.setParameter("state2", "VT").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + c.getId() + ", state:" + c.getHome().getState());
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("criteriaBuilderIn1Test failed");

		}
	}

	/*
	 * @testName: criteriaBuilderIn2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:900; PERSISTENCE:JAVADOC:901;
	 * PERSISTENCE:JAVADOC:902; PERSISTENCE:SPEC:1698; PERSISTENCE:SPEC:1786;
	 * PERSISTENCE:SPEC:1786.3;
	 * 
	 * @test_Strategy: Testing not, getOperator and isNegated SELECT s.id from
	 * Spouse s WHERE s.id NOT IN (2,3)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderIn2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		String[] expected = new String[4];
		expected[0] = spouse[0].getId();
		expected[1] = spouse[3].getId();
		expected[2] = spouse[4].getId();
		expected[3] = spouse[5].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			CriteriaBuilder.In in = cbuilder.in(spouse.get("id"));
			for (String id : new String[] { "2", "3" }) {
				in.value(id);
			}
			Predicate pred = in.not();
			cquery.where(pred);

			if (!pred.getOperator().equals(Predicate.BooleanOperator.AND)) {
				logger.log(Logger.Level.ERROR,
						"Expected: " + Predicate.BooleanOperator.AND + ", actual:" + pred.getOperator().name());
			} else {
				pass1 = true;
			}
			if (in.not().isNegated() != true) {
				logger.log(Logger.Level.ERROR,
						"Expected in.not().isNegated() to return: true, actual:" + in.isNegated());
			} else {
				pass2 = true;
			}

			List<Spouse> result = getEntityManager().createQuery(cquery).getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("criteriaBuilderIn2Test failed");

		}
	}

	/*
	 * @testName: criteriaBuilderInValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:897
	 *
	 * @test_Strategy: SELECT c.id from Customer c WHERE c.home.state IN (MA, VT)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void criteriaBuilderInValueTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[12];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[6].getId();
		expected[5] = customerRef[7].getId();
		expected[6] = customerRef[8].getId();
		expected[7] = customerRef[9].getId();
		expected[8] = customerRef[10].getId();
		expected[9] = customerRef[12].getId();
		expected[10] = customerRef[14].getId();
		expected[11] = customerRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			CriteriaBuilder.In inExp = cbuilder.in(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("state", String.class)));
			inExp.value("MA");
			inExp.value("VT");
			cquery.where(inExp);

			List<Customer> result = getEntityManager().createQuery(cquery).getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + c.getId() + ", state:" + c.getHome().getState());
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("criteriaBuilderInValueTest failed");

		}
	}

	/*
	 * @testName: expressionInObjectArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:952
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2,3)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInObjectArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = spouse[1].getId();
		expected[1] = spouse[2].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			Expression exp = spouse.get("id");

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(exp.in(new Object[] { "2", "3" }));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("expressionInObjectArrayTest failed");

		}
	}

	/*
	 * @testName: expressionInExpressionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:953
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2,3)
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInExpressionArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = spouse[1].getId();
		expected[1] = spouse[2].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			Expression exp = spouse.get("id");

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(exp.in(new Expression[] { cbuilder.literal("2"), cbuilder.literal("3") }));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("expressionInExpressionArrayTest failed");

		}
	}

	/*
	 * @testName: expressionInExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:955
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInExpressionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = spouse[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);
			Expression exp = spouse.get("id");

			ParameterExpression<String> param = cbuilder.parameter(String.class);
			Expression e = cbuilder.literal("2");
			cquery.where(exp.in(e));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("expressionInExpressionTest failed");

		}
	}

	/*
	 * @testName: expressionInCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:954
	 *
	 * @test_Strategy: SELECT s.id from Spouse s WHERE s.id IN (2,3)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void expressionInCollectionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = spouse[1].getId();
		expected[1] = spouse[2].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			Root<Spouse> spouse = cquery.from(Spouse.class);

			Expression exp = spouse.get("id");
			ParameterExpression<String> param = cbuilder.parameter(String.class);
			Collection<String> col = new ArrayList<String>();
			col.add("2");
			col.add("3");

			cquery.where(exp.in(col));
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("expressionInCollectionTest failed");

		}
	}

	/*
	 * @testName: parameterExpressionIsNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1091
	 *
	 * @test_Strategy: SELECT s.id from Spouse s where (2 IS NULL)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameterExpressionIsNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[0];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			cquery.from(Spouse.class);

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(param.isNull());
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.setParameter(param, "2").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("parameterExpressionIsNullTest failed");

		}
	}

	/*
	 * @testName: parameterExpressionIsNotNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1090
	 *
	 * @test_Strategy: SELECT s.id from Spouse s where (2 IS NOT NULL)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void parameterExpressionIsNotNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[6];
		expected[0] = spouse[0].getId();
		expected[1] = spouse[1].getId();
		expected[2] = spouse[2].getId();
		expected[3] = spouse[3].getId();
		expected[4] = spouse[4].getId();
		expected[5] = spouse[5].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Spouse> cquery = cbuilder.createQuery(Spouse.class);
		if (cquery != null) {
			cquery.from(Spouse.class);

			ParameterExpression<String> param = cbuilder.parameter(String.class);

			cquery.where(param.isNotNull());
			TypedQuery<Spouse> query = getEntityManager().createQuery(cquery);

			List<Spouse> result = query.setParameter(param, "2").getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Spouse s : result) {
				logger.log(Logger.Level.TRACE, "Customer id:" + s.getId() + ", state:" + s.getSocialSecurityNumber());
				actual.add(Integer.parseInt(s.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("parameterExpressionIsNotNullTest failed");

		}
	}

	/*
	 * @testName: isEmpty
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:781
	 *
	 * @test_Strategy: Select c fRoM Customer c where c.aliases IS EMPTY
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void isEmpty() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		expected[0] = customerRef[5].getId();
		expected[1] = customerRef[14].getId();
		expected[2] = customerRef[15].getId();
		expected[3] = customerRef[16].getId();
		expected[4] = customerRef[17].getId();
		expected[5] = customerRef[18].getId();
		expected[6] = customerRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {

			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isEmpty(customer.<Set<String>>get("aliases")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isEmpty test failed");
		}
	}

	/*
	 * @testName: isNotEmpty
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:785
	 *
	 * @test_Strategy: Select Distinct c fRoM Customer c where c.aliases IS NOT
	 * EMPTY
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void isNotEmpty() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[13];
		for (int i = 0; i < 14; i++) {
			if (i != 5) {
				expected[j++] = customerRef[i].getId();
			}
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {

			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotEmpty(customer.<Set<String>>get("aliases")));
			cquery.select(customer).distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isNotEmpty test failed");
		}
	}

	/*
	 * @testName: sizeCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:846; PERSISTENCE:SPEC:1742;
	 *
	 * @test_Strategy: Select size(c.aliases) from Customer c where c.id ="3"
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void sizeCollectionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			cquery.select(cbuilder.size(customer.<Collection<Alias>>get("aliases")));
			cquery.where(cbuilder.equal(customer.get("id"), "3"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);
			Integer result = tq.getSingleResult();
			Integer expectedSize = 2;

			if (result.intValue() == expectedSize.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"test returned:" + result.intValue() + "expected: " + expectedSize.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sizeCollectionTest failed");
		}
	}

	/*
	 * @testName: sizeExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:845
	 *
	 * @test_Strategy: Select size(c.aliases) fRoM Customer c where c.id ="3"
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void sizeExpTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
		if (cquery != null) {

			Root<Customer> customer = cquery.from(Customer.class);

			Expression<Collection<Alias>> aliases = customer.<Collection<Alias>>get("aliases");

			// Get Metamodel from Root
			cquery.select(cbuilder.size(aliases));
			cquery.where(cbuilder.equal(customer.get("id"), "3"));

			TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);
			Integer result = tq.getSingleResult();
			Integer expectedSize = 2;

			if (result.intValue() == expectedSize.intValue()) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results:" + result.intValue());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected: " + expectedSize.intValue() + ", actual:" + result.intValue());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("sizeExpTest failed");
		}
	}

	/*
	 * @testName: isMember
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:784
	 *
	 * @test_Strategy: Select c FROM Customer c WHERE "aef" MEMBER OF c.aliases
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void isMember() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Alias> cqa = cbuilder.createQuery(Alias.class);
		Root<Alias> aliasRoot = cqa.from(Alias.class);
		cqa = cqa.where(cbuilder.equal(aliasRoot.get("alias"), "aef"));
		Alias alias = getEntityManager().createQuery(cqa).getSingleResult();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isMember(alias, customer.<Collection<Alias>>get("aliases")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();
			if (result.size() == 1) {
				if (result.get(0).equals(customerRef[0])) {
					logger.log(Logger.Level.TRACE, "Successfully returned expected results" + result.toString());
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "expected customer:" + customerRef[0].toString());
					logger.log(Logger.Level.ERROR, "actual customer:" + result.toString());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected number customers: 1, actual:" + result.size());
				for (Customer c : result) {
					logger.log(Logger.Level.ERROR, "Received customer:" + c.toString());
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isMember test failed");

		}
	}

	/*
	 * @testName: isNotMember
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:786
	 *
	 * @test_Strategy: Select Distinct a FROM Alias a WHERE a.customerNoop NOT
	 * MEMBER OF a.customersNoop
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void isNotMember() throws Exception {
		boolean pass = false;

		String[] expected = new String[30];
		for (int i = 0; i < 30; i++) {
			expected[i] = aliasRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			cquery.where(cbuilder.isNotMember(alias.<Customer>get("customerNoop"),
					alias.<Collection<Customer>>get("customersNoop")));

			cquery.distinct(true);

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isNotMember test failed");

		}

	}

	/*
	 * @testName: likeExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:801
	 *
	 * @test_Strategy: SELECT Distinct c from Customer c WHERE c.home.zip LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void likeExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.like(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%77"));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("likeExpStringTest failed");

		}
	}

	/*
	 * @testName: likeExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:800
	 *
	 * @test_Strategy: SELECT Distinct c from Customer c WHERE c.home.zip LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void likeExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.like(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), cbuilder.literal("%77")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("likeExpExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:825
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[17];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if (i != 1) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%77"));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notLikeExpStringTest failed");

		}
	}

	/*
	 * @testName: notLikeExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:824
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%77"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[17];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if (i != 1) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), cbuilder.literal("%77")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notLikeExpExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:826
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(
					customer.get(Customer_.getSingularAttribute("home", Address.class))
							.get(Address_.getSingularAttribute("zip", String.class)),
					cbuilder.literal("%_7"), cbuilder.literal('\\')));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notLikeExpExpExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpExpCharTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:827
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpExpCharTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), cbuilder.literal("%_7"), '\\'));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notLikeExpExpCharTest failed");

		}
	}

	/*
	 * @testName: notLikeExpStringExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:828
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpStringExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%_7", cbuilder.literal('\\')));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notLikeExpStringExpTest failed");

		}
	}

	/*
	 * @testName: notLikeExpStringCharTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:829
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.home.zip NOT LIKE "%_7"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void notLikeExpStringCharTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[16];
		int j = 0;
		for (int i = 0; i < 18; i++) {
			if ((i != 1) && (i != 5)) {
				expected[j++] = customerRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);

			cquery.select(customer);

			cquery.where(cbuilder.notLike(customer.get(Customer_.getSingularAttribute("home", Address.class))
					.get(Address_.getSingularAttribute("zip", String.class)), "%_7", '\\'));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notLikeExpStringCharTest failed");

		}
	}

	/*
	 * @testName: concatExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:749; PERSISTENCE:JAVADOC:806
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.work.street="1 Network"
	 * CONCAT " Drive"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void concatExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);
			cquery.select(customer);

			cquery.where(cbuilder.equal(
					customer.get(Customer_.getSingularAttribute("work", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)),
					cbuilder.concat(cbuilder.literal("1 Network"), " Drive")));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("concatExpStringTest failed");

		}
	}

	/*
	 * @testName: concatStringExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:750
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.work.street="1 Network"
	 * CONCAT " Drive"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void concatStringExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);
			cquery.select(customer);

			cquery.where(cbuilder.equal(
					customer.get(Customer_.getSingularAttribute("work", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)),
					cbuilder.concat("1 Network", cbuilder.literal(" Drive"))));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("concatStringExpTest failed");

		}
	}

	/*
	 * @testName: concatExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:748
	 *
	 * @test_Strategy: SELECT c from Customer c WHERE c.work.street="1 Network"
	 * CONCAT " Drive"
	 *
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void concatExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			EntityType<Address> Address_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Address.class);
			cquery.select(customer);

			cquery.where(cbuilder.equal(
					customer.get(Customer_.getSingularAttribute("work", Address.class))
							.get(Address_.getSingularAttribute("street", String.class)),
					cbuilder.concat(cbuilder.literal("1 Network"), cbuilder.literal(" Drive"))));

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("concatExpExpTest failed");

		}
	}

	/*
	 * @testName: substringExpIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:850
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"), 1)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("substringExpIntTest failed");

		}
	}

	/*
	 * @testName: substringExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:849
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"), cbuilder.literal(1))));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("substringExpExpTest failed");

		}
	}

	/*
	 * @testName: substringExpIntIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:852
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1, 4)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpIntIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"), 1, 4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("substringExpIntIntTest failed");

		}
	}

	/*
	 * @testName: substringExpExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:851
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1, 4)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)), cbuilder
					.substring(cbuilder.parameter(String.class, "string1"), cbuilder.literal(1), cbuilder.literal(4))));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("substringExpExpExpTest failed");

		}
	}

	/*
	 * @testName: trimspecTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:915; PERSISTENCE:JAVADOC:916
	 *
	 * @test_Strategy:
	 *
	 *
	 */
	@Test
	public void trimspecTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		Collection<CriteriaBuilder.Trimspec> expected = new ArrayList();
		expected.add(CriteriaBuilder.Trimspec.BOTH);
		expected.add(CriteriaBuilder.Trimspec.LEADING);
		expected.add(CriteriaBuilder.Trimspec.TRAILING);

		try {
			CriteriaBuilder.Trimspec[] ts = CriteriaBuilder.Trimspec.values();
			if (ts.length == 3) {
				for (CriteriaBuilder.Trimspec tspec : ts) {
					pass1 = true;
					if (expected.contains(tspec)) {
						logger.log(Logger.Level.INFO, "Testing valueOf:" + tspec);
						CriteriaBuilder.Trimspec.valueOf(tspec.toString());
					} else {
						pass2 = false;
						logger.log(Logger.Level.ERROR, "values() returned incorrect value:" + tspec);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected number of values:3, actual:" + ts.length);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("trimspecTest failed");

		}
	}

	/*
	 * @testName: trimBothExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:867
	 *
	 * @test_Strategy: Select trim(both from c.name) from Customer c where c.name= '
	 * David R. Vincent'
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void trimBothExpTest() throws Exception {
		boolean pass = false;
		final String expectedResult = "David R. Vincent";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> cust = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = cust.getModel();

			cquery.where(cb.equal(cust.get(Customer_.getSingularAttribute("name", String.class)),
					cb.literal(" David R. Vincent")));
			cquery.select(cb.trim(Trimspec.BOTH, cust.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Received expected result:|" + result + "|");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Mismatch in received results - expected = |" + expectedResult
						+ "|, received = |" + result + "|");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("trimBothExpTest failed");

		}
	}

	/*
	 * @testName: trimExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:866
	 *
	 * @test_Strategy: Select trim(both from t.name) from Trim t where
	 * trim(t.name)='David R. Vincent'
	 *
	 *
	 */
	@SetupMethod(name = "setupTrimData")
	@Test
	public void trimExpTest() throws Exception {
		boolean pass = false;
		final String expected = " David R. Vincent ";
		final String expected2 = "David R. Vincent";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		/*
		 * Trim tTrim = getEntityManager().find(Trim.class, "19");
		 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
		 * (tTrim.getName().equals(expected)) {
		 * logger.log(Logger.Level.TRACE,"Received expected find result: " +
		 * tTrim.getName()); pass1 = true; } else {
		 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
		 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
		 * tTrim.getName() + "|"); }
		 * 
		 */
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Trim> trim = cquery.from(Trim.class);

			// Get Metamodel from Root
			EntityType<Trim> trim_ = trim.getModel();

			cquery.where(cb.equal(cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))),
					cb.literal(expected.trim())));
			cquery.select(cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expected2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:|" + result + "|");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Mismatch in received results - expected = |" + expected2 + "|, received = |" + result + "|");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("trimExpTest failed");

		}
	}

	/*
	 * testName: trimLeadingExpTest assertion_ids: PERSISTENCE:JAVADOC:867
	 *
	 * test_Strategy: Select trim(leading from t.name) from Trim t where t.name= '
	 * David R. Vincent '
	 *
	 *
	 */
	/*
	 * @SetupMethod(name = "setupTrimData") // TODO - once TRIM issues are resolved,
	 * re-enable this test public void trimLeadingExpTest() throws Exception {
	 * boolean pass = false; final String expected = " David R. Vincent "; final
	 * String expected2 = "David R. Vincent             ";
	 * 
	 * CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
	 * 
	 * 
	 * getEntityTransaction().begin();
	 * 
	 * Trim tTrim = getEntityManager().find(Trim.class, "19");
	 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
	 * (!tTrim.getName().equals(expected)) {
	 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
	 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
	 * tTrim.getName() + "|"); }
	 * 
	 * 
	 * CriteriaQuery<String> cquery = cb.createQuery(String.class); if (cquery !=
	 * null) { Root<Trim> trim = cquery.from(Trim.class);
	 * 
	 * 
	 * //Get Metamodel from Root EntityType<Trim> trim_ = trim.getModel();
	 * 
	 * cquery.where(cb.equal( trim.get(trim_.getSingularAttribute("name",
	 * String.class)), cb.literal(expected)));
	 * cquery.select(cb.trim(Trimspec.LEADING,
	 * trim.get(trim_.getSingularAttribute("name", String.class))));
	 * 
	 * TypedQuery<String> tq = getEntityManager().createQuery(cquery);
	 * 
	 * String result = tq.getSingleResult();
	 * 
	 * if (result.equals(expected2)) {
	 * logger.log(Logger.Level.TRACE,"Received expected result:|" + result + "|");
	 * pass = true; } else {
	 * logger.log(Logger.Level.ERROR,"Mismatch in received results - expected = |" +
	 * expected2 + "|, received = |" + result + "|"); }
	 * 
	 * } else {
	 * logger.log(Logger.Level.ERROR,"Failed to get Non-null Criteria Query"); }
	 * 
	 * getEntityTransaction().commit();
	 * 
	 * if (!pass) { throw new Exception("trimLeadingExpTest failed");
	 * 
	 * } }
	 */

	/*
	 * @testName: trimTrailingCharExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:867
	 *
	 * @test_Strategy: Select trim(trailing from t.name) from Trim t where
	 * trim(t.name)= 'David R. Vincent'
	 *
	 */
	@SetupMethod(name = "setupTrimData")
	@Test
	public void trimTrailingCharExpTest() throws Exception {
		boolean pass = false;
		final String expected = " David R. Vincent ";
		final String expected2 = " David R. Vincent";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		/*
		 * Trim tTrim = getEntityManager().find(Trim.class, "19");
		 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
		 * (tTrim.getName().equals(expected)) {
		 * logger.log(Logger.Level.TRACE,"Received expected find result: " +
		 * tTrim.getName()); pass1 = true; } else {
		 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
		 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
		 * tTrim.getName() + "|"); }
		 */

		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Trim> trim = cquery.from(Trim.class);

			// Get Metamodel from Root
			EntityType<Trim> trim_ = trim.getModel();

			cquery.where(cb.equal(cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))),
					cb.literal(expected.trim())));
			cquery.select(cb.trim(Trimspec.TRAILING, trim.get(trim_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expected2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:|" + result + "|");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Mismatch in received results - expected = |" + expected2 + "|, received = |" + result + "|");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("trimTrailingCharExpTest failed");

		}
	}

	/*
	 * @testName: lower
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:812
	 *
	 * @test_Strategy: Select lower(c.name) From Customer c a WHERE c.name = 'Lisa
	 * M. Presley'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void lower() throws Exception {
		final String expectedResult = "lisa m. presley";
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> cust = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = cust.getModel();

			cquery.where(cb.equal(cust.get(Customer_.getSingularAttribute("name", String.class)),
					cb.literal("Lisa M. Presley")));
			cquery.select(cb.lower(cust.get(Customer_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Mismatch in received results - expected = " + expectedResult + " received = " + result);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("lower test failed");

		}

	}

	/*
	 * @testName: upper
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:874
	 *
	 * @test_Strategy: Select upper(a.alias) From Alias a WHERE a.alias = 'iris'
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void upper() throws Exception {
		final String expectedResult = "IRIS";
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.where(cb.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)), cb.literal("iris")));
			cquery.select(cb.upper(alias.get(Alias_.getSingularAttribute("alias", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);
			String result = tq.getSingleResult();

			if (result != null) {
				if (result.equals(expectedResult)) {
					logger.log(Logger.Level.TRACE, "Successfully returned expected results");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Mismatch in received results - expected = " + expectedResult + " received = " + result);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Missing expected result");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("upper test failed");

		}
	}

	/*
	 * @testName: length
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:795
	 *
	 * @test_Strategy: Select a From Alias a WHERE length (a.alias) = 9
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void length() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[27].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);

			cquery.where(
					cbuilder.equal(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 9));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("length test failed");

		}
	}

	/*
	 * @testName: locateExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:809
	 *
	 * @test_Strategy: Select a from Alias a where LOCATE('ev', a.alias) = 3
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder
					.equal(cbuilder.locate(alias.get(Alias_.getSingularAttribute("alias", String.class)), "ev"), 3));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("locateExpStringTest failed");

		}
	}

	/*
	 * @testName: locateExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:808
	 *
	 * @test_Strategy: Select a from Alias a where LOCATE('ev', a.alias) = 3
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.equal(cbuilder.locate(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.literal("ev")), 3));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("locateExpExpTest failed");

		}
	}

	/*
	 * @testName: currentDate
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:759
	 *
	 * @test_Strategy: SELECT current_date() From Product p where p.id = "1"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void currentDate() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Date> cquery = cbuilder.createQuery(Date.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			cquery.select(cbuilder.currentDate());
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<Date> tq = getEntityManager().createQuery(cquery);

			Date result = tq.getSingleResult();
			Date d = Date.valueOf(result.toString());
			if (d.equals(result)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get the expected Date object");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("currentDate test failed");
		}
	}

	/*
	 * @testName: currentTime
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:760
	 *
	 * @test_Strategy: SELECT current_time() From Product p where p.id = "1"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void currentTime() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Time> cquery = cbuilder.createQuery(Time.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			cquery.select(cbuilder.currentTime());
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<Time> tq = getEntityManager().createQuery(cquery);

			Time result = tq.getSingleResult();
			Time ts = new Time(result.getTime());
			if (result.equals(ts)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get the expected Time object");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("currentTimes test failed");
		}
	}

	/*
	 * @testName: currentTimestamp
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:761
	 *
	 * @test_Strategy: SELECT current_timestamp() From Product p where p.id = "1"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void currentTimestamp() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Timestamp> cquery = cbuilder.createQuery(Timestamp.class);
		if (cquery != null) {
			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = product.getModel();
			cquery.select(cbuilder.currentTimestamp());
			cquery.where(cbuilder.equal(product.get(Product_.getSingularAttribute("id", String.class)), "1"));

			TypedQuery<Timestamp> tq = getEntityManager().createQuery(cquery);

			Timestamp result = tq.getSingleResult();
			Timestamp ts = Timestamp.valueOf(result.toString());
			if (ts.equals(result)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get the expected Timestamp object");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("currentTimestamp test failed");
		}
	}

	/*
	 * @testName: createCriteriaDeleteTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1676; PERSISTENCE:SPEC:1701;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void createCriteriaDeleteTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
			if (cd != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null CriteriaDelete");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null CriteriaDelete");
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass) {
			throw new Exception("createCriteriaDeleteTest test failed");
		}
	}

	/*
	 * @testName: createCriteriaUpdateTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1677; PERSISTENCE:SPEC:1701;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void createCriteriaUpdateTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
			if (cd != null) {
				logger.log(Logger.Level.TRACE, "Obtained Non-null CriteriaUpdate");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null CriteriaUpdate");
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass) {
			throw new Exception("createCriteriaUpdateTest test failed");
		}
	}

	/*
	 * @testName: expressionAliasTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:958;
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void expressionAliasTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();

			Expression idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			String id = idPath.alias("IDID").getAlias();
			if (id.equals("IDID")) {
				logger.log(Logger.Level.TRACE, "id=" + id);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected id value:IDID, actual value:" + id);
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass) {
			throw new Exception("expressionAliasTest failed");
		}
	}

	/*
	 * @testName: treatPathClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1683; PERSISTENCE:SPEC:1734;
	 * PERSISTENCE:SPEC:1734; PERSISTENCE:SPEC:1734.2;
	 *
	 * @test_Strategy: SELECT p.id From Product p WHERE TREAT(p as
	 * SoftwareProduct).name LIKE "Java%"
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void treatPathClassTest() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.TRACE, "*****************************");
		logger.log(Logger.Level.TRACE, "SoftwareProducts:");
		logger.log(Logger.Level.TRACE, "--------------------");
		for (SoftwareProduct p : softwareRef) {
			logger.log(Logger.Level.TRACE, "ID:" + p.getId() + ":" + getEntityManager().find(Product.class, p.getId()));
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {

			Root<Product> product = cquery.from(Product.class);
			EntityType<Product> Product_ = mm.entity(com.sun.ts.tests.jpa.common.schema30.Product.class);

			cquery.select(product.get(Product_.getSingularAttribute("id", String.class)));

			cquery.where(
					cbuilder.like(cbuilder.treat(product, SoftwareProduct.class).get(SoftwareProduct_.name), "Java%"));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			List<String> actual = tq.getResultList();
			List<String> expected = new ArrayList<String>();
			expected.add("34");
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("treatPathClassTest failed");
		}
	}

	/*
	 * @testName: joinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1716; PERSISTENCE:SPEC:1717;
	 * PERSISTENCE:SPEC:1613;
	 * 
	 * @test_Strategy: select o FROM Order o INNER JOIN o.lineItems l ON l.quantity
	 * > 5
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinOnExpressionTest() throws Exception {
		boolean pass = false;

		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsCollection, JoinType.INNER);
			Expression exp = cbuilder.equal(lineItem.get("id"), "1");

			lineItem.on(exp);
			cquery.select(customer);

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
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			throw new Exception("queryTest61 failed", e);
		}

		if (!pass)
			throw new Exception("joinOnExpressionTest failed");
	}

	/*
	 * @testName: joinOnPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1717; PERSISTENCE:JAVADOC:1715;
	 * 
	 * @test_Strategy: select o FROM Order o LEFT JOIN o.lineItems l ON (l.quantity
	 * > 5 AND l.quantity < 9)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinOnPredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsCollection, JoinType.INNER);
			Predicate pred = lineItem.getOn();
			if (pred == null) {
				logger.log(Logger.Level.TRACE, "Received expected null from getOn()");
				pass1 = true;

			} else {
				logger.log(Logger.Level.ERROR, "Expected getOn() to return null:");
				List<Expression<Boolean>> lExp = pred.getExpressions();
				for (Expression exp : lExp) {
					logger.log(Logger.Level.ERROR, "actual:" + exp.toString());
				}
			}
			Predicate[] predArray = { cbuilder.equal(lineItem.get("id"), "1") };

			lineItem.on(predArray);

			pred = lineItem.getOn();
			if (pred == null) {
				logger.log(Logger.Level.ERROR, "Received null from getOn()");
			} else {
				pass2 = true;
				logger.log(Logger.Level.TRACE, "getOn() returned non-null:");
				List<Expression<Boolean>> lExp = pred.getExpressions();
				for (Expression exp : lExp) {
					logger.log(Logger.Level.TRACE, "actual:" + exp.toString());
				}
			}

			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			throw new Exception("queryTest61 failed", e);
		}

		if (!pass1 || !pass2 || !pass3)
			throw new Exception("joinOnPredicateArrayTest failed");
	}

	/*
	 * @testName: nullifExpressionExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:830
	 *
	 * @test_Strategy: SELECT c.ID, NULLIF(LCASE(c.home.city), LCASE(c.work.city))
	 * FROM CUSTOMER c WHERE (((c.home.city IS NOT NULL) AND (c.work.city IS NOT
	 * NULL)) ORDER BY c.ID ASC
	 *
	 *
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void nullifExpressionExpressionTest() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		for (Customer c : customerRef) {
			// logger.log(Logger.Level.TRACE,"Expected Data:"+c.toString());
			String id = c.getId();
			if (Integer.parseInt(id) <= 18 && Integer.parseInt(id) != 9) {
				if (c.getHome().getCity().equals(c.getWork().getCity())) {
					expected.add(c.getId() + ",null");
				} else {
					expected.add(c.getId() + "," + c.getHome().getCity().toLowerCase());
				}
			}
		}

		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Tuple> cquery = cbuilder.createQuery(Tuple.class);
		if (cquery != null)

		{
			Root<Customer> customer = cquery.from(Customer.class);

			Expression<String> expHomeCity = cbuilder.lower(customer.get("home").<String>get("city"));
			Expression<String> expWorkCity = cbuilder.lower(customer.get("work").<String>get("city"));

			cquery.multiselect(customer.get("id"), cbuilder.nullif(expHomeCity, expWorkCity));

			cquery.where(cbuilder.and(cbuilder.isNotNull(customer.get("home").<String>get("city")),
					cbuilder.isNotNull(customer.get("work").<String>get("city"))));
			cquery.orderBy(cbuilder.asc(customer.get("id")));

			List<Tuple> result = getEntityManager().createQuery(cquery).getResultList();

			for (Tuple t : result) {
				logger.log(Logger.Level.TRACE, "actual:" + t.toString());

				String id = (String) t.get(0);
				String city = (String) t.get(1);
				if (city != null) {
					actual.add(id + "," + city);
				} else {
					actual.add(id + ",null");
				}
			}

			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "actual:" + s);
			}
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
		} else

		{
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("nullifExpressionExpressionTest failed");
		}

	}

	/*
	 * @testName: nullifExpressionObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:831
	 *
	 * @test_Strategy: SELECT c.ID, NULLIF(LCASE(c.home.city), "burlington") FROM
	 * CUSTOMER c WHERE ((c.home.city IS NOT NULL) ORDER BY c.ID ASC
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void nullifExpressionObjectTest() throws Exception {
		boolean pass = false;

		List<String> expected = new ArrayList<String>();
		for (Customer c : customerRef) {
			// logger.log(Logger.Level.TRACE,"Expected Data:"+c.toString());
			String id = c.getId();
			if (Integer.parseInt(id) <= 18 && Integer.parseInt(id) != 9) {
				if (c.getHome().getCity().equals(c.getWork().getCity())) {
					expected.add(c.getId() + ",null");
				} else {
					expected.add(c.getId() + "," + c.getHome().getCity().toLowerCase());
				}
			}
		}

		Collections.sort(expected);

		List<String> actual = new ArrayList<String>();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Tuple> cquery = cbuilder.createQuery(Tuple.class);
		if (cquery != null)

		{
			Root<Customer> customer = cquery.from(Customer.class);

			Expression<String> expHomeCity = cbuilder.lower(customer.get("home").<String>get("city"));

			cquery.multiselect(customer.get("id"), cbuilder.nullif(expHomeCity, "burlington"));

			cquery.where(cbuilder.isNotNull(customer.get("home").<String>get("city")));

			cquery.orderBy(cbuilder.asc(customer.get("id")));

			List<Tuple> result = getEntityManager().createQuery(cquery).getResultList();

			for (Tuple t : result) {
				logger.log(Logger.Level.TRACE, "actual:" + t.toString());

				String id = (String) t.get(0);
				String city = (String) t.get(1);
				if (city != null) {
					actual.add(id + "," + city);
				} else {
					actual.add(id + ",null");
				}
			}

			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "actual:" + s);
			}
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
		} else

		{
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("nullifExpressionObjectTest failed");
		}

	}

	/*
	 * @testName: locateExpressionExpressionExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:810
	 *
	 * @test_Strategy: SELECT a FROM ALIAS a WHERE (LOCATE(ev, a.alias, 1) > 0)
	 *
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpressionExpressionExpressionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null)

		{
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);

			Expression exp1 = alias.get(Alias_.getSingularAttribute("alias", String.class));
			Expression exp2 = cbuilder.literal("ev");
			Expression exp3 = cbuilder.toInteger(cbuilder.literal(1));

			cquery.where(cbuilder.gt(cbuilder.locate(exp1, exp2, exp3), 0));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("locateExpressionExpressionExpressionTest failed");

		}
	}

	/*
	 * @testName: locateExpressionStringIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:811
	 *
	 * @test_Strategy: SELECT a FROM ALIAS a WHERE (LOCATE(ev, a.alias, 1) > 0)
	 *
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpressionStringIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null)

		{
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);

			Expression exp1 = alias.get(Alias_.getSingularAttribute("alias", String.class));

			cquery.where(cbuilder.gt(cbuilder.locate(exp1, "ev", 1), 0));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("locateExpressionExpressionExpressionTest failed");

		}
	}

	/*
	 * @testName: coalesceTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:747
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void coalesceTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		CriteriaBuilder.Coalesce col = cbuilder.coalesce();
		if (col != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Coalesce");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Coalesce");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("coalesceTest failed");
		}
	}

	/*
	 * @testName: selectMultiSelectTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1707; PERSISTENCE:SPEC:1708;
	 * PERSISTENCE:SPEC:1709;
	 * 
	 * @test_Strategy: Execute a query and verify a single result can be returned
	 * when using cbuilder.createQuery() Execute a query and verify a multiple
	 * results can be returned when using cbuilder.createQuery()
	 *
	 * Select c.id, c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void selectMultiSelectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		for (Customer c : customerRef) {
			expected.add(Integer.valueOf(c.getId()));
		}
		Collections.sort(expected);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		logger.log(Logger.Level.INFO, "Testing select");

		try {

			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);

				// Get Metamodel from Root
				EntityType<Customer> Customer_ = customer.getModel();

				cquery.select(customer.get(Customer_.getSingularAttribute("id", String.class)));

				Query q = getEntityManager().createQuery(cquery);

				List result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();

				for (Object row : result) {
					Integer id = Integer.valueOf((String) row);
					logger.log(Logger.Level.TRACE, "id=" + id);
					actual.add(id);
				}
				Collections.sort(actual);

				if (!checkEntityPK(actual, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
							+ " references, got: " + actual.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass1 = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}
		logger.log(Logger.Level.INFO, "Testing multiselect");
		try {

			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);

				// Get Metamodel from Root
				EntityType<Customer> Customer_ = customer.getModel();

				cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
						customer.get(Customer_.getSingularAttribute("name", String.class)));

				Query q = getEntityManager().createQuery(cquery);

				List<Object[]> result = q.getResultList();

				List<Integer> actual = new ArrayList<Integer>();

				for (Object[] row : result) {
					Integer id = Integer.valueOf((String) row[0]);
					String name = (String) row[1];
					logger.log(Logger.Level.TRACE, "id=" + id);
					logger.log(Logger.Level.TRACE, "name=" + name);
					// some of the names have nulls so deal with them
					if (name != null && customerRef[id - 1].getName() != null) {
						if (customerRef[id - 1].getName().equals(name)) {
							actual.add(id);
						} else {
							logger.log(Logger.Level.ERROR,
									"Expected name:|" + customerRef[id - 1].getName() + "|, actual:|" + name + "|");
						}
					} else if (name == null && customerRef[id - 1].getName() == null) {
						actual.add(id);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected name:" + customerRef[id - 1].getName() + ", actual:null");
					}
				}
				Collections.sort(actual);

				if (!checkEntityPK(actual, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
							+ " references, got: " + actual.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("selectMultiSelectTest test failed");
		}
	}

	/*
	 * @testName: multiRootTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1711
	 * 
	 * @test_Strategy: Test that using mulitple roots results in a cartesian product
	 *
	 * Select s1, s2 from Spouse s1, Spouse s2
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void multiRootTest() throws Exception {
		boolean pass = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= spouse.length; i++) {
			for (int j = 1; j <= spouse.length; j++) {
				list.add(Integer.toString(i) + "," + Integer.toString(j));
			}
		}

		try {

			getEntityTransaction().begin();
			CriteriaQuery cquery = cbuilder.createQuery();
			if (cquery != null) {
				Root<Spouse> spouse1 = cquery.from(Spouse.class);
				Root<Spouse> spouse2 = cquery.from(Spouse.class);

				// Get Metamodel from Root
				EntityType<Spouse> Spouse1_ = spouse1.getModel();
				EntityType<Spouse> Spouse2_ = spouse2.getModel();

				cquery.multiselect(spouse1.get(Spouse1_.getSingularAttribute("id", String.class)),
						spouse2.get(Spouse2_.getSingularAttribute("id", String.class)));

				Query q = getEntityManager().createQuery(cquery);

				List<Object[]> result = q.getResultList();

				for (Object[] row : result) {
					Integer sId1 = Integer.valueOf((String) row[0]);
					Integer sId2 = Integer.valueOf((String) row[1]);
					logger.log(Logger.Level.TRACE, "sId1=" + sId1 + ", sId2=" + sId2);
					list.remove(sId1 + "," + sId2);
				}
				logger.log(Logger.Level.TRACE, "Size:" + list.size());
				if (list.isEmpty()) {
					logger.log(Logger.Level.TRACE, "All PK's were found");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Not all PK's were returned");
					for (String s : list) {
						logger.log(Logger.Level.ERROR, "Not all PK's were returned:" + s);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}

			getEntityTransaction().commit();
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		if (!pass) {
			throw new Exception("multiRootTest test failed");
		}
	}

}
