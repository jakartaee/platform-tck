package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilPhoneData extends Util {



	@BeforeEach
	public void setupPhoneData() throws Exception {
		logTrace( "setupPhoneData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createPhoneData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
