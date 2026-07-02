package com.sun.ts.tests.jta.ee.usertransaction.setreadonly;

import java.lang.System.Logger;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class UserSetReadOnlyClientJspTest
        extends UserSetReadOnlyClient {
    static final String VEHICLE_ARCHIVE = "setreadonly_jsp_vehicle";

    private static String packagePath = UserSetReadOnlyClientJspTest.class.getPackageName().replace( ".", "/");

    private static final Logger logger = System.getLogger( UserSetReadOnlyClientJspTest.class.getName());

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
        WebArchive setreadonly_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "setreadonly_jsp_vehicle_web.war");
        setreadonly_jsp_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                UserSetReadOnlyClient.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                SetupException.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                UserSetReadOnlyClientJspTest.class);
        // The web.xml descriptor
        URL warResURL = UserSetReadOnlyClientJspTest.class.getClassLoader().getResource( packagePath + "/jsp_vehicle_web.xml");
        if (warResURL != null) {
            setreadonly_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }
        warResURL = UserSetReadOnlyClientJspTest.class.getResource( "/vehicle/jsp/contentRoot/client.html");
        setreadonly_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
        warResURL = UserSetReadOnlyClientJspTest.class.getResource( "/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        setreadonly_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

        warResURL = UserSetReadOnlyClientJspTest.class.getClassLoader()
                .getResource(packagePath + "/setreadonly_jsp_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            setreadonly_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive( setreadonly_jsp_vehicle_web, UserSetReadOnlyClientJspTest.class, warResURL);

        return setreadonly_jsp_vehicle_web;
        // EnterpriseArchive setreadonly_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class,
        // "setreadonly_jsp_vehicle.ear");
        // setreadonly_jsp_vehicle_ear.addAsModule(setreadonly_jsp_vehicle_web);
        // return setreadonly_jsp_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserSetReadOnly001() throws Exception {
        super.testUserSetReadOnly001();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserSetReadOnly002() throws Exception {
        super.testUserSetReadOnly002();
    }

}