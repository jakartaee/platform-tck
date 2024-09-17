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

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.HardwareProduct;
import ee.jakarta.tck.persistence.common.schema30.Product;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client6 extends Util {

	public static void main(String[] args) {
		Client6 theTests = new Client6();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}




	/*
	 * @testName: typeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1738
	 * 
	 * @test_Strategy: test path.type()
	 *
	 * Select p from Product p where TYPE(p) = HardwareProduct
	 */
	@SetupMethod(name = "setupProductData")
		public void typeTest() throws Exception {
		boolean pass = false;
		List<Integer> expected = new ArrayList<Integer>();
		for (Product p : hardwareRef) {
			expected.add(Integer.valueOf(p.getId()));
		}
		Collections.sort(expected);
		List<Integer> actual = new ArrayList<Integer>();

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.and(cbuilder.equal(product.type(), HardwareProduct.class)));
			cquery.select(product);
			Query q = getEntityManager().createQuery(cquery);

			List<Product> result = q.getResultList();
			for (Product p : result) {
				actual.add(Integer.parseInt(p.getId()));
			}

			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logErr( "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception queryTest47: ", e);
		}

		if (!pass) {
			throw new Exception("typeTest failed");
		}
	}

}
