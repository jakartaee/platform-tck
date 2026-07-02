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

public class UserSetReadOnlyClientServletTest
        extends UserSetReadOnlyClient {
    static final String VEHICLE_ARCHIVE = "setreadonly_servlet_vehicle";

    private static String packagePath = UserSetReadOnlyClientServletTest.class.getPackageName().replace( ".", "/");

    private static final Logger logger = System.getLogger( UserSetReadOnlyClientServletTest.class.getName());

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
        // War
        // the war with the correct archive name
        WebArchive setreadonly_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class,
                "setreadonly_servlet_vehicle_web.war");
        // The class files
        setreadonly_servlet_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                UserSetReadOnlyClient.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.lib.harness.EETest.class, com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, UserSetReadOnlyClientServletTest.class);
        // The web.xml descriptor
        URL warResURL = UserSetReadOnlyClientServletTest.class.getClassLoader()
                .getResource(packagePath + "/servlet_vehicle_web.xml");
        if (warResURL != null) {
            setreadonly_servlet_vehicle_web.setWebXML(warResURL);
        }
        warResURL = UserSetReadOnlyClientServletTest.class.getClassLoader()
                .getResource(packagePath + "/setreadonly_servlet_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            setreadonly_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive(setreadonly_servlet_vehicle_web, UserSetReadOnlyClientServletTest.class,
                warResURL);

        return setreadonly_servlet_vehicle_web;
        // EnterpriseArchive setreadonly_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class,
        // "setreadonly_servlet_vehicle.ear");
        // setreadonly_servlet_vehicle_ear.addAsModule(setreadonly_servlet_vehicle_web);
        // return setreadonly_servlet_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetReadOnly001() throws Exception {
        super.testUserSetReadOnly001();
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetReadOnly002() throws Exception {
        super.testUserSetReadOnly002();
    }

}