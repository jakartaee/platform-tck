/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.query.flushmode;


import java.util.List;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import ee.jakarta.tck.persistence.common.schema30.Product;

public class Client3 extends Util {

	public Client3() {
	}
	public static void main(String[] args) {
		Client3 theTests = new Client3();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: secondaryTablesValueTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:192; PERSISTENCE:JAVADOC:193;
	 * 
	 * @test_Strategy:
	 */
		public void secondaryTablesValueTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[4];
		expected[0] = "20";
		expected[1] = "24";
		expected[2] = "31";
		expected[3] = "37";

		try {
			getEntityTransaction().begin();

			List<Product> result = getEntityManager()
					.createQuery("SELECT p FROM Product p WHERE p.wareHouse = 'Lowell' ").getResultList();

			if (!checkEntityPK(result, expected)) {
				logErr( "Did not get expected results. Expected " + expected.length
						+ " references, got: " + result.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}

			getEntityTransaction().rollback();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("secondaryTablesValueTest failed");
	}

}
