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

package ee.jakarta.tck.persistence.core.annotations.tableGenerator;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;




public class Client2 extends Client {

	private DataTypes2 d2;



	public Client2() {
	}

	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup2");
		try {

			super.setup(args,p);
			removeTestData();
			createTestData2();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorOnPropertyTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2111; PERSISTENCE:SPEC:2111.3;
	 * PERSISTENCE:SPEC:2113;
	 * 
	 * @test_Strategy: use a generator specified on a property
	 */
		public void generatorOnPropertyTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = d2.getId();
			logTrace( "find id: " + id);
			DataTypes2 d = getEntityManager().find(DataTypes2.class, id);
			if (d != null) {
				if (d.getStringData().equals(d2.getStringData())) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				logErr( "EntityManager.find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("generatorOnPropertyTest failed");
	}

	// Methods used for Tests

	public void createTestData2() {
		try {
			getEntityTransaction().begin();
			d2 = new DataTypes2();
			d2.setStringData("testData2");
			logTrace( "DataType2:" + d2.toString());
			getEntityManager().persist(d2);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
	}

}
