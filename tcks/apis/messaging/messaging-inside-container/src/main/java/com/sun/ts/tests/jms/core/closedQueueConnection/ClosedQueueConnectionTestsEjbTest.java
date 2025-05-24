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
public class ClosedQueueConnectionTestsEjbTest extends com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedQueueConnection_ejb_vehicle";

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

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/closedQueueConnection/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedQueueConnection_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedQueueConnection_ejb_vehicle_client.jar");
            // The class files
            closedQueueConnection_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedQueueConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              closedQueueConnection_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedQueueConnectionTests.class.getResource("closedQueueConnection_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedQueueConnection_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //closedQueueConnection_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedQueueConnectionTests.class.getName() + "\n"), "MANIFEST.MF");
            closedQueueConnection_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedQueueConnection_ejb_vehicle_client, ClosedQueueConnectionTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive closedQueueConnection_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "closedQueueConnection_ejb_vehicle_ejb.jar");
            // The class files
            closedQueueConnection_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class,
                com.sun.ts.tests.jms.common.MessageTestImpl.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                EETest.class,
                com.sun.ts.tests.jms.core.closedQueueConnection.ClosedQueueConnectionTests.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                              com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ClosedQueueConnectionTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              closedQueueConnection_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ClosedQueueConnectionTests.class.getResource("closedQueueConnection_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              closedQueueConnection_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(closedQueueConnection_ejb_vehicle_ejb, ClosedQueueConnectionTests.class, ejbResURL);

        // Ear
            EnterpriseArchive closedQueueConnection_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueConnection_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueConnection_ejb_vehicle_ear.addAsModule(closedQueueConnection_ejb_vehicle_ejb);
            closedQueueConnection_ejb_vehicle_ear.addAsModule(closedQueueConnection_ejb_vehicle_client);



        return closedQueueConnection_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCommitTest() throws java.lang.Exception {
            super.closedQueueConnectionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetTransactedTest() throws java.lang.Exception {
            super.closedQueueConnectionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionRollbackTest() throws java.lang.Exception {
            super.closedQueueConnectionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionRecoverTest() throws java.lang.Exception {
            super.closedQueueConnectionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetClientIDTest() throws java.lang.Exception {
            super.closedQueueConnectionGetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetMetaDataTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionStartTest() throws java.lang.Exception {
            super.closedQueueConnectionStartTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateQueueSessionTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateQueueSessionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSessionCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateBrowserTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateBrowserMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBrowserMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateReceiverTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateReceiverTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateReceiverMsgSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateReceiverMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateSenderTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateSenderTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateTempQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateTempQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateMapMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateTextMessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionCreateText2MessageTest() throws java.lang.Exception {
            super.closedQueueConnectionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionReceiverCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiverCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionReceiveTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionReceiverGetQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionReceiverGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSenderCloseTest() throws java.lang.Exception {
            super.closedQueueConnectionSenderCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueConnectionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetPriorityTest() throws java.lang.Exception {
            super.closedQueueConnectionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueConnectionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueConnectionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSetPriorityTest() throws java.lang.Exception {
            super.closedQueueConnectionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueConnectionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSenderGetQueueTest() throws java.lang.Exception {
            super.closedQueueConnectionSenderGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSend1Test() throws java.lang.Exception {
            super.closedQueueConnectionSend1Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSend2Test() throws java.lang.Exception {
            super.closedQueueConnectionSend2Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSend3Test() throws java.lang.Exception {
            super.closedQueueConnectionSend3Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedQueueConnectionSend4Test() throws java.lang.Exception {
            super.closedQueueConnectionSend4Test();
        }


}