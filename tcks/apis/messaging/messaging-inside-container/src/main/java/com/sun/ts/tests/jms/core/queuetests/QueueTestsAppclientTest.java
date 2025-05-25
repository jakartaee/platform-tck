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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueueTestsAppclientTest extends com.sun.ts.tests.jms.core.queuetests.QueueTests {
    static final String VEHICLE_ARCHIVE = "queuetests_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core/queuetests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive queuetests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "queuetests_appclient_vehicle_client.jar");
            // The class files
            queuetests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
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
            // The application-client.xml descriptor
            URL resURL = QueueTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              queuetests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = QueueTests.class.getResource("queuetests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              queuetests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            queuetests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + QueueTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(queuetests_appclient_vehicle_client, QueueTests.class, resURL);

        // Ear
            EnterpriseArchive queuetests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queuetests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queuetests_appclient_vehicle_ear.addAsModule(queuetests_appclient_vehicle_client);



        return queuetests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void emptyMsgsQueueTest() throws java.lang.Exception {
            super.emptyMsgsQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void autoAckQueueTest() throws java.lang.Exception {
            super.autoAckQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void simpleSendReceiveQueueTest() throws java.lang.Exception {
            super.simpleSendReceiveQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageOrderQueueTest() throws java.lang.Exception {
            super.messageOrderQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void temporaryQueueNotConsumableTest() throws java.lang.Exception {
            super.temporaryQueueNotConsumableTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageSelectorMsgRemainsOnQueueTest() throws java.lang.Exception {
            super.messageSelectorMsgRemainsOnQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgSelectorMsgHeaderQueueTest() throws java.lang.Exception {
            super.msgSelectorMsgHeaderQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void queueBrowserMsgsRemainOnQueueTest() throws java.lang.Exception {
            super.queueBrowserMsgsRemainOnQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inactiveClientReceiveQueueTest() throws java.lang.Exception {
            super.inactiveClientReceiveQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgProducerNullDestinationQueueTest() throws java.lang.Exception {
            super.msgProducerNullDestinationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void multipleCloseQueueConnectionTest() throws java.lang.Exception {
            super.multipleCloseQueueConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageOrderDeliveryModeQueueTest() throws java.lang.Exception {
            super.messageOrderDeliveryModeQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void tempQueueTests() throws java.lang.Exception {
            super.tempQueueTests();
        }


}