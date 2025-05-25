package com.sun.ts.tests.connector.noTx.connection;

import java.net.URL;

import com.sun.ts.lib.harness.Fault;
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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class connectionClient1ServletTest extends com.sun.ts.tests.connector.noTx.connection.connectionClient1 {
    static final String VEHICLE_ARCHIVE = "connection_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        connection_ejb_vehicle: 
        connection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        connection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        connection_jsp_vehicle: 
        connection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        connection_servlet_vehicle: 
        connection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        connection1_appclient_vehicle: 
        connection1_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        connection1_ejb_vehicle: 
        connection1_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        connection1_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        connection1_jsp_vehicle: 
        connection1_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        connection1_servlet_vehicle: 
        connection1_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
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

        /com/sun/ts/tests/connector/noTx/connection/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/mdcomplete/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive connection_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "connection_servlet_vehicle_web.war");
            // The class files
            connection_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.noTx.connection.connectionClient1.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = connectionClient1.class.getResource("connection_servlet_vehicle_servlet.xml");
            if(warResURL != null) {
              connection_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = connectionClient1.class.getResource("connection_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              connection_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(connection_servlet_vehicle_web, connectionClient1.class, warResURL);

        // Ear
            EnterpriseArchive connection_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "connection_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            connection_servlet_vehicle_ear.addAsModule(connection_servlet_vehicle_web);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(connection_servlet_vehicle_ear, connectionClient1.class, earResURL);
        return connection_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testGetConnection1() throws java.lang.Exception {
            super.testGetConnection1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testgetConnectionWithParameter1() throws java.lang.Exception {
            super.testgetConnectionWithParameter1();
        }


}