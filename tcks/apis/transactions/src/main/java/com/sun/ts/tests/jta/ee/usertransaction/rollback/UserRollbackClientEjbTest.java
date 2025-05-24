package com.sun.ts.tests.jta.ee.usertransaction.rollback;

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

public class UserRollbackClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.rollback.UserRollbackClient {
    static final String VEHICLE_ARCHIVE = "rollback_ejb_vehicle";

    private static String packagePath = UserRollbackClientEjbTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserRollbackClientEjbTest.class.getName());

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
        // Client
        // the jar with the correct archive name
        JavaArchive rollback_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "rollback_ejb_vehicle_client.jar");
        // The class files
        rollback_ejb_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class, com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class, EETest.class,
                ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, UserRollbackClient.class, UserRollbackClientEjbTest.class);
        // The application-client.xml descriptor
        URL resURL = UserRollbackClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            rollback_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        resURL = UserRollbackClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/rollback_ejb_vehicle_client.jar.sun-application-client.xml");
        if (resURL != null) {
            rollback_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }
        rollback_ejb_vehicle_client
                .addAsManifestResource(new StringAsset("Main-Class: " + UserRollbackClientEjbTest.class.getName() + "\n"), "MANIFEST.MF");
        archiveProcessor.processClientArchive(rollback_ejb_vehicle_client, UserRollbackClientEjbTest.class, resURL);

        // Ejb
        // the jar with the correct archive name
        JavaArchive rollback_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "rollback_ejb_vehicle_ejb.jar");
        // The class files
        rollback_ejb_vehicle_ejb.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jta.ee.usertransaction.rollback.UserRollbackClient.class, EETest.class,
                ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, UserRollbackClientEjbTest.class);
        // The ejb-jar.xml descriptor
        URL ejbResURL = UserRollbackClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            rollback_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = UserRollbackClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/rollback_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            rollback_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(rollback_ejb_vehicle_ejb, UserRollbackClientEjbTest.class, ejbResURL);

        // Ear
        EnterpriseArchive rollback_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "rollback_ejb_vehicle.ear");
        rollback_ejb_vehicle_ear.addAsModule(rollback_ejb_vehicle_ejb);
        rollback_ejb_vehicle_ear.addAsModule(rollback_ejb_vehicle_client);
        return rollback_ejb_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserRollback001() throws java.lang.Exception {
        super.testUserRollback001();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserRollback002() throws java.lang.Exception {
        super.testUserRollback002();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserRollback003() throws java.lang.Exception {
        super.testUserRollback003();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserRollback004() throws java.lang.Exception {
        super.testUserRollback004();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserRollback005() throws java.lang.Exception {
        super.testUserRollback005();
    }

}