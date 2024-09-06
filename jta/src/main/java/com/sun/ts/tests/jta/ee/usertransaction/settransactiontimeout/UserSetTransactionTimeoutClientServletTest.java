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
@Tag("tck-javatest")

public class UserSetTransactionTimeoutClientServletTest extends com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient {
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
    War:

    /com/sun/ts/tests/jta/ee/usertransaction/settransactiontimeout/servlet_vehicle_web.xml
    /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
    Ear:

    */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
    // War
        // the war with the correct archive name
        WebArchive settransactiontimeout_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "settransactiontimeout_servlet_vehicle_web.war");
        // The class files
        settransactiontimeout_servlet_vehicle_web.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.tests.jta.ee.common.Transact.class,
        com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
        com.sun.ts.tests.jta.ee.usertransaction.settransactiontimeout.UserSetTransactionTimeoutClient.class,
        com.sun.ts.tests.jta.ee.common.InitFailedException.class,
        com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
        com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
        com.sun.ts.tests.jta.ee.common.InitFailedException.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        UserSetTransactionTimeoutClientServletTest.class
        );
        // The web.xml descriptor
        URL warResURL = UserSetTransactionTimeoutClientServletTest.class.getClassLoader().getResource(packagePath+"/servlet_vehicle_web.xml");
        if(warResURL != null) {
            settransactiontimeout_servlet_vehicle_web.setWebXML(warResURL);
        }
        // The sun-web.xml descriptor
        warResURL = UserSetTransactionTimeoutClientServletTest.class.getClassLoader().getResource(packagePath+"/settransactiontimeout_servlet_vehicle_web.war.sun-web.xml");
        if(warResURL != null) {
            settransactiontimeout_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        // Web content
        archiveProcessor.processWebArchive(settransactiontimeout_servlet_vehicle_web, UserSetTransactionTimeoutClientServletTest.class, warResURL);

    // Ear
        EnterpriseArchive settransactiontimeout_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "settransactiontimeout_servlet_vehicle.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        settransactiontimeout_servlet_vehicle_ear.addAsModule(settransactiontimeout_servlet_vehicle_web);



        // // The application.xml descriptor
        // URL earResURL = UserSetTransactionTimeoutClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/settransactiontimeout/");
        // if(earResURL != null) {
        //   settransactiontimeout_servlet_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
        // }
        // // The sun-application.xml descriptor
        // earResURL = UserSetTransactionTimeoutClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/settransactiontimeout/.ear.sun-application.xml");
        // if(earResURL != null) {
        //   settransactiontimeout_servlet_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
        // }
        // archiveProcessor.processEarArchive(settransactiontimeout_servlet_vehicle_ear, UserSetTransactionTimeoutClient.class, earResURL);
    
        return settransactiontimeout_servlet_vehicle_ear;
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