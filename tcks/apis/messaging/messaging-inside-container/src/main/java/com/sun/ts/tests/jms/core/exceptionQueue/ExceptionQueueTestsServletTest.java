package com.sun.ts.tests.jms.core.exceptionQueue;

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
public class ExceptionQueueTestsServletTest extends com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests {
    static final String VEHICLE_ARCHIVE = "exceptionQueue_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        exceptionQueue_appclient_vehicle: 
        exceptionQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionQueue_ejb_vehicle: 
        exceptionQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        exceptionQueue_jsp_vehicle: 
        exceptionQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        exceptionQueue_servlet_vehicle: 
        exceptionQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/exceptionQueue/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive exceptionQueue_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "exceptionQueue_servlet_vehicle_web.war");
            // The class files
            exceptionQueue_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/jms/core/exceptionQueue/servlet_vehicle_web.xml");
            if(warResURL != null) {
              exceptionQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/jms/core/exceptionQueue/exceptionQueue_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              exceptionQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(exceptionQueue_servlet_vehicle_web, ExceptionQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive exceptionQueue_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptionQueue_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptionQueue_servlet_vehicle_ear.addAsModule(exceptionQueue_servlet_vehicle_web);



        return exceptionQueue_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xInvalidDestinationExceptionQTest() throws java.lang.Exception {
            super.xInvalidDestinationExceptionQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotReadableExceptionQueueTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionQTestforTextMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforTextMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotWriteableExceptionQTestforMapMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforMapMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xNullPointerExceptionQueueTest() throws java.lang.Exception {
            super.xNullPointerExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageEOFExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageEOFExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageFormatExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageFormatExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xInvalidSelectorExceptionQueueTest() throws java.lang.Exception {
            super.xInvalidSelectorExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xIllegalStateExceptionQueueTest() throws java.lang.Exception {
            super.xIllegalStateExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xUnsupportedOperationExceptionQTest1() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xUnsupportedOperationExceptionQTest2() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest2();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xUnsupportedOperationExceptionQTest3() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest3();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xInvalidDestinationExceptionQTests() throws java.lang.Exception {
            super.xInvalidDestinationExceptionQTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotReadableExceptionQBytesMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQBytesMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xMessageNotReadableExceptionQStreamMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQStreamMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xIllegalStateExceptionTestTopicMethodsQ() throws java.lang.Exception {
            super.xIllegalStateExceptionTestTopicMethodsQ();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void xIllegalStateExceptionTestRollbackQ() throws java.lang.Exception {
            super.xIllegalStateExceptionTestRollbackQ();
        }


}