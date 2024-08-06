package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilAliasData extends Util {



	@BeforeEach
	public void setupAliasData() throws Exception {
		logTrace( "setupAliasData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCustomerData();
			createAliasData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupAliasData failed:", e);
		}
	}

}
