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
public class TopicTestsEjbTest extends com.sun.ts.tests.jms.core.topictests.TopicTests {
    static final String VEHICLE_ARCHIVE = "topictests_ejb_vehicle";

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

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/topictests/ejb_vehicle_ejb.xml
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
            JavaArchive topictests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "topictests_ejb_vehicle_client.jar");
            // The class files
            topictests_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.topictests.TopicTests.class
            );
            // The application-client.xml descriptor
            URL resURL = TopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              topictests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TopicTests.class.getResource("topictests_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              topictests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //topictests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TopicTests.class.getName() + "\n"), "MANIFEST.MF");
            topictests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(topictests_ejb_vehicle_client, TopicTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive topictests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "topictests_ejb_vehicle_ejb.jar");
            // The class files
            topictests_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                com.sun.ts.tests.jms.core.topictests.TopicTests.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = TopicTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              topictests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = TopicTests.class.getResource("topictests_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              topictests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(topictests_ejb_vehicle_ejb, TopicTests.class, ejbResURL);

        // Ear
            EnterpriseArchive topictests_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topictests_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topictests_ejb_vehicle_ear.addAsModule(topictests_ejb_vehicle_ejb);
            topictests_ejb_vehicle_ear.addAsModule(topictests_ejb_vehicle_client);



        return topictests_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void simpleSendReceiveTopicTest() throws java.lang.Exception {
            super.simpleSendReceiveTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void inactiveNonDurableSubscriberTopicRecTest() throws java.lang.Exception {
            super.inactiveNonDurableSubscriberTopicRecTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void noLocalDeliveryTopicTest() throws java.lang.Exception {
            super.noLocalDeliveryTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void simpleDurableSubscriberTopicTest() throws java.lang.Exception {
            super.simpleDurableSubscriberTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void temporaryTopicConnectionClosesTest() throws java.lang.Exception {
            super.temporaryTopicConnectionClosesTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void temporaryTopicNotConsumableTest() throws java.lang.Exception {
            super.temporaryTopicNotConsumableTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgSelectorMsgHeaderTopicTest() throws java.lang.Exception {
            super.msgSelectorMsgHeaderTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void inactiveDurableSubscriberTopicRecTest() throws java.lang.Exception {
            super.inactiveDurableSubscriberTopicRecTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableSubscriberTopicNoLocalTest() throws java.lang.Exception {
            super.durableSubscriberTopicNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableSubscriberTopicNoLocalTest2() throws java.lang.Exception {
            super.durableSubscriberTopicNoLocalTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableSubscriberNewTopicTest() throws java.lang.Exception {
            super.durableSubscriberNewTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableSubscriberChangeSelectorTest() throws java.lang.Exception {
            super.durableSubscriberChangeSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableSubscriberChangeSelectorTest2() throws java.lang.Exception {
            super.durableSubscriberChangeSelectorTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgProducerNullDestinationTopicTest() throws java.lang.Exception {
            super.msgProducerNullDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void multipleCloseTopicConnectionTest() throws java.lang.Exception {
            super.multipleCloseTopicConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void consumerTests() throws java.lang.Exception {
            super.consumerTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void tempTopicTests() throws java.lang.Exception {
            super.tempTopicTests();
        }


}