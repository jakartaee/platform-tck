package com.sun.ts.tests.jms.core.messageProducer;

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
public class MessageProducerTestsEjbTest extends com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests {
    static final String VEHICLE_ARCHIVE = "messageProducer_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        messageProducer_appclient_vehicle: 
        messageProducer_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageProducer_ejb_vehicle: 
        messageProducer_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        messageProducer_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        messageProducer_jsp_vehicle: 
        messageProducer_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        messageProducer_servlet_vehicle: 
        messageProducer_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/messageProducer/ejb_vehicle_ejb.xml
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
            JavaArchive messageProducer_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "messageProducer_ejb_vehicle_client.jar");
            // The class files
            messageProducer_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests.class
            );
            // The application-client.xml descriptor
            URL resURL = MessageProducerTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              messageProducer_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MessageProducerTests.class.getResource("messageProducer_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              messageProducer_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //messageProducer_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MessageProducerTests.class.getName() + "\n"), "MANIFEST.MF");
            messageProducer_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(messageProducer_ejb_vehicle_client, MessageProducerTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive messageProducer_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "messageProducer_ejb_vehicle_ejb.jar");
            // The class files
            messageProducer_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MessageProducerTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              messageProducer_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MessageProducerTests.class.getResource("messageProducer_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              messageProducer_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(messageProducer_ejb_vehicle_ejb, MessageProducerTests.class, ejbResURL);

        // Ear
            EnterpriseArchive messageProducer_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "messageProducer_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            messageProducer_ejb_vehicle_ear.addAsModule(messageProducer_ejb_vehicle_ejb);
            messageProducer_ejb_vehicle_ear.addAsModule(messageProducer_ejb_vehicle_client);



        return messageProducer_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendQueueTest1() throws java.lang.Exception {
            super.sendQueueTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendQueueTest2() throws java.lang.Exception {
            super.sendQueueTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendQueueTest3() throws java.lang.Exception {
            super.sendQueueTest3();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendTopicTest4() throws java.lang.Exception {
            super.sendTopicTest4();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendTopicTest5() throws java.lang.Exception {
            super.sendTopicTest5();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendTopicTest6() throws java.lang.Exception {
            super.sendTopicTest6();
        }


}