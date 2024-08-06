package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilProductData extends Util {



	@BeforeEach
	public void setupProductData() throws Exception {
		logTrace( "setupProductData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createProductData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupProductData failed:", e);
		}
	}

}
