package com.sun.ts.tests.jms.core.streamMsgQueue;

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
public class StreamMsgQueueTestsJspTest extends com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "streamMsgQueue_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        streamMsgQueue_appclient_vehicle: 
        streamMsgQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgQueue_ejb_vehicle: 
        streamMsgQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        streamMsgQueue_jsp_vehicle: 
        streamMsgQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        streamMsgQueue_servlet_vehicle: 
        streamMsgQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/streamMsgQueue/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive streamMsgQueue_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "streamMsgQueue_jsp_vehicle_web.war");
            // The class files
            streamMsgQueue_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTests.class,
                                             com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = StreamMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/streamMsgQueue/jsp_vehicle_web.xml");
            if(warResURL != null) {
              streamMsgQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = StreamMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/streamMsgQueue/streamMsgQueue_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              streamMsgQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = StreamMsgQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              streamMsgQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = StreamMsgQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              streamMsgQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(streamMsgQueue_jsp_vehicle_web, StreamMsgQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive streamMsgQueue_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "streamMsgQueue_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            streamMsgQueue_jsp_vehicle_ear.addAsModule(streamMsgQueue_jsp_vehicle_web);



        return streamMsgQueue_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.streamMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsByte() throws java.lang.Exception {
            super.streamMessageConversionQTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsShort() throws java.lang.Exception {
            super.streamMessageConversionQTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsInt() throws java.lang.Exception {
            super.streamMessageConversionQTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsLong() throws java.lang.Exception {
            super.streamMessageConversionQTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsFloat() throws java.lang.Exception {
            super.streamMessageConversionQTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsDouble() throws java.lang.Exception {
            super.streamMessageConversionQTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsString() throws java.lang.Exception {
            super.streamMessageConversionQTestsString();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsChar() throws java.lang.Exception {
            super.streamMessageConversionQTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsBytes() throws java.lang.Exception {
            super.streamMessageConversionQTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.streamMessageConversionQTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageQTestsFullMsg() throws java.lang.Exception {
            super.streamMessageQTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageQTestNull() throws java.lang.Exception {
            super.streamMessageQTestNull();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void streamMessageQNotWritable() throws java.lang.Exception {
            super.streamMessageQNotWritable();
        }


}