package com.sun.ts.tests.jms.core.closedTopicPublisher;

import com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
public class ClosedTopicPublisherTestsJspTest extends com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests {
    static final String VEHICLE_ARCHIVE = "closedTopicPublisher_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedTopicPublisher_appclient_vehicle: 
        closedTopicPublisher_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicPublisher_ejb_vehicle: 
        closedTopicPublisher_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicPublisher_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedTopicPublisher_jsp_vehicle: 
        closedTopicPublisher_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedTopicPublisher_servlet_vehicle: 
        closedTopicPublisher_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/closedTopicPublisher/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive closedTopicPublisher_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "closedTopicPublisher_jsp_vehicle_web.war");
            // The class files
            closedTopicPublisher_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
                                             com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ClosedTopicPublisherTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicPublisher/jsp_vehicle_web.xml");
            if(warResURL != null) {
              closedTopicPublisher_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ClosedTopicPublisherTests.class.getResource("/com/sun/ts/tests/jms/core/closedTopicPublisher/closedTopicPublisher_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              closedTopicPublisher_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ClosedTopicPublisherTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              closedTopicPublisher_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ClosedTopicPublisherTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              closedTopicPublisher_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(closedTopicPublisher_jsp_vehicle_web, ClosedTopicPublisherTests.class, warResURL);

        // Ear
            EnterpriseArchive closedTopicPublisher_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicPublisher_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicPublisher_jsp_vehicle_ear.addAsModule(closedTopicPublisher_jsp_vehicle_web);



        return closedTopicPublisher_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicPublisherGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicPublisherGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicPublisherGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherGetPriorityTest() throws java.lang.Exception {
            super.closedTopicPublisherGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicPublisherGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicPublisherSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicPublisherSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicPublisherSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherSetPriorityTest() throws java.lang.Exception {
            super.closedTopicPublisherSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicPublisherSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherPublish1Test() throws java.lang.Exception {
            super.closedTopicPublisherPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void closedTopicPublisherPublish2Test() throws java.lang.Exception {
            super.closedTopicPublisherPublish2Test();
        }


}