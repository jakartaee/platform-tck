package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilAliasOnlyData extends Util {



	@BeforeEach
	public void setupAliasOnlyData() throws Exception {
		logTrace( "setupAliasOnlyData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createAliasOnlyData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupCustomerData failed:", e);
		}
	}

}
