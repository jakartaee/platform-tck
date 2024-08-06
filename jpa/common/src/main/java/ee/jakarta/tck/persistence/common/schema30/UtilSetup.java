package ee.jakarta.tck.persistence.common.schema30;



import org.junit.jupiter.api.BeforeEach;

public abstract class UtilSetup extends Util {



	@BeforeEach
	public void setup() throws Exception {
		logTrace( "setup");
		try {
			super.setup();
			createDeployment();
			getEntityManager();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

}
