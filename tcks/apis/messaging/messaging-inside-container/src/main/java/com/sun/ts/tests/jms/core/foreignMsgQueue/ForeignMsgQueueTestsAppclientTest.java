package com.sun.ts.tests.jms.core.foreignMsgQueue;

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
public class ForeignMsgQueueTestsAppclientTest extends com.sun.ts.tests.jms.core.foreignMsgQueue.ForeignMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "foreignMsgQueue_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        foreignMsgQueue_appclient_vehicle: 
        foreignMsgQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        foreignMsgQueue_ejb_vehicle: 
        foreignMsgQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        foreignMsgQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        foreignMsgQueue_jsp_vehicle: 
        foreignMsgQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        foreignMsgQueue_servlet_vehicle: 
        foreignMsgQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/foreignMsgQueue/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive foreignMsgQueue_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "foreignMsgQueue_appclient_vehicle_client.jar");
            // The class files
            foreignMsgQueue_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.foreignMsgQueue.ForeignMsgQueueTests.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
            com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
            EETest.class,
            com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = ForeignMsgQueueTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              foreignMsgQueue_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ForeignMsgQueueTests.class.getResource("foreignMsgQueue_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              foreignMsgQueue_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            foreignMsgQueue_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ForeignMsgQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(foreignMsgQueue_appclient_vehicle_client, ForeignMsgQueueTests.class, resURL);

        // Ear
            EnterpriseArchive foreignMsgQueue_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "foreignMsgQueue_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            foreignMsgQueue_appclient_vehicle_ear.addAsModule(foreignMsgQueue_appclient_vehicle_client);



        return foreignMsgQueue_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveBytesMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveBytesMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveMapMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveMapMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveObjectMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveObjectMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveStreamMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveStreamMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveTextMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveTextMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSDestinationQueueTest() throws java.lang.Exception {
            super.sendSetsJMSDestinationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSExpirationQueueTest() throws java.lang.Exception {
            super.sendSetsJMSExpirationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSPriorityQueueTest() throws java.lang.Exception {
            super.sendSetsJMSPriorityQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSMessageIDQueueTest() throws java.lang.Exception {
            super.sendSetsJMSMessageIDQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSTimestampQueueTest() throws java.lang.Exception {
            super.sendSetsJMSTimestampQueueTest();
        }


}