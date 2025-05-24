package com.sun.ts.tests.jms.ee20.resourcedefs.descriptor;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@Disabled("https://github.com/jakartaee/platform-tck/issues/2231")
@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("jms_optional")
@Tag("platform_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientAppclientTest extends com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "resourcedefs_descriptor_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        resourcedefs_descriptor_appclient_vehicle: 
        resourcedefs_descriptor_appclient_vehicle_client: META-INF/application-client.xml
        resourcedefs_descriptor_ejb_vehicle: 
        resourcedefs_descriptor_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        resourcedefs_descriptor_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        resourcedefs_descriptor_jsp_vehicle: 
        resourcedefs_descriptor_jsp_vehicle_web: WEB-INF/web.xml
        resourcedefs_descriptor_servlet_vehicle: 
        resourcedefs_descriptor_servlet_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive resourcedefs_descriptor_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "resourcedefs_descriptor_appclient_vehicle_client.jar");
            // The class files
            resourcedefs_descriptor_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("xml/appclient_vehicle_client.xml");
            if(resURL != null) {
                resourcedefs_descriptor_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              resourcedefs_descriptor_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // resourcedefs_descriptor_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            resourcedefs_descriptor_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");

            // Call the archive processor
            archiveProcessor.processClientArchive(resourcedefs_descriptor_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive resourcedefs_descriptor_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "resourcedefs_descriptor_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            resourcedefs_descriptor_appclient_vehicle_ear.addAsModule(resourcedefs_descriptor_appclient_vehicle_client);



        return resourcedefs_descriptor_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvQueueTest() throws java.lang.Exception {
            super.sendAndRecvQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvTopicTest() throws java.lang.Exception {
            super.sendAndRecvTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void checkClientIDOnDurableConnFactoryTest() throws java.lang.Exception {
            super.checkClientIDOnDurableConnFactoryTest();
        }


}