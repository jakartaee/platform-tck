package com.sun.ts.tests.jms.core.exceptionTopic;

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
public class ExceptionTopicTestsServletTest extends com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTests {
    static final String VEHICLE_ARCHIVE = "exceptionTopic_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        exceptionTopic_appclient_vehicle: 
        exceptionTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionTopic_ejb_vehicle: 
        exceptionTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        exceptionTopic_jsp_vehicle: 
        exceptionTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        exceptionTopic_servlet_vehicle: 
        exceptionTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/exceptionTopic/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive exceptionTopic_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "exceptionTopic_servlet_vehicle_web.war");
            // The class files
            exceptionTopic_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ExceptionTopicTests.class.getResource("/com/sun/ts/tests/jms/core/exceptionTopic/servlet_vehicle_web.xml");
            if(warResURL != null) {
              exceptionTopic_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ExceptionTopicTests.class.getResource("/com/sun/ts/tests/jms/core/exceptionTopic/exceptionTopic_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              exceptionTopic_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(exceptionTopic_servlet_vehicle_web, ExceptionTopicTests.class, warResURL);

        // Ear
            EnterpriseArchive exceptionTopic_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptionTopic_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptionTopic_servlet_vehicle_ear.addAsModule(exceptionTopic_servlet_vehicle_web);



        return exceptionTopic_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xInvalidDestinationExceptionTopicTest() throws java.lang.Exception {
            super.xInvalidDestinationExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotReadableExceptionTopicTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionTTestforTextMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTTestforTextMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionTestforBytesMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionTTestforStreamMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionTTestforMapMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTTestforMapMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xNullPointerExceptionTopicTest() throws java.lang.Exception {
            super.xNullPointerExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageEOFExceptionTTestforBytesMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionTTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageEOFExceptionTTestforStreamMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionTTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageFormatExceptionTTestforBytesMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionTTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageFormatExceptionTTestforStreamMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionTTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xInvalidSelectorExceptionTopicTest() throws java.lang.Exception {
            super.xInvalidSelectorExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xIllegalStateExceptionTopicTest() throws java.lang.Exception {
            super.xIllegalStateExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xUnsupportedOperationExceptionTTest1() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionTTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xInvalidDestinationExceptionTTests() throws java.lang.Exception {
            super.xInvalidDestinationExceptionTTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotReadableExceptionTBytesMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionTBytesMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotReadableExceptionTStreamMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionTStreamMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xIllegalStateExceptionTestQueueMethodsT() throws java.lang.Exception {
            super.xIllegalStateExceptionTestQueueMethodsT();
        }


}