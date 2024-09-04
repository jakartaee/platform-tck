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
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("tck-appclient")

public class ClientEjbTest extends com.sun.ts.tests.jta.ee.txpropagationtest.Client {
    static final String VEHICLE_ARCHIVE = "jta_ejb_vehicle";

    private static String packagePath = ClientJspTest.class.getPackageName().replace(".", "/");


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
    Client:

    /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
    Ejb:

    /com/sun/ts/tests/jta/ee/txpropagationtest/ejb_vehicle_ejb.xml
    /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
    Ear:

    */
    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
    // Client
        // the jar with the correct archive name
        JavaArchive jta_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jta_ejb_vehicle_client.jar");
        // The class files
        jta_ejb_vehicle_client.addClasses(
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
        URL resURL = ClientEjbTest.class.getResource("/vehicle/ejb/ejb_vehicle_client.xml");

        if(resURL != null) {
          jta_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
        resURL = ClientEjbTest.class.getResource("/vehicle/ejb/ejb_vehicle_client.jar.sun-application-client.xml");
        if(resURL != null) {
          jta_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        jta_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClientEjbTest.class.getName() + "\n"), "MANIFEST.MF");
        archiveProcessor.processClientArchive(jta_ejb_vehicle_client, ClientEjbTest.class, resURL);


    // Ejb
        // the jar with the correct archive name
        JavaArchive jta_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jta_ejb_vehicle_ejb.jar");
        // The class files
        jta_ejb_vehicle_ejb.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.Client.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.DBSupport.class,
            ClientEjbTest.class
        );
        // The ejb-jar.xml descriptor
        URL ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.xml");
        if(ejbResURL != null) {
          jta_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/jta_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if(ejbResURL != null) {
          jta_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jta_ejb_vehicle_ejb, ClientEjbTest.class, ejbResURL);


        JavaArchive jta_ee_txpropagate1_ejb = ShrinkWrap.create(JavaArchive.class, "jta_ee_txpropagate1_ejb.jar");
        // The class files
        jta_ee_txpropagate1_ejb.addClasses(
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBean.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanEJB.class,
            com.sun.ts.tests.jta.ee.txpropagationtest.TxBeanHome.class
        );
        // The ejb-jar.xml descriptor
        ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/jta_ee_txpropagate1_ejb.xml");
        if(ejbResURL != null) {
          jta_ee_txpropagate1_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        // The sun-ejb-jar.xml file
        ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/jta_ee_txpropagate1_ejb.jar.sun-ejb-jar.xml");
        if(ejbResURL != null) {
          jta_ee_txpropagate1_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jta_ee_txpropagate1_ejb, ClientEjbTest.class, ejbResURL);


    // Ear
        EnterpriseArchive jta_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jta_ejb_vehicle.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        jta_ejb_vehicle_ear.addAsModule(jta_ee_txpropagate1_ejb);
        // jta_ejb_vehicle_ear.addAsModule(jta_ee_txpropagate2_ejb);
        // jta_ejb_vehicle_ear.addAsModule(jta_ee_txpropagate3_ejb);
        jta_ejb_vehicle_ear.addAsModule(jta_ejb_vehicle_ejb);
        jta_ejb_vehicle_ear.addAsModule(jta_ejb_vehicle_client);



        // // The application.xml descriptor
        // URL earResURL = Client.class.getResource("/com/sun/ts/tests/jta/ee/txpropagationtest/");
        // if(earResURL != null) {
        //   jta_ejb_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
        // }
        // The sun-application.xml descriptor
        URL earResURL = Client.class.getClassLoader().getResource(packagePath+"/jta_ejb_vehicle_client.jar.sun-application-client.xml");
        if(earResURL != null) {
          jta_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
        }
        archiveProcessor.processEarArchive(jta_ejb_vehicle_ear, Client.class, earResURL);
        
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