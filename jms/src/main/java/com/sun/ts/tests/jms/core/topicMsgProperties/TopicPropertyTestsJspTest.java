package com.sun.ts.tests.jms.core.topicMsgProperties;

import com.sun.ts.tests.jms.core.topicMsgProperties.TopicPropertyTests;
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
public class TopicPropertyTestsJspTest extends com.sun.ts.tests.jms.core.topicMsgProperties.TopicPropertyTests {
    static final String VEHICLE_ARCHIVE = "topicMsgProperties_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        topicMsgProperties_appclient_vehicle: 
        topicMsgProperties_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicMsgProperties_ejb_vehicle: 
        topicMsgProperties_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        topicMsgProperties_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        topicMsgProperties_jsp_vehicle: 
        topicMsgProperties_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        topicMsgProperties_servlet_vehicle: 
        topicMsgProperties_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/topicMsgProperties/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive topicMsgProperties_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "topicMsgProperties_jsp_vehicle_web.war");
            // The class files
            topicMsgProperties_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.core.topicMsgProperties.TopicPropertyTests.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = TopicPropertyTests.class.getResource("/com/sun/ts/tests/jms/core/topicMsgProperties/jsp_vehicle_web.xml");
            if(warResURL != null) {
              topicMsgProperties_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = TopicPropertyTests.class.getResource("/com/sun/ts/tests/jms/core/topicMsgProperties/topicMsgProperties_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              topicMsgProperties_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = TopicPropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              topicMsgProperties_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = TopicPropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              topicMsgProperties_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(topicMsgProperties_jsp_vehicle_web, TopicPropertyTests.class, warResURL);

        // Ear
            EnterpriseArchive topicMsgProperties_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "topicMsgProperties_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topicMsgProperties_jsp_vehicle_ear.addAsModule(topicMsgProperties_jsp_vehicle_web);



        return topicMsgProperties_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgPropertiesTopicTest() throws java.lang.Exception {
            super.msgPropertiesTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgPropertiesConversionTopicTest() throws java.lang.Exception {
            super.msgPropertiesConversionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgJMSXPropertiesTopicTest() throws java.lang.Exception {
            super.msgJMSXPropertiesTopicTest();
        }


}