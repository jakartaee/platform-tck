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
public class ClosedTopicSessionTestsJspTest extends com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests {
    static final String VEHICLE_ARCHIVE = "closedTopicSession_jsp_vehicle";

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
        War:

        /com/sun/ts/tests/jms/core/closedTopicSession/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedTopicSession_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedTopicSession_jsp_vehicle_web.war");
            // The class files
            closedTopicSession_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
                                             com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedTopicSessionTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicSession/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedTopicSession_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedTopicSessionTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicSession/closedTopicSession_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedTopicSession_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedTopicSessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedTopicSession_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedTopicSessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedTopicSession_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedTopicSession_jsp_vehicle_web, ClosedTopicSessionTests.class, warResURL);

        // Ear
            EnterpriseArchive closedTopicSession_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicSession_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicSession_jsp_vehicle_ear.addAsModule(closedTopicSession_jsp_vehicle_web);



        return closedTopicSession_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCommitTest() throws java.lang.Exception {
            super.closedTopicSessionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCloseTest() throws java.lang.Exception {
            super.closedTopicSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateDurableSubscriberTest() throws java.lang.Exception {
            super.closedTopicSessionCreateDurableSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateDurableSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionCreateDurableSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateTopicTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateSubscriberTest() throws java.lang.Exception {
            super.closedTopicSessionCreateSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionCreateSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreatePublisherTest() throws java.lang.Exception {
            super.closedTopicSessionCreatePublisherTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateTempTopicTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTempTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionUnsubscribeTest() throws java.lang.Exception {
            super.closedTopicSessionUnsubscribeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateMapMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateTextMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionCreateText2MessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetTransactedTest() throws java.lang.Exception {
            super.closedTopicSessionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionRollbackTest() throws java.lang.Exception {
            super.closedTopicSessionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionRecoverTest() throws java.lang.Exception {
            super.closedTopicSessionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicSessionSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionReceiveTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicSessionGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicSessionSubscriberGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicSessionPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicSessionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicSessionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicSessionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetPriorityTest() throws java.lang.Exception {
            super.closedTopicSessionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicSessionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicSessionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicSessionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicSessionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSetPriorityTest() throws java.lang.Exception {
            super.closedTopicSessionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicSessionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicSessionPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionPublish1Test() throws java.lang.Exception {
            super.closedTopicSessionPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSessionPublish2Test() throws java.lang.Exception {
            super.closedTopicSessionPublish2Test();
        }


}