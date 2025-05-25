package com.sun.ts.tests.jms.core.closedQueueConnection;

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
public class ClosedQueueConnectionTestsJspTest extends com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedQueueConnection_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueConnection_appclient_vehicle: 
        closedQueueConnection_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueConnection_ejb_vehicle: 
        closedQueueConnection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueConnection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueConnection_jsp_vehicle: 
        closedQueueConnection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueConnection_servlet_vehicle: 
        closedQueueConnection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedQueueConnection/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedQueueConnection_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedQueueConnection_jsp_vehicle_web.war");
            // The class files
            closedQueueConnection_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
                                             com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            EETest.class,
            com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedQueueConnectionTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueConnection/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedQueueConnection_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedQueueConnectionTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueConnection/closedQueueConnection_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedQueueConnection_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedQueueConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedQueueConnection_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedQueueConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedQueueConnection_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedQueueConnection_jsp_vehicle_web, ClosedQueueConnectionTests.class, warResURL);

        // Ear
            EnterpriseArchive closedQueueConnection_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueConnection_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueConnection_jsp_vehicle_ear.addAsModule(closedQueueConnection_jsp_vehicle_web);



        return closedQueueConnection_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCommitTest() throws java.lang.Exception {
            super.closedQueueConnectionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetTransactedTest() throws java.lang.Exception {
            super.closedQueueConnectionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionRollbackTest() throws java.lang.Exception {
            super.closedQueueConnectionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionRecoverTest() throws java.lang.Exception {
            super.closedQueueConnectionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetClientIDTest() throws java.lang.Exception {
            super.closedQueueConnectionGetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetMetaDataTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionStartTest() throws java.lang.Exception {
            super.closedQueueConnectionStartTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateQueueSessionTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateQueueSessionTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSessionCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateBrowserTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateBrowserMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBrowserMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateReceiverTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateReceiverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateReceiverMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateReceiverMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateSenderTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateSenderTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateTempQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateTempQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateMapMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateTextMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionCreateText2MessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionReceiverCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiverCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionReceiveTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionReceiverGetQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiverGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSenderCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionSenderCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetPriorityTest() throws java.lang.Exception {
            super.closedQueueConnectionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueConnectionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSetPriorityTest() throws java.lang.Exception {
            super.closedQueueConnectionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueConnectionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSenderGetQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionSenderGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSend1Test() throws java.lang.Exception {
            super.closedQueueConnectionSend1Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSend2Test() throws java.lang.Exception {
            super.closedQueueConnectionSend2Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSend3Test() throws java.lang.Exception {
            super.closedQueueConnectionSend3Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueConnectionSend4Test() throws java.lang.Exception {
            super.closedQueueConnectionSend4Test();
        }


}