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
public class ClientAppclientTest extends com.sun.ts.tests.jms.core20.connectionfactorytests.Client {
    static final String VEHICLE_ARCHIVE = "connectionfactorytests_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core20/connectionfactorytests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive connectionfactorytests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "connectionfactorytests_appclient_vehicle_client.jar");
            // The class files
            connectionfactorytests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core20.connectionfactorytests.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              connectionfactorytests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("connectionfactorytests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              connectionfactorytests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            connectionfactorytests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(connectionfactorytests_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive connectionfactorytests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "connectionfactorytests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            connectionfactorytests_appclient_vehicle_ear.addAsModule(connectionfactorytests_appclient_vehicle_client);



        return connectionfactorytests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void qcfCreateJMSContextTest1() throws java.lang.Exception {
            super.qcfCreateJMSContextTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void qcfCreateJMSContextTest3() throws java.lang.Exception {
            super.qcfCreateJMSContextTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void qcfCreateConnectionTest() throws java.lang.Exception {
            super.qcfCreateConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void tcfCreateJMSContextTest1() throws java.lang.Exception {
            super.tcfCreateJMSContextTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void tcfCreateJMSContextTest3() throws java.lang.Exception {
            super.tcfCreateJMSContextTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void tcfCreateConnectionTest() throws java.lang.Exception {
            super.tcfCreateConnectionTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createConnectionExceptionTests() throws java.lang.Exception {
            super.createConnectionExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void createJMSContextExceptionTests() throws java.lang.Exception {
            super.createJMSContextExceptionTests();
        }


}