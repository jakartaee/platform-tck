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
public class ClosedQueueConnectionTestsAppclientTest extends com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedQueueConnection_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core/closedQueueConnection/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedQueueConnection_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedQueueConnection_appclient_vehicle_client.jar");
            // The class files
            closedQueueConnection_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedQueueConnectionTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              closedQueueConnection_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedQueueConnectionTests.class.getResource("closedQueueConnection_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedQueueConnection_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            closedQueueConnection_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedQueueConnectionTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedQueueConnection_appclient_vehicle_client, ClosedQueueConnectionTests.class, resURL);

        // Ear
            EnterpriseArchive closedQueueConnection_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueConnection_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueConnection_appclient_vehicle_ear.addAsModule(closedQueueConnection_appclient_vehicle_client);



        return closedQueueConnection_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCommitTest() throws java.lang.Exception {
            super.closedQueueConnectionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetTransactedTest() throws java.lang.Exception {
            super.closedQueueConnectionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionRollbackTest() throws java.lang.Exception {
            super.closedQueueConnectionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionRecoverTest() throws java.lang.Exception {
            super.closedQueueConnectionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetClientIDTest() throws java.lang.Exception {
            super.closedQueueConnectionGetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetMetaDataTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionStartTest() throws java.lang.Exception {
            super.closedQueueConnectionStartTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateQueueSessionTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateQueueSessionTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSessionCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateBrowserTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateBrowserMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBrowserMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateReceiverTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateReceiverTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateReceiverMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateReceiverMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateSenderTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateSenderTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateTempQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateTempQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateMapMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateTextMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionCreateText2MessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionReceiverCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiverCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionReceiveTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionReceiverGetQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiverGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSenderCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionSenderCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetPriorityTest() throws java.lang.Exception {
            super.closedQueueConnectionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueConnectionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetPriorityTest() throws java.lang.Exception {
            super.closedQueueConnectionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueConnectionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSenderGetQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionSenderGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSend1Test() throws java.lang.Exception {
            super.closedQueueConnectionSend1Test();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSend2Test() throws java.lang.Exception {
            super.closedQueueConnectionSend2Test();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSend3Test() throws java.lang.Exception {
            super.closedQueueConnectionSend3Test();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSend4Test() throws java.lang.Exception {
            super.closedQueueConnectionSend4Test();
        }


}