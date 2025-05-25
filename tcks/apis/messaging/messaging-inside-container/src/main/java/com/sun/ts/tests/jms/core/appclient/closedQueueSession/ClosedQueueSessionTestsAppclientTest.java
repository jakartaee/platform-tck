package com.sun.ts.tests.jms.core.appclient.closedQueueSession;

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
public class ClosedQueueSessionTestsAppclientTest extends com.sun.ts.tests.jms.core.appclient.closedQueueSession.ClosedQueueSessionTests {
    static final String VEHICLE_ARCHIVE = "closedQueueSession_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        closedQueueSession_appclient_vehicle: 
        closedQueueSession_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSession_ejb_vehicle: 
        closedQueueSession_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        closedQueueSession_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        closedQueueSession_jsp_vehicle: 
        closedQueueSession_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        closedQueueSession_servlet_vehicle: 
        closedQueueSession_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/appclient/closedQueueSession/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive closedQueueSession_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "closedQueueSession_appclient_vehicle_client.jar");
            // The class files
            closedQueueSession_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                                                             com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
            Fault.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core.appclient.closedQueueSession.ClosedQueueSessionTests.class,
                                                             com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
                                                             com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
                                                             com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.common.ObjectMessageTestImpl.class,
            com.sun.ts.tests.jms.common.MapMessageTestImpl.class,
                                                             com.sun.ts.tests.jms.common.StreamMessageTestImpl.class,
            EETest.class,
            com.sun.ts.tests.jms.common.BytesMessageTestImpl.class,
            ServiceEETest.class,
                                                             com.sun.ts.tests.jms.common.MessageTestImpl.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );

            // The application-client.xml descriptor
            URL resURL = ClosedQueueSessionTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              closedQueueSession_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ClosedQueueSessionTests.class.getResource("closedQueueSession_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              closedQueueSession_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            closedQueueSession_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ClosedQueueSessionTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(closedQueueSession_appclient_vehicle_client, ClosedQueueSessionTests.class, resURL);

        // Ear
            EnterpriseArchive closedQueueSession_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "closedQueueSession_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            closedQueueSession_appclient_vehicle_ear.addAsModule(closedQueueSession_appclient_vehicle_client);



        return closedQueueSession_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueSessionGetMessageListenerTest() throws java.lang.Exception {
            super.closedQueueSessionGetMessageListenerTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void closedQueueSessionSetMessageListenerTest() throws java.lang.Exception {
            super.closedQueueSessionSetMessageListenerTest();
        }


}