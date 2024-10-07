package ee.jakarta.tck.persistence.core.annotations.access.property;

import java.util.Properties;
import com.sun.ts.lib.harness.Status;

public class Client2 extends Client {
	
	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	  }

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			removeTestData();
			createTestData2();
			logTrace( "Done creating test data");

		} catch (Exception e) {
			logErr( "Exception: ", e);
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

	public void transientTest() throws Exception {

		boolean pass = false;
		final String newString = "NEW_VALUE";
		try {
			getEntityTransaction().begin();
			clearCache();
			d1 = null;
			d1 = getEntityManager().find(DataTypes.class, 1);
			logMsg( "d1.toString():" + d1);

			if (null != d1) {
				if (d1.getShouldNotPersist() == null) {
					logTrace( "Int value after find=" + d1.getShouldNotPersist());
					d1.setShouldNotPersist(newString);
					logTrace( "Int value after set=" + d1.getShouldNotPersist());
					getEntityManager().merge(d1);
					getEntityManager().flush();
					clearCache();
					d1 = null;
					d1 = getEntityManager().find(DataTypes.class, 1);

					if (d1.getShouldNotPersist() == null) {
						pass = true;
						logTrace( "Received expected null value");
					} else {
						logErr( "Expected:null, actual:" + d1.getShouldNotPersist());
					}
					getEntityTransaction().commit();

				} else {
					logErr( "getShouldNotPersist() returned null");
				}
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

	public void createTestData2() {
		logTrace( "createTestData2");

		try {
			getEntityTransaction().begin();

			d1 = new DataTypes(1, "INITIAL_VALUE");

			getEntityManager().persist(d1);

			getEntityManager().flush();

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

}
