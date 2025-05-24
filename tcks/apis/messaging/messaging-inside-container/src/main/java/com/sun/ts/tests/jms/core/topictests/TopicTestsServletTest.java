package com.sun.ts.tests.jms.core.topictests;

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
public class TopicTestsServletTest extends com.sun.ts.tests.jms.core.topictests.TopicTests {
    static final String VEHICLE_ARCHIVE = "topictests_servlet_vehicle";

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
        War:

        /com/sun/ts/tests/jms/core/topictests/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive topictests_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "topictests_servlet_vehicle_web.war");
            // The class files
            topictests_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.topictests.TopicTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = TopicTests.class.getResource("/com/sun/ts/tests/jms/core/topictests/servlet_vehicle_web.xml");
            if(warResURL != null) {
              topictests_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = TopicTests.class.getResource("/com/sun/ts/tests/jms/core/topictests/topictests_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              topictests_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(topictests_servlet_vehicle_web, TopicTests.class, warResURL);

        // Ear
            EnterpriseArchive topictests_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topictests_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topictests_servlet_vehicle_ear.addAsModule(topictests_servlet_vehicle_web);



        return topictests_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void simpleSendReceiveTopicTest() throws java.lang.Exception {
            super.simpleSendReceiveTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void inactiveNonDurableSubscriberTopicRecTest() throws java.lang.Exception {
            super.inactiveNonDurableSubscriberTopicRecTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void noLocalDeliveryTopicTest() throws java.lang.Exception {
            super.noLocalDeliveryTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void simpleDurableSubscriberTopicTest() throws java.lang.Exception {
            super.simpleDurableSubscriberTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void temporaryTopicConnectionClosesTest() throws java.lang.Exception {
            super.temporaryTopicConnectionClosesTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void temporaryTopicNotConsumableTest() throws java.lang.Exception {
            super.temporaryTopicNotConsumableTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgSelectorMsgHeaderTopicTest() throws java.lang.Exception {
            super.msgSelectorMsgHeaderTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void inactiveDurableSubscriberTopicRecTest() throws java.lang.Exception {
            super.inactiveDurableSubscriberTopicRecTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableSubscriberTopicNoLocalTest() throws java.lang.Exception {
            super.durableSubscriberTopicNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableSubscriberTopicNoLocalTest2() throws java.lang.Exception {
            super.durableSubscriberTopicNoLocalTest2();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableSubscriberNewTopicTest() throws java.lang.Exception {
            super.durableSubscriberNewTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableSubscriberChangeSelectorTest() throws java.lang.Exception {
            super.durableSubscriberChangeSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableSubscriberChangeSelectorTest2() throws java.lang.Exception {
            super.durableSubscriberChangeSelectorTest2();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgProducerNullDestinationTopicTest() throws java.lang.Exception {
            super.msgProducerNullDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void multipleCloseTopicConnectionTest() throws java.lang.Exception {
            super.multipleCloseTopicConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void consumerTests() throws java.lang.Exception {
            super.consumerTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void tempTopicTests() throws java.lang.Exception {
            super.tempTopicTests();
        }


}