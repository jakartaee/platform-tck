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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("tck-appclient")

public class UserSetReadOnlyClientEjbTest
        extends UserSetReadOnlyClient {
    static final String VEHICLE_ARCHIVE = "setreadonly_ejb_vehicle";

    private static String packagePath = UserSetReadOnlyClientEjbTest.class.getPackageName().replace( ".", "/");

    private static final Logger logger = System.getLogger( UserSetReadOnlyClientEjbTest.class.getName());

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
        JavaArchive setreadonly_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class,
                "setreadonly_ejb_vehicle_client.jar");
        setreadonly_ejb_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class, com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class, com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, UserSetReadOnlyClient.class,
                UserSetReadOnlyClientEjbTest.class);
        // The application-client.xml descriptor
        URL resURL = UserSetReadOnlyClientEjbTest.class.getClassLoader().getResource( packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            setreadonly_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        resURL = UserSetReadOnlyClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/setreadonly_ejb_vehicle_client.jar.sun-application-client.xml");
        if (resURL != null) {
            setreadonly_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }
        setreadonly_ejb_vehicle_client.addAsManifestResource(
                new StringAsset("Main-Class: " + UserSetReadOnlyClientEjbTest.class.getName() + "\n"), "MANIFEST.MF");
        archiveProcessor.processClientArchive(setreadonly_ejb_vehicle_client, UserSetReadOnlyClientEjbTest.class,
                resURL);

        JavaArchive setreadonly_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class,
                "setreadonly_ejb_vehicle_ejb.jar");
        setreadonly_ejb_vehicle_ejb.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                UserSetReadOnlyClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class, com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class, com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class, SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, UserSetReadOnlyClientEjbTest.class);
        // The ejb-jar.xml descriptor
        URL ejbResURL = UserSetReadOnlyClientEjbTest.class.getClassLoader().getResource( packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            setreadonly_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = UserSetReadOnlyClientEjbTest.class.getClassLoader()
                .getResource(packagePath + "/setreadonly_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            setreadonly_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive( setreadonly_ejb_vehicle_ejb, UserSetReadOnlyClientEjbTest.class, ejbResURL);

        EnterpriseArchive setreadonly_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class,
                "setreadonly_ejb_vehicle.ear");
        setreadonly_ejb_vehicle_ear.addAsModule(setreadonly_ejb_vehicle_ejb);
        setreadonly_ejb_vehicle_ear.addAsModule(setreadonly_ejb_vehicle_client);

        return setreadonly_ejb_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetReadOnly001() throws Exception {
        super.testUserSetReadOnly001();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetReadOnly002() throws Exception {
        super.testUserSetReadOnly002();
    }

}