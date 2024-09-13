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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.core.types.generator;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;

public class Client4 extends Client {



	private DataTypes4 d12;

	private boolean supports_sequence = false;

	public Client4() {
	}
	public static void main(String[] args) {
		Client4 theTests = new Client4();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @class.setup_props: db.supports.sequence;
	 */
	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			String s = System.getProperty("db.supports.sequence");
			if (s != null) {
				supports_sequence = Boolean.parseBoolean(s);
				logMsg( "db.supports.sequence:" + supports_sequence);
				if (supports_sequence) {
					createSequenceGenerator();
					removeTestData();
					createDataTypes4Data();
				}
			} else {
				logErr(
						"The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
				throw new Exception("setupDataTypes4 failed");

			}

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupDataTypes4 failed:", e);
		}
	}

	/*
	 * @testName: sequenceGeneratorOnPropertyTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2107; PERSISTENCE:SPEC:2107.3;
	 * 
	 * @test_Strategy: A sequence generator may be specified on the primary key
	 * property
	 */
	
	public void sequenceGeneratorOnPropertyTest() throws Exception {

		boolean pass = true;
		if (supports_sequence) {
			final Integer newInt = 1000;

			try {
				getEntityTransaction().begin();
				clearCache();
				logMsg( "Doing a find of id: " + d12.getId());
				DataTypes4 d = getEntityManager().find(DataTypes4.class, d12.getId());

				if (d != null) {
					Integer i = d.getIntegerData();
					if (i.equals(d12.getIntegerData())) {
						logTrace( "find returned correct Integer value:" + i);
						d.setIntegerData(newInt);
					} else {
						logErr( "find did not return correct Integer value, expected: "
								+ d12.getIntegerData() + ", actual:" + i);
						pass = false;
					}

					getEntityManager().merge(d);
					getEntityManager().flush();
					clearCache();
					logMsg( "Doing a find of merged data for id: " + d.getId());
					DataTypes4 d2 = getEntityManager().find(DataTypes4.class, d.getId());
					i = d2.getIntegerData();
					if (i.equals(d2.getIntegerData())) {
						logTrace( "find returned correct merged Integer value:" + i);
					} else {
						logErr( "find did not return correct Integer value, expected: "
								+ d.getIntegerData() + ", actual:" + i);
						pass = false;
					}

					getEntityTransaction().commit();
				} else {
					logErr( "find returned null result");
					pass = false;
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
				pass = false;
			}
		} else {
			logMsg( "WARNING: Test not run because db.supports.sequence set to false in ts.jte");
		}
		if (!pass)
			throw new Exception("sequenceGeneratorOnEntityTest failed");

	}

	// Methods used for Tests

	public void createDataTypes4Data() {
		try {
			getEntityTransaction().begin();

			logTrace( "in createDataTypes4Data");

			logTrace( "new DataType4");
			d12 = new DataTypes4(500);
			logTrace( "Persist DataType4");
			getEntityManager().persist(d12);
			logTrace( "DataType4 id:" + d12.getId());

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
	}

}
