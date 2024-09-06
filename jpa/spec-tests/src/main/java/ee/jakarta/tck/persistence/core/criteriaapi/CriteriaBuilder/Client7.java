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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;


import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;


import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Product;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client7 extends Util {


	public static void main(String[] args) {
		Client7 theTests = new Client7();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
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
	public void tupleGetTupleElementIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logTrace( "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			logTrace( "Execute first Tuple Query");

			cquery.multiselect(customer.get("id").alias("ID"), customer.get("name").alias("NAME"));

			cquery.where(cbuilder.equal(customer.get("id"), "3"));

			TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);
			List<Tuple> result = tq.getResultList();

			logTrace( "Number of Tuples from first query:" + result.size());
			Tuple t1 = result.get(0);

			logTrace( "Tuples Received:" + t1.get(0) + ", " + t1.get(1));

			// get second Tuple and second TupleElement inorder to trigger
			// IllegalArgumentException
			CriteriaQuery<Tuple> cquery1 = cbuilder.createTupleQuery();
			Root<Product> product = cquery1.from(Product.class);

			logTrace( "Execute second Tuple Query");

			cquery1.multiselect(product.get("id").alias("ID"), product.get("quantity").alias("QUANTITY"));

			cquery1.where(cbuilder.equal(product.get("id"), "1"));

			TypedQuery<Tuple> tq2 = getEntityManager().createQuery(cquery1);
			List<Tuple> result2 = tq2.getResultList();
			Tuple t2 = null;
			logTrace( "Number of Tuples received from second query:" + result2.size());
			try {
				t2 = result2.get(0);
				pass1 = true;
			} catch (Exception e) {
				logErr( "Received unexpected exception", e);
			}
			if (t2 != null) {

				logTrace( "Tuple Received:" + t2.get(0) + ", " + t2.get(1));

				List<TupleElement<?>> lte2 = t2.getElements();
				TupleElement<?> te2 = lte2.get(1);

				logTrace(
						"TupleElement from second query that will be looked up in the Tuple result returned from first query:"
								+ te2.getAlias());
				try {

					// Using a tuple element returned in the second query, try to get a
					// tuple from the first query using
					// that tuple element
					t1.get(te2);
					logErr(
							"Did not throw IllegalArgumentException when calling Tuple.get with a TupleElement that doesn't exist");

				} catch (IllegalArgumentException iae) {
					logTrace( "Got expected IllegalArgumentException");
					if (getEntityTransaction().getRollbackOnly() != true) {
						pass2 = true;
					} else {
						logErr( "Transaction was marked for rollback and should not have been");
					}
				} catch (Exception e) {
					logErr( "Received unexpected exception", e);
				}
			} else {
				logErr( "result2.get(0) returned null");
			}

		} else {
			logErr( "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("tupleGetTupleElementIllegalArgumentExceptionTest failed");
		}
	}

	public void setupCustAliasProductData(String[] args, Properties p) throws Exception {
		logTrace("setupCustAliasProductData");
		try {
			super.setup(args,p);
			removeTestData();
			createCustomerData();
			createProductData();
			createAliasData();
		} catch (Exception e) {
			logErr("Exception: ", e);
			throw new Exception("setupCustAliasProductData failed:", e);
		}
	}

}
