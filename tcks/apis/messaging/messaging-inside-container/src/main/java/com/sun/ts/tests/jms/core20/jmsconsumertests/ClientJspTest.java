package com.sun.ts.tests.jms.core20.jmsconsumertests;

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
public class ClientJspTest extends com.sun.ts.tests.jms.core20.jmsconsumertests.Client {
    static final String VEHICLE_ARCHIVE = "jmsconsumertests_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        jmsconsumertests_appclient_vehicle: 
        jmsconsumertests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        jmsconsumertests_ejb_vehicle: 
        jmsconsumertests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jmsconsumertests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        jmsconsumertests_jsp_vehicle: 
        jmsconsumertests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        jmsconsumertests_servlet_vehicle: 
        jmsconsumertests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core20/jmsconsumertests/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jmsconsumertests_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "jmsconsumertests_jsp_vehicle_web.war");
            // The class files
            jmsconsumertests_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core20.jmsconsumertests.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmsconsumertests/jsp_vehicle_web.xml");
            if(warResURL != null) {
              jmsconsumertests_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmsconsumertests/jmsconsumertests_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jmsconsumertests_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              jmsconsumertests_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              jmsconsumertests_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jmsconsumertests_jsp_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive jmsconsumertests_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmsconsumertests_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmsconsumertests_jsp_vehicle_ear.addAsModule(jmsconsumertests_jsp_vehicle_web);



        return jmsconsumertests_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void queueReceiveTests() throws java.lang.Exception {
            super.queueReceiveTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void queueReceiveBodyTests() throws java.lang.Exception {
            super.queueReceiveBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void queueReceiveBodyExceptionTests() throws java.lang.Exception {
            super.queueReceiveBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void queueGetMessageSelectorTest() throws java.lang.Exception {
            super.queueGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void queueCloseTest() throws java.lang.Exception {
            super.queueCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void topicReceiveTests() throws java.lang.Exception {
            super.topicReceiveTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void topicReceiveBodyTests() throws java.lang.Exception {
            super.topicReceiveBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void topicReceiveBodyExceptionTests() throws java.lang.Exception {
            super.topicReceiveBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void topicGetMessageSelectorTest() throws java.lang.Exception {
            super.topicGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void topicCloseTest() throws java.lang.Exception {
            super.topicCloseTest();
        }


}