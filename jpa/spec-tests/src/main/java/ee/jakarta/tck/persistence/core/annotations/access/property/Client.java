package ee.jakarta.tck.persistence.core.annotations.access.property;



import org.junit.jupiter.api.AfterEach;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	protected DataTypes d1;

	protected DataTypes2 d2;

	final protected java.util.Date dateId = getPKDate(2006, 04, 15);



	@AfterEach
	public void cleanup() throws Exception {
		try {
		logTrace( "cleanup");
		removeTestData();
		logTrace( "cleanup complete, calling super.cleanup");
		super.cleanup();
		} finally {

        }
	}

	protected void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from DATATYPES").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DATATYPES2").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
