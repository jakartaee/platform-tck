package com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests;

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
public class ClientAppclientTest extends com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests.Client {
    static final String VEHICLE_ARCHIVE = "listenerexceptiontests_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        listenerexceptiontests_appclient_vehicle: 
        listenerexceptiontests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core20/appclient/listenerexceptiontests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive listenerexceptiontests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "listenerexceptiontests_appclient_vehicle_client.jar");
            // The class files
            listenerexceptiontests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.TextMessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests.Client.class,
            com.sun.ts.tests.jms.common.MessageTestImpl.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests.MyCompletionListener.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests.MyMessageListener.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              listenerexceptiontests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("listenerexceptiontests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              listenerexceptiontests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            listenerexceptiontests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(listenerexceptiontests_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive listenerexceptiontests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "listenerexceptiontests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            listenerexceptiontests_appclient_vehicle_ear.addAsModule(listenerexceptiontests_appclient_vehicle_client);



        return listenerexceptiontests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateExceptionTest1() throws java.lang.Exception {
            super.illegalStateExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateExceptionTest2() throws java.lang.Exception {
            super.illegalStateExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateExceptionTest3() throws java.lang.Exception {
            super.illegalStateExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void callingMessageConsumerCloseIsAllowed() throws java.lang.Exception {
            super.callingMessageConsumerCloseIsAllowed();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void callingJMSConsumerCloseIsAllowed() throws java.lang.Exception {
            super.callingJMSConsumerCloseIsAllowed();
        }


}