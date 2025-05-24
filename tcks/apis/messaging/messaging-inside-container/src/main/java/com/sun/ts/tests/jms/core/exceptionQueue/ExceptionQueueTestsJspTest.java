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
public class ExceptionQueueTestsJspTest extends com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests {
    static final String VEHICLE_ARCHIVE = "exceptionQueue_jsp_vehicle";

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

        /com/sun/ts/tests/jms/core/exceptionQueue/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive exceptionQueue_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "exceptionQueue_jsp_vehicle_web.war");
            // The class files
            exceptionQueue_jsp_vehicle_web.addClasses(
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
            URL warResURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/jms/core/exceptionQueue/jsp_vehicle_web.xml");
            if(warResURL != null) {
              exceptionQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/jms/core/exceptionQueue/exceptionQueue_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              exceptionQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              exceptionQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              exceptionQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(exceptionQueue_jsp_vehicle_web, ExceptionQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive exceptionQueue_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptionQueue_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptionQueue_jsp_vehicle_ear.addAsModule(exceptionQueue_jsp_vehicle_web);



        return exceptionQueue_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xInvalidDestinationExceptionQTest() throws java.lang.Exception {
            super.xInvalidDestinationExceptionQTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotReadableExceptionQueueTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotWriteableExceptionQTestforTextMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforTextMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotWriteableExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotWriteableExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotWriteableExceptionQTestforMapMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforMapMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xNullPointerExceptionQueueTest() throws java.lang.Exception {
            super.xNullPointerExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageEOFExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageEOFExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageFormatExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageFormatExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xInvalidSelectorExceptionQueueTest() throws java.lang.Exception {
            super.xInvalidSelectorExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xIllegalStateExceptionQueueTest() throws java.lang.Exception {
            super.xIllegalStateExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xUnsupportedOperationExceptionQTest1() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xUnsupportedOperationExceptionQTest2() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xUnsupportedOperationExceptionQTest3() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest3();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xInvalidDestinationExceptionQTests() throws java.lang.Exception {
            super.xInvalidDestinationExceptionQTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotReadableExceptionQBytesMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQBytesMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xMessageNotReadableExceptionQStreamMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQStreamMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xIllegalStateExceptionTestTopicMethodsQ() throws java.lang.Exception {
            super.xIllegalStateExceptionTestTopicMethodsQ();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void xIllegalStateExceptionTestRollbackQ() throws java.lang.Exception {
            super.xIllegalStateExceptionTestRollbackQ();
        }


}