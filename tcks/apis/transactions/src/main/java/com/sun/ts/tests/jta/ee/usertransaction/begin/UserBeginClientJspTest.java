package com.sun.ts.tests.jta.ee.usertransaction.begin;

import java.lang.System.Logger;
import java.net.URL;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class UserBeginClientJspTest extends com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient {
    static final String VEHICLE_ARCHIVE = "begin_jsp_vehicle";

    private static String packagePath = UserBeginClientJspTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserBeginClientJspTest.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @Override
    @AfterEach
    public void cleanup() {
        logger.log(Logger.Level.INFO, "cleanup ok");
    }

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        WebArchive begin_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "begin_jsp_vehicle_web.war");
        begin_jsp_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, Fault.class,
                EETest.class, ServiceEETest.class,
                SetupException.class, UserBeginClientJspTest.class);
        // The web.xml descriptor
        URL warResURL = UserBeginClientJspTest.class.getClassLoader().getResource(packagePath + "/jsp_vehicle_web.xml");
        if (warResURL != null) {
            begin_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }
        // Web content
        warResURL = UserBeginClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        begin_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
        warResURL = UserBeginClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        begin_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

        warResURL = UserBeginClientJspTest.class.getClassLoader().getResource(packagePath + "/begin_jsp_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            begin_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive(begin_jsp_vehicle_web, UserBeginClientJspTest.class, warResURL);

        return begin_jsp_vehicle_web;
        // EnterpriseArchive begin_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "begin_jsp_vehicle.ear");
        // begin_jsp_vehicle_ear.addAsModule(begin_jsp_vehicle_web);
        // return begin_jsp_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserBegin001() throws java.lang.Exception {
        super.testUserBegin001();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserBegin002() throws java.lang.Exception {
        super.testUserBegin002();
    }

}