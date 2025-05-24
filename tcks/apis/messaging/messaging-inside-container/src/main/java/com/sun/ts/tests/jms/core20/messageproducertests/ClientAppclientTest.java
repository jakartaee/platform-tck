package com.sun.ts.tests.jms.core20.messageproducertests;

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
public class ClientAppclientTest extends com.sun.ts.tests.jms.core20.messageproducertests.Client {
    static final String VEHICLE_ARCHIVE = "messageproducertests_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        messageproducertests_appclient_vehicle: 
        messageproducertests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        messageproducertests_ejb_vehicle: 
        messageproducertests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageproducertests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        messageproducertests_jsp_vehicle: 
        messageproducertests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        messageproducertests_servlet_vehicle: 
        messageproducertests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core20/messageproducertests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive messageproducertests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "messageproducertests_appclient_vehicle_client.jar");
            // The class files
            messageproducertests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core20.messageproducertests.Client.class,
            com.sun.ts.tests.jms.core20.messageproducertests.Client.MyCompletionListener.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              messageproducertests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("messageproducertests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              messageproducertests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            messageproducertests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(messageproducertests_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive messageproducertests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "messageproducertests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            messageproducertests_appclient_vehicle_ear.addAsModule(messageproducertests_appclient_vehicle_client);



        return messageproducertests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSendAndRecvTest1() throws java.lang.Exception {
            super.queueSendAndRecvTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSendAndRecvTest2() throws java.lang.Exception {
            super.queueSendAndRecvTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSendAndRecvTest3() throws java.lang.Exception {
            super.queueSendAndRecvTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSendAndRecvTest4() throws java.lang.Exception {
            super.queueSendAndRecvTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSetGetDeliveryModeTest() throws java.lang.Exception {
            super.queueSetGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSetGetDeliveryDelayTest() throws java.lang.Exception {
            super.queueSetGetDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSetGetDisableMessageIDTest() throws java.lang.Exception {
            super.queueSetGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSetGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.queueSetGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSetGetPriorityTest() throws java.lang.Exception {
            super.queueSetGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueSetGetTimeToLiveTest() throws java.lang.Exception {
            super.queueSetGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueInvalidDestinationExceptionTests() throws java.lang.Exception {
            super.queueInvalidDestinationExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueUnsupportedOperationExceptionTests() throws java.lang.Exception {
            super.queueUnsupportedOperationExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueDeliveryDelayTest() throws java.lang.Exception {
            super.queueDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSendAndRecvTest1() throws java.lang.Exception {
            super.topicSendAndRecvTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSendAndRecvTest2() throws java.lang.Exception {
            super.topicSendAndRecvTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSendAndRecvTest3() throws java.lang.Exception {
            super.topicSendAndRecvTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSendAndRecvTest4() throws java.lang.Exception {
            super.topicSendAndRecvTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSetGetDeliveryModeTest() throws java.lang.Exception {
            super.topicSetGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSetGetDeliveryDelayTest() throws java.lang.Exception {
            super.topicSetGetDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSetGetDisableMessageIDTest() throws java.lang.Exception {
            super.topicSetGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSetGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.topicSetGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicSetGetPriorityTest() throws java.lang.Exception {
            super.topicSetGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicInvalidDestinationExceptionTests() throws java.lang.Exception {
            super.topicInvalidDestinationExceptionTests();
        }
        
        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicUnsupportedOperationExceptionTests() throws java.lang.Exception {
            super.topicUnsupportedOperationExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicDeliveryDelayTest() throws java.lang.Exception {
            super.topicDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void JMSExceptionTests() throws java.lang.Exception {
            super.JMSExceptionTests();
        }


}