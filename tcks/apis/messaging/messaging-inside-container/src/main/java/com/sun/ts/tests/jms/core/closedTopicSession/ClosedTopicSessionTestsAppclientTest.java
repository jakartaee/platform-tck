package com.sun.ts.tests.jms.core.closedTopicSession;

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
public class ClosedTopicSessionTestsAppclientTest extends com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests {
    static final String VEHICLE_ARCHIVE = "closedTopicSession_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedTopicSession_appclient_vehicle: 
        closedTopicSession_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicSession_ejb_vehicle: 
        closedTopicSession_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicSession_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedTopicSession_jsp_vehicle: 
        closedTopicSession_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedTopicSession_servlet_vehicle: 
        closedTopicSession_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/closedTopicSession/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedTopicSession_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedTopicSession_appclient_vehicle_client.jar");
            // The class files
            closedTopicSession_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedTopicSessionTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              closedTopicSession_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedTopicSessionTests.class.getResource("closedTopicSession_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedTopicSession_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            closedTopicSession_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedTopicSessionTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedTopicSession_appclient_vehicle_client, ClosedTopicSessionTests.class, resURL);

        // Ear
            EnterpriseArchive closedTopicSession_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicSession_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicSession_appclient_vehicle_ear.addAsModule(closedTopicSession_appclient_vehicle_client);



        return closedTopicSession_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCommitTest() throws java.lang.Exception {
            super.closedTopicSessionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCloseTest() throws java.lang.Exception {
            super.closedTopicSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateDurableSubscriberTest() throws java.lang.Exception {
            super.closedTopicSessionCreateDurableSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateDurableSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionCreateDurableSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateTopicTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateSubscriberTest() throws java.lang.Exception {
            super.closedTopicSessionCreateSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionCreateSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreatePublisherTest() throws java.lang.Exception {
            super.closedTopicSessionCreatePublisherTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateTempTopicTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTempTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionUnsubscribeTest() throws java.lang.Exception {
            super.closedTopicSessionUnsubscribeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateMapMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateTextMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionCreateText2MessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetTransactedTest() throws java.lang.Exception {
            super.closedTopicSessionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionRollbackTest() throws java.lang.Exception {
            super.closedTopicSessionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionRecoverTest() throws java.lang.Exception {
            super.closedTopicSessionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicSessionSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionReceiveTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicSessionGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicSessionSubscriberGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicSessionPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicSessionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicSessionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicSessionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetPriorityTest() throws java.lang.Exception {
            super.closedTopicSessionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicSessionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicSessionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicSessionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicSessionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSetPriorityTest() throws java.lang.Exception {
            super.closedTopicSessionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicSessionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicSessionPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionPublish1Test() throws java.lang.Exception {
            super.closedTopicSessionPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSessionPublish2Test() throws java.lang.Exception {
            super.closedTopicSessionPublish2Test();
        }


}