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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
@Tag("tck-appclient")

public class UserBeginClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient {
    static final String VEHICLE_ARCHIVE = "begin_ejb_vehicle";

    private static String packagePath = UserBeginClientEjbTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserBeginClientEjbTest.class.getName());

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

    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        JavaArchive begin_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "begin_ejb_vehicle_client.jar");
        begin_ejb_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class, com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class, EETest.class,
                ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient.class,
                com.sun.ts.tests.jta.ee.common.Transact.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                UserBeginClientEjbTest.class);
        // The application-client.xml descriptor
        URL resURL = UserBeginClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            begin_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        resURL = UserBeginClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/begin_ejb_vehicle_client.jar.sun-application-client.xml");
        if (resURL != null) {
            begin_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }
        begin_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + UserBeginClientEjbTest.class.getName() + "\n"),
                "MANIFEST.MF");
        archiveProcessor.processClientArchive(begin_ejb_vehicle_client, UserBeginClientEjbTest.class, resURL);

        JavaArchive begin_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "begin_ejb_vehicle_ejb.jar");
        // The class files
        begin_ejb_vehicle_ejb.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, EETest.class,
                ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient.class,
                UserBeginClientEjbTest.class);
        // The ejb-jar.xml descriptor
        URL ejbResURL = UserBeginClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            begin_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = UserBeginClientEjbTest.class.getClassLoader().getResource(packagePath + "/begin_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            begin_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(begin_ejb_vehicle_ejb, UserBeginClientEjbTest.class, ejbResURL);

        EnterpriseArchive begin_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "begin_ejb_vehicle.ear");
        begin_ejb_vehicle_ear.addAsModule(begin_ejb_vehicle_ejb);
        begin_ejb_vehicle_ear.addAsModule(begin_ejb_vehicle_client);

        return begin_ejb_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserBegin001() throws java.lang.Exception {
        super.testUserBegin001();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserBegin002() throws java.lang.Exception {
        super.testUserBegin002();
    }

}