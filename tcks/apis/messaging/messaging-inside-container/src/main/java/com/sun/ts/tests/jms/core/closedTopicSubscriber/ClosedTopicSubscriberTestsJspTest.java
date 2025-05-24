package com.sun.ts.tests.jms.core.closedTopicSubscriber;

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
public class ClosedTopicSubscriberTestsJspTest extends com.sun.ts.tests.jms.core.closedTopicSubscriber.ClosedTopicSubscriberTests {
    static final String VEHICLE_ARCHIVE = "closedTopicSubscriber_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedTopicSubscriber_appclient_vehicle: 
        closedTopicSubscriber_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicSubscriber_ejb_vehicle: 
        closedTopicSubscriber_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicSubscriber_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedTopicSubscriber_jsp_vehicle: 
        closedTopicSubscriber_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedTopicSubscriber_servlet_vehicle: 
        closedTopicSubscriber_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedTopicSubscriber/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedTopicSubscriber_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedTopicSubscriber_jsp_vehicle_web.war");
            // The class files
            closedTopicSubscriber_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.closedTopicSubscriber.ClosedTopicSubscriberTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedTopicSubscriberTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicSubscriber/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedTopicSubscriber_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedTopicSubscriberTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicSubscriber/closedTopicSubscriber_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedTopicSubscriber_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedTopicSubscriberTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedTopicSubscriber_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedTopicSubscriberTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedTopicSubscriber_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedTopicSubscriber_jsp_vehicle_web, ClosedTopicSubscriberTests.class, warResURL);

        // Ear
            EnterpriseArchive closedTopicSubscriber_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicSubscriber_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicSubscriber_jsp_vehicle_ear.addAsModule(closedTopicSubscriber_jsp_vehicle_web);



        return closedTopicSubscriber_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicSubscriberGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberReceiveTest() throws java.lang.Exception {
            super.closedTopicSubscriberReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicSubscriberReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicSubscriberReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicSubscriberGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicSubscriberGetTopicTest();
        }


}