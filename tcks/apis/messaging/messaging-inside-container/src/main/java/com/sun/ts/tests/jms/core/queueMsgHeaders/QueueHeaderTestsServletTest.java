package com.sun.ts.tests.jms.core.queueMsgHeaders;

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
public class QueueHeaderTestsServletTest extends com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTests {
    static final String VEHICLE_ARCHIVE = "queueMsgHeaders_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        queueMsgHeaders_appclient_vehicle: 
        queueMsgHeaders_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgHeaders_ejb_vehicle: 
        queueMsgHeaders_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgHeaders_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queueMsgHeaders_jsp_vehicle: 
        queueMsgHeaders_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queueMsgHeaders_servlet_vehicle: 
        queueMsgHeaders_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/queueMsgHeaders/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive queueMsgHeaders_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "queueMsgHeaders_servlet_vehicle_web.war");
            // The class files
            queueMsgHeaders_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = QueueHeaderTests.class.getResource("/com/sun/ts/tests/jms/core/queueMsgHeaders/servlet_vehicle_web.xml");
            if(warResURL != null) {
              queueMsgHeaders_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = QueueHeaderTests.class.getResource("/com/sun/ts/tests/jms/core/queueMsgHeaders/queueMsgHeaders_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              queueMsgHeaders_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(queueMsgHeaders_servlet_vehicle_web, QueueHeaderTests.class, warResURL);

        // Ear
            EnterpriseArchive queueMsgHeaders_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queueMsgHeaders_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queueMsgHeaders_servlet_vehicle_ear.addAsModule(queueMsgHeaders_servlet_vehicle_web);



        return queueMsgHeaders_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrIDQTest() throws java.lang.Exception {
            super.msgHdrIDQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrTimeStampQTest() throws java.lang.Exception {
            super.msgHdrTimeStampQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrCorlIdQTest() throws java.lang.Exception {
            super.msgHdrCorlIdQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrReplyToQTest() throws java.lang.Exception {
            super.msgHdrReplyToQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSTypeQTest() throws java.lang.Exception {
            super.msgHdrJMSTypeQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSPriorityQTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSExpirationQueueTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSDestinationQTest() throws java.lang.Exception {
            super.msgHdrJMSDestinationQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSDeliveryModeQTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSRedeliveredTest() throws java.lang.Exception {
            super.msgHdrJMSRedeliveredTest();
        }


}