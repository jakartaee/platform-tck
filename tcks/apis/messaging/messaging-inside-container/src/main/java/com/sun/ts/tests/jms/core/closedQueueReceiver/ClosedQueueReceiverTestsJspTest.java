package com.sun.ts.tests.jms.core.closedQueueReceiver;

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
public class ClosedQueueReceiverTestsJspTest extends com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests {
    static final String VEHICLE_ARCHIVE = "closedQueueReceiver_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueReceiver_appclient_vehicle: 
        closedQueueReceiver_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml
        closedQueueReceiver_ejb_vehicle: 
        closedQueueReceiver_ejb_vehicle_client: jar.sun-application-client.xml,META-INF/application-client.xml
        closedQueueReceiver_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueReceiver_jsp_vehicle: 
        closedQueueReceiver_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueReceiver_servlet_vehicle: 
        closedQueueReceiver_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedQueueReceiver/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedQueueReceiver_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedQueueReceiver_jsp_vehicle_web.war");
            // The class files
            closedQueueReceiver_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.jms.core.closedQueueReceiver.ClosedQueueReceiverTests.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedQueueReceiverTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueReceiver/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedQueueReceiver_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedQueueReceiverTests.class.getResource("/com/sun/ts/tests/jms/core/closedQueueReceiver/closedQueueReceiver_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedQueueReceiver_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedQueueReceiverTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedQueueReceiver_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedQueueReceiverTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedQueueReceiver_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedQueueReceiver_jsp_vehicle_web, ClosedQueueReceiverTests.class, warResURL);

        // Ear
            EnterpriseArchive closedQueueReceiver_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueReceiver_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueReceiver_jsp_vehicle_ear.addAsModule(closedQueueReceiver_jsp_vehicle_web);



        return closedQueueReceiver_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueReceiverCloseTest() throws java.lang.Exception {
            super.closedQueueReceiverCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueReceiverGetMessageSelectorTest() throws java.lang.Exception {
            super.closedQueueReceiverGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueReceiverReceiveTest() throws java.lang.Exception {
            super.closedQueueReceiverReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueReceiverReceiveTimeoutTest() throws java.lang.Exception {
            super.closedQueueReceiverReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueReceiverReceiveNoWaitTest() throws java.lang.Exception {
            super.closedQueueReceiverReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedQueueReceiverGetQueueTest() throws java.lang.Exception {
            super.closedQueueReceiverGetQueueTest();
        }


}