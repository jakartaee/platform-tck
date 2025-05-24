package com.sun.ts.tests.jms.core.topicMsgHeaders;

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
public class TopicHeaderTestsAppclientTest extends com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests {
    static final String VEHICLE_ARCHIVE = "topicMsgHeaders_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        topicMsgHeaders_appclient_vehicle: 
        topicMsgHeaders_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicMsgHeaders_ejb_vehicle: 
        topicMsgHeaders_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicMsgHeaders_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        topicMsgHeaders_jsp_vehicle: 
        topicMsgHeaders_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        topicMsgHeaders_servlet_vehicle: 
        topicMsgHeaders_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/topicMsgHeaders/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive topicMsgHeaders_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "topicMsgHeaders_appclient_vehicle_client.jar");
            // The class files
            topicMsgHeaders_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests.class
            );
            // The application-client.xml descriptor
            URL resURL = TopicHeaderTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              topicMsgHeaders_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TopicHeaderTests.class.getResource("topicMsgHeaders_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              topicMsgHeaders_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            topicMsgHeaders_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TopicHeaderTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(topicMsgHeaders_appclient_vehicle_client, TopicHeaderTests.class, resURL);

        // Ear
            EnterpriseArchive topicMsgHeaders_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topicMsgHeaders_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topicMsgHeaders_appclient_vehicle_ear.addAsModule(topicMsgHeaders_appclient_vehicle_client);



        return topicMsgHeaders_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrIDTopicTest() throws java.lang.Exception {
            super.msgHdrIDTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrTimeStampTopicTest() throws java.lang.Exception {
            super.msgHdrTimeStampTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrCorlIdTopicTest() throws java.lang.Exception {
            super.msgHdrCorlIdTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrReplyToTopicTest() throws java.lang.Exception {
            super.msgHdrReplyToTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSTypeTopicTest() throws java.lang.Exception {
            super.msgHdrJMSTypeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSPriorityTopicTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSExpirationTopicTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSDestinationTopicTest() throws java.lang.Exception {
            super.msgHdrJMSDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSDeliveryModeTopicTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTopicTest();
        }


}