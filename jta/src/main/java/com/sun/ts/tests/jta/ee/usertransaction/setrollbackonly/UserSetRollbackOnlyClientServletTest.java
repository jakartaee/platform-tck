package com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly;

import com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class UserSetRollbackOnlyClientServletTest extends com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient {
    static final String VEHICLE_ARCHIVE = "setrollbackonly_servlet_vehicle";

    private static String packagePath = UserSetRollbackOnlyClientServletTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserSetRollbackOnlyClientServletTest.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }
  
    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    public void cleanup() {
      logger.log(Logger.Level.INFO, "cleanup ok");
    }

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        WebArchive setrollbackonly_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "setrollbackonly_servlet_vehicle_web.war");
        setrollbackonly_servlet_vehicle_web.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.tests.jta.ee.common.Transact.class,
        com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
        com.sun.ts.tests.jta.ee.common.InitFailedException.class,
        com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
        com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
        com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
        com.sun.ts.tests.jta.ee.common.InitFailedException.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        UserSetRollbackOnlyClientServletTest.class
        );
        // The web.xml descriptor
        URL warResURL = UserSetRollbackOnlyClientServletTest.class.getClassLoader().getResource(packagePath+"/servlet_vehicle_web.xml");
        if(warResURL != null) {
            setrollbackonly_servlet_vehicle_web.setWebXML(warResURL);
        }
        // The sun-web.xml descriptor
        warResURL = UserSetRollbackOnlyClientServletTest.class.getClassLoader().getResource(packagePath+"/setrollbackonly_servlet_vehicle_web.war.sun-web.xml");
        if(warResURL != null) {
            setrollbackonly_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive(setrollbackonly_servlet_vehicle_web, UserSetRollbackOnlyClientServletTest.class, warResURL);

    // Ear
        EnterpriseArchive setrollbackonly_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "setrollbackonly_servlet_vehicle.ear");
        setrollbackonly_servlet_vehicle_ear.addAsModule(setrollbackonly_servlet_vehicle_web);
        return setrollbackonly_servlet_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetRollbackOnly001() throws java.lang.Exception {
        super.testUserSetRollbackOnly001();
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetRollbackOnly002() throws java.lang.Exception {
        super.testUserSetRollbackOnly002();
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetRollbackOnly003() throws java.lang.Exception {
        super.testUserSetRollbackOnly003();
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetRollbackOnly004() throws java.lang.Exception {
        super.testUserSetRollbackOnly004();
    }


}