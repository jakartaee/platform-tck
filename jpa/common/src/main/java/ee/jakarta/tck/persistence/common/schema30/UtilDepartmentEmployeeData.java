package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilDepartmentEmployeeData extends Util {



	@BeforeEach
	public void setupDepartmentEmployeeData() throws Exception {
		logTrace( "setupDepartmentEmployeeData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createDepartmentEmployeeData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("setupDepartmentEmployeeData failed:", e);
		}
	}

}
