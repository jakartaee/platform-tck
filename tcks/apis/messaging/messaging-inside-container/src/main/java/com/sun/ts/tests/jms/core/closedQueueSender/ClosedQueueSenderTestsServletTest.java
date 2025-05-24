package com.sun.ts.tests.jms.core.closedQueueSender;

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
public class ClosedQueueSenderTestsServletTest extends com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTests {
    static final String VEHICLE_ARCHIVE = "closedQueueSender_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueSender_appclient_vehicle: 
        closedQueueSender_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSender_ejb_vehicle: 
        closedQueueSender_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSender_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueSender_jsp_vehicle: 
        closedQueueSender_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueSender_servlet_vehicle: 
        closedQueueSender_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedQueueSender/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedQueueSender_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedQueueSender_servlet_vehicle_web.war");
            // The class files
            closedQueueSender_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
                                             com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTests.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedQueueSenderTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueSender/servlet_vehicle_web.xml");
            if(warResURL != null) {
              closedQueueSender_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedQueueSenderTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueSender/closedQueueSender_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedQueueSender_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(closedQueueSender_servlet_vehicle_web, ClosedQueueSenderTests.class, warResURL);

        // Ear
            EnterpriseArchive closedQueueSender_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueSender_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueSender_servlet_vehicle_ear.addAsModule(closedQueueSender_servlet_vehicle_web);



        return closedQueueSender_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderCloseTest() throws java.lang.Exception {
            super.closedQueueSenderCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderGetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueSenderGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueSenderGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueSenderGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderGetPriorityTest() throws java.lang.Exception {
            super.closedQueueSenderGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderGetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueSenderGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSetDeliveryModeTest() throws java.lang.Exception {
            super.closedQueueSenderSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedQueueSenderSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedQueueSenderSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSetPriorityTest() throws java.lang.Exception {
            super.closedQueueSenderSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSetTimeToLiveTest() throws java.lang.Exception {
            super.closedQueueSenderSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderGetQueueTest() throws java.lang.Exception {
            super.closedQueueSenderGetQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSend1Test() throws java.lang.Exception {
            super.closedQueueSenderSend1Test();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSend2Test() throws java.lang.Exception {
            super.closedQueueSenderSend2Test();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSend3Test() throws java.lang.Exception {
            super.closedQueueSenderSend3Test();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void closedQueueSenderSend4Test() throws java.lang.Exception {
            super.closedQueueSenderSend4Test();
        }


}