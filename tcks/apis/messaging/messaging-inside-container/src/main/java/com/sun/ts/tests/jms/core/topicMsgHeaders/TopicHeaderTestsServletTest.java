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
public class TopicHeaderTestsServletTest extends com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests {
    static final String VEHICLE_ARCHIVE = "topicMsgHeaders_servlet_vehicle";

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
        War:

        /com/sun/ts/tests/jms/core/topicMsgHeaders/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive topicMsgHeaders_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "topicMsgHeaders_servlet_vehicle_web.war");
            // The class files
            topicMsgHeaders_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests.class
            );
            // The web.xml descriptor
            URL warResURL = TopicHeaderTests.class.getResource("/com/sun/ts/tests/jms/core/topicMsgHeaders/servlet_vehicle_web.xml");
            if(warResURL != null) {
              topicMsgHeaders_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = TopicHeaderTests.class.getResource("/com/sun/ts/tests/jms/core/topicMsgHeaders/topicMsgHeaders_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              topicMsgHeaders_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(topicMsgHeaders_servlet_vehicle_web, TopicHeaderTests.class, warResURL);

        // Ear
            EnterpriseArchive topicMsgHeaders_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topicMsgHeaders_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topicMsgHeaders_servlet_vehicle_ear.addAsModule(topicMsgHeaders_servlet_vehicle_web);



        return topicMsgHeaders_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrIDTopicTest() throws java.lang.Exception {
            super.msgHdrIDTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrTimeStampTopicTest() throws java.lang.Exception {
            super.msgHdrTimeStampTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrCorlIdTopicTest() throws java.lang.Exception {
            super.msgHdrCorlIdTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrReplyToTopicTest() throws java.lang.Exception {
            super.msgHdrReplyToTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSTypeTopicTest() throws java.lang.Exception {
            super.msgHdrJMSTypeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSPriorityTopicTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSExpirationTopicTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSDestinationTopicTest() throws java.lang.Exception {
            super.msgHdrJMSDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSDeliveryModeTopicTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTopicTest();
        }


}