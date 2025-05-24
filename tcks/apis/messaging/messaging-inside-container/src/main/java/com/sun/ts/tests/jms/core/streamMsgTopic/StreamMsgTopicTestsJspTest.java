package com.sun.ts.tests.jms.core.streamMsgTopic;

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
public class StreamMsgTopicTestsJspTest extends com.sun.ts.tests.jms.core.streamMsgTopic.StreamMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "streamMsgTopic_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        streamMsgTopic_appclient_vehicle: 
        streamMsgTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgTopic_ejb_vehicle: 
        streamMsgTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        streamMsgTopic_jsp_vehicle: 
        streamMsgTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        streamMsgTopic_servlet_vehicle: 
        streamMsgTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/streamMsgTopic/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive streamMsgTopic_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "streamMsgTopic_jsp_vehicle_web.war");
            // The class files
            streamMsgTopic_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.streamMsgTopic.StreamMsgTopicTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = StreamMsgTopicTests.class.getResource("/com/sun/ts/tests/jms/core/streamMsgTopic/jsp_vehicle_web.xml");
            if(warResURL != null) {
              streamMsgTopic_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = StreamMsgTopicTests.class.getResource("/com/sun/ts/tests/jms/core/streamMsgTopic/streamMsgTopic_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              streamMsgTopic_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = StreamMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              streamMsgTopic_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = StreamMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              streamMsgTopic_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(streamMsgTopic_jsp_vehicle_web, StreamMsgTopicTests.class, warResURL);

        // Ear
            EnterpriseArchive streamMsgTopic_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "streamMsgTopic_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            streamMsgTopic_jsp_vehicle_ear.addAsModule(streamMsgTopic_jsp_vehicle_web);



        return streamMsgTopic_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsBoolean() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsByte() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsShort() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsInt() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsLong() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsFloat() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsDouble() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsString() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsString();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsChar() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsBytes() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionTopicTestsInvFormatString() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageTopicTestsFullMsg() throws java.lang.Exception {
            super.streamMessageTopicTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageTopicTestNull() throws java.lang.Exception {
            super.streamMessageTopicTestNull();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageQNotWritable() throws java.lang.Exception {
            super.streamMessageQNotWritable();
        }


}