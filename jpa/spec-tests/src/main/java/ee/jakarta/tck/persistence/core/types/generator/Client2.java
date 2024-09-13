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

public class Client2 extends Client {
	
	private DataTypes2 d10;

	public Client2() {
	}
	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @class.setup_props: db.supports.sequence;
	 */
	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setupDataTypes2");
		try {

			super.setup(args,p);
			String s = System.getProperty("db.supports.sequence");
			if (s != null) {
				supports_sequence = Boolean.parseBoolean(s);
				logMsg( "db.supports.sequence:" + supports_sequence);
				if (supports_sequence) {
					createSequenceGenerator();
					removeTestData();
					createDataTypes2Data();
				}
			} else {
				logErr(
						"The property db.supports.sequence is not defined in the ts.jte, this must be corrected before running tests");
				throw new Exception("setupDataTypes2 failed");

			}

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupDataTypes2 failed:", e);
		}
	}

	/*
	 * @testName: generatorTypeSequenceTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:194; PERSISTENCE:JAVADOC:195;
	 * PERSISTENCE:JAVADOC:196; PERSISTENCE:SPEC:2107.2; PERSISTENCE:SPEC:2106;
	 * 
	 * @test_Strategy: The GeneratedValue annotation provides for the specification
	 * of generation strategies for the values of primary keys.
	 * GenerationType.SEQUENCE, indicates the persistence provider must assign
	 * primary keys for the entity using an underlying database sequence generator
	 * to ensure uniqueness.
	 *
	 * Using GenerationType.SEQUENCE, access a persisted entity and modify its'
	 * data.
	 */
	
	public void generatorTypeSequenceTest() throws Exception {

		boolean pass = true;
		if (supports_sequence) {
			final Float newFloat = 3.0F;

			try {
				getEntityTransaction().begin();
				int id = d10.getId();
				logTrace( "Doing a find of id: " + id);
				DataTypes2 d = getEntityManager().find(DataTypes2.class, id);

				if (null != d) {
					Float f = d.getFloatData();
					if (f.equals(d10.getFloatData())) {
						logTrace( "find returned correct float value:" + f);
						d.setFloatData(newFloat);
					} else {
						logErr(
								"find did not return correct float value, expected: 1.0, actual:" + f);
						pass = false;
					}

					getEntityManager().merge(d);
					getEntityManager().flush();
					f = d.getFloatData();
					if (f.equals(newFloat)) {
						logTrace( "Successfully set float value to:" + newFloat);
					} else {
						logErr(
								"Could not update float value, expected: " + newFloat + ", actual:" + f);
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
			throw new Exception("generatorTypeSequenceTest failed");

	}

	public void createDataTypes2Data() {
		try {

			getEntityTransaction().begin();

			logTrace( "in createDataTypes2Data");

			logTrace( "new DataType2");
			d10 = new DataTypes2('a', (short) 100, 500, 300L, 50D, 1.0F);
			logTrace( "Persist DataType2");
			getEntityManager().persist(d10);
			logTrace( "DataType2 id:" + d10.getId());

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
	}

}
