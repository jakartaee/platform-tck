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
public class ClosedTopicConnectionTestsEjbTest extends com.sun.ts.tests.jms.core.closedTopicConnection.ClosedTopicConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedTopicConnection_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/closedTopicConnection/ejb_vehicle_ejb.xml
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
            JavaArchive closedTopicConnection_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedTopicConnection_ejb_vehicle_client.jar");
            // The class files
            closedTopicConnection_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.closedTopicConnection.ClosedTopicConnectionTests.class,
                                          com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedTopicConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              closedTopicConnection_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedTopicConnectionTests.class.getResource("closedTopicConnection_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedTopicConnection_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //closedTopicConnection_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedTopicConnectionTests.class.getName() + "\n"), "MANIFEST.MF");
            closedTopicConnection_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedTopicConnection_ejb_vehicle_client, ClosedTopicConnectionTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive closedTopicConnection_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "closedTopicConnection_ejb_vehicle_ejb.jar");
            // The class files
            closedTopicConnection_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class,
                com.sun.ts.tests.jms.common.MessageTestImpl.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jms.core.closedTopicConnection.ClosedTopicConnectionTests.class,
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                              com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ClosedTopicConnectionTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              closedTopicConnection_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ClosedTopicConnectionTests.class.getResource("closedTopicConnection_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              closedTopicConnection_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(closedTopicConnection_ejb_vehicle_ejb, ClosedTopicConnectionTests.class, ejbResURL);

        // Ear
            EnterpriseArchive closedTopicConnection_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicConnection_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicConnection_ejb_vehicle_ear.addAsModule(closedTopicConnection_ejb_vehicle_ejb);
            closedTopicConnection_ejb_vehicle_ear.addAsModule(closedTopicConnection_ejb_vehicle_client);



        return closedTopicConnection_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCommitTest() throws java.lang.Exception {
            super.closedTopicConnectionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionRollbackTest() throws java.lang.Exception {
            super.closedTopicConnectionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionRecoverTest() throws java.lang.Exception {
            super.closedTopicConnectionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetClientIDTest() throws java.lang.Exception {
            super.closedTopicConnectionGetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetMetaDataTest() throws java.lang.Exception {
            super.closedTopicConnectionGetMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionStartTest() throws java.lang.Exception {
            super.closedTopicConnectionStartTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateTopicSessionTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTopicSessionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSessionCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateSubscriberTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreatePublisherTest() throws java.lang.Exception {
            super.closedTopicConnectionCreatePublisherTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateTempTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTempTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateMapMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateTextMessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionCreateText2MessageTest() throws java.lang.Exception {
            super.closedTopicConnectionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetTransactedTest() throws java.lang.Exception {
            super.closedTopicConnectionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicConnectionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionReceiveTest() throws java.lang.Exception {
            super.closedTopicConnectionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicConnectionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicConnectionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicConnectionGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionSubscriberGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicConnectionPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicConnectionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicConnectionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicConnectionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetPriorityTest() throws java.lang.Exception {
            super.closedTopicConnectionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicConnectionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicConnectionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicConnectionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicConnectionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSetPriorityTest() throws java.lang.Exception {
            super.closedTopicConnectionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicConnectionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicConnectionPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionPublish1Test() throws java.lang.Exception {
            super.closedTopicConnectionPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicConnectionPublish2Test() throws java.lang.Exception {
            super.closedTopicConnectionPublish2Test();
        }


}