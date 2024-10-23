package com.sun.ts.tests.jms.core.closedQueueReceiver;

import com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests;
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
public class ClosedQueueReceiverTestsEjbTest extends com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests {
    static final String VEHICLE_ARCHIVE = "closedQueueReceiver_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueReceiver_appclient_vehicle: 
        closedQueueReceiver_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml
        closedQueueReceiver_ejb_vehicle: 
        closedQueueReceiver_ejb_vehicle_client: jar.sun-application-client.xml,META-INF/application-client.xml
        closedQueueReceiver_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueReceiver_jsp_vehicle: 
        closedQueueReceiver_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueReceiver_servlet_vehicle: 
        closedQueueReceiver_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/closedQueueReceiver/ejb_vehicle_ejb.xml
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
            JavaArchive closedQueueReceiver_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedQueueReceiver_ejb_vehicle_client.jar");
            // The class files
            closedQueueReceiver_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
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
            com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedQueueReceiverTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              closedQueueReceiver_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedQueueReceiverTests.class.getResource("closedQueueReceiver_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedQueueReceiver_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //closedQueueReceiver_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedQueueReceiverTests.class.getName() + "\n"), "MANIFEST.MF");
            closedQueueReceiver_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedQueueReceiver_ejb_vehicle_client, ClosedQueueReceiverTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive closedQueueReceiver_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "closedQueueReceiver_ejb_vehicle_ejb.jar");
            // The class files
            closedQueueReceiver_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ClosedQueueReceiverTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              closedQueueReceiver_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ClosedQueueReceiverTests.class.getResource("closedQueueReceiver_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              closedQueueReceiver_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(closedQueueReceiver_ejb_vehicle_ejb, ClosedQueueReceiverTests.class, ejbResURL);

        // Ear
            EnterpriseArchive closedQueueReceiver_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueReceiver_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueReceiver_ejb_vehicle_ear.addAsModule(closedQueueReceiver_ejb_vehicle_ejb);
            closedQueueReceiver_ejb_vehicle_ear.addAsModule(closedQueueReceiver_ejb_vehicle_client);



        return closedQueueReceiver_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueReceiverCloseTest() throws java.lang.Exception {
            super.closedQueueReceiverCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueReceiverGetMessageSelectorTest() throws java.lang.Exception {
            super.closedQueueReceiverGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueReceiverReceiveTest() throws java.lang.Exception {
            super.closedQueueReceiverReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueReceiverReceiveTimeoutTest() throws java.lang.Exception {
            super.closedQueueReceiverReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueReceiverReceiveNoWaitTest() throws java.lang.Exception {
            super.closedQueueReceiverReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueReceiverGetQueueTest() throws java.lang.Exception {
            super.closedQueueReceiverGetQueueTest();
        }


}