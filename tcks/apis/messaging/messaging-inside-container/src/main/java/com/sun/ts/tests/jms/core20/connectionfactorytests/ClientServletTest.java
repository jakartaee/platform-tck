package com.sun.ts.tests.jms.core20.connectionfactorytests;

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
public class ClientServletTest extends com.sun.ts.tests.jms.core20.connectionfactorytests.Client {
    static final String VEHICLE_ARCHIVE = "connectionfactorytests_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        connectionfactorytests_appclient_vehicle: 
        connectionfactorytests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        connectionfactorytests_ejb_vehicle: 
        connectionfactorytests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        connectionfactorytests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        connectionfactorytests_jsp_vehicle: 
        connectionfactorytests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        connectionfactorytests_servlet_vehicle: 
        connectionfactorytests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core20/connectionfactorytests/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive connectionfactorytests_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "connectionfactorytests_servlet_vehicle_web.war");
            // The class files
            connectionfactorytests_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.jms.core20.connectionfactorytests.Client.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/connectionfactorytests/servlet_vehicle_web.xml");
            if(warResURL != null) {
              connectionfactorytests_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/connectionfactorytests/connectionfactorytests_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              connectionfactorytests_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(connectionfactorytests_servlet_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive connectionfactorytests_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "connectionfactorytests_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            connectionfactorytests_servlet_vehicle_ear.addAsModule(connectionfactorytests_servlet_vehicle_web);



        return connectionfactorytests_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void qcfCreateJMSContextTest1() throws java.lang.Exception {
            super.qcfCreateJMSContextTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void qcfCreateJMSContextTest3() throws java.lang.Exception {
            super.qcfCreateJMSContextTest3();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void qcfCreateConnectionTest() throws java.lang.Exception {
            super.qcfCreateConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void tcfCreateJMSContextTest1() throws java.lang.Exception {
            super.tcfCreateJMSContextTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void tcfCreateJMSContextTest3() throws java.lang.Exception {
            super.tcfCreateJMSContextTest3();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void tcfCreateConnectionTest() throws java.lang.Exception {
            super.tcfCreateConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void createConnectionExceptionTests() throws java.lang.Exception {
            super.createConnectionExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void createJMSContextExceptionTests() throws java.lang.Exception {
            super.createJMSContextExceptionTests();
        }


}