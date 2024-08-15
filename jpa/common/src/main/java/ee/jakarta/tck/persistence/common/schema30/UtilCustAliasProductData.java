package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilCustAliasProductData extends Util {



	@BeforeEach
	public void setupCustAliasProductData() throws Exception {
		logTrace( "setupCustAliasProductData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createProductData();
			createAliasData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupCustAliasProductData failed:", e);
		}
	}

}
