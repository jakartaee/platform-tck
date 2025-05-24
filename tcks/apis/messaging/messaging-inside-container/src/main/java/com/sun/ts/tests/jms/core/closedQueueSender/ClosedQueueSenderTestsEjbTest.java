package com.sun.ts.tests.jms.core.closedQueueSender;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

import com.sun.ts.lib.harness.SetupException;
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
public class ClosedQueueSenderTestsEjbTest extends com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTests {
    static final String VEHICLE_ARCHIVE = "closedQueueSender_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueSender_appclient_vehicle: 
        closedQueueSender_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSender_ejb_vehicle: 
        closedQueueSender_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSender_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueSender_jsp_vehicle: 
        closedQueueSender_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueSender_servlet_vehicle: 
        closedQueueSender_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/closedQueueSender/ejb_vehicle_ejb.xml
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
            JavaArchive closedQueueSender_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedQueueSender_ejb_vehicle_client.jar");
            // The class files
            closedQueueSender_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTests.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedQueueSenderTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              closedQueueSender_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedQueueSenderTests.class.getResource("closedQueueSender_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedQueueSender_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //closedQueueSender_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedQueueSenderTests.class.getName() + "\n"), "MANIFEST.MF");
            closedQueueSender_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedQueueSender_ejb_vehicle_client, ClosedQueueSenderTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive closedQueueSender_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "closedQueueSender_ejb_vehicle_ejb.jar");
            // The class files
            closedQueueSender_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class,
                com.sun.ts.tests.jms.common.MessageTestImpl.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTests.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                              com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ClosedQueueSenderTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              closedQueueSender_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ClosedQueueSenderTests.class.getResource("closedQueueSender_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              closedQueueSender_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(closedQueueSender_ejb_vehicle_ejb, ClosedQueueSenderTests.class, ejbResURL);

        // Ear
            EnterpriseArchive closedQueueSender_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueSender_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueSender_ejb_vehicle_ear.addAsModule(closedQueueSender_ejb_vehicle_ejb);
            closedQueueSender_ejb_vehicle_ear.addAsModule(closedQueueSender_ejb_vehicle_client);



        return closedQueueSender_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderCloseTest() throws java.lang.Exception {
            super.closedQueueSenderCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderGetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueSenderGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueSenderGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueSenderGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderGetPriorityTest() throws java.lang.Exception {
            super.closedQueueSenderGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderGetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueSenderGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueSenderSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueSenderSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueSenderSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSetPriorityTest() throws java.lang.Exception {
            super.closedQueueSenderSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueSenderSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderGetQueueTest() throws java.lang.Exception {
            super.closedQueueSenderGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSend1Test() throws java.lang.Exception {
            super.closedQueueSenderSend1Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSend2Test() throws java.lang.Exception {
            super.closedQueueSenderSend2Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSend3Test() throws java.lang.Exception {
            super.closedQueueSenderSend3Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueSenderSend4Test() throws java.lang.Exception {
            super.closedQueueSenderSend4Test();
        }


}