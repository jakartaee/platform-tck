package com.sun.ts.tests.jms.ee.ejbweb.xa;

import com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionTestsEjbTest extends com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests {
    static final String VEHICLE_ARCHIVE = "transaction_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        transaction_ejb_vehicle: 
        transaction_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        transaction_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        transaction_jsp_vehicle: 
        transaction_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        transaction_servlet_vehicle: 
        transaction_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        transactional_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/ejbweb/xa/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive transaction_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "transaction_ejb_vehicle_client.jar");
            // The class files
            transaction_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests.class
            );
            // The application-client.xml descriptor
            URL resURL = TransactionTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              transaction_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TransactionTests.class.getResource("transaction_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              transaction_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // transaction_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TransactionTests.class.getName() + "\n"), "MANIFEST.MF");
            transaction_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(transaction_ejb_vehicle_client, TransactionTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive transaction_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "transaction_ejb_vehicle_ejb.jar");
            // The class files
            transaction_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = TransactionTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              transaction_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = TransactionTests.class.getResource("transaction_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              transaction_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(transaction_ejb_vehicle_ejb, TransactionTests.class, ejbResURL);

        // Ear
            EnterpriseArchive transaction_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "transaction_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            transaction_ejb_vehicle_ear.addAsModule(transaction_ejb_vehicle_ejb);
            transaction_ejb_vehicle_ear.addAsModule(transaction_ejb_vehicle_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = TransactionTests.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              transaction_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(transaction_ejb_vehicle_ear, TransactionTests.class, earResURL);
        return transaction_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test01() throws java.lang.Exception {
            super.Test01();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test02() throws java.lang.Exception {
            super.Test02();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test03() throws java.lang.Exception {
            super.Test03();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test04() throws java.lang.Exception {
            super.Test04();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test05() throws java.lang.Exception {
            super.Test05();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test06() throws java.lang.Exception {
            super.Test06();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test07() throws java.lang.Exception {
            super.Test07();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test08() throws java.lang.Exception {
            super.Test08();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test09() throws java.lang.Exception {
            super.Test09();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void Test10() throws java.lang.Exception {
            super.Test10();
        }


}