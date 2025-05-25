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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MessageQueueTestsJspTest extends com.sun.ts.tests.jms.core.messageQueue.MessageQueueTests {
    static final String VEHICLE_ARCHIVE = "messageQueue_jsp_vehicle";

        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive messageQueue_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "messageQueue_jsp_vehicle_web.war");
            // The class files
            messageQueue_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.messageQueue.MessageQueueTests.class
            );
            // The web.xml descriptor
            URL warResURL = MessageQueueTests.class.getResource("jsp_vehicle_web.xml");
            if(warResURL != null) {
              messageQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = MessageQueueTests.class.getResource("messageQueue_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              messageQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = MessageQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              messageQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = MessageQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              messageQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(messageQueue_jsp_vehicle_web, MessageQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive messageQueue_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "messageQueue_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            messageQueue_jsp_vehicle_ear.addAsModule(messageQueue_jsp_vehicle_web);



        return messageQueue_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgClearBodyQueueTest() throws java.lang.Exception {
            super.msgClearBodyQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgResetQueueTest() throws java.lang.Exception {
            super.msgResetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void readNullCharNotValidQueueTest() throws java.lang.Exception {
            super.readNullCharNotValidQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void messageQIllegalarg() throws java.lang.Exception {
            super.messageQIllegalarg();
        }



}