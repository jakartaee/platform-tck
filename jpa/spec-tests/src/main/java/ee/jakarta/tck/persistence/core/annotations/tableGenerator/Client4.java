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

public class Client4 extends Client {

	private DataTypes4 d4;

	

	public Client4() {
	}


	
	public void setup4(String[] args, Properties p) throws Exception {
		logTrace( "setup4");
		try {

			super.setup(args,p);
			removeTestData();
			createTestData4();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorGlobalTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2112; PERSISTENCE:SPEC:2113;
	 * 
	 * @test_Strategy: Use the generator defined by another entity
	 */
	public void generatorGlobalTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = d4.getId();
			logTrace( "find id: " + id);
			DataTypes4 d = getEntityManager().find(DataTypes4.class, id);
			if (d != null) {
				if (d.getStringData().equals(d4.getStringData())) {
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
			throw new Exception("generatorGlobalTest failed");
	}

	// Methods used for Tests

	public void createTestData4() {
		try {
			getEntityTransaction().begin();

			d4 = new DataTypes4();
			d4.setStringData("testData4");
			logTrace( "DataType4:" + d4.toString());
			getEntityManager().persist(d4);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
	}

}
