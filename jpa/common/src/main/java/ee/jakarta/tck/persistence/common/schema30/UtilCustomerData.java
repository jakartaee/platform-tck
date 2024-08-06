package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilCustomerData extends Util {



	@BeforeEach
	public void setupCustomerData() throws Exception {
		logTrace( "setupCustomerData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
