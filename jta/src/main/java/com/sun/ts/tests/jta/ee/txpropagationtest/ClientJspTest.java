package com.sun.ts.tests.jta.ee.txpropagationtest;

import com.sun.ts.tests.jta.ee.txpropagationtest.Client;
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
@Tag("tck-javatest")

public class ClientJspTest extends com.sun.ts.tests.jta.ee.txpropagationtest.Client {
    static final String VEHICLE_ARCHIVE = "jta_jsp_vehicle";

    private static String packagePath = ClientJspTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(ClientJspTest.class.getName());

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
    jta_ee_txpropagate1_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
    jta_ee_txpropagate2_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
    jta_ee_txpropagate3_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
    jta_ejb_vehicle: 
    jta_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
    jta_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
    jta_jsp_vehicle: 
    jta_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
    jta_servlet_vehicle: 
    jta_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

    Found Descriptors:
    Ejb:

    /com/sun/ts/tests/jta/ee/txpropagationtest/jta_ee_txpropagate3_ejb.xml
    /com/sun/ts/tests/jta/ee/txpropagationtest/jta_ee_txpropagate3_ejb.jar.sun-ejb-jar.xml
    War:

    /com/sun/ts/tests/jta/ee/txpropagationtest/jsp_vehicle_web.xml
    /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
    Ear:

    */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
    // War
        // the war with the correct archive name
        WebArchive jta_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "jta_jsp_vehicle_web.war");
        // The class files
        jta_jsp_vehicle_web.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
        com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.jta.ee.txpropagationtest.Client.class,
        com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
        com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanHome.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
        ClientJspTest.class
        );
        // The web.xml descriptor
        URL warResURL = ClientJspTest.class.getClassLoader().getResource(packagePath+"/jsp_vehicle_web.xml");
        if(warResURL != null) {
            jta_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // Web content
        warResURL = ClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        jta_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
        warResURL = ClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        jta_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
        
        // The sun-web.xml descriptor
        warResURL = ClientJspTest.class.getClassLoader().getResource(packagePath+"/jta_jsp_vehicle_web.war.sun-web.xml");
        if(warResURL != null) {
            jta_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        archiveProcessor.processWebArchive(jta_jsp_vehicle_web, ClientJspTest.class, warResURL);

    // Ejb
        // the jar with the correct archive name
        JavaArchive jta_ee_txpropagate3_ejb = ShrinkWrap.create(JavaArchive.class, "jta_ee_txpropagate3_ejb.jar");
        // The class files
        jta_ee_txpropagate3_ejb.addClasses(
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanHome.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class
        );
        // The ejb-jar.xml descriptor
        URL ejbResURL = ClientJspTest.class.getClassLoader().getResource(packagePath+"/jta_ee_txpropagate3_ejb.xml");
        if(ejbResURL != null) {
            jta_ee_txpropagate3_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = ClientJspTest.class.getClassLoader().getResource(packagePath+"/jta_ee_txpropagate3_ejb.jar.sun-ejb-jar.xml");
        if(ejbResURL != null) {
            jta_ee_txpropagate3_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jta_ee_txpropagate3_ejb, ClientJspTest.class, ejbResURL);

    // Ear
        EnterpriseArchive jta_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jta_jsp_vehicle.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        // jta_jsp_vehicle_ear.addAsModule(jta_ee_txpropagate1_ejb);
        // jta_jsp_vehicle_ear.addAsModule(jta_ee_txpropagate2_ejb);
        jta_jsp_vehicle_ear.addAsModule(jta_ee_txpropagate3_ejb);
        jta_jsp_vehicle_ear.addAsModule(jta_jsp_vehicle_web);



        // // The application.xml descriptor
        // URL earResURL = Client.class.getResource("/com/sun/ts/tests/jta/ee/txpropagationtest/");
        // if(earResURL != null) {
        //   jta_jsp_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
        // }
        // // The sun-application.xml descriptor
        // earResURL = Client.class.getResource("/com/sun/ts/tests/jta/ee/txpropagationtest/.ear.sun-application.xml");
        // if(earResURL != null) {
        //   jta_jsp_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
        // }
        // archiveProcessor.processEarArchive(jta_jsp_vehicle_ear, Client.class, earResURL);
        return jta_jsp_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void test1() throws java.lang.Exception {
        super.test1();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void test2() throws java.lang.Exception {
        super.test2();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void test3() throws java.lang.Exception {
        super.test3();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void test4() throws java.lang.Exception {
        super.test4();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void test5() throws java.lang.Exception {
        super.test5();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void test6() throws java.lang.Exception {
        super.test6();
    }


}