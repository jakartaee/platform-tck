package com.sun.ts.tests.jms.core.foreignMsgQueue;

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
public class ForeignMsgQueueTestsJspTest extends com.sun.ts.tests.jms.core.foreignMsgQueue.ForeignMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "foreignMsgQueue_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        foreignMsgQueue_appclient_vehicle: 
        foreignMsgQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        foreignMsgQueue_ejb_vehicle: 
        foreignMsgQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        foreignMsgQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        foreignMsgQueue_jsp_vehicle: 
        foreignMsgQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        foreignMsgQueue_servlet_vehicle: 
        foreignMsgQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/foreignMsgQueue/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive foreignMsgQueue_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "foreignMsgQueue_jsp_vehicle_web.war");
            // The class files
            foreignMsgQueue_jsp_vehicle_web.addClasses(
                                com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
                                com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
            com.sun.ts.tests.jms.core.foreignMsgQueue.ForeignMsgQueueTests.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
                                com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
                                com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            EETest.class,
            com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ForeignMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/foreignMsgQueue/jsp_vehicle_web.xml");
            if(warResURL != null) {
              foreignMsgQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ForeignMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/foreignMsgQueue/foreignMsgQueue_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              foreignMsgQueue_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


            warResURL = ForeignMsgQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              foreignMsgQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ForeignMsgQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              foreignMsgQueue_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(foreignMsgQueue_jsp_vehicle_web, ForeignMsgQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive foreignMsgQueue_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "foreignMsgQueue_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            foreignMsgQueue_jsp_vehicle_ear.addAsModule(foreignMsgQueue_jsp_vehicle_web);



        return foreignMsgQueue_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveBytesMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveBytesMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveMapMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveMapMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveObjectMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveObjectMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveStreamMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveStreamMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendReceiveTextMsgQueueTest() throws java.lang.Exception {
            super.sendReceiveTextMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSDestinationQueueTest() throws java.lang.Exception {
            super.sendSetsJMSDestinationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSExpirationQueueTest() throws java.lang.Exception {
            super.sendSetsJMSExpirationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSPriorityQueueTest() throws java.lang.Exception {
            super.sendSetsJMSPriorityQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSMessageIDQueueTest() throws java.lang.Exception {
            super.sendSetsJMSMessageIDQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendSetsJMSTimestampQueueTest() throws java.lang.Exception {
            super.sendSetsJMSTimestampQueueTest();
        }


}