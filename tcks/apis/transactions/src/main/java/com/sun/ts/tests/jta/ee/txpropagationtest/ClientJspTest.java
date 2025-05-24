package com.sun.ts.tests.jta.ee.txpropagationtest;

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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

    @Override
    @AfterEach
    public void cleanup() {
        logger.log(Logger.Level.INFO, "cleanup ok");
    }

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        WebArchive jta_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "jta_jsp_vehicle_web.war");
        jta_jsp_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.Client.class, com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
                EETest.class, ServiceEETest.class,
                SetupException.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class, ClientJspTest.class);
        // The web.xml descriptor
        URL warResURL = ClientJspTest.class.getClassLoader().getResource(packagePath + "/jsp_vehicle_web.xml");
        if (warResURL != null) {
            jta_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }
        // Web content
        warResURL = ClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        jta_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
        warResURL = ClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        jta_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

        // The sun-web.xml descriptor
        warResURL = ClientJspTest.class.getClassLoader().getResource(packagePath + "/jta_jsp_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            jta_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        archiveProcessor.processWebArchive(jta_jsp_vehicle_web, ClientJspTest.class, warResURL);

        JavaArchive jta_ee_txpropagate3_ejb = ShrinkWrap.create(JavaArchive.class, "jta_ee_txpropagate3_ejb.jar");
        jta_ee_txpropagate3_ejb.addClasses(com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class, com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class);
        // The ejb-jar.xml descriptor
        URL ejbResURL = ClientJspTest.class.getClassLoader().getResource(packagePath + "/jta_ee_txpropagate3_ejb.xml");
        if (ejbResURL != null) {
            jta_ee_txpropagate3_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = ClientJspTest.class.getClassLoader().getResource(packagePath + "/jta_ee_txpropagate3_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            jta_ee_txpropagate3_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jta_ee_txpropagate3_ejb, ClientJspTest.class, ejbResURL);

        EnterpriseArchive jta_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jta_jsp_vehicle.ear");
        jta_jsp_vehicle_ear.addAsModule(jta_ee_txpropagate3_ejb);
        jta_jsp_vehicle_ear.addAsModule(jta_jsp_vehicle_web);

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