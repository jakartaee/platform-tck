package com.sun.ts.tests.jms.core.appclient.topictests;

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
public class TopicTestsAppclientTest extends com.sun.ts.tests.jms.core.appclient.topictests.TopicTests {
    static final String VEHICLE_ARCHIVE = "topictests_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        topictests_appclient_vehicle: 
        topictests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        topictests_ejb_vehicle: 
        topictests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topictests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        topictests_jsp_vehicle: 
        topictests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        topictests_servlet_vehicle: 
        topictests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/appclient/topictests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive topictests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "topictests_appclient_vehicle_client.jar");
            // The class files
            topictests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.core.appclient.topictests.TopicTests.class,
                                          com.sun.ts.tests.jms.common.DoneLatch.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.appclient.topictests.TopicTests.RequestorMsgListener.class,
                                          com.sun.ts.tests.jms.common.SessionThread.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
                                          com.sun.ts.tests.jms.common.TestMessageListener.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.SerialTestMessageListenerImpl.class,
            com.sun.ts.tests.jms.core.appclient.topictests.TopicTests.AutoAckMsgListener.class,
            com.sun.ts.tests.jms.common.TestMessageListener.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.common.DoneLatch.class,
            com.sun.ts.tests.jms.common.SessionThread.class,
                                          com.sun.ts.tests.jms.common.SerialTestMessageListenerImpl.class
            );

            // The application-client.xml descriptor
            URL resURL = TopicTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              topictests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TopicTests.class.getResource("topictests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              topictests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            topictests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TopicTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(topictests_appclient_vehicle_client, TopicTests.class, resURL);

        // Ear
            EnterpriseArchive topictests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topictests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topictests_appclient_vehicle_ear.addAsModule(topictests_appclient_vehicle_client);



        return topictests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void receiveNullClosedSessionTopicTest() throws java.lang.Exception {
            super.receiveNullClosedSessionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setClientIDLateTopicTest() throws java.lang.Exception {
            super.setClientIDLateTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void autoAckMsgListenerTopicTest() throws java.lang.Exception {
            super.autoAckMsgListenerTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void serialMsgListenerTopicTest() throws java.lang.Exception {
            super.serialMsgListenerTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetChangeClientIDTopicTest() throws java.lang.Exception {
            super.setGetChangeClientIDTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetExceptionListenerTest() throws java.lang.Exception {
            super.setGetExceptionListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void reverseReceiveClientAckTest() throws java.lang.Exception {
            super.reverseReceiveClientAckTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void clientAckTopicTest() throws java.lang.Exception {
            super.clientAckTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nonAckMsgsRedeliveredTopicTest() throws java.lang.Exception {
            super.nonAckMsgsRedeliveredTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicRequestorSimpleSendAndRecvTest() throws java.lang.Exception {
            super.topicRequestorSimpleSendAndRecvTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void topicRequestorExceptionTests() throws java.lang.Exception {
            super.topicRequestorExceptionTests();
        }


}