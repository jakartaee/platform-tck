package com.sun.ts.tests.jta.ee.txpropagationtest;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;

import java.lang.System.Logger;
import java.net.URL;

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
public class ClientEjbTest extends com.sun.ts.tests.jta.ee.txpropagationtest.Client {
    static final String VEHICLE_ARCHIVE = "jta_ejb_vehicle";

    private static String packagePath = ClientEjbTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(ClientEjbTest.class.getName());

    public static void main(String args[]) {
        ClientEjbTest tests = new ClientEjbTest();
        Status s = tests.run(args, System.out, System.err);
        s.exit();
    }

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

        JavaArchive jta_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jta_ejb_vehicle_client.jar");
        jta_ejb_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class, Fault.class,
                EETest.class, ServiceEETest.class,
                SetupException.class, com.sun.ts.tests.jta.ee.txpropagationtest.Client.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class, com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
                ClientEjbTest.class);
        URL resURL = ClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            jta_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        resURL = ClientEjbTest.class.getClassLoader().getResource(packagePath + "/jta_ejb_vehicle_client.jar.sun-application-client.xml");
        if (resURL != null) {
            jta_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }
        jta_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
                "MANIFEST.MF");
        archiveProcessor.processClientArchive(jta_ejb_vehicle_client, ClientEjbTest.class, resURL);

        JavaArchive jta_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jta_ejb_vehicle_ejb.jar");
        jta_ejb_vehicle_ejb.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.Client.class, com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class, com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class,
                EETest.class, Fault.class, ServiceEETest.class,
                SetupException.class, ClientEjbTest.class);
        // The ejb-jar.xml descriptor
        URL ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            jta_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath + "/jta_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            jta_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jta_ejb_vehicle_ejb, ClientEjbTest.class, ejbResURL);

        JavaArchive jta_ee_txpropagate1_ejb = ShrinkWrap.create(JavaArchive.class, "jta_ee_txpropagate1_ejb.jar");
        jta_ee_txpropagate1_ejb.addClasses(com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
                com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class

        );
        // The ejb-jar.xml descriptor
        URL ejbJarResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath + "/jta_ee_txpropagate1_ejb.xml");
        if (ejbJarResURL != null) {
            jta_ee_txpropagate1_ejb.addAsManifestResource(ejbJarResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbJarResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath + "/jta_ee_txpropagate1_ejb.jar.sun-ejb-jar.xml");
        if (ejbJarResURL != null) {
            jta_ee_txpropagate1_ejb.addAsManifestResource(ejbJarResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jta_ee_txpropagate1_ejb, ClientEjbTest.class, ejbJarResURL);

        EnterpriseArchive jta_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jta_ejb_vehicle.ear");
        jta_ejb_vehicle_ear.addAsModule(jta_ee_txpropagate1_ejb);
        jta_ejb_vehicle_ear.addAsModule(jta_ejb_vehicle_ejb);
        jta_ejb_vehicle_ear.addAsModule(jta_ejb_vehicle_client);

        return jta_ejb_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void test1() throws java.lang.Exception {
        super.test1();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void test2() throws java.lang.Exception {
        super.test2();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void test3() throws java.lang.Exception {
        super.test3();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void test4() throws java.lang.Exception {
        super.test4();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void test5() throws java.lang.Exception {
        super.test5();
    }

    @Test
    @Override
    @TargetVehicle("ejb")
    public void test6() throws java.lang.Exception {
        super.test6();
    }

}