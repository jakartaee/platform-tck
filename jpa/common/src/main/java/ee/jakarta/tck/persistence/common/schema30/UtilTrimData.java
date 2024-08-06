package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilTrimData extends Util {



	@BeforeEach
	public void setupTrimData() throws Exception {
		logTrace( "setupTrimData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTrimData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupTrimData failed:", e);
		}
	}

}
