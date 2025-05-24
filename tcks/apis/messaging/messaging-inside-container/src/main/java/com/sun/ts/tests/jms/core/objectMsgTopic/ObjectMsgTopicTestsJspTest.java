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
public class ObjectMsgTopicTestsJspTest extends com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "objectMsgTopic_jsp_vehicle";

        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive objectMsgTopic_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "objectMsgTopic_jsp_vehicle_web.war");
            // The class files
            objectMsgTopic_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTests.class
            );
            // The web.xml descriptor
            URL warResURL = ObjectMsgTopicTests.class.getResource("jsp_vehicle_web.xml");
            if(warResURL != null) {
              objectMsgTopic_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = ObjectMsgTopicTests.class.getResource("objectMsgTopic_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              objectMsgTopic_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = ObjectMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              objectMsgTopic_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ObjectMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              objectMsgTopic_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(objectMsgTopic_jsp_vehicle_web, ObjectMsgTopicTests.class, warResURL);

        // Ear
            EnterpriseArchive objectMsgTopic_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "objectMsgTopic_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            objectMsgTopic_jsp_vehicle_ear.addAsModule(objectMsgTopic_jsp_vehicle_web);



        return objectMsgTopic_jsp_vehicle_ear;
        }


        @Test
        @Override
        @TargetVehicle("jsp")
        public void messageObjectCopyTopicTest() throws java.lang.Exception {
            super.messageObjectCopyTopicTest();
        }

   
}