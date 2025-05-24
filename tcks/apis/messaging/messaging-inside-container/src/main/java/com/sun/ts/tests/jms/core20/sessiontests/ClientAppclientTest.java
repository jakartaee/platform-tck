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
public class ClientAppclientTest extends com.sun.ts.tests.jms.core20.sessiontests.Client {
    static final String VEHICLE_ARCHIVE = "sessiontests_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core20/sessiontests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive sessiontests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "sessiontests_appclient_vehicle_client.jar");
            // The class files
            sessiontests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core20.sessiontests.Client.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              sessiontests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("sessiontests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              sessiontests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            sessiontests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(sessiontests_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive sessiontests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "sessiontests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            sessiontests_appclient_vehicle_ear.addAsModule(sessiontests_appclient_vehicle_client);



        return sessiontests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendRecvMsgsOfEachMsgTypeTopicTest() throws java.lang.Exception {
            super.sendRecvMsgsOfEachMsgTypeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createTemporayTopicTest() throws java.lang.Exception {
            super.createTemporayTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void getTransactedTopicTest() throws java.lang.Exception {
            super.getTransactedTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void getAcknowledgeModeTopicTest() throws java.lang.Exception {
            super.getAcknowledgeModeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createConsumerProducerTopicTest() throws java.lang.Exception {
            super.createConsumerProducerTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createDurableSubscriberTopicTest1() throws java.lang.Exception {
            super.createDurableSubscriberTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createDurableSubscriberTopicTest2() throws java.lang.Exception {
            super.createDurableSubscriberTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createDurableConsumerTopicTest1() throws java.lang.Exception {
            super.createDurableConsumerTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createDurableConsumerTopicTest2() throws java.lang.Exception {
            super.createDurableConsumerTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createSharedConsumerTopicTest1() throws java.lang.Exception {
            super.createSharedConsumerTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createSharedConsumerTopicTest2() throws java.lang.Exception {
            super.createSharedConsumerTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createSharedDurableConsumerTopicTest1() throws java.lang.Exception {
            super.createSharedDurableConsumerTopicTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createSharedDurableConsumerTopicTest2() throws java.lang.Exception {
            super.createSharedDurableConsumerTopicTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createSharedDurableConsumerTopicTest3() throws java.lang.Exception {
            super.createSharedDurableConsumerTopicTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidDestinationExceptionTests() throws java.lang.Exception {
            super.invalidDestinationExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidSelectorExceptionTopicTests() throws java.lang.Exception {
            super.invalidSelectorExceptionTopicTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateExceptionTests() throws java.lang.Exception {
            super.illegalStateExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jMSExceptionTests() throws java.lang.Exception {
            super.jMSExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvMsgsOfEachMsgTypeQueueTest() throws java.lang.Exception {
            super.sendAndRecvMsgsOfEachMsgTypeQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createTemporayQueueTest() throws java.lang.Exception {
            super.createTemporayQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createQueueBrowserTest() throws java.lang.Exception {
            super.createQueueBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void getTransactedQueueTest() throws java.lang.Exception {
            super.getTransactedQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void getAcknowledgeModeQueueTest() throws java.lang.Exception {
            super.getAcknowledgeModeQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createConsumerProducerQueueTest() throws java.lang.Exception {
            super.createConsumerProducerQueueTest();
        }


}