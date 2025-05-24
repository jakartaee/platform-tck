package com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly;

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

public class UserSetRollbackOnlyClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient {
    static final String VEHICLE_ARCHIVE = "setrollbackonly_ejb_vehicle";

    private static String packagePath = UserSetRollbackOnlyClientEjbTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserSetRollbackOnlyClientEjbTest.class.getName());

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
        JavaArchive setrollbackonly_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "setrollbackonly_ejb_vehicle_client.jar");
        setrollbackonly_ejb_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class, com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class, EETest.class,
                ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, UserSetRollbackOnlyClient.class,
                UserSetRollbackOnlyClientEjbTest.class);
        // The application-client.xml descriptor
        URL resURL = UserSetRollbackOnlyClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            setrollbackonly_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        resURL = UserSetRollbackOnlyClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/setrollbackonly_ejb_vehicle_client.jar.sun-application-client.xml");
        if (resURL != null) {
            setrollbackonly_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }
        setrollbackonly_ejb_vehicle_client.addAsManifestResource(
                new StringAsset("Main-Class: " + UserSetRollbackOnlyClientEjbTest.class.getName() + "\n"), "MANIFEST.MF");
        archiveProcessor.processClientArchive(setrollbackonly_ejb_vehicle_client, UserSetRollbackOnlyClientEjbTest.class, resURL);

        JavaArchive setrollbackonly_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "setrollbackonly_ejb_vehicle_ejb.jar");
        setrollbackonly_ejb_vehicle_ejb.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                EETest.class, ServiceEETest.class,
                SetupException.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                UserSetRollbackOnlyClientEjbTest.class);
        // The ejb-jar.xml descriptor
        URL ejbResURL = UserSetRollbackOnlyClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            setrollbackonly_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = UserSetRollbackOnlyClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/setrollbackonly_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            setrollbackonly_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(setrollbackonly_ejb_vehicle_ejb, UserSetRollbackOnlyClientEjbTest.class, ejbResURL);

        // Ear
        EnterpriseArchive setrollbackonly_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "setrollbackonly_ejb_vehicle.ear");
        setrollbackonly_ejb_vehicle_ear.addAsModule(setrollbackonly_ejb_vehicle_ejb);
        setrollbackonly_ejb_vehicle_ear.addAsModule(setrollbackonly_ejb_vehicle_client);
        return setrollbackonly_ejb_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetRollbackOnly001() throws java.lang.Exception {
        super.testUserSetRollbackOnly001();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetRollbackOnly002() throws java.lang.Exception {
        super.testUserSetRollbackOnly002();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetRollbackOnly003() throws java.lang.Exception {
        super.testUserSetRollbackOnly003();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetRollbackOnly004() throws java.lang.Exception {
        super.testUserSetRollbackOnly004();
    }

}