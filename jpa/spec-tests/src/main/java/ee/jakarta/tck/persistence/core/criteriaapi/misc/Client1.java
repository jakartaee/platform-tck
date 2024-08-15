/*
 * Copyright (c) 2012, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.misc;


import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.UtilSetup;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.EntityType;

public class Client1 extends UtilSetup {



	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_misc1.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: predicateIsNegatedTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1118; PERSISTENCE:JAVADOC:1119
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void predicateIsNegatedTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		logMsg( "Testing default");
		Predicate pred = cbuilder.equal(cbuilder.literal("1"), "1");
		Boolean result = pred.isNegated();
		if (!result) {
			logTrace( "Received expected result:" + result);
			pass1 = true;
		} else {
			logErr( "Expected:false , actual:" + result);
		}
		pred = null;
		logMsg( "Testing when Predicate.not is present");
		pred = cbuilder.equal(cbuilder.literal("1"), "1").not();
		result = pred.isNegated();

		if (result) {
			logTrace( "Received expected result:" + result);
			pass2 = true;
		} else {
			logErr( "Expected:true, actual:" + result);
		}
		pred = null;
		logMsg( "Testing when CriteriaBuilder.not is present");
		pred = cbuilder.not(cbuilder.equal(cbuilder.literal("1"), "1"));
		result = pred.isNegated();
		if (result) {
			logTrace( "Received expected result:" + result);
			pass3 = true;

		} else {
			logErr( "Expected:true, actual:" + result);
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("predicateIsNegatedTest failed");

		}
	}

	/*
	 * @testName: predicateBooleanOperatorTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1127; PERSISTENCE:JAVADOC:1128
	 *
	 * @test_Strategy:
	 */
	@Test
	public void predicateBooleanOperatorTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {

			logMsg( "Testing values()");
			Predicate.BooleanOperator[] results = Predicate.BooleanOperator.values();
			if (results.length == 2) {
				if (results[0].equals(Predicate.BooleanOperator.AND) && results[1].equals(Predicate.BooleanOperator.OR)
						|| results[0].equals(Predicate.BooleanOperator.OR)
								&& results[1].equals(Predicate.BooleanOperator.AND)) {
					logTrace( "Received expected values from values()");
					pass1 = true;
				}

			} else {
				logErr( "Expected number of values: 2, actual:" + results.length);
			}

			logMsg( "Testing valueOf(...)");
			for (Predicate.BooleanOperator pb : Predicate.BooleanOperator.values()) {
				logTrace( "Testing:" + pb.name());
				try {
					Predicate.BooleanOperator.valueOf(pb.name());
					pass2 = true;
				} catch (IllegalArgumentException iae) {
					logTrace( "Received unexpected IllegalArgumentException exception");
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}
			}

			logMsg( "Testing valueOf(Invalid_value)");
			try {
				Predicate.BooleanOperator.valueOf("Invalid_value");
				logErr( "Did not received IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logTrace( "Received expected exception");
				pass3 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("predicateBooleanOperatorTest failed");

		}
	}

	/*
	 * @testName: predicateGetOperatorTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1117
	 *
	 * @test_Strategy:
	 */
	@Test
	public void predicateGetOperatorTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			EntityType<Customer> Customer_ = customer.getModel();

			logMsg( "Testing default");
			Predicate predicate = cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1");
			Predicate.BooleanOperator result = predicate.getOperator();
			if (!result.equals(Predicate.BooleanOperator.AND)) {
				logErr(
						"Expected:" + Predicate.BooleanOperator.AND.name() + ", actual:" + result.name());
			} else {
				pass1 = true;
			}
			logMsg( "Testing AND");
			predicate = cbuilder.and(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"));
			if (!predicate.getOperator().equals(Predicate.BooleanOperator.AND)) {
				logErr(
						"Expected:" + Predicate.BooleanOperator.AND.name() + ", actual:" + result.name());
			} else {
				pass2 = true;
			}
			logMsg( "Testing OR");
			predicate = cbuilder.or(
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"),
					cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"));
			if (!predicate.getOperator().equals(Predicate.BooleanOperator.OR)) {
				logErr(
						"Expected:" + Predicate.BooleanOperator.OR.name() + ", actual:" + result.name());
			} else {
				pass3 = true;
			}
		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("predicateGetOperatorTest failed");

		}
	}

	/*
	 * @testName: selectionGetCompoundSelectionItemsIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1166;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void selectionGetCompoundSelectionItemsIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);

		Selection sel = cbuilder.length(customer.get(Customer_.id));
		try {
			sel.getCompoundSelectionItems();
			logErr( "Did not throw IllegalStateException");
		} catch (IllegalStateException ise) {
			logTrace( "Received IllegalStateException");
			pass = true;
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("selectionGetCompoundSelectionItemsIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: expressionIsCompoundSelectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:961;
	 *
	 * @test_Strategy:
	 *
	 * expression will never be a compound expression
	 */
	@Test
	public void expressionIsCompoundSelectionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		Expression exp = cbuilder.literal("1");
		boolean actual = exp.isCompoundSelection();
		if (actual == false) {
			logTrace( "Received expected result:" + actual);
			pass = true;
		} else {
			logErr( "Expected isCompoundSelection() to return: false, actual:" + actual);
		}
		if (!pass) {
			throw new Exception("expressionGetCompoundSelectionItemsTest failed");
		}
	}

	/*
	 * @testName: expressionGetCompoundSelectionItemsIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:960;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void expressionGetCompoundSelectionItemsIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		Expression exp = cbuilder.literal("1");
		try {
			exp.getCompoundSelectionItems();
			logErr( "Did not throw IllegalStateException");
		} catch (IllegalStateException ise) {
			logTrace( "Received IllegalStateException");
			pass = true;
		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("expressionGetCompoundSelectionItemsIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: getRoots
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:941;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery
	 *
	 */
	@Test
	public void getRoots() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
			if (cquery != null) {

				cquery.from(Customer.class);
				cquery.from(Order.class);

				Set<Root<?>> rootSet = cquery.getRoots();

				if (rootSet != null) {

					if (rootSet.size() == 2) {
						int count = 0;
						boolean foundCustomer = false;
						boolean foundOrder = false;
						for (Root newRoot : rootSet) {
							EntityType eType1 = newRoot.getModel();
							String name = eType1.getName();
							logTrace( "entityType Name = " + name);
							if (name.equals("Customer")) {
								logTrace( "Received expected name:" + name);
								foundCustomer = true;
								count++;
							}
							if (name.equals("Order")) {
								logTrace( "Received expected name:" + name);
								foundOrder = true;
								count++;
							}
						}
						if (count == 2 && foundCustomer && foundOrder) {
							pass = true;
						} else {
							logErr( "Did not get Customer and Order roots back from getRoot");
						}
					} else {
						logErr( "getRoots did not return 2 entries in the set");
					}

				} else {
					logErr( "getRoots returned null");

				}

			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}

		} catch (Exception e) {
			logErr( "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("getRoots test failed");
		}
	}

	/*
	 * @testName: getSelection
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1167; PERSISTENCE:JAVADOC:942
	 * 
	 * @test_Strategy:
	 *
	 *
	 */
	@Test
	public void getSelection() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		try {

			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			logMsg( "Testing non-compound selection");

			CriteriaQuery<Customer> cquery = qbuilder.createQuery(Customer.class);

			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);
				cquery.select(customer);
				Selection<Customer> _select = cquery.getSelection();
				if (_select != null) {
					if (!_select.isCompoundSelection()) {
						logTrace( "isCompoundSelection returned expected false");
						pass1 = true;
					} else {
						logErr( "isCompoundSelection returned true instead of false");
					}

					String javaName = _select.getJavaType().getName();
					if (javaName.equals("ee.jakarta.tck.persistence.common.schema30.Customer")) {
						pass2 = true;
					} else {
						logErr(
								"Expected: ee.jakarta.tck.persistence.common.schema30.Customer, actual:" + javaName);
					}
				} else {
					logErr( "get Selection returned null");
				}

				logMsg( "Testing compound selection");
				CriteriaQuery cquery1 = qbuilder.createQuery();
				customer = cquery1.from(Customer.class);
				EntityType<Customer> CUSTOMER_ = customer.getModel();
				cquery1.multiselect(customer.get(CUSTOMER_.getSingularAttribute("id", String.class)),
						customer.get(CUSTOMER_.getSingularAttribute("name", String.class)));

				Selection _select1 = cquery1.getSelection();
				if (_select1 != null) {
					if (_select1.isCompoundSelection()) {
						logTrace( "isCompoundSelection returned expected true");
						pass3 = true;
					} else {
						logErr( "isCompoundSelection returned false");
					}
				} else {
					logErr( "get Selection returned null");
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
		} catch (Exception e) {
			logErr( "Caught unexpected exception ", e);
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("getSelection failed");

		}
	}

	/*
	 * @testName: getGroupList
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1129; PERSISTENCE:JAVADOC:937
	 *
	 * @test_Strategy: select c.country.code FROM Customer c GROUP BY
	 * c.country.code"
	 */
	@Test
	public void getGroupList() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			CriteriaQuery<Customer> cquery = qbuilder.createQuery(Customer.class);
			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);

				logMsg( "Testing with NO group expressions");

				List<Expression<?>> groupList = cquery.getGroupList();
				if (groupList != null) {
					if (groupList.size() == 0) {
						logTrace( "Received empty list from getGroupList");
						pass1 = true;
					} else {
						logErr( "Expected : 0" + " Received :" + groupList.size());
						for (Expression strExpr : groupList) {
							logErr( "Expression:" + strExpr.toString());
						}

					}
				} else {
					logErr(
							"getGroupList returned null instead of empty list when no groupby expressions have been specified");

				}
				logMsg( "Testing with group expressions");

				Expression e = customer.get("name");
				cquery.groupBy(e);

				groupList = cquery.getGroupList();
				if (groupList != null) {
					if (groupList.size() == 1) {
						for (Expression strExpr : groupList) {
							String sType = strExpr.getJavaType().getName();
							if (sType.equals("java.lang.String")) {
								logTrace( "Received expected type:" + sType);
								pass2 = true;

							} else {
								logErr( "Expected type: java.lang.String, actual:" + sType);

							}
						}
					} else {
						logErr( "Expected : 1" + " Received :" + groupList.size());
						for (Expression strExpr : groupList) {
							logErr( "Actual expression:" + strExpr.toString());
						}

					}
				} else {
					logErr(
							"getGroupList returned null instead of a populated list when groupby expressions have been specified");

				}
			} else {
				logErr( "Failed to get Non-null Criteria Query");

			}
		} catch (Exception e) {
			logErr( "Caught exception groupBy: " + e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("getGroupList failed");
		}
	}

	/*
	 * @testName: isDistinct
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:947
	 *
	 * @test_Strategy: Use Conjunction
	 *
	 *
	 */
	@Test
	public void isDistinct() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			CriteriaQuery cquery = qbuilder.createQuery();
			if (cquery != null) {
				cquery.from(Customer.class);
				cquery.distinct(true);

				Boolean isDistinct = cquery.isDistinct();
				if (isDistinct) {
					pass = true;
				}

			} else {
				logErr( "Failed to get Non-null Criteria Query");
			}
		} catch (Exception e) {
			logErr( "Caught unexpected excetion: " + e);
		}

		if (!pass) {
			throw new Exception("isDistinct test failed");

		}
	}

	/*
	 * @testName: getResultType
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:940
	 *
	 * @test_Strategy: Use Conjunction Select Distinct c FROM Customer c where
	 * customer.name = 'Robert E. Bissett'
	 *
	 *
	 */
	@Test
	public void getResultType() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			logMsg( "Testing specific class return type");

			String expected = "ee.jakarta.tck.persistence.common.schema30.Customer";
			CriteriaQuery cquery = qbuilder.createQuery(Customer.class);
			if (cquery != null) {
				Class resultType = cquery.getResultType();
				if (resultType != null) {
					if (resultType.getName().equals(expected)) {
						logTrace( "Got Expected Result Type");
						pass1 = true;
					} else {
						logErr( "Received  UnExpected Result Type :" + resultType.getName());
					}

				} else {
					logErr( "getResultType returned null instead of:" + expected);
				}
			} else {
				logErr( "Failed to get Non-null Criteria Query for:" + expected);
			}

			logMsg( "Testing Tuple return type");
			expected = "jakarta.persistence.Tuple";

			cquery = qbuilder.createQuery(Tuple.class);
			if (cquery != null) {
				Class resultType = cquery.getResultType();

				if (resultType != null) {
					if (resultType.getName().equals(expected)) {
						logTrace( "Got Expected Result Type");
						pass2 = true;

					} else {
						logErr( "Received  UnExpected Result Type :" + resultType.getName());
					}

				} else {
					logErr( "getResultType returned null instead of:" + expected);
				}
			} else {
				logErr( "getResultType returned null instead of:" + expected);
			}

			logMsg( "Testing Object return type");
			expected = "java.lang.Object";

			cquery = qbuilder.createQuery();
			if (cquery != null) {
				Class resultType = cquery.getResultType();

				if (resultType != null) {
					if (resultType.getName().equals(expected)) {
						logTrace( "Got Expected Result Type");
						pass3 = true;

					} else {
						logErr( "Received  UnExpected Result Type :" + resultType.getName());
					}

				} else {
					logErr( "getResultType returned null instead of:" + expected);
				}
			} else {
				logErr( "getResultType returned null instead of:" + expected);
			}
		} catch (Exception e) {
			logErr( "Caught exception getGroupRestriction: " + e);
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("getResultType test failed");

		}
	}

}
