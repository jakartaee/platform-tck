package com.sun.ts.tests.jms.core.topicMsgHeaders;

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
public class TopicHeaderTestsEjbTest extends com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests {
    static final String VEHICLE_ARCHIVE = "topicMsgHeaders_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        topicMsgHeaders_appclient_vehicle: 
        topicMsgHeaders_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicMsgHeaders_ejb_vehicle: 
        topicMsgHeaders_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicMsgHeaders_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        topicMsgHeaders_jsp_vehicle: 
        topicMsgHeaders_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        topicMsgHeaders_servlet_vehicle: 
        topicMsgHeaders_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/topicMsgHeaders/ejb_vehicle_ejb.xml
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
            JavaArchive topicMsgHeaders_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "topicMsgHeaders_ejb_vehicle_client.jar");
            // The class files
            topicMsgHeaders_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests.class
            );
            // The application-client.xml descriptor
            URL resURL = TopicHeaderTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              topicMsgHeaders_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TopicHeaderTests.class.getResource("topicMsgHeaders_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              topicMsgHeaders_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //topicMsgHeaders_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TopicHeaderTests.class.getName() + "\n"), "MANIFEST.MF");
            topicMsgHeaders_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(topicMsgHeaders_ejb_vehicle_client, TopicHeaderTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive topicMsgHeaders_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "topicMsgHeaders_ejb_vehicle_ejb.jar");
            // The class files
            topicMsgHeaders_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = TopicHeaderTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              topicMsgHeaders_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = TopicHeaderTests.class.getResource("topicMsgHeaders_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              topicMsgHeaders_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(topicMsgHeaders_ejb_vehicle_ejb, TopicHeaderTests.class, ejbResURL);

        // Ear
            EnterpriseArchive topicMsgHeaders_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topicMsgHeaders_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topicMsgHeaders_ejb_vehicle_ear.addAsModule(topicMsgHeaders_ejb_vehicle_ejb);
            topicMsgHeaders_ejb_vehicle_ear.addAsModule(topicMsgHeaders_ejb_vehicle_client);



        return topicMsgHeaders_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrIDTopicTest() throws java.lang.Exception {
            super.msgHdrIDTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrTimeStampTopicTest() throws java.lang.Exception {
            super.msgHdrTimeStampTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrCorlIdTopicTest() throws java.lang.Exception {
            super.msgHdrCorlIdTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrReplyToTopicTest() throws java.lang.Exception {
            super.msgHdrReplyToTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSTypeTopicTest() throws java.lang.Exception {
            super.msgHdrJMSTypeTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSPriorityTopicTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSExpirationTopicTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSDestinationTopicTest() throws java.lang.Exception {
            super.msgHdrJMSDestinationTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSDeliveryModeTopicTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTopicTest();
        }


}