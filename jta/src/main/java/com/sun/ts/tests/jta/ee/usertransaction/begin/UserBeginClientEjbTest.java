package com.sun.ts.tests.jta.ee.usertransaction.begin;

import com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient;
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

    @AfterEach
    public void cleanup() {
      logger.log(Logger.Level.INFO, "cleanup ok");
    }

    /**
    EE10 Deployment Descriptors:
    begin_ejb_vehicle: 
    begin_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
    begin_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
    begin_jsp_vehicle: 
    begin_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
    begin_servlet_vehicle: 
    begin_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

    Found Descriptors:
    Client:

    /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
    Ejb:

    /com/sun/ts/tests/jta/ee/usertransaction/begin/ejb_vehicle_ejb.xml
    /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
    Ear:

    */
    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
    // Client
        // the jar with the correct archive name
        JavaArchive begin_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "begin_ejb_vehicle_client.jar");
        // The class files
        begin_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            UserBeginClientEjbTest.class
        );
        // The application-client.xml descriptor
        //TODO : replace display-name begin_ejb_vehicle_client
        URL resURL = UserBeginClientEjbTest.class.getResource("/vehicle/ejb/ejb_vehicle_client.xml");
        if(resURL != null) {
          begin_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
        resURL = UserBeginClientEjbTest.class.getResource("/vehicle/ejb/ejb_vehicle_client.jar.sun-application-client.xml");
        if(resURL != null) {
          begin_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        begin_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + UserBeginClientEjbTest.class.getName() + "\n"), "MANIFEST.MF");
        archiveProcessor.processClientArchive(begin_ejb_vehicle_client, UserBeginClientEjbTest.class, resURL);


    // Ejb
        // the jar with the correct archive name
        JavaArchive begin_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "begin_ejb_vehicle_ejb.jar");
        // The class files
        begin_ejb_vehicle_ejb.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient.class,
            com.sun.ts.tests.jta.ee.common.Transact.class,
            com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
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
            UserBeginClientEjbTest.class
        );
        // The ejb-jar.xml descriptor
        URL ejbResURL = UserBeginClient.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.xml");
        if(ejbResURL != null) {
          begin_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = UserBeginClient.class.getClassLoader().getResource(packagePath+"/begin_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if(ejbResURL != null) {
          begin_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(begin_ejb_vehicle_ejb, UserBeginClient.class, ejbResURL);

    // Ear
        EnterpriseArchive begin_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "begin_ejb_vehicle.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        begin_ejb_vehicle_ear.addAsModule(begin_ejb_vehicle_ejb);
        begin_ejb_vehicle_ear.addAsModule(begin_ejb_vehicle_client);


        // The application.xml descriptor
        URL earResURL = UserBeginClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/begin/");
        if(earResURL != null) {
          begin_ejb_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
        }
        // The sun-application.xml descriptor
        earResURL = UserBeginClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/begin/begin_ejb_vehicle_client.jar.sun-application-client.xml");
        if(earResURL != null) {
          begin_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
        }
        archiveProcessor.processEarArchive(begin_ejb_vehicle_ear, UserBeginClient.class, earResURL);

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