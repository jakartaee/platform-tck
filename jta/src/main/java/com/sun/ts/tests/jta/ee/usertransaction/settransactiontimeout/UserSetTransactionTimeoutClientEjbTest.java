package com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout;

import com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient;
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
@Tag("tck-appclient")

public class UserSetTransactionTimeoutClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient {
    static final String VEHICLE_ARCHIVE = "settransactiontimeout_ejb_vehicle";

    private static String packagePath = UserSetTransactionTimeoutClientEjbTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserSetTransactionTimeoutClientEjbTest.class.getName());

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

    /**
    EE10 Deployment Descriptors:
    settransactiontimeout_ejb_vehicle: 
    settransactiontimeout_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
    settransactiontimeout_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
    settransactiontimeout_jsp_vehicle: 
    settransactiontimeout_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
    settransactiontimeout_servlet_vehicle: 
    settransactiontimeout_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

    Found Descriptors:
    Client:

    /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
    Ejb:

    /com/sun/ts/tests/jta/ee/usertransaction/settransactiontimeout/ejb_vehicle_ejb.xml
    /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
    Ear:

    */
    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
    // Client
        // the jar with the correct archive name
        JavaArchive settransactiontimeout_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "settransactiontimeout_ejb_vehicle_client.jar");
        // The class files
        settransactiontimeout_ejb_vehicle_client.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class
        );
        // The application-client.xml descriptor
        // TODO change client name
        URL resURL = UserSetTransactionTimeoutClientEjbTest.class.getResource("/vehicle/ejb/ejb_vehicle_client.xml");
        if(resURL != null) {
          settransactiontimeout_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        // // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
        // resURL = UserSetTransactionTimeoutClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.jar.sun-application-client.xml");
        // if(resURL != null) {
        //   settransactiontimeout_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        // }
        settransactiontimeout_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + UserSetTransactionTimeoutClientEjbTest.class.getName() + "\n"), "MANIFEST.MF");
        archiveProcessor.processClientArchive(settransactiontimeout_ejb_vehicle_client, UserSetTransactionTimeoutClientEjbTest.class, resURL);


    // Ejb
        // the jar with the correct archive name
        JavaArchive settransactiontimeout_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "settransactiontimeout_ejb_vehicle_ejb.jar");
        // The class files
        settransactiontimeout_ejb_vehicle_ejb.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jta.ee.common.Transact.class,
            com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
            com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
            com.sun.ts.tests.jta.ee.common.InitFailedException.class,
            com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
            com.sun.ts.tests.jta.ee.common.InitFailedException.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            UserSetTransactionTimeoutClientEjbTest.class
        );
        // The ejb-jar.xml descriptor
        URL ejbResURL = UserSetTransactionTimeoutClientEjbTest.class.getResource("/ejb_vehicle_ejb.xml");
        if(ejbResURL != null) {
          settransactiontimeout_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = UserSetTransactionTimeoutClientEjbTest.class.getResource("/settransactiontimeout_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if(ejbResURL != null) {
          settransactiontimeout_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(settransactiontimeout_ejb_vehicle_ejb, UserSetTransactionTimeoutClientEjbTest.class, ejbResURL);

    // Ear
        EnterpriseArchive settransactiontimeout_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "settransactiontimeout_ejb_vehicle.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        settransactiontimeout_ejb_vehicle_ear.addAsModule(settransactiontimeout_ejb_vehicle_ejb);
        settransactiontimeout_ejb_vehicle_ear.addAsModule(settransactiontimeout_ejb_vehicle_client);


        // // The application.xml descriptor
        // URL earResURL = UserSetTransactionTimeoutClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/settransactiontimeout/");
        // if(earResURL != null) {
        //   settransactiontimeout_ejb_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
        // }
        // The sun-application.xml descriptor
        URL earResURL = UserSetTransactionTimeoutClientEjbTest.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/settransactiontimeout/settransactiontimeout_ejb_vehicle_client.jar.sun-application-client.xml");
        if(earResURL != null) {
          settransactiontimeout_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
        }
        archiveProcessor.processEarArchive(settransactiontimeout_ejb_vehicle_ear, UserSetTransactionTimeoutClientEjbTest.class, earResURL);
    
        return settransactiontimeout_ejb_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetTransactionTimeout001() throws java.lang.Exception {
        super.testUserSetTransactionTimeout001();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void testUserSetTransactionTimeout002() throws java.lang.Exception {
        super.testUserSetTransactionTimeout002();
    }


}