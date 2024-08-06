package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilOrderData extends Util {

	

	@BeforeEach
	public void setupOrderData() throws Exception {
		logTrace( "setupOrderData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createProductData();
			createOrderData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
