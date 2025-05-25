package com.sun.ts.tests.jms.core.sessiontests;

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
public class SessionTestsJspTest extends com.sun.ts.tests.jms.core.sessiontests.SessionTests {
    static final String VEHICLE_ARCHIVE = "sessiontests_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        sessiontests_appclient_vehicle: 
        sessiontests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        sessiontests_ejb_vehicle: 
        sessiontests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        sessiontests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        sessiontests_jsp_vehicle: 
        sessiontests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml,WEB-INF/web.xml,war.sun-web.xml
        sessiontests_servlet_vehicle: 
        sessiontests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml,WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/sessiontests/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive sessiontests_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "sessiontests_jsp_vehicle_web.war");
            // The class files
            sessiontests_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.jms.core.sessiontests.SessionTests.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = SessionTests.class.getResource("/com/sun/ts/tests/jms/core/sessiontests/jsp_vehicle_web.xml");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = SessionTests.class.getResource("/com/sun/ts/tests/jms/core/sessiontests/sessiontests_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = SessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = SessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              sessiontests_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(sessiontests_jsp_vehicle_web, SessionTests.class, warResURL);

        // Ear
            EnterpriseArchive sessiontests_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "sessiontests_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            sessiontests_jsp_vehicle_ear.addAsModule(sessiontests_jsp_vehicle_web);



        return sessiontests_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void SimpleSendAndReceiveQ() throws java.lang.Exception {
            super.SimpleSendAndReceiveQ();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void SimpleSendAndReceiveT() throws java.lang.Exception {
            super.SimpleSendAndReceiveT();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void selectorAndBrowserTests() throws java.lang.Exception {
            super.selectorAndBrowserTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void SubscriberTests() throws java.lang.Exception {
            super.SubscriberTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void IllegalStateTestQ() throws java.lang.Exception {
            super.IllegalStateTestQ();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void ackTests() throws java.lang.Exception {
            super.ackTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void InvalidDestinationTests() throws java.lang.Exception {
            super.InvalidDestinationTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void InvalidSelectorTests() throws java.lang.Exception {
            super.InvalidSelectorTests();
        }


}