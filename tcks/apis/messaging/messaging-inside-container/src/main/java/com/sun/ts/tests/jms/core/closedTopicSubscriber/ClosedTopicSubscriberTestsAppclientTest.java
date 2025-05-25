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
public class ClosedTopicSubscriberTestsAppclientTest extends com.sun.ts.tests.jms.core.closedTopicSubscriber.ClosedTopicSubscriberTests {
    static final String VEHICLE_ARCHIVE = "closedTopicSubscriber_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core/closedTopicSubscriber/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedTopicSubscriber_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedTopicSubscriber_appclient_vehicle_client.jar");
            // The class files
            closedTopicSubscriber_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            com.sun.ts.tests.jms.core.closedTopicSubscriber.ClosedTopicSubscriberTests.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedTopicSubscriberTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              closedTopicSubscriber_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedTopicSubscriberTests.class.getResource("closedTopicSubscriber_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedTopicSubscriber_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            closedTopicSubscriber_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedTopicSubscriberTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedTopicSubscriber_appclient_vehicle_client, ClosedTopicSubscriberTests.class, resURL);

        // Ear
            EnterpriseArchive closedTopicSubscriber_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicSubscriber_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicSubscriber_appclient_vehicle_ear.addAsModule(closedTopicSubscriber_appclient_vehicle_client);



        return closedTopicSubscriber_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberCloseTest() throws java.lang.Exception {
            super.closedTopicSubscriberCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberGetMessageSelectorTest() throws java.lang.Exception {
            super.closedTopicSubscriberGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberReceiveTest() throws java.lang.Exception {
            super.closedTopicSubscriberReceiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberReceiveTimeoutTest() throws java.lang.Exception {
            super.closedTopicSubscriberReceiveTimeoutTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberReceiveNoWaitTest() throws java.lang.Exception {
            super.closedTopicSubscriberReceiveNoWaitTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberGetNoLocalTest() throws java.lang.Exception {
            super.closedTopicSubscriberGetNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicSubscriberGetTopicTest() throws java.lang.Exception {
            super.closedTopicSubscriberGetTopicTest();
        }


}