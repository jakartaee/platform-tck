/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.version;


import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;

public class Client4 extends Client {



	public Client4() {
	}
	public static void main(String[] args) {
		Client4 theTests = new Client4();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setupTimestampData");
		try {
			super.setup(args,p);
	
			removeTestData();
			createTimestampTestData();

		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: timestampFieldTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.7
	 *
	 * @test_Strategy:
	 */
	
	public void timestampFieldTest() throws Exception {

		boolean pass = false;
		try {
			Timestamp_Field a = getEntityManager().find(Timestamp_Field.class, "1");
			if (a != null) {
				logTrace( "version:" + a.getVersion());
				// if (a.getVersion() == 1) {
				Timestamp version = a.getVersion();
				a.setName("two");
				// Sleep for 1 second
				try {
					Thread.sleep(1 * 1000L);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Timestamp_Field a1 = getEntityManager().find(Timestamp_Field.class, "1");
				if (a1 != null) {
					if (a1.getVersion() == null) {
						logErr("version is null for updated entity");
					} else if (version == null || a1.getVersion().after(version)) {
						logTrace( "version:" + a1.getVersion());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getVersion());
					}
				} else {
					logErr( "Second find returned null result");
				}
				/*
				 * } else {
				 * logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("timestampFieldTest failed");
		}

	}

	/*
	 * @testName: timestampPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.7
	 *
	 * @test_Strategy:
	 */
	
	public void timestampPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Timestamp_Property a = getEntityManager().find(Timestamp_Property.class, "2");
			if (a != null) {
				logTrace( "version:" + a.getBasicTimestamp());
				// if (a.getVersion() == 1) {
				Timestamp version = a.getBasicTimestamp();
				a.setName("two");
				// Sleep for 1 second
				try {
					Thread.sleep(1 * 1000L);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				getEntityTransaction().begin();
				getEntityManager().merge(a);
				getEntityManager().flush();
				getEntityTransaction().commit();
				Timestamp_Property a1 = getEntityManager().find(Timestamp_Property.class, "2");
				if (a1 != null) {
					if (a1.getBasicTimestamp() == null) {
						logErr("version is null for updated entity");
					} else if (version == null || a1.getBasicTimestamp().after(version)) {
						logTrace( "version:" + a1.getBasicTimestamp());
						pass = true;
					} else {
						logErr(
								"Did not get a greater version after a modification:" + a1.getBasicTimestamp());
					}
				} else {
					logErr( "Second find returned null result");
				}
				/*
				 * } else {
				 * logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				logErr( "Find returned null result");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("timestampPropertyTest failed");
		}

	}

	public void createTimestampTestData() {
		logTrace( "createTimestampTestData");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(new Timestamp_Field("1"));
			getEntityManager().persist(new Timestamp_Property("2"));
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected Exception in createTimestampTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

	}

}
