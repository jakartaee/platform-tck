package com.sun.ts.tests.jms.core.messageProducer;

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
public class MessageProducerTestsJspTest extends com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests {
    static final String VEHICLE_ARCHIVE = "messageProducer_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        messageProducer_appclient_vehicle: 
        messageProducer_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageProducer_ejb_vehicle: 
        messageProducer_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageProducer_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        messageProducer_jsp_vehicle: 
        messageProducer_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        messageProducer_servlet_vehicle: 
        messageProducer_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/messageProducer/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive messageProducer_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "messageProducer_jsp_vehicle_web.war");
            // The class files
            messageProducer_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests.class
            );
            // The web.xml descriptor
            URL warResURL = MessageProducerTests.class.getResource("/com/sun/ts/tests/jms/core/messageProducer/jsp_vehicle_web.xml");
            if(warResURL != null) {
              messageProducer_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = MessageProducerTests.class.getResource("/com/sun/ts/tests/jms/core/messageProducer/messageProducer_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              messageProducer_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = MessageProducerTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              messageProducer_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = MessageProducerTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              messageProducer_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(messageProducer_jsp_vehicle_web, MessageProducerTests.class, warResURL);

        // Ear
            EnterpriseArchive messageProducer_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "messageProducer_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            messageProducer_jsp_vehicle_ear.addAsModule(messageProducer_jsp_vehicle_web);



        return messageProducer_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendQueueTest1() throws java.lang.Exception {
            super.sendQueueTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendQueueTest2() throws java.lang.Exception {
            super.sendQueueTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendQueueTest3() throws java.lang.Exception {
            super.sendQueueTest3();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendTopicTest4() throws java.lang.Exception {
            super.sendTopicTest4();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendTopicTest5() throws java.lang.Exception {
            super.sendTopicTest5();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendTopicTest6() throws java.lang.Exception {
            super.sendTopicTest6();
        }


}