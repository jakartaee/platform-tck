package com.sun.ts.tests.jms.core.appclient.closedQueueConnection;

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
public class ClosedQueueConnectionTestsAppclientTest extends com.sun.ts.tests.jms.core.appclient.closedQueueConnection.ClosedQueueConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedQueueConnection_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueConnection_appclient_vehicle: 
        closedQueueConnection_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueConnection_ejb_vehicle: 
        closedQueueConnection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueConnection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueConnection_jsp_vehicle: 
        closedQueueConnection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueConnection_servlet_vehicle: 
        closedQueueConnection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/appclient/closedQueueConnection/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedQueueConnection_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedQueueConnection_appclient_vehicle_client.jar");
            // The class files
            closedQueueConnection_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            com.sun.ts.tests.jms.core.appclient.closedQueueConnection.ClosedQueueConnectionTests.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                          com.sun.ts.tests.jms.common.MessageTestImpl.class
            );

            // The application-client.xml descriptor
            URL resURL = ClosedQueueConnectionTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              closedQueueConnection_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedQueueConnectionTests.class.getResource("closedQueueConnection_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedQueueConnection_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            closedQueueConnection_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedQueueConnectionTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedQueueConnection_appclient_vehicle_client, ClosedQueueConnectionTests.class, resURL);

        // Ear
            EnterpriseArchive closedQueueConnection_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueConnection_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueConnection_appclient_vehicle_ear.addAsModule(closedQueueConnection_appclient_vehicle_client);



        return closedQueueConnection_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetClientIDTest() throws java.lang.Exception {
            super.closedQueueConnectionSetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetExceptionListenerTest() throws java.lang.Exception {
            super.closedQueueConnectionSetExceptionListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetMessageListenerTest() throws java.lang.Exception {
            super.closedQueueConnectionGetMessageListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionSetMessageListenerTest() throws java.lang.Exception {
            super.closedQueueConnectionSetMessageListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionGetExceptionListenerTest() throws java.lang.Exception {
            super.closedQueueConnectionGetExceptionListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionStopTest() throws java.lang.Exception {
            super.closedQueueConnectionStopTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueConnectionAckTest() throws java.lang.Exception {
            super.closedQueueConnectionAckTest();
        }


}