package com.sun.ts.tests.jms.core.queuetests;

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
public class QueueTestsServletTest extends com.sun.ts.tests.jms.core.queuetests.QueueTests {
    static final String VEHICLE_ARCHIVE = "queuetests_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        queuetests_appclient_vehicle: 
        queuetests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        queuetests_ejb_vehicle: 
        queuetests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queuetests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queuetests_jsp_vehicle: 
        queuetests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queuetests_servlet_vehicle: 
        queuetests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/queuetests/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive queuetests_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "queuetests_servlet_vehicle_web.war");
            // The class files
            queuetests_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
                                com.sun.ts.tests.jms.common.SessionThread.class,
                                com.sun.ts.tests.jms.common.SerialTestMessageListenerImpl.class,
                                com.sun.ts.tests.jms.common.DoneLatch.class,
                                com.sun.ts.tests.jms.common.TestMessageListener.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.SerialTestMessageListenerImpl.class,
            com.sun.ts.tests.jms.common.TestMessageListener.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.core.queuetests.QueueTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.common.DoneLatch.class,
            com.sun.ts.tests.jms.common.SessionThread.class
            );
            // The web.xml descriptor
            URL warResURL = QueueTests.class.getResource("/com/sun/ts/tests/jms/core/queuetests/servlet_vehicle_web.xml");
            if(warResURL != null) {
              queuetests_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = QueueTests.class.getResource("/com/sun/ts/tests/jms/core/queuetests/queuetests_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              queuetests_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(queuetests_servlet_vehicle_web, QueueTests.class, warResURL);

        // Ear
            EnterpriseArchive queuetests_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queuetests_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queuetests_servlet_vehicle_ear.addAsModule(queuetests_servlet_vehicle_web);



        return queuetests_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void emptyMsgsQueueTest() throws java.lang.Exception {
            super.emptyMsgsQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void autoAckQueueTest() throws java.lang.Exception {
            super.autoAckQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void simpleSendReceiveQueueTest() throws java.lang.Exception {
            super.simpleSendReceiveQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void messageOrderQueueTest() throws java.lang.Exception {
            super.messageOrderQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void temporaryQueueNotConsumableTest() throws java.lang.Exception {
            super.temporaryQueueNotConsumableTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void messageSelectorMsgRemainsOnQueueTest() throws java.lang.Exception {
            super.messageSelectorMsgRemainsOnQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgSelectorMsgHeaderQueueTest() throws java.lang.Exception {
            super.msgSelectorMsgHeaderQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void queueBrowserMsgsRemainOnQueueTest() throws java.lang.Exception {
            super.queueBrowserMsgsRemainOnQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void inactiveClientReceiveQueueTest() throws java.lang.Exception {
            super.inactiveClientReceiveQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgProducerNullDestinationQueueTest() throws java.lang.Exception {
            super.msgProducerNullDestinationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void multipleCloseQueueConnectionTest() throws java.lang.Exception {
            super.multipleCloseQueueConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void messageOrderDeliveryModeQueueTest() throws java.lang.Exception {
            super.messageOrderDeliveryModeQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void tempQueueTests() throws java.lang.Exception {
            super.tempQueueTests();
        }


}