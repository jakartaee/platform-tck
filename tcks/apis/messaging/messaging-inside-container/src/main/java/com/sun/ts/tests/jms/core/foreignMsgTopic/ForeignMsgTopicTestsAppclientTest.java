package com.sun.ts.tests.jms.core.foreignMsgTopic;

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
public class ForeignMsgTopicTestsAppclientTest extends com.sun.ts.tests.jms.core.foreignMsgTopic.ForeignMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "foreignMsgTopic_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        foreignMsgTopic_appclient_vehicle: 
        foreignMsgTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        foreignMsgTopic_ejb_vehicle: 
        foreignMsgTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        foreignMsgTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        foreignMsgTopic_jsp_vehicle: 
        foreignMsgTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        foreignMsgTopic_servlet_vehicle: 
        foreignMsgTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/foreignMsgTopic/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive foreignMsgTopic_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "foreignMsgTopic_appclient_vehicle_client.jar");
            // The class files
            foreignMsgTopic_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
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
            com.sun.ts.tests.jms.core.foreignMsgTopic.ForeignMsgTopicTests.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = ForeignMsgTopicTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              foreignMsgTopic_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ForeignMsgTopicTests.class.getResource("foreignMsgTopic_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              foreignMsgTopic_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            foreignMsgTopic_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ForeignMsgTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(foreignMsgTopic_appclient_vehicle_client, ForeignMsgTopicTests.class, resURL);

        // Ear
            EnterpriseArchive foreignMsgTopic_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "foreignMsgTopic_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            foreignMsgTopic_appclient_vehicle_ear.addAsModule(foreignMsgTopic_appclient_vehicle_client);



        return foreignMsgTopic_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveBytesMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveBytesMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveMapMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveMapMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveObjectMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveObjectMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveStreamMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveStreamMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendReceiveTextMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveTextMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSDestinationTopicTest() throws java.lang.Exception {
            super.sendSetsJMSDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSExpirationTopicTest() throws java.lang.Exception {
            super.sendSetsJMSExpirationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSPriorityTopicTest() throws java.lang.Exception {
            super.sendSetsJMSPriorityTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSMessageIDTopicTest() throws java.lang.Exception {
            super.sendSetsJMSMessageIDTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendSetsJMSTimestampTopicTest() throws java.lang.Exception {
            super.sendSetsJMSTimestampTopicTest();
        }


}