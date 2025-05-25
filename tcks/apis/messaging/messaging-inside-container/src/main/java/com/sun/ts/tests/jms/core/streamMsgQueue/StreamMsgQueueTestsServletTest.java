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
public class StreamMsgQueueTestsServletTest extends com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "streamMsgQueue_servlet_vehicle";

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

        /com/sun/ts/tests/jms/core/streamMsgQueue/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive streamMsgQueue_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "streamMsgQueue_servlet_vehicle_web.war");
            // The class files
            streamMsgQueue_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
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
            URL warResURL = StreamMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/streamMsgQueue/servlet_vehicle_web.xml");
            if(warResURL != null) {
              streamMsgQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = StreamMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/streamMsgQueue/streamMsgQueue_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              streamMsgQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(streamMsgQueue_servlet_vehicle_web, StreamMsgQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive streamMsgQueue_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "streamMsgQueue_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            streamMsgQueue_servlet_vehicle_ear.addAsModule(streamMsgQueue_servlet_vehicle_web);



        return streamMsgQueue_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.streamMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsByte() throws java.lang.Exception {
            super.streamMessageConversionQTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsShort() throws java.lang.Exception {
            super.streamMessageConversionQTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsInt() throws java.lang.Exception {
            super.streamMessageConversionQTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsLong() throws java.lang.Exception {
            super.streamMessageConversionQTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsFloat() throws java.lang.Exception {
            super.streamMessageConversionQTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsDouble() throws java.lang.Exception {
            super.streamMessageConversionQTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsString() throws java.lang.Exception {
            super.streamMessageConversionQTestsString();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsChar() throws java.lang.Exception {
            super.streamMessageConversionQTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsBytes() throws java.lang.Exception {
            super.streamMessageConversionQTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.streamMessageConversionQTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageQTestsFullMsg() throws java.lang.Exception {
            super.streamMessageQTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageQTestNull() throws java.lang.Exception {
            super.streamMessageQTestNull();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void streamMessageQNotWritable() throws java.lang.Exception {
            super.streamMessageQNotWritable();
        }


}