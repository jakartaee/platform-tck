package com.sun.ts.tests.jms.core.topicConnection;

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
public class TopicConnectionTestsJspTest extends com.sun.ts.tests.jms.core.topicConnection.TopicConnectionTests {
    static final String VEHICLE_ARCHIVE = "topicConnection_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        topicConnection_appclient_vehicle: 
        topicConnection_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicConnection_ejb_vehicle: 
        topicConnection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicConnection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        topicConnection_jsp_vehicle: 
        topicConnection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        topicConnection_servlet_vehicle: 
        topicConnection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/topicConnection/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive topicConnection_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "topicConnection_jsp_vehicle_web.war");
            // The class files
            topicConnection_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.jms.core.topicConnection.TopicConnectionTests.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = TopicConnectionTests.class.getResource("/com/sun/ts/tests/jms/core/topicConnection/jsp_vehicle_web.xml");
            if(warResURL != null) {
              topicConnection_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = TopicConnectionTests.class.getResource("/com/sun/ts/tests/jms/core/topicConnection/topicConnection_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              topicConnection_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = TopicConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              topicConnection_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = TopicConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              topicConnection_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(topicConnection_jsp_vehicle_web, TopicConnectionTests.class, warResURL);

        // Ear
            EnterpriseArchive topicConnection_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topicConnection_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topicConnection_jsp_vehicle_ear.addAsModule(topicConnection_jsp_vehicle_web);



        return topicConnection_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void connNotStartedTopicTest() throws java.lang.Exception {
            super.connNotStartedTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void metaDataTests() throws java.lang.Exception {
            super.metaDataTests();
        }


}