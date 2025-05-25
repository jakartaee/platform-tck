package com.sun.ts.tests.jms.core20.sessiontests;

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
public class ClientJspTest extends com.sun.ts.tests.jms.core20.sessiontests.Client {
    static final String VEHICLE_ARCHIVE = "sessiontests_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        sessiontests_appclient_vehicle: 
        sessiontests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        sessiontests_ejb_vehicle: 
        sessiontests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        sessiontests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        sessiontests_jsp_vehicle: 
        sessiontests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml,WEB-INF/web.xml,war.sun-web.xml
        sessiontests_servlet_vehicle: 
        sessiontests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml,WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core20/sessiontests/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive sessiontests_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "sessiontests_jsp_vehicle_web.war");
            // The class files
            sessiontests_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core20.sessiontests.Client.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/sessiontests/jsp_vehicle_web.xml");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/sessiontests/sessiontests_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(sessiontests_jsp_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive sessiontests_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "sessiontests_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            sessiontests_jsp_vehicle_ear.addAsModule(sessiontests_jsp_vehicle_web);



        return sessiontests_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendRecvMsgsOfEachMsgTypeTopicTest() throws java.lang.Exception {
            super.sendRecvMsgsOfEachMsgTypeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createTemporayTopicTest() throws java.lang.Exception {
            super.createTemporayTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getTransactedTopicTest() throws java.lang.Exception {
            super.getTransactedTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getAcknowledgeModeTopicTest() throws java.lang.Exception {
            super.getAcknowledgeModeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createConsumerProducerTopicTest() throws java.lang.Exception {
            super.createConsumerProducerTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createDurableSubscriberTopicTest1() throws java.lang.Exception {
            super.createDurableSubscriberTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createDurableSubscriberTopicTest2() throws java.lang.Exception {
            super.createDurableSubscriberTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createDurableConsumerTopicTest1() throws java.lang.Exception {
            super.createDurableConsumerTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createDurableConsumerTopicTest2() throws java.lang.Exception {
            super.createDurableConsumerTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedConsumerTopicTest1() throws java.lang.Exception {
            super.createSharedConsumerTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedConsumerTopicTest2() throws java.lang.Exception {
            super.createSharedConsumerTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedDurableConsumerTopicTest1() throws java.lang.Exception {
            super.createSharedDurableConsumerTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedDurableConsumerTopicTest2() throws java.lang.Exception {
            super.createSharedDurableConsumerTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedDurableConsumerTopicTest3() throws java.lang.Exception {
            super.createSharedDurableConsumerTopicTest3();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidDestinationExceptionTests() throws java.lang.Exception {
            super.invalidDestinationExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidSelectorExceptionTopicTests() throws java.lang.Exception {
            super.invalidSelectorExceptionTopicTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void illegalStateExceptionTests() throws java.lang.Exception {
            super.illegalStateExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void jMSExceptionTests() throws java.lang.Exception {
            super.jMSExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendAndRecvMsgsOfEachMsgTypeQueueTest() throws java.lang.Exception {
            super.sendAndRecvMsgsOfEachMsgTypeQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createTemporayQueueTest() throws java.lang.Exception {
            super.createTemporayQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createQueueBrowserTest() throws java.lang.Exception {
            super.createQueueBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getTransactedQueueTest() throws java.lang.Exception {
            super.getTransactedQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getAcknowledgeModeQueueTest() throws java.lang.Exception {
            super.getAcknowledgeModeQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createConsumerProducerQueueTest() throws java.lang.Exception {
            super.createConsumerProducerQueueTest();
        }


}