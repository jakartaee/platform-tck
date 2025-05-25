package com.sun.ts.tests.jms.core.closedTopicConnection;

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
public class ClosedTopicConnectionTestsJspTest extends com.sun.ts.tests.jms.core.closedTopicConnection.ClosedTopicConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedTopicConnection_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedTopicConnection_appclient_vehicle: 
        closedTopicConnection_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicConnection_ejb_vehicle: 
        closedTopicConnection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicConnection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedTopicConnection_jsp_vehicle: 
        closedTopicConnection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedTopicConnection_servlet_vehicle: 
        closedTopicConnection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedTopicConnection/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedTopicConnection_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedTopicConnection_jsp_vehicle_web.war");
            // The class files
            closedTopicConnection_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.core.closedTopicConnection.ClosedTopicConnectionTests.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
                                             com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedTopicConnectionTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicConnection/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedTopicConnection_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedTopicConnectionTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicConnection/closedTopicConnection_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedTopicConnection_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedTopicConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedTopicConnection_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedTopicConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedTopicConnection_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedTopicConnection_jsp_vehicle_web, ClosedTopicConnectionTests.class, warResURL);

        // Ear
            EnterpriseArchive closedTopicConnection_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicConnection_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicConnection_jsp_vehicle_ear.addAsModule(closedTopicConnection_jsp_vehicle_web);



        return closedTopicConnection_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCommitTest() throws java.lang.Exception {
            super.closedTopicConnectionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionRollbackTest() throws java.lang.Exception {
            super.closedTopicConnectionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionRecoverTest() throws java.lang.Exception {
            super.closedTopicConnectionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetClientIDTest() throws java.lang.Exception {
            super.closedTopicConnectionGetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetMetaDataTest() throws java.lang.Exception {
            super.closedTopicConnectionGetMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionStartTest() throws java.lang.Exception {
            super.closedTopicConnectionStartTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateTopicSessionTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTopicSessionTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSessionCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateSubscriberTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreatePublisherTest() throws java.lang.Exception {
            super.closedTopicConnectionCreatePublisherTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateTempTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTempTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateMapMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateTextMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionCreateText2MessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetTransactedTest() throws java.lang.Exception {
            super.closedTopicConnectionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicConnectionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionReceiveTest() throws java.lang.Exception {
            super.closedTopicConnectionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicConnectionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicConnectionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicConnectionGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionSubscriberGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicConnectionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicConnectionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicConnectionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetPriorityTest() throws java.lang.Exception {
            super.closedTopicConnectionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicConnectionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicConnectionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicConnectionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicConnectionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSetPriorityTest() throws java.lang.Exception {
            super.closedTopicConnectionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicConnectionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionPublish1Test() throws java.lang.Exception {
            super.closedTopicConnectionPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicConnectionPublish2Test() throws java.lang.Exception {
            super.closedTopicConnectionPublish2Test();
        }


}