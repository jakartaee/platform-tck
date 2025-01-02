/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.query.language;


import java.util.List;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;



import com.sun.ts.lib.harness.SetupMethod;

public class Client5 extends Util {

	public static void main(String[] args) {
		Client5 theTests = new Client5();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	/* Run test */

	/*
	 * @testName: queryTest55
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358
	 * 
	 * @test_Strategy: The LIKE expression uses an input parameter for the
	 * condition. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupPhoneData")
		public void queryTest55() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logTrace( "determine which customers have an area code beginning with 9");
			c = getEntityManager()
					.createQuery(
							"SELECT Distinct Object(c) From Customer c, IN(c.home.phones) p where p.area LIKE :area")
					.setParameter("area", "9%").getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "3";
			expectedPKs[1] = "12";
			expectedPKs[2] = "16";

			if (!checkEntityPK(c, expectedPKs)) {
				logErr(
						"Did not get expected results.  Expected 3 references, got: " + c.size());
			} else {
				logTrace( "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest55 failed");
	}

}
