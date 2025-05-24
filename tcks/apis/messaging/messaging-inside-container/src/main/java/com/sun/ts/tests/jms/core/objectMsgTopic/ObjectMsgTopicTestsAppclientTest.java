package com.sun.ts.tests.jms.core.objectMsgTopic;

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
public class ObjectMsgTopicTestsAppclientTest extends com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "objectMsgTopic_appclient_vehicle";

        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive objectMsgTopic_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "objectMsgTopic_appclient_vehicle_client.jar");
            // The class files
            objectMsgTopic_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests.class
            );
            // The application-client.xml descriptor
            URL resURL = ObjectMsgTopicTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              objectMsgTopic_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ObjectMsgTopicTests.class.getResource("objectMsgTopic_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              objectMsgTopic_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            objectMsgTopic_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ObjectMsgTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(objectMsgTopic_appclient_vehicle_client, ObjectMsgTopicTests.class, resURL);

        // Ear
            EnterpriseArchive objectMsgTopic_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "objectMsgTopic_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            objectMsgTopic_appclient_vehicle_ear.addAsModule(objectMsgTopic_appclient_vehicle_client);



        return objectMsgTopic_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageObjectCopyTopicTest() throws java.lang.Exception {
            super.messageObjectCopyTopicTest();
        }

}