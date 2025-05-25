package com.sun.ts.tests.jms.core.closedQueueSession;

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
public class ClosedQueueSessionTestsJspTest extends com.sun.ts.tests.jms.core.closedQueueSession.ClosedQueueSessionTests {
    static final String VEHICLE_ARCHIVE = "closedQueueSession_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueSession_appclient_vehicle: 
        closedQueueSession_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSession_ejb_vehicle: 
        closedQueueSession_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSession_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueSession_jsp_vehicle: 
        closedQueueSession_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueSession_servlet_vehicle: 
        closedQueueSession_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedQueueSession/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedQueueSession_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedQueueSession_jsp_vehicle_web.war");
            // The class files
            closedQueueSession_jsp_vehicle_web.addClasses(
                                com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
                                com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
            com.sun.ts.tests.jms.core.closedQueueSession.ClosedQueueSessionTests.class,
                                com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            EETest.class,
            com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedQueueSessionTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueSession/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedQueueSession_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedQueueSessionTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueSession/closedQueueSession_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedQueueSession_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedQueueSessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedQueueSession_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedQueueSessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedQueueSession_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedQueueSession_jsp_vehicle_web, ClosedQueueSessionTests.class, warResURL);

        // Ear
            EnterpriseArchive closedQueueSession_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueSession_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueSession_jsp_vehicle_ear.addAsModule(closedQueueSession_jsp_vehicle_web);



        return closedQueueSession_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCloseTest() throws java.lang.Exception {
            super.closedQueueSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateBrowserTest() throws java.lang.Exception {
            super.closedQueueSessionCreateBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateBrowserMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueSessionCreateBrowserMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateQueueTest() throws java.lang.Exception {
            super.closedQueueSessionCreateQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateReceiverTest() throws java.lang.Exception {
            super.closedQueueSessionCreateReceiverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateReceiverMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueSessionCreateReceiverMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateSenderTest() throws java.lang.Exception {
            super.closedQueueSessionCreateSenderTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateTempQueueTest() throws java.lang.Exception {
            super.closedQueueSessionCreateTempQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateMessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateMapMessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateTextMessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCreateText2MessageTest() throws java.lang.Exception {
            super.closedQueueSessionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionReceiverCloseTest() throws java.lang.Exception {
            super.closedQueueSessionReceiverCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedQueueSessionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionReceiveTest() throws java.lang.Exception {
            super.closedQueueSessionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedQueueSessionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedQueueSessionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionReceiverGetQueueTest() throws java.lang.Exception {
            super.closedQueueSessionReceiverGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSenderCloseTest() throws java.lang.Exception {
            super.closedQueueSessionSenderCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueSessionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueSessionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueSessionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetPriorityTest() throws java.lang.Exception {
            super.closedQueueSessionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueSessionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueSessionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueSessionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueSessionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSetPriorityTest() throws java.lang.Exception {
            super.closedQueueSessionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueSessionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSenderGetQueueTest() throws java.lang.Exception {
            super.closedQueueSessionSenderGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSend1Test() throws java.lang.Exception {
            super.closedQueueSessionSend1Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSend2Test() throws java.lang.Exception {
            super.closedQueueSessionSend2Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSend3Test() throws java.lang.Exception {
            super.closedQueueSessionSend3Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionSend4Test() throws java.lang.Exception {
            super.closedQueueSessionSend4Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionRecoverTest() throws java.lang.Exception {
            super.closedQueueSessionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedSessionRecoverTest() throws java.lang.Exception {
            super.closedSessionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionCommitTest() throws java.lang.Exception {
            super.closedQueueSessionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionGetTransactedTest() throws java.lang.Exception {
            super.closedQueueSessionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueSessionRollbackTest() throws java.lang.Exception {
            super.closedQueueSessionRollbackTest();
        }


}