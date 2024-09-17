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


import java.util.Set;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;


public class Client7 extends Util {

	public static void main(String[] args) {
		Client7 theTests = new Client7();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: getCorrelatedJoinsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1179;
	 * 
	 * @test_Strategy: Test getting correlated joins from subquery.
	 */
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
					logErr(
							"Received expected 0 correlated joins from subquery.getCorrelatedJoins() when none exist");

					// correlate subquery
					Join<Customer, Order> sqo = sq.correlate(customer.join(Customer_.orders));
					sq.select(sqo);
					cJoins = sq.getCorrelatedJoins();
					if (cJoins != null) {
						if (cJoins.size() == 1) {
							logTrace(
									"Received expected 1 correlated join from subquery.getCorrelatedJoins()");
							pass = true;
						} else {
							logErr( "Received " + cJoins.size()
									+ " correlated joins from subquery.getCorrelatedJoins() when 1 exist");

						}
					} else {
						logErr(
								"Received null from subquery.getCorrelatedJoins() when 1 correlated join exists");

					}
				} else {
					logErr( "Received " + cJoins.size()
							+ " unexpected correlated joins from subquery.getCorrelatedJoins() when non exist");

				}
			} else {
				logErr(
						"Received null from subquery.getCorrelatedJoins() instead of empty set when non exist");

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" getCorrelatedJoinsTest failed");
		}
	}

}
