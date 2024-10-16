package com.sun.ts.tests.jta.ee.usertransaction.getstatus;

import com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient;
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

public class UserGetStatusClientJspTest extends com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient {
    static final String VEHICLE_ARCHIVE = "getstatus_jsp_vehicle";

    private static String packagePath = UserGetStatusClientJspTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserGetStatusClientJspTest.class.getName());

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
        WebArchive getstatus_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "getstatus_jsp_vehicle_web.war");
        getstatus_jsp_vehicle_web.addClasses(
        com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.tests.jta.ee.common.Transact.class,
        com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
        com.sun.ts.tests.jta.ee.common.InitFailedException.class,
        com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
        com.sun.ts.tests.jta.ee.common.InitFailedException.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        UserGetStatusClientJspTest.class
        );
        // The web.xml descriptor
        URL warResURL = UserGetStatusClientJspTest.class.getClassLoader().getResource(packagePath+"/jsp_vehicle_web.xml");
        if(warResURL != null) {
            getstatus_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // Web content
        warResURL = UserGetStatusClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        getstatus_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
        warResURL = UserGetStatusClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        getstatus_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

        // The sun-web.xml descriptor
        warResURL = UserGetStatusClientJspTest.class.getClassLoader().getResource(packagePath+"/getstatus_jsp_vehicle_web.war.sun-web.xml");
        if(warResURL != null) {
            getstatus_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive(getstatus_jsp_vehicle_web, UserGetStatusClientJspTest.class, warResURL);


        EnterpriseArchive getstatus_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "getstatus_jsp_vehicle.ear");
        getstatus_jsp_vehicle_ear.addAsModule(getstatus_jsp_vehicle_web);
        return getstatus_jsp_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserGetStatus001() throws java.lang.Exception {
        super.testUserGetStatus001();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserGetStatus002() throws java.lang.Exception {
        super.testUserGetStatus002();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserGetStatus003() throws java.lang.Exception {
        super.testUserGetStatus003();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserGetStatus004() throws java.lang.Exception {
        super.testUserGetStatus004();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserGetStatus005() throws java.lang.Exception {
        super.testUserGetStatus005();
    }


}