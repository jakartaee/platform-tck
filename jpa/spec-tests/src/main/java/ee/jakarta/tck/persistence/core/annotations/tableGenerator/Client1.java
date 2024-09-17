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




public class Client1 extends Client {

	private DataTypes d0;



	public Client1() {
	}

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			
			removeTestData();
			createTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorOnEntityTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2111; PERSISTENCE:SPEC:2111.1;
	 * PERSISTENCE:SPEC:2113;
	 * 
	 * @test_Strategy: use a generator specified on an entity
	 */
		public void generatorOnEntityTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = d0.getId();
			logTrace( "find id: " + id);
			DataTypes d = getEntityManager().find(DataTypes.class, id);
			if (d != null) {
				if (d.getStringData().equals(d0.getStringData())) {
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
			throw new Exception("generatorOnEntityTest failed");
	}

	// Methods used for Tests

	public void createTestData() {
		try {
			getEntityTransaction().begin();

			d0 = new DataTypes();
			d0.setStringData("testData");
			logTrace( "DataType:" + d0.toString());
			getEntityManager().persist(d0);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
	}

}
