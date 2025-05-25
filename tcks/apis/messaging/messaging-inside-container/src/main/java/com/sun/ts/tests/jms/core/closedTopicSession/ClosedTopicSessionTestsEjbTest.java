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
public class ClosedTopicSessionTestsEjbTest extends com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests {
    static final String VEHICLE_ARCHIVE = "closedTopicSession_ejb_vehicle";

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

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/closedTopicSession/ejb_vehicle_ejb.xml
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
            JavaArchive closedTopicSession_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedTopicSession_ejb_vehicle_client.jar");
            // The class files
            closedTopicSession_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests.class,
                                          com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedTopicSessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              closedTopicSession_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedTopicSessionTests.class.getResource("closedTopicSession_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedTopicSession_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //closedTopicSession_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedTopicSessionTests.class.getName() + "\n"), "MANIFEST.MF");
            closedTopicSession_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedTopicSession_ejb_vehicle_client, ClosedTopicSessionTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive closedTopicSession_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "closedTopicSession_ejb_vehicle_ejb.jar");
            // The class files
            closedTopicSession_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class,
                com.sun.ts.tests.jms.common.MessageTestImpl.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jms.core.closedTopicSession.ClosedTopicSessionTests.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                              com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ClosedTopicSessionTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              closedTopicSession_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ClosedTopicSessionTests.class.getResource("closedTopicSession_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              closedTopicSession_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(closedTopicSession_ejb_vehicle_ejb, ClosedTopicSessionTests.class, ejbResURL);

        // Ear
            EnterpriseArchive closedTopicSession_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicSession_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicSession_ejb_vehicle_ear.addAsModule(closedTopicSession_ejb_vehicle_ejb);
            closedTopicSession_ejb_vehicle_ear.addAsModule(closedTopicSession_ejb_vehicle_client);



        return closedTopicSession_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCommitTest() throws java.lang.Exception {
            super.closedTopicSessionCommitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCloseTest() throws java.lang.Exception {
            super.closedTopicSessionCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateDurableSubscriberTest() throws java.lang.Exception {
            super.closedTopicSessionCreateDurableSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateDurableSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionCreateDurableSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateTopicTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateSubscriberTest() throws java.lang.Exception {
            super.closedTopicSessionCreateSubscriberTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateSubscriberMsgSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionCreateSubscriberMsgSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreatePublisherTest() throws java.lang.Exception {
            super.closedTopicSessionCreatePublisherTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateTempTopicTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTempTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionUnsubscribeTest() throws java.lang.Exception {
            super.closedTopicSessionUnsubscribeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateBytesMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateBytesMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateMapMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateMapMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateObjectMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateObjectMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateObject2MessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateObject2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateStreamMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateStreamMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateTextMessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateTextMessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionCreateText2MessageTest() throws java.lang.Exception {
            super.closedTopicSessionCreateText2MessageTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetTransactedTest() throws java.lang.Exception {
            super.closedTopicSessionGetTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionRollbackTest() throws java.lang.Exception {
            super.closedTopicSessionRollbackTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionRecoverTest() throws java.lang.Exception {
            super.closedTopicSessionRecoverTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicSessionSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicSessionGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionReceiveTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicSessionReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicSessionGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicSessionSubscriberGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicSessionPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicSessionGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicSessionGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicSessionGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetPriorityTest() throws java.lang.Exception {
            super.closedTopicSessionGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicSessionGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicSessionSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicSessionSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicSessionSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSetPriorityTest() throws java.lang.Exception {
            super.closedTopicSessionSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicSessionSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicSessionPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionPublish1Test() throws java.lang.Exception {
            super.closedTopicSessionPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicSessionPublish2Test() throws java.lang.Exception {
            super.closedTopicSessionPublish2Test();
        }


}