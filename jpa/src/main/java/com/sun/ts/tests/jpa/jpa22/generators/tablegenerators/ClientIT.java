/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.jpa22.generators.tablegenerators;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class ClientIT extends PMClientBase {
	private static final long serialVersionUID = 22L;

	private DataTypes d0;

	public ClientIT() {
	}

	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes" };
		return createDeploymentJar("jpa_jpa22_generators_tablegenerators.jar", pkgNameWithoutSuffix,
				(String[]) classes);

	}

	@BeforeAll
	public void setup() throws Exception {
		TestUtil.logTrace("setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: generatorOnEntityTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3489;
	 * 
	 * @test_Strategy: use a generator specified on an entity
	 */
	@Test
	public void generatorOnEntityTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = d0.getId();
			TestUtil.logTrace("find id: " + id);
			DataTypes d = getEntityManager().find(DataTypes.class, id);
			if (d != null) {
				if (d.getStringData().equals(d0.getStringData())) {
					pass = true;
				}

				getEntityTransaction().commit();
			} else {
				TestUtil.logErr("EntityManager.find returned null result");
			}
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
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
			TestUtil.logTrace("DataType:" + d0.toString());
			getEntityManager().persist(d0);

			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
		}
	}

	@AfterAll
	public void cleanup() throws Exception {
		TestUtil.logTrace("Cleanup data");
		removeTestData();
		TestUtil.logTrace("cleanup complete, calling super.cleanup");
		super.cleanup();
	}

	private void removeTestData() {
		TestUtil.logTrace("removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
