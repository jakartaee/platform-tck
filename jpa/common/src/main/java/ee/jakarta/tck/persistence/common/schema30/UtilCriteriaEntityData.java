package ee.jakarta.tck.persistence.common.schema30;

import org.junit.jupiter.api.BeforeEach;



public abstract class UtilCriteriaEntityData extends Util {

	

	@BeforeEach
	public void setupCriteriaEntityData() throws Exception {
		logTrace( "setupAliasData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createCriteriaEntityData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupCriteriaEntityData failed:", e);
		}
	}

}
