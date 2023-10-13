package com.sun.ts.tests.jpa.core.annotations.access.property;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;


public class Client2IT extends Client {

	public static JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = Client.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"com.sun.ts.tests.jpa.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_property2.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeAll
	public void setup2() throws Exception {
		TestUtil.logTrace("setup2");
		try {

			super.setup();
			createDeployment();

			removeTestData();
			createTestData2();
			TestUtil.logTrace("Done creating test data");

		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: transientTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1327.9
	 * 
	 * @test_Strategy: when transient is specified, verify data isn't persisted
	 */
	@Test
	public void transientTest() throws Exception {

		boolean pass = false;
		final String newString = "NEW_VALUE";
		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);
			TestUtil.logMsg("d1.toString():" + d1.toString());

			if (null != d1) {
				if (d1.getShouldNotPersist() == null) {
					TestUtil.logTrace("Int value after find=" + d1.getShouldNotPersist());
					d1.setShouldNotPersist(newString);
					TestUtil.logTrace("Int value after set=" + d1.getShouldNotPersist());
					getEntityManager().merge(d1);
					getEntityManager().flush();
					clearCache();
					d1 = null;
					d1 = getEntityManager().find(DataTypes.class, 1);

					if (d1.getShouldNotPersist() == null) {
						pass = true;
						TestUtil.logTrace("Received expected null value");
					} else {
						TestUtil.logErr("Expected:null, actual:" + d1.getShouldNotPersist());
					}
					getEntityTransaction().commit();

				} else {
					TestUtil.logErr("getShouldNotPersist() returned null");
				}
			} else {
				TestUtil.logErr("find returned null");
			}
		} catch (Exception e) {
			TestUtil.logErr("Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("transientTest failed");
	}

	public void createTestData2() {
		TestUtil.logTrace("createTestData2");

		try {
			getEntityTransaction().begin();

			d1 = new DataTypes(1, "INITIAL_VALUE");

			getEntityManager().persist(d1);

			getEntityManager().flush();

			getEntityTransaction().commit();

		} catch (Exception e) {
			TestUtil.logErr("Unexpected Exception in createTestData:", e);
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
