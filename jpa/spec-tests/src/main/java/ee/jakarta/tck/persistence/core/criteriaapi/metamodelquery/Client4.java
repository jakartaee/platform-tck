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

package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;


import java.sql.Date;
import java.util.Collections;
import java.util.List;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.Product_;
import ee.jakarta.tck.persistence.common.schema30.ShelfLife_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client4 extends Util {


	public static void main(String[] args) {
		Client4 theTests = new Client4();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/* Run test */

	/*
	 * @testName: queryTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:735; PERSISTENCE:SPEC:784;
	 * 
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest7() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logTrace( "find All Products");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.lt(product.get(Product_.quantity), 10));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(productRef.length);
			List<Product> plist = tquery.getResultList();

			/*
			 * for (Product p : plist){
			 * logTrace("id:"+p.getId()+", name:"+p.getName()+","+p.
			 * getClass( ).getSimpleName()); }
			 */
			expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "21";

			if (!checkEntityPK(plist, expectedPKs)) {
				logErr( "Did not get expected results.  Expected:" + expectedPKs.length
						+ " references, got: " + plist.size());

			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest7 failed");
		}
	}

	/*
	 * @testName: queryTest38
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.7
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function MOD in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest38() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logTrace( "find orders that have the quantity of 50 available");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), 100), product.get(Product_.quantity)));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(productRef.length);
			List<Product> plist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "5";
			expectedPKs[1] = "20";
			if (!checkEntityPK(plist, expectedPKs)) {
				logErr(
						"Did not get expected results.  Expected 2 references, got: " + plist.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest38 failed");
		}
	}

	/*
	 * @testName: queryTest40
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:350
	 * 
	 * @test_Strategy: Execute two methods using the comparison operator BETWEEN in
	 * a comparison expression within the WHERE clause and verify the results of the
	 * two queries are equivalent regardless of the way the expression is composed.
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest40() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		String expectedPKs2[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logTrace(
					"Execute two queries composed differently and verify results" + " Execute Query 1");
			CriteriaQuery<Product> cquery1 = cbuilder.createQuery(Product.class);
			Root<Product> product1 = cquery1.from(Product.class);
			cquery1.where(cbuilder.between(product1.get(Product_.quantity), 10, 20));
			cquery1.select(product1);
			TypedQuery<Product> tquery1 = getEntityManager().createQuery(cquery1);
			tquery1.setMaxResults(productRef.length);
			List<Product> plist1 = tquery1.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "8";
			expectedPKs[1] = "9";
			expectedPKs[2] = "17";
			expectedPKs[3] = "27";
			expectedPKs[4] = "28";
			expectedPKs[5] = "31";
			expectedPKs[6] = "36";

			logTrace( "Execute Query 2");
			CriteriaQuery<Product> cquery2 = cbuilder.createQuery(Product.class);
			Root<Product> product2 = cquery2.from(Product.class);
			cquery2.where(cbuilder.and(cbuilder.ge(product2.get(Product_.quantity), 10),
					cbuilder.le(product2.get(Product_.quantity), 20))).select(product2);
			TypedQuery<Product> tquery2 = getEntityManager().createQuery(cquery2);
			tquery2.setMaxResults(productRef.length);
			List<Product> plist2 = tquery2.getResultList();

			expectedPKs2 = new String[7];
			expectedPKs2[0] = "8";
			expectedPKs2[1] = "9";
			expectedPKs2[2] = "17";
			expectedPKs2[3] = "27";
			expectedPKs2[4] = "28";
			expectedPKs2[5] = "31";
			expectedPKs2[6] = "36";

			if (!checkEntityPK(plist1, expectedPKs)) {
				logErr( "Did not get expected results for first query in queryTest40. "
						+ "  Expected 7 references, got: " + plist1.size());
			} else {
				logTrace( "Expected results received for first query in queryTest40.");
				pass1 = true;
			}

			if (!checkEntityPK(plist2, expectedPKs2)) {
				logErr( "Did not get expected results for second query in queryTest40. "
						+ "  Expected 7 references, got: " + plist2.size());
			} else {
				logTrace( "Expected results received for second query in queryTest40.");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest40 failed");
		}
	}

	/*
	 * @testName: queryTest41
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:350
	 * 
	 * @test_Strategy: Execute two methods using the comparison operator NOT BETWEEN
	 * in a comparison expression within the WHERE clause and verify the results of
	 * the two queries are equivalent regardless of the way the expression is
	 * composed.
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest41() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		String expectedPKs2[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logTrace(
					"Execute two queries composed differently and verify results" + " Execute first query");
			CriteriaQuery<Product> cquery1 = cbuilder.createQuery(Product.class);
			Root<Product> product1 = cquery1.from(Product.class);
			cquery1.where(cbuilder.between(product1.get(Product_.quantity), 20, 200).not());
			cquery1.select(product1);
			TypedQuery<Product> tquery1 = getEntityManager().createQuery(cquery1);
			tquery1.setMaxResults(productRef.length);
			List<Product> plist1 = tquery1.getResultList();

			expectedPKs = new String[10];
			expectedPKs[0] = "8";
			expectedPKs[1] = "9";
			expectedPKs[2] = "10";
			expectedPKs[3] = "11";
			expectedPKs[4] = "14";
			expectedPKs[5] = "15";
			expectedPKs[6] = "17";
			expectedPKs[7] = "21";
			expectedPKs[8] = "29";
			expectedPKs[9] = "31";

			if (TestUtil.traceflag) {
				Collections.sort(plist1);
				for (Product p : plist1) {
					logTrace( "id:" + p.getId() + ", quantity:" + p.getQuantity());
				}
			}
			if (!checkEntityPK(plist1, expectedPKs)) {
				logErr(
						"Did not get expected results for first query.  Expected 31 references, got: " + plist1.size());
			} else {
				logTrace( "Expected results received for first query");
				pass1 = true;

			}
			logTrace( "Execute second query");
			CriteriaQuery<Product> cquery2 = cbuilder.createQuery(Product.class);
			Root<Product> product2 = cquery2.from(Product.class);
			cquery2.where(cbuilder.or(cbuilder.lt(product2.get(Product_.quantity), 20),
					cbuilder.gt(product2.get(Product_.quantity), 200))).select(product2);
			TypedQuery<Product> tquery2 = getEntityManager().createQuery(cquery2);
			tquery2.setMaxResults(productRef.length);
			List<Product> plist2 = tquery2.getResultList();

			expectedPKs2 = new String[10];
			expectedPKs2[0] = "8";
			expectedPKs2[1] = "9";
			expectedPKs2[2] = "10";
			expectedPKs2[3] = "11";
			expectedPKs2[4] = "14";
			expectedPKs2[5] = "15";
			expectedPKs2[6] = "17";
			expectedPKs2[7] = "21";
			expectedPKs2[8] = "29";
			expectedPKs2[9] = "31";

			if (TestUtil.traceflag) {
				Collections.sort(plist2);
				for (Product p : plist2) {
					logTrace( "id:" + p.getId() + ", quantity:" + p.getQuantity());
				}
			}

			if (!checkEntityPK(plist2, expectedPKs2)) {
				logErr(
						"Did not get expected results for second query.  Expected 31 references, got: "
								+ plist2.size());
			} else {
				logTrace( "Expected results received for second query");
				pass2 = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest41 failed");
		}
	}

	/*
	 * @testName: queryTest43
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: Execute a query using Boolean operator AND in a conditional
	 * test ( False AND False = False) where the second condition is not NULL.
	 *
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest43() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logTrace( "Check results of AND operator: False AND False = False");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.and(
					cbuilder.gt(product.get(Product_.quantity),
							cbuilder.sum(cbuilder.literal(500), cbuilder.parameter(Integer.class, "int1"))),
					cbuilder.isNull(product.get(Product_.partNumber))));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("int1", Integer.valueOf(100));
			tquery.setMaxResults(productRef.length);
			List<Product> plist = tquery.getResultList();

			expectedPKs = new String[0];
			if (!checkEntityPK(plist, expectedPKs)) {
				logErr(
						"Did not get expected results.  Expected 0 references, got: " + plist.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest43 failed");
		}
	}

	/*
	 * @testName: queryTest44
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:416
	 * 
	 * @test_Strategy: If an input parameter is NULL, comparison operations
	 * involving the input parameter will return an unknown value.
	 *
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest44() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logTrace( "provide a null value for a comparison operation and verify the results");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.equal(product.get(Product_.name), cbuilder.parameter(String.class, "num1")));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("num1", null);
			tquery.setMaxResults(productRef.length);
			List<Product> plist = tquery.getResultList();

			expectedPKs = new String[0];

			if (!checkEntityPK(plist, expectedPKs)) {
				logErr(
						"Did not get expected results.  Expected 0 references, got: " + plist.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest44 failed");
		}
	}

	/*
	 * @testName: queryTest68
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:383; PERSISTENCE:SPEC:406;
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function SUM.
	 * SUM returns Double when applied to state-fields of floating types. Verify the
	 * results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest68() throws Exception {
		boolean pass = false;
		final Double d1 = 33387.14D;
		final Double d2 = 33387.15D;
		Double d3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logTrace( "find SUM of all product prices");
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.select(cbuilder.sum(product.get(Product_.price)));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			d3 = tquery.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logTrace( "queryTest68 returned expected results: " + d1);
				pass = true;
			} else {
				logTrace( "queryTest68 returned " + d3 + "expected: " + d1);
			}
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("queryTest68 failed");
		}
	}

	/*
	 * @testName: queryTest70
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:383; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:827; PERSISTENCE:SPEC:821
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function SUM.
	 * SUM returns Long when applied to state-fields of integral types. Verify the
	 * results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
		public void queryTest70() throws Exception {
		boolean pass = false;
		final Integer expectedValue = Integer.valueOf(3277);
		Integer result;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logTrace( "find SUM of all product prices");
			CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.select(cbuilder.sum(product.get(Product_.quantity)));
			TypedQuery<Integer> tquery = getEntityManager().createQuery(cquery);
			result = tquery.getSingleResult();

			if (expectedValue.equals(result)) {
				logTrace( "queryTest70 returned expected results: " + result);
				pass = true;
			} else {
				logTrace( "queryTest70 returned " + result + "expected: " + expectedValue);
			}
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("queryTest70 failed");
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
			logTrace( "The dates used in test_betweenDates is : " + date1 + " and " + date6);
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
				logErr(
						"Did not get expected results.  Expected 3 references, got: " + result.size());
			} else {

				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass)
			throw new Exception("test_betweenDates failed");
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
		logTrace( "The dates used in test_betweenDates is : " + date1 + " and " + newdate);

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
				logErr(
						"Did not get expected results.  Expected 1 references, got: " + result.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught unexpected exception:", e);

		}

		if (!pass)
			throw new Exception("test_notBetweenDates failed");
	}

}
