package ee.jakarta.tck.persistence.core.annotations.access.field;



import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client3 extends Client {



	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = Client3.class.getPackageName() + ".";
		String[] classes = { pkgName + "DataTypes", pkgName + "DataTypes2",
				"ee.jakarta.tck.persistence.core.types.common.Grade" };
		return createDeploymentJar("jpa_core_annotations_access_field3.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setup3() throws Exception {
		logTrace( "setup3");
		try {

			super.setup();
			createDeployment();
			removeTestData();
			createTestData3();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			throw new Exception("Setup failed:", e);
		}
	}

	public void createTestData3() {
		logTrace( "createTestData3");

		try {
			getEntityTransaction().begin();
			d1 = new DataTypes(1, (byte) 5);

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
	 * @testName: transientTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1327.2
	 * 
	 * @test_Strategy: when transient is specified, verify data isn't persisted
	 */
	@Test
	public void transientTest() throws Exception {

		boolean pass = false;
		byte newByte = (byte) 111;

		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = getEntityManager().find(DataTypes.class, 1);
			if (null != d1) {
				if ((d1.getTransient() == (byte) 0)) {
					logTrace( "First find returned expected result:" + d1.getTransient());
					d1.setTransient(newByte);

					getEntityManager().merge(d1);
					getEntityManager().flush();
					clearCache();
					d1 = null;
					d1 = getEntityManager().find(DataTypes.class, 1);

					if (d1.getTransient() == (byte) 0) {
						pass = true;
						logTrace( "Second find returned expected value:" + d1.getTransient());
					} else {
						logErr( "Second find expected:0, actual:" + d1.getTransient());
					}
				} else {
					logErr( "Expected first find to return:0, actual:" + d1.getTransient());
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
			throw new Exception("transientTest failed");
	}

}
