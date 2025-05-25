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
public class ForeignMsgTopicTestsJspTest extends com.sun.ts.tests.jms.core.foreignMsgTopic.ForeignMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "foreignMsgTopic_jsp_vehicle";

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
        War:

        /com/sun/ts/tests/jms/core/foreignMsgTopic/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive foreignMsgTopic_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "foreignMsgTopic_jsp_vehicle_web.war");
            // The class files
            foreignMsgTopic_jsp_vehicle_web.addClasses(
                                com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
                                com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
                                 com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            EETest.class,
            com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            ServiceEETest.class,
            com.sun.ts.tests.jms.core.foreignMsgTopic.ForeignMsgTopicTests.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ForeignMsgTopicTests.class.getResource("/com/sun/ts/tests/jms/core/foreignMsgTopic/jsp_vehicle_web.xml");
            if(warResURL != null) {
              foreignMsgTopic_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ForeignMsgTopicTests.class.getResource("/com/sun/ts/tests/jms/core/foreignMsgTopic/foreignMsgTopic_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              foreignMsgTopic_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ForeignMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              foreignMsgTopic_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ForeignMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              foreignMsgTopic_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(foreignMsgTopic_jsp_vehicle_web, ForeignMsgTopicTests.class, warResURL);

        // Ear
            EnterpriseArchive foreignMsgTopic_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "foreignMsgTopic_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            foreignMsgTopic_jsp_vehicle_ear.addAsModule(foreignMsgTopic_jsp_vehicle_web);



        return foreignMsgTopic_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveBytesMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveBytesMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveMapMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveMapMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveObjectMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveObjectMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveStreamMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveStreamMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveTextMsgTopicTest() throws java.lang.Exception {
            super.sendReceiveTextMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSDestinationTopicTest() throws java.lang.Exception {
            super.sendSetsJMSDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSExpirationTopicTest() throws java.lang.Exception {
            super.sendSetsJMSExpirationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSPriorityTopicTest() throws java.lang.Exception {
            super.sendSetsJMSPriorityTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSMessageIDTopicTest() throws java.lang.Exception {
            super.sendSetsJMSMessageIDTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSTimestampTopicTest() throws java.lang.Exception {
            super.sendSetsJMSTimestampTopicTest();
        }


}