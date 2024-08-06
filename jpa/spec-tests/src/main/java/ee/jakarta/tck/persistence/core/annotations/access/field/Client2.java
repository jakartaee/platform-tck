package ee.jakarta.tck.persistence.core.annotations.access.field;



import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client2 extends Client {



	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = Client2.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_field2.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setup2() throws Exception {
		logTrace( "setup2");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData2();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	public void createTestData2() {
		logTrace( "createTestData2");

		try {
			getEntityTransaction().begin();
			d1 = new DataTypes(1, 300);

			getEntityManager().persist(d1);
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected Exception in createTestData:", e);
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

	/*
	 * @testName: mixedAccessTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1327.3;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void mixedAccessTest() throws Exception {

		boolean pass = false;
		final int newInt = 500;

		try {
			getEntityTransaction().begin();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if ((null != d1) && (d1.getIntData2() == 300)) {
				logMsg( "Int value after find=" + d1.getIntData2());
				d1.setIntData2(newInt);
				logMsg( "Int value after set=" + d1.getIntData2());
				getEntityManager().merge(d1);
				getEntityManager().flush();
				clearCache();
				d1 = null;
				d1 = getEntityManager().find(DataTypes.class, 1);

				if (d1.getIntData2() == newInt) {
					pass = true;
					logTrace( "Received expected value:" + d1.getIntData2());
				} else {
					logErr( "Expected:" + newInt + ", actual:" + d1.getIntData2());
				}
				getEntityTransaction().commit();
			} else {
				logErr( "find returned null");
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception during Rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("mixedAccessTest failed");
	}

}
