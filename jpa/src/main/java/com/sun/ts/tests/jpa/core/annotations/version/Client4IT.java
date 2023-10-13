/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.version;

import java.sql.Timestamp;
import java.util.Date;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.core.annotations.tableGenerator.Client;

public class Client4IT extends Client {

	public Client4IT() {
	}

	public static JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "Int_Field", pkgName + "Int_Property", pkgName + "Integer_Field",
				pkgName + "Integer_Property", pkgName + "Long_Field", pkgName + "Long_Property",
				pkgName + "LongClass_Field", pkgName + "LongClass_Property", pkgName + "Short_Field",
				pkgName + "Short_Property", pkgName + "ShortClass_Field", pkgName + "ShortClass_Property",
				pkgName + "Timestamp_Field", pkgName + "Timestamp_Property" };
		return createDeploymentJar("jpa_core_annotations_version4.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeAll
	public void setupTimestampData() throws Exception {
		TestUtil.logTrace("setupTimestampData");
		try {
			super.setup();
			createDeployment();

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
	@Test
	public void timestampFieldTest() throws Exception {

		boolean pass = false;
		try {
			Timestamp_Field a = getEntityManager().find(Timestamp_Field.class, "1");
			if (a != null) {
				TestUtil.logTrace("version:" + a.getVersion());
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
					if (a1.getVersion().after(version)) {
						TestUtil.logTrace("version:" + a1.getVersion());
						pass = true;
					} else {
						TestUtil.logErr("Did not get a greater version after a modification:" + a1.getVersion());
					}
				} else {
					TestUtil.logErr("Second find returned null result");
				}
				/*
				 * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				TestUtil.logErr("Find returned null result");
			}
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
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
	@Test
	public void timestampPropertyTest() throws Exception {
		boolean pass = false;
		try {
			Timestamp_Property a = getEntityManager().find(Timestamp_Property.class, "2");
			if (a != null) {
				TestUtil.logTrace("version:" + a.getBasicTimestamp());
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
					if (a1.getBasicTimestamp().after(version)) {
						TestUtil.logTrace("version:" + a1.getBasicTimestamp());
						pass = true;
					} else {
						TestUtil.logErr("Did not get a greater version after a modification:" + a1.getBasicTimestamp());
					}
				} else {
					TestUtil.logErr("Second find returned null result");
				}
				/*
				 * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
				 */
			} else {
				TestUtil.logErr("Find returned null result");
			}
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("timestampPropertyTest failed");
		}

	}

	public void createTimestampTestData() {
		TestUtil.logTrace("createTimestampTestData");

		try {
			getEntityTransaction().begin();
			Timestamp currentTime = new Timestamp(new Date().getTime());
			getEntityManager().persist(new Timestamp_Field("1", currentTime));
			getEntityManager().persist(new Timestamp_Property("2", currentTime));
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Unexpected Exception in createTimestampTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception during Rollback:", re);
			}
		}

	}

}
