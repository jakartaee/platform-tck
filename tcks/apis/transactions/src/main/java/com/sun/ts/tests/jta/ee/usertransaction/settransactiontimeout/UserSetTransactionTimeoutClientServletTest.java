package com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout;

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

public class UserSetTransactionTimeoutClientServletTest
        extends com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient {
    static final String VEHICLE_ARCHIVE = "settransactiontimeout_servlet_vehicle";

    private static String packagePath = UserSetTransactionTimeoutClientServletTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserSetTransactionTimeoutClientServletTest.class.getName());

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
        WebArchive settransactiontimeout_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class,
                "settransactiontimeout_servlet_vehicle_web.war");
        // The class files
        settransactiontimeout_servlet_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                EETest.class, ServiceEETest.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, UserSetTransactionTimeoutClientServletTest.class);
        // The web.xml descriptor
        URL warResURL = UserSetTransactionTimeoutClientServletTest.class.getClassLoader()
                .getResource(packagePath + "/servlet_vehicle_web.xml");
        if (warResURL != null) {
            settransactiontimeout_servlet_vehicle_web.setWebXML(warResURL);
        }
        warResURL = UserSetTransactionTimeoutClientServletTest.class.getClassLoader()
                .getResource(packagePath + "/settransactiontimeout_servlet_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            settransactiontimeout_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive(settransactiontimeout_servlet_vehicle_web, UserSetTransactionTimeoutClientServletTest.class,
                warResURL);

        return settransactiontimeout_servlet_vehicle_web;
        // EnterpriseArchive settransactiontimeout_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class,
        // "settransactiontimeout_servlet_vehicle.ear");
        // settransactiontimeout_servlet_vehicle_ear.addAsModule(settransactiontimeout_servlet_vehicle_web);
        // return settransactiontimeout_servlet_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetTransactionTimeout001() throws java.lang.Exception {
        super.testUserSetTransactionTimeout001();
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void testUserSetTransactionTimeout002() throws java.lang.Exception {
        super.testUserSetTransactionTimeout002();
    }

}