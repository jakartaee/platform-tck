package com.sun.ts.tests.jms.core20.jmscontextqueuetests;

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
public class ClientServletTest extends com.sun.ts.tests.jms.core20.jmscontextqueuetests.Client {
    static final String VEHICLE_ARCHIVE = "jmscontextqueuetests_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jmscontextqueuetests_appclient_vehicle: 
        jmscontextqueuetests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        jmscontextqueuetests_ejb_vehicle: 
        jmscontextqueuetests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jmscontextqueuetests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        jmscontextqueuetests_jsp_vehicle: 
        jmscontextqueuetests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        jmscontextqueuetests_servlet_vehicle: 
        jmscontextqueuetests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core20/jmscontextqueuetests/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jmscontextqueuetests_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jmscontextqueuetests_servlet_vehicle_web.war");
            // The class files
            jmscontextqueuetests_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core20.jmscontextqueuetests.Client.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmscontextqueuetests/servlet_vehicle_web.xml");
            if(warResURL != null) {
              jmscontextqueuetests_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmscontextqueuetests/jmscontextqueuetests_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jmscontextqueuetests_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jmscontextqueuetests_servlet_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive jmscontextqueuetests_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmscontextqueuetests_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmscontextqueuetests_servlet_vehicle_ear.addAsModule(jmscontextqueuetests_servlet_vehicle_web);



        return jmscontextqueuetests_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void createTemporayQueueTest() throws java.lang.Exception {
            super.createTemporayQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void createQueueBrowserTest() throws java.lang.Exception {
            super.createQueueBrowserTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void createConsumerTest() throws java.lang.Exception {
            super.createConsumerTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void getMetaDataTest() throws java.lang.Exception {
            super.getMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void getSessionModeTest() throws java.lang.Exception {
            super.getSessionModeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void getTransactedTest() throws java.lang.Exception {
            super.getTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void getClientIDTest() throws java.lang.Exception {
            super.getClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetAutoStartTest() throws java.lang.Exception {
            super.setGetAutoStartTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void multipleCloseContextTest() throws java.lang.Exception {
            super.multipleCloseContextTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void invalidDestinationRuntimeExceptionTests() throws java.lang.Exception {
            super.invalidDestinationRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void invalidSelectorRuntimeExceptionTests() throws java.lang.Exception {
            super.invalidSelectorRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void illegalStateRuntimeExceptionTest() throws java.lang.Exception {
            super.illegalStateRuntimeExceptionTest();
        }


}