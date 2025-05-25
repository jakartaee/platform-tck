package com.sun.ts.tests.jms.core.appclient.closedTopicConnection;

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
public class ClosedTopicConnectionTestsAppclientTest extends com.sun.ts.tests.jms.core.appclient.closedTopicConnection.ClosedTopicConnectionTests {
    static final String VEHICLE_ARCHIVE = "closedTopicConnection_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedTopicConnection_appclient_vehicle: 
        closedTopicConnection_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicConnection_ejb_vehicle: 
        closedTopicConnection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedTopicConnection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedTopicConnection_jsp_vehicle: 
        closedTopicConnection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedTopicConnection_servlet_vehicle: 
        closedTopicConnection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/appclient/closedTopicConnection/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedTopicConnection_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedTopicConnection_appclient_vehicle_client.jar");
            // The class files
            closedTopicConnection_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.jms.core.appclient.closedTopicConnection.ClosedTopicConnectionTests.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                          com.sun.ts.tests.jms.common.MessageTestImpl.class
            );

            // The application-client.xml descriptor
            URL resURL = ClosedTopicConnectionTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              closedTopicConnection_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedTopicConnectionTests.class.getResource("closedTopicConnection_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedTopicConnection_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            closedTopicConnection_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedTopicConnectionTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedTopicConnection_appclient_vehicle_client, ClosedTopicConnectionTests.class, resURL);

        // Ear
            EnterpriseArchive closedTopicConnection_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedTopicConnection_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedTopicConnection_appclient_vehicle_ear.addAsModule(closedTopicConnection_appclient_vehicle_client);



        return closedTopicConnection_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicConnectionGetExceptionListenerTest() throws java.lang.Exception {
            super.closedTopicConnectionGetExceptionListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicConnectionSetClientIDTest() throws java.lang.Exception {
            super.closedTopicConnectionSetClientIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicConnectionSetExceptionListenerTest() throws java.lang.Exception {
            super.closedTopicConnectionSetExceptionListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicConnectionGetMessageListenerTest() throws java.lang.Exception {
            super.closedTopicConnectionGetMessageListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicConnectionSetMessageListenerTest() throws java.lang.Exception {
            super.closedTopicConnectionSetMessageListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedTopicConnectionStopTest() throws java.lang.Exception {
            super.closedTopicConnectionStopTest();
        }


}