package com.sun.ts.tests.jms.core.messageQueue;

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
public class MessageQueueTestsEjbTest extends com.sun.ts.tests.jms.core.messageQueue.MessageQueueTests {
    static final String VEHICLE_ARCHIVE = "messageQueue_ejb_vehicle";

        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive messageQueue_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "messageQueue_ejb_vehicle_client.jar");
            // The class files
            messageQueue_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.messageQueue.MessageQueueTests.class
            );
            // The application-client.xml descriptor
            URL resURL = MessageQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              messageQueue_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MessageQueueTests.class.getResource("messageQueue_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              messageQueue_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //messageQueue_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MessageQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            messageQueue_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(messageQueue_ejb_vehicle_client, MessageQueueTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive messageQueue_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "messageQueue_ejb_vehicle_ejb.jar");
            // The class files
            messageQueue_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.jms.core.messageQueue.MessageQueueTests.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MessageQueueTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              messageQueue_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MessageQueueTests.class.getResource("messageQueue_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              messageQueue_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(messageQueue_ejb_vehicle_ejb, MessageQueueTests.class, ejbResURL);

        // Ear
            EnterpriseArchive messageQueue_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "messageQueue_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            messageQueue_ejb_vehicle_ear.addAsModule(messageQueue_ejb_vehicle_ejb);
            messageQueue_ejb_vehicle_ear.addAsModule(messageQueue_ejb_vehicle_client);



        return messageQueue_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgClearBodyQueueTest() throws java.lang.Exception {
            super.msgClearBodyQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgResetQueueTest() throws java.lang.Exception {
            super.msgResetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void readNullCharNotValidQueueTest() throws java.lang.Exception {
            super.readNullCharNotValidQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void messageQIllegalarg() throws java.lang.Exception {
            super.messageQIllegalarg();
        }


}