package com.sun.ts.tests.jms.core.closedTopicPublisher;

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
public class ClosedTopicPublisherTestsEjbTest extends com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests {
    static final String VEHICLE_ARCHIVE = "closedTopicPublisher_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/closedTopicPublisher/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedTopicPublisher_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedTopicPublisher_ejb_vehicle_client.jar");
            // The class files
            closedTopicPublisher_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests.class,
                                          com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The application-client.xml descriptor
            URL resURL = ClosedTopicPublisherTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              closedTopicPublisher_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedTopicPublisherTests.class.getResource("closedTopicPublisher_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedTopicPublisher_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //closedTopicPublisher_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedTopicPublisherTests.class.getName() + "\n"), "MANIFEST.MF");
            closedTopicPublisher_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedTopicPublisher_ejb_vehicle_client, ClosedTopicPublisherTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive closedTopicPublisher_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "closedTopicPublisher_ejb_vehicle_ejb.jar");
            // The class files
            closedTopicPublisher_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class,
                com.sun.ts.tests.jms.common.MessageTestImpl.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                              com.sun.ts.tests.jms.common.MessageTestImpl.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ClosedTopicPublisherTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              closedTopicPublisher_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ClosedTopicPublisherTests.class.getResource("closedTopicPublisher_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              closedTopicPublisher_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(closedTopicPublisher_ejb_vehicle_ejb, ClosedTopicPublisherTests.class, ejbResURL);

        // Ear
            EnterpriseArchive closedTopicPublisher_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicPublisher_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicPublisher_ejb_vehicle_ear.addAsModule(closedTopicPublisher_ejb_vehicle_ejb);
            closedTopicPublisher_ejb_vehicle_ear.addAsModule(closedTopicPublisher_ejb_vehicle_client);



        return closedTopicPublisher_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherCloseTest() throws java.lang.Exception {
            super.closedTopicPublisherCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherGetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicPublisherGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherGetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicPublisherGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicPublisherGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherGetPriorityTest() throws java.lang.Exception {
            super.closedTopicPublisherGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherGetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicPublisherGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherSetDeliveryModeTest() throws java.lang.Exception {
            super.closedTopicPublisherSetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherSetDisableMessageIDTest() throws java.lang.Exception {
            super.closedTopicPublisherSetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherSetDisableMessageTimestampTest() throws java.lang.Exception {
            super.closedTopicPublisherSetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherSetPriorityTest() throws java.lang.Exception {
            super.closedTopicPublisherSetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherSetTimeToLiveTest() throws java.lang.Exception {
            super.closedTopicPublisherSetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherGetTopicTest() throws java.lang.Exception {
            super.closedTopicPublisherGetTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherPublish1Test() throws java.lang.Exception {
            super.closedTopicPublisherPublish1Test();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void closedTopicPublisherPublish2Test() throws java.lang.Exception {
            super.closedTopicPublisherPublish2Test();
        }


}